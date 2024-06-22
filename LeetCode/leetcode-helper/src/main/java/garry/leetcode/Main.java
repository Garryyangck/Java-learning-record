package garry.leetcode;

/**
 * @author Garry
 * ---------2024/3/22 11:40
 **/

public class Main {
    public static void main(String[] args) {
    }
}

class Solution {
    public boolean findTargetIn2DPlants(int[][] matrix, int target) {
        if(matrix == null || matrix.length <= 0 || matrix[0].length <= 0){
            return false;
        }
        // 这里 cols 表示多少行的意思（但是有人说 cols 是表示列，那我可能记错了）
        int rows = matrix.length;
        // rows 表示多少列的意思
        int cols = matrix[0].length;
        // 左下角的位置
        int row = rows - 1;
        int col = 0;
        // 向上向右走的过程中不能出界
        while(row >= 0 && col < cols){
            if(target > matrix[row][col]){
                col++;
            } else if(target < matrix[row][col]){
                row--;
            } else {
                return true;
            }
        }
        return false;
    }
}