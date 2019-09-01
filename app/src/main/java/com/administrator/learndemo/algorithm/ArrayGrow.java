package com.administrator.learndemo.algorithm;

public class ArrayGrow extends BaseAlgorithmActivity{
    int arr[] = {5,1,4,1,5,9,2,6,5};

    @Override
    protected void startCompile() {
       int max =  maxChildArrayOrder(arr);
        sb.append("最长子串序长度：" + max);
    }



    private int maxChildArrayOrder(int a[]) {
        int n = a.length;
        int temp[] = new int[n]; //temp[i]代表0...i上最长递增子序列
        for(int i = 0; i < n; i ++) {
            temp[i] = 1; //初始值都为1
        }

        for (int i = 1; i < n; i ++) {
            for (int j = 0; j < i; j ++) {
                if (a[i] > a[j] && temp[j] + 1 > temp[i]) {
                    //如果有a[i]比它前面所有的数都大，则temp[i]
                    temp[i] = temp[j] + 1;
                }
            }
        }

        int max = temp[0];
        int index = 0;
        //从temp数组里取出最大值
        for (int i = 1; i < n; i ++) {
            if (temp[i] > max) {
                max = temp[i];
                index = i;
            }
        }

        sb.append("最长子串序列为：\n");
        for (int i = index; i >= (index - max + 1);i--) {
            boolean needAddd = true;
            for (int j = index - max; j < i; j ++) {
                if (a[i] < a[j]) {
                    needAddd = false;
                    break;
                }
            }

            if (needAddd || max ==1) {
                sb.append(a[i] + "\t");
            }
        }

        sb.append(a[index - max] + "\t");

        sb.append("\n");

        return max;
    }
}
