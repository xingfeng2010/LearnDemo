package com.administrator.learndemo.algorithm;

public class Fibonacci extends BaseAlgorithmActivity {
    @Override
    protected void startCompile() {
        long start = System.currentTimeMillis();
        int result =  function1(40);
        sb.append("递归结果：" + result + "  耗时：" + (System.currentTimeMillis() - start));
        sb.append("\n");

        long start2 = System.currentTimeMillis();
        int result2 = function2(40);
        sb.append("动态规划结果：" + result2 + "  耗时：" + (System.currentTimeMillis() - start2));
        sb.append("\n");
    }

    /**
     * 递归
     */
    private int function1(int n) {
        if (n == 0 ) {
            return 0;
        } else if (n == 1) {
            return 1;
        } else {
            return function1(n - 1) + function1(n - 2);
        }
    }


    /**
     * 动态规划解问题
     * @param n
     * @return
     */
    private int function2(int n) {
        if (n == 0) {
            return 0;
        } else if (n == 1) {
            return 1;
        } else {
            int[] temp = new int[n + 1];
            temp[0] = 0;
            temp[1] = 1;
            for (int i = 2; i <= n;i ++) {
                temp[i] = temp[i - 1] + temp[i - 2];
            }

            return temp[n];
        }
    }

}
