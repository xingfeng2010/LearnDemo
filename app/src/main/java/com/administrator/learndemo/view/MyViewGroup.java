package com.administrator.learndemo.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2018/2/25.
 */

public class MyViewGroup extends RelativeLayout {
    private boolean moveIntercept = false;

    public MyViewGroup(Context context) {
        super(context);
    }

    public MyViewGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i("InterceptView", "dispatchTouchEvent action:" + ev.getAction());
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("InterceptView", "onTouchEvent action:" + event.getAction());
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i("InterceptView", "onInterceptTouchEvent action:" + ev.getAction());
        boolean intercept = false;
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            return false;
        } else {
            return false;
        }
    }
}
