package com.administrator.learndemo.VelocityTracker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

/**
 * Created by Administrator on 2018/1/8.
 */

public class TestVelocityView extends View implements GestureDetector.OnGestureListener {
    String TAG = "TestVelocity";
    VelocityTracker velocityTracker;
    Paint mPaint = new Paint();
    GestureDetector mGestureDetector;

    int xVelocity = 0;
    int yVelocity = 0;

    public TestVelocityView(Context context) {
        this(context, null);
    }

    public TestVelocityView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestVelocityView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint.setTextSize(50);
        mGestureDetector = new GestureDetector(this);
        //解决长按屏幕后无法拖动的现象
         //mGestureDetector.setIsLongpressEnabled(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //画文字的代码。
        canvas.save();
        mPaint.setColor(Color.BLACK);
        canvas.drawText("x = " + xVelocity + "y =" + yVelocity, getLeft(), getTop(), mPaint);
        //画完之后回收一下
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //初始化
                velocityTracker = VelocityTracker.obtain();
                break;
            case MotionEvent.ACTION_MOVE:
                //追踪
                velocityTracker.addMovement(event);
                velocityTracker.computeCurrentVelocity(1000);
                xVelocity = (int) velocityTracker.getXVelocity();
                yVelocity = (int) velocityTracker.getYVelocity();

//                if (listener != null) {
//                    listener.get(xVelocity, yVelocity);
                //强制刷新一下view，否则不会一直掉onDraw。
                invalidate();
//                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //回收
                velocityTracker.clear();
                velocityTracker.recycle();
                break;
        }

        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
        Log.i(TAG, "onShowPress");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        Log.i(TAG, "onSingleTapUp");
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return true;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        Log.i(TAG, "onLongPress");
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        Log.i(TAG, "onFling");
        return true;
    }
}
