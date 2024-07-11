package garry.leetcode.utils.sort;

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
                    // 下面为一个分组内的插入排序，此时j已经是第二个数
                    // 就是把>0改为>i，++改为+=h，k-1改为k-h
                    for (int k = j; k > i && comparator.compare(arr[k - h], arr[k]) > 0; k -= h) {
                        swap(arr, k - h, k);
                    }
                }
            }
        }
    }

    public int nextH(int oldH) {
        return oldH >> 1;
    }// 间隔h的缩小策略，最小为1

    private void swap(int[] nums, int i1, int i2) {
        int temp = nums[i1];
        nums[i1] = nums[i2];
        nums[i2] = temp;
    }
}
