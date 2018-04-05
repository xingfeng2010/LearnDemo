package com.administrator.learndemo;

import android.app.Application;
import android.content.Context;
import android.view.Choreographer;

import com.administrator.learndemo.frame.FPSFrameCallback;

/**
 * Created by Administrator on 2018/2/7.
 */

public class LearnApplication extends Application {
    private static LearnApplication sInstance;


    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;

        Choreographer.getInstance().postFrameCallback(new FPSFrameCallback(System.nanoTime()));
    }

    public static Context instance() {
        return sInstance;
    }
}
