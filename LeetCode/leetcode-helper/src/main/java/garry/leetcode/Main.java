package garry.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * @author Garry
 * ---------2024/3/22 11:40
 **/

public class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.permuteUnique(new int[]{1, 1, 2});
    }
}

@SuppressWarnings({"all"})
class Solution {
    public List<List<Integer>> ans = new ArrayList<>();
    public List<Integer> now = new ArrayList<>();
    public boolean[] visit = null;
    public int[] nums;

    public List<List<Integer>> permuteUnique(int[] nums) {
        visit = new boolean[nums.length];
        Arrays.sort(nums);
        this.nums = nums;
        backTrace(0);
        return ans;
    }

    public void backTrace(int start) {
        if (now.size() >= nums.length) {
            ans.add(new ArrayList(now));
            return;
        }

        for (int i = 0; i < nums.length; i++) {
            if (!visit[i]) {
                if (start >= nums.length - 1 && i > 0 && nums[i] == nums[i - 1]) {
                    continue;
                }
                if (i > start && nums[i] == nums[i - 1]) {
                    continue;
                }
                visit[i] = true;
                now.add(nums[i]);
                backTrace(start + 1);
                visit[i] = false;
                now.remove(now.size() - 1);
            }
        }
    }
}