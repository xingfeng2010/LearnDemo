package com.administrator.learndemo;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.view.Choreographer;

import com.administrator.learndemo.frame.FPSFrameCallback;

import androidx.annotation.RequiresApi;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

/**
 * Created by Administrator on 2018/2/7.
 */

public class LearnApplication extends MultiDexApplication {
    private static LearnApplication sInstance;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();

        sInstance = this;

        Choreographer.getInstance().postFrameCallback(new FPSFrameCallback(System.nanoTime()));
    }

    public static Context instance() {
        return sInstance;
    }
}
