package com.administrator.learndemo.algorithm;

/**
 * 有一个二维矩阵 grid ，每个位置要么是陆地（记号为 0 ）要么是水域（记号为 1 ）。
 *
 * 我们从一块陆地出发，每次可以往上下左右 4 个方向相邻区域走，能走到的所有陆地区域，我们将其称为一座「岛屿」。
 *
 * 如果一座岛屿 完全 由水域包围，即陆地边缘上下左右所有相邻区域都是水域，那么我们将其称为 「封闭岛屿」。
 *
 * 请返回封闭岛屿的数目。
 *
*/
public class IlandActivity extends BaseAlgorithmActivity {

    @Override
    protected void startCompile() {
        int[][] grid = {
                {1, 1, 1, 1, 1, 1, 1, 0},
                {1, 0, 0, 0, 0, 1, 1, 0},
                {1, 0, 1, 0, 1, 1, 1, 0},
                {1, 0, 0, 0, 0, 1, 0, 1},
                {1, 1, 1, 1, 1, 1, 1, 0}
        };

        int[][] grid2 = {
                {0, 0, 1, 0, 0},
                {0, 1, 0, 1, 0},
                {0, 1, 1, 1, 0}
        };

        int totalNum = searchIland(grid);
        sb.append("数目是：" + totalNum);
    }

    private int searchIland(int[][] grid) {
        int row = grid.length;
        int colum = grid[0].length;

        int totalNum = 0;

        for (int i = 0; i < row; i ++)
            for (int j =0; j < colum; j++) {
                if (grid[i][j] == 0) {
                    if (dfsSearchIsLand(grid, i, j, row, colum)) {
                        totalNum ++;
                    }
                }
            }

        return totalNum;
    }

    private boolean dfsSearchIsLand(int[][] grid,
                           int i, int j, int rows, int cols) {
        boolean left = false;
        boolean right = false;
        boolean top = false;
        boolean bottom = false;
        for (int index = i; index >=0; index --) {
            if (grid[index][j] == 1) {
                left =  true;
                break;
            }
        }

        for (int index = i; index < rows; index ++) {
            if (grid[index][j] == 1) {
                right =  true;
                break;
            }
        }

        for (int index = j; index >= 0; index --) {
            if (grid[i][index] == 1) {
                top =  true;
                break;
            }
        }

        for (int index = j; index < cols; index ++) {
            if (grid[i][index] == 1) {
                bottom =  true;
                break;
            }
        }

        boolean isLand = left&&right&&bottom&&top;
        for (int index = i; index >=0; index --) {
            if (grid[index][j] == 0 && isLand) {
                grid[index][j] = 1;
            }
        }

        for (int index = i; index < rows; index ++) {
            if (grid[index][j] == 1 && isLand) {
                grid[index][j] = 1;
            }
        }

        for (int index = j; index >= 0; index --) {
            if (grid[i][index] == 1 && isLand) {
                grid[index][j] = 1;
            }
        }

        for (int index = j; index < cols; index ++) {
            if (grid[i][index] == 1) {
                bottom =  true;
                break;
            } else {
                grid[index][j] = 1;
            }
        }

        return isLand;
    }
}
