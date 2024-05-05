package garry.leetcode;

import java.util.Arrays;

/**
 * @author Garry
 * ---------2024/3/22 11:40
 **/

public class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.coinChange(new int[]{2, 5, 1}, 11);
    }
}

class Solution {
    public int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        for(int i = 0; i <= amount; i++)
            dp[i] = Integer.MAX_VALUE;

        dp[0] = 0;
        Arrays.sort(coins);

        for(int i = 0; i < coins.length; i++) {
            for(int num = 1; num * coins[i] <= amount; num++) {
                dp[num * coins[i]] = num;
            }
        }

        for(int i = 0; i <= amount; i++) {
            for(int j = 0; j < coins.length; j++) {
                if(i - coins[j] < 0)
                    break;
                if(dp[i - coins[j]] == Integer.MAX_VALUE)
                    continue;
                dp[i] = Math.min(dp[i], dp[i - coins[j]] + 1);
            }
        }
        return dp[amount] == Integer.MAX_VALUE ? -1 : dp[amount];
    }
}