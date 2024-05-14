package garry.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Garry
 * ---------2024/3/22 11:40
 **/

public class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.threeSum(new int[]{-1, 0, 1, 2, -1, -4});
    }
}

class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        int n = nums.length;
        Arrays.sort(nums);
        List<List<Integer>> ans = new ArrayList<>();
        for (int first = 0; first < n; first++) {
            if (first > 0 && nums[first] == nums[first - 1])
                continue;
            for (int third = n - 1; third >= 0; third--) {
                if (third < n - 1 && nums[third] == nums[third + 1])
                    continue;
                for (int second = first + 1; second < third; second++) {
                    if (second > first + 1 && nums[second] == nums[second - 1])
                        continue;
                    if (nums[first] + nums[second] + nums[third] == 0) {
                        List<Integer> list = new ArrayList<>();
                        list.add(nums[first]);
                        list.add(nums[second]);
                        list.add(nums[third]);
                        ans.add(list);
                    }
                }
            }
        }
        return ans;
    }
}