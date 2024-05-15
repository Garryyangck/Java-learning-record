package garry.leetcode;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
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
    public int[] maxSlidingWindow(int[] nums, int k) {
        Deque<Integer/* index */> deque = new LinkedList<>(); // 存储index，按nums[i]单调递减
        // 初始化第一个滑动窗口
        for (int i = 0; i < k; i++) {
            while (!deque.isEmpty() && nums[deque.getLast()] <= nums[i])
                deque.removeLast();
            deque.addLast(i);
        }
        List<Integer> ans = new ArrayList<>();
        ans.add(nums[deque.getFirst()]);
        for (int i = k; i < nums.length; i++) {
            while (!deque.isEmpty() && deque.getFirst() <= i - k)
                deque.removeFirst();
            while (!deque.isEmpty() && nums[deque.getLast()] <= nums[i])
                deque.removeLast();
            deque.addLast(i);
            ans.add(nums[deque.getFirst()]);
        }
        return ans.toArray(new int[ans.size()]);
    }
}