package garry.leetcode.utils;

import java.util.Comparator;

/**
 * @author Garry
 * ---------2024/5/28 21:17
 **/
public class QuickSort {
    private Comparator<Integer> comparator;

    public QuickSort(Comparator<Integer> comparator) {
        this.comparator = comparator;
    }

    public void sort(int[] arr, int left, int right) {
        if (left >= right) return;

        int p = partition1(arr, left, right);

        sort(arr, left, p - 1);
        sort(arr, p + 1, right);
    }

    /**
     * 单路快速排序
     *
     * @param arr
     * @param left
     * @param right
     */
    private int partition1(int[] arr, int left, int right) {
        int base = arr[left]; // 选取基准点
        int i = left + 1; // i 是右侧的结束位置，并用于遍历
        int j = left; // j 是左侧的结束位置
        /*
            =============== 初始化left, right, i, j
            l             r
            4 6 5 2 3 8 7 1
            j i
            =============== 如果arr[i]比arr[left]大，则继续遍历，因为i是右侧的边界嘛
            l             r
            4 6 5 2 3 8 7 1
            j   i
            =============== 此时arr[i]比arr[left]小，则说明左侧需要扩大了，++j，然后交换
            l             r
            4 6 5 2 3 8 7 1
            j     i
            =============== ++j
            l             r
            4 6 5 2 3 8 7 1
              j   i
            =============== 交换
            l             r
            4 2 5 6 3 8 7 1
              j   i
            =============== 遇到3比4小，同理++j，然后交换
            l             r
            4 2 5 6 3 8 7 1
              j     i
            ===============
            l             r
            4 2 5 6 3 8 7 1
                j   i
            ===============
            l             r
            4 2 3 6 5 8 7 1
                j   i
            ===============
            l             r
            4 2 3 6 5 8 7 1
                j     i
            ===============
            l             r
            4 2 3 6 5 8 7 1
                j       i
            ===============
            l             r
            4 2 3 6 5 8 7 1
                j         i
            =============== 最后[l+1, j]为左侧，[j+1, i]为右侧
            l             r
            4 2 3 1 5 8 7 6
                  j       i
            =============== 交换arr[left]和arr[j]
            l             r
            1 2 3 4 5 8 7 6
                  j       i
            =============== 之后进一步递归即可
         */
        while (i <= right) {
            if (comparator.compare(base, arr[i]) >= 0) // 当前的数换到左侧去
                swap(arr, ++j, i);
            i++;
        }
        swap(arr, left, j);
        return j;
    }

    /**
     * 双路快速排序
     *
     * @param arr
     * @param left
     * @param right
     * @return
     */
    private int partition2(int[] arr, int left, int right) {
        int base = arr[left]; // 选取基准点
        int i = left + 1; // i 是右侧的结束位置，并用于遍历
        int j = left; // j 是左侧的结束位置
        
    }

    private void swap(int[] arr, int left, int right) {
        int temp = arr[left];
        arr[left] = arr[right];
        arr[right] = temp;
    }
}
