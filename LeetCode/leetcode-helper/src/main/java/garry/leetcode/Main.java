package garry.leetcode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Garry
 * ---------2024/3/22 11:40
 **/

public class Main {
    public static void main(String[] args) {
        new Solution().maxAltitude(new int[]{14, 2, 27, -5, 28, 13, 39}, 3);
    }
}

class Solution {
    public int[] maxAltitude(int[] heights, int limit) {
        LinkedList<Integer/* 存放index，要求heights[index]递减 */> deque = new LinkedList<>();
        for (int i = 0; i < limit; i++) {
            // 将前面<=的都出队，因为等于时后面的index更大，更后受到出队的限制
            while (!deque.isEmpty() && heights[deque.getLast()] <= heights[i])
                deque.removeLast();
            deque.addLast(i);
        }
        List<Integer> ans = new ArrayList<>();
        ans.add(heights[deque.getFirst()]);
        for (int i = limit; i < heights.length; i++) {
            while (!deque.isEmpty() && heights[deque.getLast()] <= heights[i])
                deque.removeLast();
            while (!deque.isEmpty() && deque.getFirst() <= i - limit)
                deque.removeFirst();
            deque.addLast(i);
            ans.add(heights[deque.getFirst()]);
        }
        int[] res = new int[ans.size()];
        for (int i = 0; i < ans.size(); i++) {
            res[i] = ans.get(i);
        }
        return res;
    }
}