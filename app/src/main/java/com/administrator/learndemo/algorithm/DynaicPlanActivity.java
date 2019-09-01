package com.administrator.learndemo.algorithm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.administrator.learndemo.R;

public class DynaicPlanActivity extends BaseAlgorithmActivity {
    @Override
    protected void startCompile() {
        startComputer();
    }

    private void startComputer() {
//        int[] w = new int[]{ 0 , 2 , 3 , 4 , 5 };      //商品的体积2、3、4、5
//        int[] v = new int[]{ 0 , 3 , 4 , 5 , 6 };      //商品的价值3、4、5、6

        int[] w = new int[]{ 0 , 4 , 3 , 1 };      //商品的体积2、3、4、5
        int[] v = new int[]{ 0 , 3000 , 2000,1500 };      //商品的价值3、4、5、6

        int bagV = 4;        //背包大小
        int dp[][] = new int[4][5];        //动态规划表

        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= bagV; j ++) {
                if (j < w[i]) {
                    dp[i][j] = dp[i - 1] [j];
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - w[i]] + v[i]);
                }
            }
        }


        sb.append("选择商品是：\n");
        //动态规划表的输出
        for (int i = 0; i < 4; i++) {
            sb.append("\n");
            for (int j = 0; j < 5; j++) {
                sb.append(dp[i][j] + "\t\t\t\t");
            }
        }
    }
}
