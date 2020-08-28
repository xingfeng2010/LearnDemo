package com.administrator.learndemo.mvvm;

import android.os.Bundle;

import com.administrator.learndemo.R;
import com.administrator.learndemo.databinding.ActivityBindbindBinding;
import com.administrator.learndemo.mvvm.data.UserInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class DataBindActivity extends AppCompatActivity {

    private ActivityBindbindBinding dataBindBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        dataBindBinding = DataBindingUtil.setContentView(this, R.layout.activity_bindbind);
//
//        UserInfo xiaoMing = new UserInfo("xiaoMing", "tstp456");
//        dataBindBinding.setUserInfo(xiaoMing);
    }
}
