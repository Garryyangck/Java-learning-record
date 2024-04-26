1.==在mybatis-config.xml中添加如下标签：==

```xml
<!--将数据库属性如 brand_name -> brandName -->
<settings>
    <setting name="mapUnderscoreToCamelCase" value="true"/>
</settings>
```



2.==利用LinkedHashMap进行多表查询，可以保持顺序：==

```xml
<select id-"xxx" resultType="java.util.LinkedHashMap">
	<!--sql...-->
</select>
```



3.==insert完一个商品后将自动生成的id添加到goods实例中：==

```xml
<insert id="add">
    insert into tb_brand (brand_name, company_name, ordered, description, status)
    values (#{brandName}, #{companyName}, #{ordered}, #{description}, #{status});
    <selectKey resultType="Integer" keyProperty="id" order="AFTER">
        select last_insert_id()<!--mysql自带方法-->
    </selectKey>
</insert>

<!--或采用useGeneratedKeys的方法：-->
<insert id="add" useGeneratedKeys="true" keyProperty="id" keyColumn="id">
    insert into tb_brand (brand_name, company_name, ordered, description, status)
    values (#{brandName}, #{companyName}, #{ordered}, #{description}, #{status});
</insert>

<!--useGeneratedKeys只支持"自增主键"的数据库，而selectKey支持所有关系型数据库-->
```



4.==Mybatis一级缓存默认开启，作用范围是当前的sqlSession会话==，比如在同一sqlSession会话中调用查找两次相同的数据，第一次会从磁盘中的数据库中读取并存到一级缓存中，第二次则直接从内存(一级缓存)中查找，大大提高了效率，两次查找得到的对象相同(hashCode一致)。



5.==sqlSession.commit()会强制清空缓存中的数据==(因为涉及到增删改，数据在数据库中已更改，此时再先从一级缓存中查找就会找到修改前的旧数据)



6.==二级缓存的开启：==

```xml
<!--在mapper中.xml文件的namespace下添加标签-->
<cache eviction="LRU" flushInterval="600000" size="512" readOnly="true"/>
```



7.eviction：缓存清除策略(LRU, FIFO, SOFT, WEAK)；flushInterval：缓存时间。单位ms



8.二级缓存不适用于储存List集合，因为命中率低



9.多对一，一对多讲得太烂了，一堆名字分都分不清



10.pagehelper插件，可通过Maven插入



11.连接池，可以在mybatis-config.xml中：

```xml
<dataSource type="POOLED">
<!--数据库连接信息-->
</dataSource>

改为：

<dataSource type="你需要的连接池(druid,c3p0...)">
<!--数据库连接信息-->
</dataSource>

注意创建一个对应的xxxDataSourceFactory类，继承UnpooledDataSourceFactory，然后在构造器中:
this.dataSource = new xxxPooledDataSource
就可以取代sqlSessionFactory了，如druidDataSourceFactory
```



12.批处理，<foreach>标签