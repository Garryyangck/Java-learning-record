package garry.leetcode.utils;

import java.util.Comparator;
import java.util.Random;

/**
 * @author Garry
 * ---------2024/5/29 14:35
 **/
public class ShellSort {
    @SuppressWarnings("DuplicatedCode")
    public static void main(String[] args) {
        int[] arr = new int[1000000];
        for (int i = 0; i < arr.length; i++) {
            Random random = new Random();
            arr[i] = random.nextInt(arr.length);
        }
        //noinspection Convert2Lambda
        ShellSort shellSort = new ShellSort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });
        long before = System.nanoTime();
        shellSort.sort(arr);
        long after = System.nanoTime();
        double secondTime = (after - before) / 1000000000.0;
        System.out.println("The time is " + secondTime + " seconds.");
    }

    private final Comparator<Integer> comparator;

    public ShellSort(Comparator<Integer> comparator) {
        this.comparator = comparator;
    }

    public void sort(int[] arr) {
        int n = arr.length;
        int h = n;
        while ((h = nextH(h)) >= 1) { // h初始为n>>1，之后逐渐减小，直到最小为1
            for (int i = 0; i < h; i++) { // i为每个分组的开头
                for (int j = i + h; j < n; j += h) { // j为遍历每个分组后续的数
                    // 下面为一个分组内的插入排序
                    int key = arr[j];
                    int k = j - h;
                    while (k >= 0 && comparator.compare(arr[k], key) > 0) {
                        arr[k + h] = arr[k];
                        k -= h;
                    }
                    arr[k + h] = key;
                }
            }
        }
    }

    public int nextH(int oldH) {
        return oldH >> 1;
    }
}
