package garry.leetcode;

import garry.leetcode.utils.ListNode;
import javafx.util.Pair;
import sun.misc.LRUCache;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author Garry
 * ---------2024/3/22 11:40
 **/

public class Main {
    @SuppressWarnings("ImplicitArrayToString")
    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(solution.combine(4, 2));
    }
}

@SuppressWarnings({"all"})
class Solution {
    private List<List<Integer>> ans = new ArrayList<>();
    private List<Integer> now = new ArrayList<>();
    private int n;
    private int k;

    public List<List<Integer>> combine(int n, int k) {
        this.n = n;
        this.k = k;
        backTrace(1);
        return ans;
    }

    private void backTrace(int index) {
        if (now.size() >= k) {
            ans.add(new ArrayList(now));
            return;
        }

        for (int i = index; i <= n; i++) {
                now.add(i);
            backTrace(index + 1);
            now.remove(now.size() - 1);
        }
    }
}