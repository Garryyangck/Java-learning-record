package garry.leetcode;

/**
 * @author Garry
 * ---------2024/3/22 11:40
 **/

public class Main {
    public static void main(String[] args) {
        new Solution().nthUglyNumber(10);
    }
}

class Solution {
    public int nthUglyNumber(int n) {
        int[] dp = new int[1600];
        int twoIndex = 0, threeIndex = 0, fiveIndex = 0;
        dp[0] = 1;
        for (int i = 1; i < dp.length; i++) {
            int twoMul = 2 * dp[twoIndex];
            int threeMul = 3 * dp[threeIndex];
            int fiveMul = 5 * dp[fiveIndex];
            int min = Math.min(Math.min(twoMul, threeMul), fiveMul);
            if (twoMul == min)
                twoIndex++;
            if (threeMul == min)
                threeIndex++;
            if (fiveMul == min)
                fiveIndex++;
            dp[i] = min;
        }
        return dp[n - 1];
    }
}