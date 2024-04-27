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
        solution.candy(new int[]{3, 2, 1, 1, 4, 3, 3});
    }
}

@SuppressWarnings({"all"})
class Solution {
    public int candy(int[] ratings) {
        int[] candy = new int[ratings.length];
        for(int i = 0; i < ratings.length - 1; i++) {
            if(i == 0) {
                if(ratings[i] > ratings[i + 1]) {
                    candy[i] = Math.max(candy[i], candy[i + 1] + 1);
                }
                continue;
            }
            if(ratings[i] > ratings[i + 1]) {
                candy[i] = Math.max(candy[i], candy[i + 1] + 1);
            }
            if(ratings[i] > ratings[i - 1]) {
                candy[i] = Math.max(candy[i], candy[i - 1] + 1);
            }
        }
        for(int i = ratings.length - 1; i > 0; i--) {
            if(i == ratings.length - 1) {
                if(ratings[i] > ratings[i - 1]) {
                    candy[i] = Math.max(candy[i], candy[i - 1] + 1);
                }
                continue;
            }
            if(ratings[i] > ratings[i + 1]) {
                candy[i] = Math.max(candy[i], candy[i + 1] + 1);
            }
            if(ratings[i] > ratings[i - 1]) {
                candy[i] = Math.max(candy[i], candy[i - 1] + 1);
            }
        }
        int ans = candy.length;
        for(int i = 0; i < candy.length; i++) {
            ans += candy[i];
        }
        return ans;
    }
}