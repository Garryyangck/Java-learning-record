## 红黑树

1. > ![image-20240430120355893](DataStructure.assets/image-20240430120355893.png)

2. > ![image-20240430120511759](DataStructure.assets/image-20240430120511759.png)

3. 红黑树和2-3树是等价的，因此由于2-3树是绝对平衡的，因此红黑树根节点到任意一个空节点所经过的黑色节点（一个黑色节点代表要么是一个2节点，要么是一个3节点）总数是相同的。

4. 红黑树是一颗“黑平衡的树”，在添加和删除节点的过程中比AVL树有更好的性能。

---



## 并查集

1. 对于一组数据，支持两种操作：

	```java
	union(E p, E q);
	isConnected(E p, E q);
	// 之所以使用 E，是因为并查集中并不需要考虑两个元素的类型是什么，只需体现它们之间的连接关系
	```

2. ==基于 Quick Find 的实现思路==：

	> find O(1)，union O(n)
	>
	> 1. <img src="DataStructure.assets/image-20240527210420638.png" alt="image-20240527210420638" style="zoom:50%;" />
	>
	> 2. find 函数，查询 index 数据所属的集合
	>
	> 	```java
	> 	find(E q);
	> 	```
	>
	> 3. ```java
	> 	public UnionFind(int n) {
	> 	    parent = new int[n];
	> 	    for (int i = 0; i < n; i++) {
	> 	        parent[i] = i; // 每个元素最开始没有连接的时候，都独属于一个集合
	> 	    }
	> 	}
	> 	```
	>
	> 4. 假如上图中的 0 和 5 union，那么它们所属集合的所有元素也会相连，表现是最终所有元素相连。

3. 如果满足 `find(p) == find(q)`，那么就可以认为 p 和 q 所属同一个集合，即 `isConnected`。

4. ==基于 Quick Union 的实现思路（常用思路）==：

	> 1. <img src="DataStructure.assets/image-20240527213152360.png" alt="image-20240527213152360" style="zoom:33%;" />
	>
	> 2. ==执行 union 后的结果==：
	>
	> 	<img src="DataStructure.assets/image-20240527213252319.png" alt="image-20240527213252319" style="zoom:33%;" />
	>
	> 3. <img src="DataStructure.assets/image-20240527213330144.png" alt="image-20240527213330144" style="zoom:50%;" />
	>
	> 4. 最终表现为树的结果，其中根节点的 parent 为自己，即指向自己。
	>
	> 	<img src="DataStructure.assets/image-20240527213651921.png" alt="image-20240527213651921" style="zoom:50%;" />
	>
	> 5. Union O(h)，Find O(h)，其中 h 为树的高度。
	>
	> 6. ```java
	> 	class UnionFind {
	> 	    int[] parent;
	> 	
	> 	    public UnionFind(int n) {
	> 	        parent = new int[n];
	> 	        for (int i = 0; i < n; i++)
	> 	            parent[i] = i; // 每个元素最开始没有连接的时候，都指向自己，表示独立
	> 	    }
	> 	
	> 	    /*
	> 	     * 联合，本质就是将它们各自唯一的根节点连接起来
	> 	     */
	> 	    public void union(int index1, int index2) {
	> 	        parent[find(index2)] = find(index1); // index2 的根节点的父节点设置为 index1 的根节点
	> 	    }
	> 	
	> 	    public boolean isConnected(int index1, int index2) {
	> 	        return find(index1) == find(index2);
	> 	    }
	> 	
	> 	    /*
	> 	     * 查询 index 的根节点
	> 	     */
	> 	    p int find(int index) {
	> 	        if (parent[index] != index) // 父节点不是自己，即自己不是根节点，查询父节点的根节点
	> 	            return parent[index] = find(parent[index]);
	> 	        else
	> 	            return index;
	> 	    }
	> 	}
	> 	```

---



