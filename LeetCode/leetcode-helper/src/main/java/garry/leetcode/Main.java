package garry.leetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Garry
 * ---------2024/3/22 11:40
 **/

public class Main {
    public static void main(String[] args) {
    }
}

class LFUCache {
    class DLinkedNode {
        int key;
        int value;
        int cnt = 0;
        DLinkedNode pre;
        DLinkedNode next;

        public DLinkedNode() {
        }

        public DLinkedNode(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    private final Map<Integer/* key */, DLinkedNode/* specific node */> map = new HashMap<>();
    private final Map<Integer/* count */, List<DLinkedNode>/* count list */> counter = new HashMap<>();
    private int minCnt = Integer.MAX_VALUE;
    private int size;
    private final int capacity;

    public LFUCache(int capacity) {
        this.size = 0;
        this.capacity = capacity;
    }

    public int get(int key) {
        if (map.containsKey(key)) {
            addCnt(map.get(key));
            return map.get(key).value;
        } else {
            return -1;
        }
    }

    public void put(int key, int value) {
        if (map.containsKey(key)) {
            DLinkedNode node = map.get(key);
            node.value = value;
            addCnt(node);
        } else {
            if (size >= capacity) {
                DLinkedNode delNode = counter.get(minCnt).get(0);
                map.remove(delNode.key);
                counter.get(minCnt).remove(0);
                if (counter.get(minCnt).isEmpty())
                    counter.remove(minCnt);
                size--;
            }
            DLinkedNode newNode = new DLinkedNode(key, value);
            map.put(key, newNode);
            addCnt(newNode);
            size++;
        }
    }

    private void addCnt(DLinkedNode node) {
        List<DLinkedNode> preNodeList = counter.get(node.cnt);
        if (preNodeList != null)
            preNodeList.remove(node);
        if (minCnt == node.cnt && (preNodeList == null || preNodeList.size() == 0))
            minCnt++;
        else
            minCnt = Math.min(minCnt, node.cnt + 1);
        node.cnt++;
        List<DLinkedNode> nodeList = counter.get(node.cnt);
        if (nodeList == null)
            nodeList = new ArrayList<>();
        nodeList.add(node);
        counter.put(node.cnt, nodeList);
    }
}