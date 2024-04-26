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
        solution.singleNumber(new int[]{0, 1, 0, 1, 0, 1, 99});
    }
}

@SuppressWarnings({"all"})
class Solution {
    public int singleNumber(int[] nums) {
        int[] wei = new int[32];
        int ans = 0;
        for (int i = 31; i >= 0; i--) {
            for (int j = 0; j < nums.length; j++) {
                if (((nums[j] >>> i) & 1) == 1)
                    wei[i]++;
            }
            if (wei[i] % 3 != 0) {
                ans <<= 1;
                ans |= 1;
            }
        }
        return ans;
    }
}