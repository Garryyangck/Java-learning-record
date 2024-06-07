package garry.leetcode;

/**
 * @author Garry
 * ---------2024/3/22 11:40
 **/

public class Main {
    public static void main(String[] args) {
        int[] arr = new int[]{1};
        int target = 1;
        new Solution().countTarget(arr, target);
    }
}

class Solution {
    public int countTarget(int[] scores, int target) {
        if (scores.length == 0)
            return 0;
        int left = 0;
        int right = scores.length - 1;
        while (left < right) {
            int mid = (left + right) / 2; // 左偏中点
            if (scores[mid] == target) {
                int cnt = 0;
                int i = mid;
                while (i - 1 >= 0 && scores[i] == scores[i - 1]) {
                    i--;
                    cnt++;
                }
                i = mid;
                while (i + 1 < scores.length && scores[i] == scores[i + 1]) {
                    i++;
                    cnt++;
                }
                return cnt + 1;
            } else if (scores[mid] > target) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return scores[left] == target ? 1 : 0;
    }
}