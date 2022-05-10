package com.administrator.learndemo.aop;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity
import com.administrator.learndemo.R

public class TextActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        Log.d("alan","--------------TextActivity");
    }

    fun testCancel(view:View) {
        TestOperation().canCancel("haha");
    }
}
