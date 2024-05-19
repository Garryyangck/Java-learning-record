package garry.leetcode;

/**
 * @author Garry
 * ---------2024/3/22 11:40
 **/

public class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.trap(new int[]{5, 5, 1, 7, 1, 1, 5, 2, 7, 6});
    }
}

class Solution {
    public int trap(int[] height) {
        int ans = 0;
        int left = 0, right = height.length - 1;
        int maxLeft = height[left], maxRight = height[right];
        while (left < right) {
            while (left < right && maxLeft <= maxRight) {
                if (height[left] > maxLeft) {
                    maxLeft = height[left];
                } else {
                    ans += maxLeft - height[left];
                }
                left++;
            }
            while (left < right && maxRight < maxLeft) {
                if (height[right] > maxRight) {
                    maxRight = height[right];
                } else {
                    ans += maxRight - height[right];
                }
                right--;
                ;
            }
        }
        return ans;
    }
}