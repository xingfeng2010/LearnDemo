package com.administrator.learndemo.aop;

import android.os.Bundle
import android.util.Log;
import android.view.View;
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.administrator.learndemo.LearnApplication
import com.administrator.learndemo.R

public class LoginActivity : AppCompatActivity() {

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d("alan","--------------LoginActivity");
    }

    public fun login(view:View) {
        LearnApplication.getInstance().isLogin = true;
    }
}
