package garry.leetcode;

/**
 * @author Garry
 * ---------2024/3/22 11:40
 **/

public class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.maxProfit(2, new int[]{2, 4, 1});
    }
}

class Solution {
    public int maxProfit(int k, int[] prices) {
        if (prices.length == 1)
            return 0;

        int[][] dp = new int[prices.length][2 * k + 1];
        // 初始化dp[0][j]
        for (int i = 1; i < 2 * k + 1; i += 2) {
            dp[0][i] = 0;
        }
        dp[0][1] = -prices[0];

        for (int i = 1; i < prices.length; i++) {
            dp[i][0] = dp[i - 1][0];
            for (int j = 1; j < 2 * k + 1; j++) {
                if (j % 2 == 0) { // 没有股票
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - 1] + prices[i]);
                } else { // 有股票
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - 1] - prices[i]);
                }
            }
        }

        int ans = 0;
        for (int i = 0; i < 2 * k + 1; i++) {
            ans = Math.max(ans, dp[prices.length - 1][i]);
        }
        return ans;
    }
}