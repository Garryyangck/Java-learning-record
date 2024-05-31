package garry.leetcode;

import garry.leetcode.utils.ListNode;

/**
 * @author Garry
 * ---------2024/3/22 11:40
 **/

public class Main {
    public static void main(String[] args) {
        ListNode node1 = new ListNode(9, new ListNode(9, new ListNode(9, new ListNode(9))));
        ListNode node2 = new ListNode(9, new ListNode(9, new ListNode(9, new ListNode(9))));
        ListNode plus = new Solution().plus(node1, node2);
    }
}

class Solution {
    public ListNode plus(ListNode head1, ListNode head2) {
        if (head1 == null) return head2;
        if (head2 == null) return head1;
        ListNode num1 = reverse(head1);
        ListNode num2 = reverse(head2);
        boolean jinwei = false;
        ListNode pre = new ListNode(-1);
        ListNode ans = pre;
        while (num1 != null || num2 != null || jinwei) {
            int num = jinwei ? 1 : 0;
            if (num1 != null) num += num1.val;
            if (num2 != null) num += num2.val;
            if (num >= 10) jinwei = true;
            else jinwei = false;
            pre.next = new ListNode(num % 10);
            if (num1 != null) num1 = num1.next;
            if (num2 != null) num2 = num2.next;
            pre = pre.next;
        }
        return reverse(ans.next);
    }

    private ListNode reverse(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode newNode = reverse(head.next);
        head.next.next = head;
        head.next = null;
        return newNode;
    }
}

