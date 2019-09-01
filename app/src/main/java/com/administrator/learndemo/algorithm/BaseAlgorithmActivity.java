package com.administrator.learndemo.algorithm;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.administrator.learndemo.R;

public abstract class BaseAlgorithmActivity extends AppCompatActivity {
    private TextView mTextView;
    protected StringBuilder sb = new StringBuilder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynaic_plan);

        mTextView = (TextView) this.findViewById(R.id.text_view);
        new Thread() {

            @Override
            public void run() {
                startCompile();
                printResult();
            }

        }.start();

    }

    protected abstract void startCompile();

    protected void printResult() {
        mTextView.post(new Runnable() {
            @Override
            public void run() {
                mTextView.setText(sb.toString());
            }
        });
    }
}
