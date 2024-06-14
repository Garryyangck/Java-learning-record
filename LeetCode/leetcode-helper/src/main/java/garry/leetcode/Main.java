package garry.leetcode;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @author Garry
 * ---------2024/3/22 11:40
 **/

public class Main {
    public static void main(String[] args) {
        System.out.println(new Solution().crackPassword(new int[]{0, 30, 3, 34, 5, 9}));
    }
}

class Solution {
    public String crackPassword(int[] password) {
        String[] arr = new String[password.length];
        for (int i = 0; i < password.length; i++) {
            arr[i] = String.valueOf(password[i]);
        }
        Arrays.sort(arr, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return (s1 + s2).compareTo(s2 + s1);
            }
        });
        StringBuffer ans = new StringBuffer();
        for (int i = 0; i < password.length; i++) {
            ans.append(arr[i]);
        }
        return new String(ans);
    }
}