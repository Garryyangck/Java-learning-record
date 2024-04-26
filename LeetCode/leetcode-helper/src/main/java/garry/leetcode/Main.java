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
        System.out.println(solution.leastInterval(new char[]{'A', 'A', 'A', 'B', 'B', 'B'}, 2));
    }
}

@SuppressWarnings({"all"})
class Solution {
    public int leastInterval(char[] tasks, int n) {
        int[] nums = new int[26];
        for (int i = 0; i < tasks.length; i++) {
            nums[(int) (tasks[i] - 'A')]++;
        }
        Arrays.sort(nums);
        int ans = (nums[nums.length - 1] - 1) * (n + 1) + 1;
        for (int i = nums.length - 1; i >= 0; i--) {
            if (nums[i] == 0)
                return ans;
            ans = Math.max(ans, (nums[i] - 1) * (n + 1) + 1 + (25 - i));
        }
        return Math.max(ans, tasks.length);
    }
}