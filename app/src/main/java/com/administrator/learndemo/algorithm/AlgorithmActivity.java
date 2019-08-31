package com.administrator.learndemo.algorithm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.administrator.learndemo.R;

public class AlgorithmActivity extends AppCompatActivity {


    // 单位重量价值分别为:10 5 7 6 3 8 90 100
    double w[] = { 0, 50, 80, 30, 40, 20, 60, 10 ,1};//物体的重量

    double v[] = { 0, 500, 400, 210, 240, 60, 480, 900,100 };//物体的价值

    double M = 170;// 背包所能容纳的重量

    private TextView mTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_algorithm);

        mTextView = (TextView) this.findViewById(R.id.text_view);

        int n = w.length;// 物体的个数

        double[] x = new double[n];// 每个物体装进的比例,大于等于0并且小于等于1

        AlgorithmUtil.function(w, v, M, n, x);//调用贪心算法函数

        StringBuilder sb = new StringBuilder();
        sb.append("排序后的物体的重量:\n");
        for(int i=0;i<n;i++){
            sb.append(w[i]+"\t");
        }

        sb.append("\n");
        sb.append("排序后的物体的价值:\n");
        for(int i=0;i<n;i++){
            sb.append(v[i]+"\t");
        }
        sb.append("\n");

        int maxValue = 0;

        sb.append("装进去的物体:\n");
        for (int i =0; i < x.length; i ++) {
            if ((x[i]) >=1) {
                maxValue += v[i];
                sb.append(w[i]+"\t");
            }
        }

        sb.append("装\n");
        sb.append("最大价值为:\n");
        sb.append(maxValue);

        mTextView.setText(sb.toString());
    }
}
