package garry.leetcode.utils;

/**
 * @author Garry
 * ---------2024/5/28 11:39
 **/
public class UnionFind {
    private final int[] parent;

    public UnionFind(int n) {
        parent = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i; // 每个元素最开始没有连接的时候，都指向自己，表示独立
        }
    }

    /*
     * 联合，本质就是将它们各自唯一的根节点连接起来
     */
    public void union(int index1, int index2) {
        parent[find(index2)] = find(index1); // 由于在 find 中采取了路径压缩方法，把所有的树高度压缩到2，因此 union 中无需根据 size 或 rank 优化了，因为已经在 find 中得到优化了
    }

    public boolean isConnected(int index1, int index2) {
        return find(index1) == find(index2);
    }

    /*
     * 查询 index 的根节点
     */
    public int find(int index) {
        if (parent[index] != index) // 父节点不是自己，即自己不是根节点，查询父节点的根节点
            return parent[index] = find(parent[index]); // 将树压缩成高度为2的树，即根节点在第一层，其它所有子节点都在第二层
        else
            return index;
    }
}
