package com.administrator.learndemo;

import android.content.Context;
import android.os.Build;
import android.view.Choreographer;

import com.administrator.learndemo.frame.FPSFrameCallback;
import com.administrator.learndemo.rxjava.model.Events;
import com.administrator.learndemo.rxjava.ui.rxbus.RxBus;

import java.util.concurrent.TimeUnit;

import androidx.annotation.RequiresApi;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2018/2/7.
 */

public class LearnApplication extends MultiDexApplication {
    private static LearnApplication sInstance;
    private RxBus bus;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();

        sInstance = this;

        Choreographer.getInstance().postFrameCallback(new FPSFrameCallback(System.nanoTime()));

        bus = new RxBus();
    }

    public static Context instance() {
        return sInstance;
    }

    public RxBus bus() {
        return bus;
    }

    public void sendAutoEvent() {
        Observable.timer(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) {
                        bus.send(new Events.AutoEvent());
                    }
                });
    }
}
