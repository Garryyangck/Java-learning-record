package garry.leetcode.utils.sort;

import java.util.Comparator;
import java.util.Random;

/**
 * @author Garry
 * ---------2024/5/28 21:17
 **/
@SuppressWarnings("DuplicatedCode")
public class QuickSort {
    public static void main(String[] args) {
        int[] arr = new int[10000000];
        for (int i = 0; i < arr.length; i++) {
            Random random = new Random();
            arr[i] = random.nextInt(arr.length);
        }
        QuickSort quickSort = new QuickSort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });
        long before = System.nanoTime();
        quickSort.sort3Ways(arr, 0, arr.length - 1);
        long after = System.nanoTime();
        double secondTime = (after - before) / 1000000000.0;
        System.out.println("The time is " + secondTime + " seconds.");
    }

    private final Comparator<Integer> comparator;

    public QuickSort(Comparator<Integer> comparator) {
        this.comparator = comparator;
    }

    public void sort(int[] arr, int left, int right) {
        if (left >= right) return;

        int p = partition2(arr, left, right);

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
            if (comparator.compare(base, arr[i]) >= 0) // 当前的数换到左侧去，相等的数也会交换到左侧去
                swap(arr, ++j, i);
            i++;
        }
        swap(arr, left, j);
        return j;
    }

    /**
     * 双路快速排序
     * 为了解决当所有元素相同时，把所有元素放到标定点的左侧或右侧，导致性能退化到O(n^2)的问题
     *
     * @param arr
     * @param left
     * @param right
     * @return
     */
    private int partition2(int[] arr, int left, int right) {
        int base = arr[left]; // 选取基准点
        int i = left + 1;
        int j = right;
        while (i <= j) {
            while (i <= j && !(comparator.compare(arr[i], base) >= 0)) { // 找到第一个>=base的数
                i++;
            }
            while (i <= j && !(comparator.compare(arr[j], base) <= 0)) { // 找到第一个<=base的数
                j--;
            }
            if (i <= j) { // 防止i出界，比如从小到大排序，[4,1]，i找不到>=4的数而出界，j找到的第一个<=4的数是4本身
                swap(arr, i, j);
                i++; // 如果已经i>j了，那么就不用再i++，j--了
                j--;
            }
        }
        swap(arr, left, j); // 交换left和j的位置，注意必须是j，因为i可能出界，但是j肯定不会出界，因为它可以遍历到left，arr[left]是满足<=arr[left]的
        return j;
    }

    /**
     * 三路快速排序
     * 将区间分成三部分，在解决所有元素都相同的情况下，只需遍历一级就直接返回，在这种情况下比双路更快
     *
     * @param arr
     * @param left
     * @param right
     */
    public void sort3Ways(int[] arr, int left, int right) {
        if (left >= right)
            return;

        int base = arr[left];

        int i = left + 1; // 等于base的区间[lt,i)
        int lt = left + 1; // 小于的区间[left,lt)
        int gt = right; // 大于的区间(gt,right]

        while (i <= gt) { // i == gt 时就说明所有元素都已经遍历完了
            if (i <= gt && comparator.compare(arr[i], base) == 0) { // 和基准点相等，i++，因为i的部分就是和基准点相等的部分
                i++;
            } else if (i <= gt && comparator.compare(arr[i], base) < 0) { // 小于基准点，和左侧交换
                swap(arr, lt++, i++);// i 需要移动，因为lt在移动，避免被超过
            } else if (i <= gt && comparator.compare(arr[i], base) > 0) { // 大于基准点，和右侧交换
                swap(arr, gt--, i);// i不能移动，因为右侧交换过来的数可能还是>base
            }
        }
        swap(arr, left, lt - 1); // 最后left(base)必须和lt-1(小于base的数)交换，这样[lt,i)是等于base的

        sort3Ways(arr, left, lt - 2);
        sort3Ways(arr, gt + 1, right);
    }

    private void swap(int[] arr, int left, int right) {
        int temp = arr[left];
        arr[left] = arr[right];
        arr[right] = temp;
    }
}
