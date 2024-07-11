package garry.leetcode.utils.sort;

/**
 * @author Garry
 * ---------2024/6/26 21:23
 **/
public class HeapSort {

    public static void main(String[] args) {
        int[] nums = {1, 3, 5, 2, 4, 6};
        new HeapSort().sort(nums);
        System.out.println();// 1,2,3,4,5,6
    }

    public int[] sort(int[] nums) {
        int N = nums.length - 1;
        // 自底向上把数组变成一个最大堆
        for (int i = (N - 1) / 2/*最后一个parent*/; i >= 0; i--) {
            sink(i, N, nums);
        }
        while (N > 0) {
            // 把第0个，也就是当前最大堆种最大的元素换到最后一个元素，然后把换上来的元素再下沉
            swap(nums, 0, N);
            N--;
            sink(0, N, nums);
        }
        return nums;
    }

    private void sink(int i, int N, int[] nums) {
        while (leftChild(i) <= N) {
            int j = leftChild(i);
            j = j + 1 <= N && nums[j] < nums[j + 1] ? j + 1 : j;
            if (nums[i] > nums[j]) {
                break;
            }
            swap(nums, i, j);
            i = j;
        }
    }

    private int parent(int index) {
        return (index - 1) / 2;
    }

    private int leftChild(int index) {
        return index * 2 + 1;
    }

    private int rightChild(int index) {
        return index * 2 + 2;
    }

    private void swap(int[] nums, int i1, int i2) {
        int temp = nums[i1];
        nums[i1] = nums[i2];
        nums[i2] = temp;
    }
}
