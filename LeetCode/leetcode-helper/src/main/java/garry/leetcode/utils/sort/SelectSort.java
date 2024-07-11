package garry.leetcode.utils.sort;

/**
 * @author Garry
 * ---------2024/6/26 22:08
 **/
public class SelectSort {
    public static void main(String[] args) {
        new SelectSort().sort(new int[]{1, 3, 5, 2, 4, 6});
    }

    public int[] sort(int[] nums) {
        for (int i = 1; i < nums.length; i++) {
            for (int j = i; j > 0 && nums[j - 1] > nums[j]; j--) {
                swap(nums, i, j);
            }
        }
        return nums;
    }

    private void swap(int[] nums, int i1, int i2) {
        int temp = nums[i1];
        nums[i1] = nums[i2];
        nums[i2] = temp;
    }
}
