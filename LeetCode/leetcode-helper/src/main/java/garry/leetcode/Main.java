package garry.leetcode;

import java.util.*;

/**
 * @author Garry
 * ---------2024/3/22 11:40
 **/

public class Main {
    public static void main(String[] args) {
        ArrayList<List<String>> accounts = new ArrayList<>();
        ArrayList<String> account1 = new ArrayList<>();
        account1.add("John");
        account1.add("johnsmith@mail.com");
        account1.add("john00@mail.com");
        ArrayList<String> account2 = new ArrayList<>();
        account2.add("John");
        account2.add("johnnybravo@mail.com");
        ArrayList<String> account3 = new ArrayList<>();
        account3.add("John");
        account3.add("johnsmith@mail.com");
        account3.add("john_newyork@mail.com");
        ArrayList<String> account4 = new ArrayList<>();
        account4.add("Mary");
        account4.add("mary@mail.com");
        accounts.add(account1);
        accounts.add(account2);
        accounts.add(account3);
        accounts.add(account4);
        System.out.println(new Solution().accountsMerge(accounts));
    }
}

class Solution {
    public List<List<String>> accountsMerge(List<List<String>> accounts) {
        int n = accounts.size();
        // 创建并查集
        UnionFind uf = new UnionFind(n);
        // 便于我们做并查集的合并
        Map<String/* email */, Integer/* index of username */> map = new HashMap<>();

        // 将并查集中的元素进行合并
        for (int index = 0; index < n; index++) {
            List<String> account = accounts.get(index);
            account = account.subList(1, account.size());
            for (String email : account) {
                if (!map.containsKey(email)) { // email 没有重复
                    map.put(email, index);
                } else { // email 出现重复，即需要合并
                    uf.union(index, map.get(email));
                }
            }
        }

        // 合并相同账户
        List<List<String>> ans = new ArrayList<>();
        Map<Integer/* 根节点 */, Integer/* 在ans中的下标位置 */> mp = new HashMap<>();
        for (int index = 0; index < n; index++) {
            int rootIndex = uf.find(index);
            if (!mp.containsKey(rootIndex)) {
                List<String> account = new ArrayList<>(accounts.get(index));
                mp.put(rootIndex, ans.size());
                ans.add(account);
            } else { // 已有相同根节点插入，当前账户和那个账户合并
                List<String> oldAccount = ans.get(mp.get(rootIndex));
                List<String> addAccount = new ArrayList<>(accounts.get(index));
                for (int i = 1; i < addAccount.size(); i++)
                    oldAccount.add(addAccount.get(i));
            }
        }

        List<List<String>> res = new ArrayList<>();
        // 排序并去重 email
        for (List<String> account : ans) {
            List<String> emails = account.subList(1, account.size());
            emails.sort(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareTo(o2);
                }
            });
            List<String> newAccount = new ArrayList<>();
            newAccount.add(account.get(0));
            for (int i = 0; i < emails.size(); i++) {
                if (i > 0 && emails.get(i).equals(emails.get(i - 1))) {
                    continue;
                } else {
                    newAccount.add(emails.get(i));
                }
            }
            res.add(newAccount);
        }
        return res;
    }
}

class UnionFind {
    int[] parent;

    public UnionFind(int n) {
        parent = new int[n];
        for (int i = 0; i < n; i++)
            parent[i] = i; // 每个元素最开始没有连接的时候，都指向自己，表示独立
    }

    /*
     * 联合，本质就是将它们各自唯一的根节点连接起来
     */
    public void union(int index1, int index2) {
        parent[find(index2)] = find(index1); // index2 的根节点的父节点设置为 index1 的根节点
    }

    /*
     * 查询 index 的根节点
     */
    public int find(int index) {
        if (parent[index] != index) // 父节点不是自己，即自己不是根节点，查询父节点的根节点
            return parent[index] = find(parent[index]);
        else
            return index;
    }
}