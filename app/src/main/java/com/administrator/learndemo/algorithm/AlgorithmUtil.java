package com.administrator.learndemo.algorithm;

public class AlgorithmUtil {

    /**
     * 贪心算法解决0、1背包问题
     * @param w 物体的重量
     * @param v 物体的价值
     * @param M 背包的容量
     * @param n 物体的个数
     * @param x 每个物体装进被包的比例，取值0<=x[i]<=1 (1<=i<=n)
     */
    public static void function(double[] w, double[] v, double M, int n, double[] x) {
        sort(w, v, n); //首先按照物体的单位重量的价值进行排序，单位重量价值大的排在前面

        double c = M; //被包剩余的容量，刚开始时还没有装东西，为M
        int i; //表示第几物体

        for (i = 0; i < n; i++) {
            if (w[i] <= c) {//如果被包剩余的容量大于等于第i个物体的重量
                x[i] = 1; //把第i个物体整个装进被包
                c -= w[i]; //背包的剩余容量减少了第i个物体的重量
            } else {
                break; //退出循环
            }
        }

        if (i <= n) { //判断是否第n个物体整个装进去背包里了，如果 i <= n 表示否定
            x[i] = c / w[i];
        }
    }

    /**
     * 按冒泡排序来写，先计算W中每个物体单位价值存成一个数组，然后冒泡排序
     * 若有元素交换，对应W中相同标号的元素也交换就可以了
     * @param w
     * @param v
     * @param n
     */
    private static void sort(double[] w, double[] v, int n) {
        double[] t = new double[n];
        for (int i = 0; i < n; i ++) {
            t[i] = v[i] / w[i];
        }

        for (int i = n - 1; i >= 0; i --) {
            for (int j = 0; j < i; j ++) {
                if (t[j] < t[j + 1]) {

                    double temp = t[j];
                    t[j] = t[j + 1];
                    t[j + 1] = temp;

                    double temp2 = w[j];
                    w[j] = w[j + 1];
                    w[j + 1] = temp2;

                    double temp3 = v[j];
                    v[j] = v[j + 1];
                    v[j + 1] = temp3;
                }
            }
        }
    }

    

}
