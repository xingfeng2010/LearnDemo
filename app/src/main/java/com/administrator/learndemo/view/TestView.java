package com.administrator.learndemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.administrator.learndemo.R;

/**
 * Created by Administrator on 2018/2/25.
 */

public class TestView extends View {
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    int mColor;

    public TestView(Context context) {
        super(context);
        init(context,null);
    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TestView);
        mColor = a.getColor(R.styleable.TestView_circle_color, Color.YELLOW);
        a.recycle();
    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i("InterceptView", "test dispatchTouchEvent action:" + ev.getAction());
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
            case MotionEvent.ACTION_MOVE:
                //getParent().requestDisallowInterceptTouchEvent(false);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("InterceptView", "test onTouchEvent action:" + event.getAction());
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = this.getWidth();
        int height = this.getHeight();
        int radius = Math.min(width- this.getPaddingLeft() - this.getPaddingRight(),height- this.getPaddingBottom() - this.getPaddingTop()) / 2;
        mPaint.setColor(mColor);
        canvas.drawCircle(width / 2, height / 2,radius,mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        ViewGroup.LayoutParams params = this.getLayoutParams();
        if (params.width == ViewGroup.LayoutParams.WRAP_CONTENT
                && params.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(400,400);
        } else if (params.width == ViewGroup.LayoutParams.WRAP_CONTENT){
            setMeasuredDimension(400,height);
        } else if (params.height == ViewGroup.LayoutParams.WRAP_CONTENT){
            setMeasuredDimension(width,400);
        }
    }
}
