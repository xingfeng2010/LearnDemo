package com.administrator.learndemo.activitymanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.administrator.learndemo.R;
import com.xingfeng.FingerPrintLib.asm.AppTest;

import java.util.ArrayList;
import java.util.List;

@FileAnnotation("TestActivity")
public class TestActivity extends AppCompatActivity {

    public List<AppTest> moduleApplications = new ArrayList<>();

    @ActivityAnnotation("onCreate")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("DEBUG_TEST", " start size:"+ moduleApplications.size());
        setContentView(R.layout.activity_test);
        Log.i("DEBUG_TEST", " end size:"+ moduleApplications.size());
    }
}
