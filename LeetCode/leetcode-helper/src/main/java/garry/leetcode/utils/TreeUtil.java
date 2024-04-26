package garry.leetcode.utils;

/**
 * @author Garry
 * ---------2024/3/22 11:44
 **/

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 二叉树基于层次遍历的序列化和反序列化
 */
public class TreeUtil {
    /**
     * 树序列化
     *
     * @param root
     * @return
     */
    public static String serialize(TreeNode root) {
        if (root == null) return "[]";
        List<String> valList = new ArrayList<String>();
        Queue<TreeNode> queue = new LinkedList<TreeNode>();
        queue.add(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            boolean hasNode = false;
            List<TreeNode> level = new ArrayList<TreeNode>();
            for (int i = 0; i < size; i++) {
                TreeNode poll = queue.poll();
                level.add(poll);//无论是不是null都加入
                if (poll == null) {
                    queue.add(null);
                    queue.add(null);
                } else {//poll不为null
                    hasNode = true;
                    queue.add(poll.left);
                    queue.add(poll.right);
                }
            }
            if (!hasNode) break;
            for (TreeNode node : level) {
                if (node == null) {
                    valList.add("null");
                } else {
                    valList.add(String.valueOf(node.val));
                }
            }
        }
        StringBuilder withoutEdge = new StringBuilder();
        for (String val : valList) {
            withoutEdge.append(val).append(",");
        }
        withoutEdge = new StringBuilder(withoutEdge.substring(0, withoutEdge.length() - 1));
        return "[" + withoutEdge + "]";
    }

    /**
     * 反序列化得到树
     *
     * @param data
     * @return
     */
    public static TreeNode deserialize(String data) {
        if (data.equals("[]")) return null;
        data = data.substring(0, data.length() - 1).substring(1);
        String[] valList = data.split(",");
        return createNode(0, valList);
    }

    private static TreeNode createNode(int index, String[] valList) {
        if (index >= valList.length) return null;
        if (valList[index].equals("null")) return null;
        TreeNode newNode = new TreeNode(Integer.parseInt(valList[index]));
        int leftIndex = (index + 1) * 2 - 1;
        int rightIndex = (index + 1) * 2;
        newNode.left = createNode(leftIndex, valList);
        newNode.right = createNode(rightIndex, valList);
        return newNode;
    }
}
