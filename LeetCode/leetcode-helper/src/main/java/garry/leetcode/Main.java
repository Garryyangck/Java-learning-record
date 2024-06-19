package garry.leetcode;

/**
 * @author Garry
 * ---------2024/3/22 11:40
 **/

public class Main {
    public static void main(String[] args) {
        new Solution().articleMatch("", ".*");
    }
}

class Solution {
    public boolean articleMatch(String s, String p) {
        int len1 = s.length();
        int len2 = p.length();
        boolean[][] dp = new boolean[len1 + 1][len2 + 1];
        dp[0][0] = true;
        for (int j = 2; j <= len2; j++) {
            // s为空，因此*必须全部匹配0个才有可能成功
            if (p.charAt(j - 1) == '*') {
                dp[0][j] = dp[0][j - 2];
            }
        }

        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                // 不是*的情况
                if (p.charAt(j - 1) != '*') {
                    // 匹配成功
                    if (p.charAt(j - 1) == '.' || p.charAt(j - 1) == s.charAt(i - 1)) {
                        dp[i][j] = dp[i - 1][j - 1];
                    }
                    // 匹配不成功，寄
                    else {
                        dp[i][j] = false;
                    }
                }
                // 是*的情况
                else {
                    // 匹配不成功，只能匹配0个
                    if (p.charAt(j - 2) != '.' && p.charAt(j - 2) != s.charAt(i - 1)) {
                        dp[i][j] = dp[i][j - 2];
                    }
                    // 匹配成功，可以尝试匹配0个，1个，多个
                    else {
                        dp[i][j] = dp[i][j - 2] ||
                                dp[i - 1][j - 2] ||
                                dp[i - 1][j]; // 匹配多个，相当于用这两个字符继续向前匹配
                    }
                }
            }
        }

        return dp[len1][len2];
    }
}