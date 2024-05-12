package garry.leetcode;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author Garry
 * ---------2024/3/22 11:40
 **/

public class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.merge(new int[][]{new int[]{1, 3}, new int[]{2, 6}, new int[]{8, 10}, new int[]{15, 18}});
    }
}

class Solution {
    public int[][] merge(int[][] intervals) {
        int len = intervals.length;
        if (len == 1)
            return intervals;
        Arrays.sort(intervals, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                if (o1[1] != o2[1])
                    return o1[1] - o2[1];
                else
                    return o1[0] - o2[0];
            }
        });
        List<Pair<Integer, Integer>> ans = new ArrayList<>();
        int begin = intervals[0][0];
        int end = intervals[0][1];
        for (int i = 1; i < len; i++) {
            if (intervals[i][0] <= end) {
                end = intervals[i][1];
            } else {
                ans.add(new Pair(begin, end));
                begin = intervals[i][0];
                end = intervals[i][1];
            }
        }
        int[][] res = new int[ans.size()][];
        int i = 0;
        for (Pair<Integer, Integer> interval : ans) {
            int[] tmp = new int[2];
            tmp[0] = interval.getKey();
            tmp[1] = interval.getValue();
            res[i++] = tmp;
        }
        return res;
    }
}