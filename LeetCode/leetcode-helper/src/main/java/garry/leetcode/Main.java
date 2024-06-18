package garry.leetcode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Garry
 * ---------2024/3/22 11:40
 **/

public class Main {
    public static void main(String[] args) {
        
    }
}

class Solution {
    private final ReentrantLock lock = new ReentrantLock(true);
    private String goods;
    private StringBuffer buffer;
    private boolean[] visit;
    private List<String> ans;

    public String[] goodsOrder(String goods) {
        this.buffer = new StringBuffer();
        this.goods = goods;
        this.visit = new boolean[goods.length()];
        this.ans = new ArrayList<>();
        backTrace(0);
        //noinspection ToArrayCallWithZeroLengthArrayArgument
        return ans.toArray(new String[ans.size()]);
    }

    private void backTrace(int num) {
        lock.lock();
        try {
            if (num == goods.length()) {
                ans.add(new String(buffer));
                return;
            }

            for (int i = 0; i < goods.length(); i++) {
                if (!visit[i]) {
                    char ch = goods.charAt(i);
                    buffer.append(ch);
                    visit[i] = true;
                    backTrace(num + 1);
                    buffer.deleteCharAt(buffer.length() - 1);
                    visit[i] = false;
                }
            }
        } finally {
            lock.unlock();
        }
    }
}