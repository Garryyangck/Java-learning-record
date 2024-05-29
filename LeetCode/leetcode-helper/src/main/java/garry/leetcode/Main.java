package garry.leetcode;

import garry.leetcode.utils.ListNode;

/**
 * @author Garry
 * ---------2024/3/22 11:40
 **/

public class Main {
    public static void main(String[] args) {
        ListNode node = new ListNode(4, new ListNode(5, new ListNode(1, new ListNode(9))));
        new Solution().deleteNode(node, 5);
    }
}

class Solution {
    public ListNode deleteNode(ListNode head, int val) {
        if (head == null)
            return head;
        ListNode pre = new ListNode(-1);
        ListNode ans = pre;
        pre.next = head;
        ListNode post = head;
        while (post != null && post.val != val) {
            post = post.next;
            pre = pre.next;
        }
        pre.next = post.next;
        return pre.next;
    }
}

