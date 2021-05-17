package com.administrator.learndemo.algorithm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/**
 * 如上图所示二维数组，大多值是默认值（0），所以记录大量无意义的数据意义不大，此时可以引入稀疏数组。
 *
 * 　　稀疏数组介绍：当一个数组大部分元素为固定值时，可以使用稀疏数组来保存类似数组；
 *
 * 　　稀疏数组处理思路：
 *
 * 稀疏数组记录二维数组的行列数以及非默认值数目；
 */
public class SparseArrayActivity extends BaseAlgorithmActivity {


    @Override
    protected void startCompile() {
        // 创建原始二维数组（0 表示无子，1 表示黑子 2 表示 白子）
        int chessArr1[][] = new int[11][11];
        chessArr1[1][2] = 1;
        chessArr1[3][3] = 2;
        chessArr1[5][1] = 2;

        sb.append("--------------------------------原始二维数组--------------------------------- \n");
        for (int row[] : chessArr1) {
            for (int data : row) {
                sb.append(data+"\t");
            }
            sb.append("\n");
        }

        // 将二维数组转换为稀疏数组
        // 获取原始二维数组非零数目
        int sum = 0;
        for (int i = 0; i < chessArr1.length; i++) {
            for (int j = 0; j < chessArr1.length; j++) {
                if (chessArr1[i][j] != 0) {
                    sum++;
                }
            }
        }
        sb.append("sum = " + sum + "\n");

        // 创建稀疏数组
        int sparseArr[][] = new int[sum + 1][3];
        // 为稀疏数组赋值
        sparseArr[0][0] = chessArr1.length;
        sparseArr[0][1] = chessArr1.length;
        sparseArr[0][2] = sum;
        // 便利原始二维数组，进行存放
        int n = 0;
        for (int i = 0; i < chessArr1.length; i++) {
            for (int j = 0; j < chessArr1.length; j++) {
                if (chessArr1[i][j] != 0) {
                    n++;
                    sparseArr[n][0] = i;
                    sparseArr[n][1] = j;
                    sparseArr[n][2] = chessArr1[i][j];
                }
            }
        }
        // 遍历稀疏数组
        sb.append("-------------------------------稀疏数组--------------------------------- \n");
        for (int i = 0; i < sparseArr.length; i++) {
            sb.append(sparseArr[i][0] + "\t" + sparseArr[i][1] + "\t" + sparseArr[i][2] + "\t\n");
        }
        // 将稀疏数组还原为原始二维数组
        int chessArr2[][] = new int[sparseArr[0][0]][sparseArr[0][1]];
        for (int i = 1; i < sparseArr.length; i++) {
            chessArr2[chessArr2[i][0]][chessArr2[i][1]] = chessArr2[i][2];
        }
        sb.append("-----------------------------恢复后的二维数组---------------------------------\n");
        for (int row[] : chessArr1) {
            for (int data : row) {
                sb.append(data + "\t");
            }
            sb.append("\n");
        }
    }
}
