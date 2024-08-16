# SQL 优化

### 1.普通索引 vs 唯一索引，孰优孰劣？

> 1. 普通索引进行等值查询，如果==查到了数据不会立即停止==，而是会在叶子链表上遍历，以防还有相同的数据。
> 2. 唯一索引等值查询，==查到了就立刻停止==，因为不会存在相同的数据。
> 3. 因此仅是 select 的话，普通索引会多执行一次指针的移动和值判断操作，但这与磁盘 IO 所消耗的时间相比，完全可以忽略不计。
>
> ---
>
> 1. 下面考虑 update 的情况。首先我们需要清除一个概念 ==change buffer==，简而言之，change buffer 就是在进行 update 操作之后，如果需要修改的数据行在内存中，就直接进行修改；==如果不在内存中，就把 update 的操作暂存在内存中，等待需要修改的数据行被加载到内存中时，再进行修改==，从而减少磁盘 IO 的次数。当然，为了保证 update 不会丢失，Mysql 也会定期将 change buffer 中的 update 操作持久化到磁盘，在关闭 Mysql 时也会进行此操作。
> 2. 那么问题就是，什么样的 update 操作可以使用 change buffer 呢？普通索引可以使用，但是唯一索引在进行 update 操作时必须检测是否会与“唯一”限制相冲突，而检测所需的数据必须从磁盘中读取出来，换言之，==唯一索引的 update 操作必须依赖磁盘中存储的所有数据==，这样就算需要修改的数据行被加载到内存中，也无法进行 update，因此==唯一索引无法使用 change buffer==。
> 3. 考虑到 select 操作时两者的速度差距不大（普通索引多一次指针移动和值判断的操作），而由于唯一索引无法使用 change buffer，在 update 操作中增加了大量消耗时间的磁盘 IO 的次数，因此如果可以不使用唯一索引，就不要把索引设置为唯一索引。

### 2.Mysql 为什么会错选索引？

> 1. 索引不是我们选的，是 Mysql 自己选的，它选索引就是参考索引会扫描的行数、是否使用临时表、是否采取排序。
>
> 2. 扫描行数看起来不是很好判断，我们有一个更容易量化的概念：==区分度==，即该索引上不同的数据个数。==区分度越高，就代表索引越好==。
>
> 3. 如果要让 Mysql 遍历所有的行，给出一个准确的区分度代价未免过大了，Mysql 的做法是抽取几个数据页，计算这几个数据页中需要扫描行数的平均值，然后乘以总数据页数。这下看懂了，因为 ==Mysql 采取抽样检测的方法统计区分度==，因此存在偶然性，可能会导致错选慢索引。
>
> 4. 我们可以使用：
>
> 	```sql
> 	analyze table T;
> 	```
>
> 	命令让 Mysql 重新计算表 T 上索引的区分度。
>
> ---
>
> 1. 我们上面也说了，扫描行数并不是 Mysql 选取索引唯一的标准，它也会根据是否在索引字段上进行排序来选择索引（==倾向于选择使用了 order by 的索引，索引本身有序，这样不需要再做排序，只需要遍历==）。比如下面这个例子中：
>
> 	```sql
> 	select * from t where (a between 1 and 1000) and (b between 50000 and 100000) order by b limit 1;
> 	```
>
> 	Mysql 没有选择扫描行数为 1000 的 a 作为索引，而是选择了扫描行数为 50000 的 b 作为索引，其依据就是在 b 上进行了排序。
>
> ---
>
> 1. 我们有没有什么办法可以人为强制使用某个索引呢？显然是有的：
>
> 	```sql
> 	select * from t force index(a) where (a between 1 and 1000) and (b between 50000 and 100000) order by b limit 1;
> 	```
>
> 	使用 `force index(expected_index)` 就可以强制 Mysql 选取索引。
>
> 2. 当然，我们可以人为地创建一个更好的索引，或者删除不好的索引。

### 3.Mysql 为什么偶尔“卡一下”？

> 1. 先说结论，Mysql ==偶尔“卡一下”的时候是在刷脏页（flush）==。
> 2. 什么是脏页，我们知道，Mysql 在进行 update 操作时会将 update 的结果先写到 redo log 中，然后在系统不是很忙的时候写入磁盘。
> 3. 但是如果我们 update 的操作很频繁，还没等到 Mysql 自行刷脏页，redo log 就被写满了，那么这个时候 Mysql 就不得不停下所有的事务，先把脏页同步到磁盘中，这就导致 Mysql 偶尔“卡一下”。

### 4.count(*)、count(1)、count(字段)，究竟有什么区别？

> 1. ==count(字段) 只会返回该字段不为 NULL 的总行数==，因此 ==count(字段)$\leq$count(\*)==
> 2. 在速度上：count(\*)>count(主键)，因为 count(\*) 底层做了优化，不会把全部字段取出来。
> 3. count(1) 的速度和 count(\*) 相似，但是使用 count(\*) 是更推荐的做法。

### 5.Mysql 为什么有索引却不用？

> 1. 使用==聚合函数==不会使用索引。原因是原来的索引 B+ 底层的链表是有序的，而如果使用==聚合函数可能会破坏索引的有序性==，因此优化器不敢使用索引，只能全表扫描。比如我们为 Date 创建了一个索引，B+ 树的底层是有序的，但是如果使用 Month(Date)，由于现有 B+ 树的底层不是按照月份有序的，自然不能使用该 B+ 树查询。
>
> 	那如果我们想要在不改变聚合函数查询结果的条件下，使用索引该怎么办呢，以上述的 Date 为例，假如原来的查询语句是：
>
> 	```sql
> 	select count(*) from T where month(Date) = 7;  # 查询7月总行数
> 	```
>
> 	可以将 sql 语句改写为：
>
> 	```sql
> 	select count(*) from tradelog where
> 	    (t_modified >= '2023-7-1' and t_modified<'2023-8-1') or 
> 	    (t_modified >= '2024-7-1' and t_modified<'2024-8-1');
> 	```
>
> 2. ==隐式类型转换==不会使用索引。在 Mysql 中，字符串和数字作比较时，会隐式地把==字符串转换为数字==，比如：
>
> 	```sql
> 	select * from tradelog where tradeid(varchar 6) = 110717;
> 	```
>
> 	tradeid 的类型是字符串，会被隐式转换为数字，而==类型转换也是一个聚合函数==，因此实际是：
>
> 	```sql
> 	select * from tradelog where cast(tradeid AS signed int) = 110717;
> 	```
>
> 	使用聚合函数，因此隐式类型转换也无法使用索引。
>
> 	但要注意，在下面这个例子中：
>
> 	```sql
> 	select * from tradelog where id = "83126";
> 	```
>
> 	并不会隐式调用类型转换函数，因为 id 是整数，无需转换，真正转换的是右侧的 `"83126"`，没有在 id 上使用聚合函数，可以使用索引。
>
> 3. ==隐式字符编码转换==也不会使用索引。类似于将字符串隐式转换为数字，Mysql 在处理等值查询时，如果两边字段的编码类型不同，那么就会把==范围较小的字符集隐式转换为较大的字符集==，这同样是隐式调用类型转换的聚合函数。比如 uft8 就会被隐式转换为 utf8mb4，后者是前者的超集。
>
> 4. 总而言之，==显示或隐式的聚合函数调用，会破坏索引的有序性，优化器不敢冒险使用索引==。

### 6.为什么偶尔一条查询很慢？

> 1. ==等 MDL 锁==：比如另一个线程正在对表进行 DDL 操作，那么就会自动加上 MDL 写锁。MDL 是一种表级锁，我们可以使用：
>
> 	```sql
> 	select blocking_pid from sys.schema_table_lock_waits;
> 	```
>
> 	查到造成阻塞的 pid。
>
> 2. ==等行锁==：可以通过下面的命令查询谁在占用写锁：
>
> 	```sql
> 	select * from sys.innodb_lock_waits where lock_table = "`<DATABASE>`.`<TABLE>`"
> 	```
>
> 3. ==RR 下一致性读时，执行大量 undo 命令查找可视事务==：比如在下面的场景中：
>
> 	| Session A                                     | Session B                                           |
> 	| --------------------------------------------- | --------------------------------------------------- |
> 	| start transaction with consistent snapshot;   |                                                     |
> 	|                                               | update T set c = c + 1 where id = 1; (100000 times) |
> 	| select * from T where id = 1;                 |                                                     |
> 	| select * from T where id = 1 with share mode; |                                                     |
>
> 	由于 `select * from T where id = 1` 是一致性读，Session B 对于 id=1 这一行的 100000 次更新对于它是不可见的，因此它==必须执行 100000 次 undo==，不断向前 undo，==直到得到可见的那个事务==。
>
> 	而 `select * from T where id = 1 with share mode;` 是当前读，就不会进行一系列 undo。

### 7.为什么我的 Join 这么慢？

> 1. 在 Join 中，驱动表（外层）和被驱动表的顺序对于 Join 的执行性能有决定性影响。驱动表我们不得不进行全表扫描，但是对于被驱动表，本质上它执行的是对于驱动表遍历的数据的等值查询，因此如果被驱动表等值联立的字段上有索引的话，就可以大幅缩短查询时间。==将小表作为驱动表，大表或有索引的表作为被驱动表==。
>
> 2. 既然 Join 的顺序对于性能有决定性影响，那么有没有什么手段可以人为控制谁是驱动表，谁是被驱动表呢：
>
> 	```sql
> 	select * from t1 straight_join t2 on (t1.a=t2.a);
> 	```
>
> 	==使用 straight_join 可以固定连接的方式==。
>
> 3. 但是如果被驱动表没有索引，那就会采用 Block Nested-Loop 算法，消耗大量时间。我们可以使用 explain 查看 Extra 字段中是否有 `Using join buffer(Blocked Nested Loop)` 来判断 Join 操作是否会使用 Blocked Nested Loop 算法。

### 8.耳熟能详的 explain 到底怎么用？

1. 最为常见的扫描方式（==type==）有：

	> - ==system==：系统表，少量数据，往往不需要进行磁盘IO；
	> - ==const==：常量连接；
	> - ==eq_ref==：主键索引(primary key)或者非空唯一索引(unique not null)等值扫描；
	> - ==ref==：非主键非唯一索引等值扫描；
	> - ==range==：范围扫描；
	> - ==index==：索引树扫描；
	> - ==ALL==：全表扫描(full table scan)；

2. 上面各类扫描方式==由快到慢==：

	```java
	system > const > eq_ref > ref > range > index > ALL
	```

3. ==system==

	> - ```sql
	> 	explain select * from mysql.time_zone;
	> 	```
	>
	> 	上例中，从系统库 mysql 的系统表 time_zone 里查询数据，扫码类型 为 system，==这些数据已经加载到内存里，不需要进行磁盘 IO==。因此这类扫描是速度最快的。
	>
	> - ```sql
	> 	explain select * from (select * from user where id=1) tmp;
	> 	```
	>
	> 	再举一个例子，==内层嵌套(const)返回了一个临时表==，==外层嵌套从临时表查询，其扫描类型也是system==，也不需要走磁盘IO，速度超快。

4. ==const==

	> - const扫描的条件为：
	>
	>   （1）命中==主键(primary key)==或者==唯一索引(unique)==；
	>
	>   （2）被==连接的部分是一个常量(const)值（右边的值为常量）==；
	>
	>   ```sql
	>   explain select * from user where id=1;
	>   ```
	>
	>   如上例，id是PK，连接部分是常量1。
	>
	>   这类扫描效率极高，返回数据量少，速度非常快。
	
5. ==eq_ref==

	> - eq_ref扫描的条件为，对于==前表的每一行(row)==，==后表只有一行被扫描==。
	>
	> 	再细化一点：
	>
	> 	（1）==join查询==；
	>
	> 	（2）命中==主键(primary key)==或者非空==唯一(unique not null)索引==；
	>
	> 	（3）==等值连接==； 
	>
	> - ```sql
	> 	explain select * from user,user_ex where user.id = user_ex.id;
	> 	```
	>
	> 	如上例，==user.id 和 user_ex.id 都是主键==，该 join 查询为 eq_ref 扫描。
	>
	> 	这类扫描的速度也异常之快。

6. ==ref==

	> - 如果把上例eq_ref案例中的主键索引，改为普通==非唯一(non unique)索引==。
	>
	> 	```sql
	> 	explain select * from user,user_ex where user.id=user_ex.id;
	> 	```
	>
	> 	就==由 eq_ref 降级为了 ref==，此时对于前表的每一行(row)，后表可能有多于一行的数据被扫描。
	>
	> - 当id==改为普通非唯一索引后==，常量的连接查询，也由==const降级为了ref==，因为也==可能有多于一行的数据被扫描==。
	>
	> - ref扫描，可能出现在join里，也可能出现在==单表普通索引==里，==每一次匹配可能有多行数据返回==，虽然它比eq_ref要慢，但它仍然是一个很快的join类型。

7. ==range==

	> - 像上例中的 ==between，in，>== 都是典型的范围(range)查询。
	>
	> 	==必须是索引，否则不能批量“跳过”。==

8. ==index==

      > - 需要扫描索引上的所有数据。
      > - 使用场景：count(\*)的时候扫描索引上的所有数据。

9. ==All==

      > - 没有索引的全局扫描。
      > - 比如上述的 user_id 和 user_ex.id 上不建索引，对于前表的每一行(row)，后表都要被全表扫描。

9. ==总结==：

	> - ==system==最快：==不进行磁盘IO==
	> - ==const==：==PK或者unique==上的==等值查询（右侧是常量）==
	> - ==eq_ref==：==PK或者unique==上的==join查询==，==等值匹配（多表联立）==，对于前表的每一行(row)，后表只有一行命中
	> - ==ref==：==非唯一索引，等值匹配==，可能有多行命中
	> - ==range==：索引上的==范围扫描==，例如：==between、in、>\===
	> - ==index==：索引上的==全集扫描==，例如：InnoDB的==count==
	> - ==ALL==最慢：全表扫描(full table scan)

### 9.explain 的 Extra 字段有什么用？

1. ```sql
	drop table if exists `user`;
	create table user (
	    id int primary key,
	    name varchar(20),
	    sex varchar(5),
	    index(name)
	) engine=innodb;
	
	insert into user values(1, 'shenjian','no');
	insert into user values(2, 'zhangsan','no');
	insert into user values(3, 'lisi', 'yes');
	insert into user values(4, 'lisi', 'no');
	```
	
2. ==Using where==

	> - Extra为Using where说明，==SQL使用了where条件过滤数据==。

3. ==Using index==

	> - Extra为Using index说明，==SQL所需要返回的所有列数据均在一棵索引树上==，而无需访问实际的行记录（比如索引覆盖中，返回的字段都在索引树上，不需要进行回表）。
	>
	> - ```sql
	>  explain select id, name from user where name='shenjian';
	>   ```
	
4. ==Using index condition==

      > - Extra 为 Using index condition 说明，确实==命中了索引，但不是所有的列数据都在索引树上==，还需要访问实际的行记录。
      >
      > - ```sql
      > 	explain select id,name,sex from user where name like 'li';
      > 	```

5. ==Using filesort==

	> - ```sql
	> 	explain select * from user order by sex;
	> 	```
	>
	> - Extra为 Using filesort 说明，得到==所需结果集，需要对所有记录进行文件排序==。
	>
	> - 这类SQL语句==性能极差==，需要进行优化。
	>
	> - 典型的，==在一个没有建立索引的列上进行了order by，就会触发filesort==，常见的==优化方案==是，在==order by的列上添加索引==，==避免每次查询都全量排序==。
	
6. ==Using temporary==

	> - ```sql
	> 	explain select * from user group by name order by sex;
	> 	```
	>
	> - Extra为Using temporary说明，==需要建立临时表(temporary table)来暂存中间结果==。
	>
	> - 这类SQL语句==性能较低==，往往也需要进行优化。
	>
	> - 典型的，==group by 和 order by 同时存在，且作用于不同的字段时，就会建立临时表==，以便计算出最终的结果集。
	
7. ==Using join buffer (Blocked Nested Loop)==

	> - ```sql
	> 	explain select * from user join (select * from user where sex = 'no') tmp on user.sex = tmp.sex;
	> 	```
	>
	> - Extra为Using join buffer (Block Nested Loop)说明，需要进行==嵌套循环计算==。
	>
	> - 这类SQL语句==性能往往也较低==，需要进行优化。
	>
	> - 典型的，两个关联表 join，关联字段均未建立索引，就会出现这种情况。常见的优化方案是，==在关联字段上添加索引，避免每次嵌套循环计算==。

---

