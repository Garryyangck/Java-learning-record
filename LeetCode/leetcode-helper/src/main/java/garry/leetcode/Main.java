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
    public int[] sockCollocation(int[] sockets) {
        int ans = 0;
        for (int i = 0; i < sockets.length; i++) {
            ans ^= sockets[i];
        }
        int diff = 1;
        while (ans != 0) {
            if ((diff & ans) != 0)
                break;
            diff <<= 1;
        }
        int res1 = 0, res2 = 0;
        for (int i = 0; i < sockets.length; i++) {
            if ((sockets[i] & diff) == 0)
                res1 ^= sockets[i];
            else
                res2 ^= sockets[i];
        }
        return new int[]{res1, res2};
    }
}