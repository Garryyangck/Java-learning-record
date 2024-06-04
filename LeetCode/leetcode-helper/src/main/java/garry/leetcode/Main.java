package garry.leetcode;

import garry.leetcode.utils.TreeNode;

import java.util.Arrays;

/**
 * @author Garry
 * ---------2024/3/22 11:40
 **/

public class Main {
    public static void main(String[] args) {
        int[] arr = {1, 2};
        int[] ans = Arrays.copyOfRange(arr, 0, 0);
        for (int i = 0; i < ans.length; i++) {
            System.out.println(ans[i]);
        }
    }
}

class Solution {
    public boolean isSubStructure(TreeNode A, TreeNode B) {
        if (A == null || B == null)
            return false;
        if (A.val == B.val) {
            boolean left = false;
            if (B.left == null)
                left = true;
            else if (A.left != null && A.left.val == B.left.val)
                left = isSubStructure(A.left, B.left);
            boolean right = false;
            if (B.right == null)
                right = true;
            else if (A.right != null && A.right.val == B.right.val)
                right = isSubStructure(A.right, B.right);
            if (left && right)
                return true;
        }
        return isSubStructure(A.left, B) || isSubStructure(A.right, B);
    }
}

