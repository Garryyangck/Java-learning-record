package garry.leetcode;

/**
 * @author Garry
 * ---------2024/3/22 11:40
 **/

public class Main {
    public static void main(String[] args) {
        new Solution().mySqrt(Integer.MAX_VALUE);
    }
}

class Solution {
    public int mySqrt(int x) {
        if (x == 0 || x == 1)
            return x;

        int left = 0, right = x;
        while (left < right) {
            int mid = (left + right + 1) / 2; // 偏右中点，让最后答案偏左，得到省略小数部分的结果
            if (mid * 1L * mid == x)
                return mid;
            if (mid * 1L * mid < x) {
                left = mid;
            } else {
                right = mid - 1;
            }
        }

        return left;
    }
}