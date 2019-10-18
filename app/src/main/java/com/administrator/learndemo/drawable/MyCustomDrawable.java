package com.administrator.learndemo.drawable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;

import com.administrator.learndemo.R;

public class MyCustomDrawable extends Drawable {

    private Paint mPaint;
    private int mWidth;
    private Bitmap mBitmap;
    private Bitmap mLaunchBack,mLaunchLogo, mLaunchChangan;

    public MyCustomDrawable(Context context) {
        mLaunchBack = BitmapFactory.decodeResource(context.getResources(), R.drawable.launch_back);
        mLaunchLogo = BitmapFactory.decodeResource(context.getResources(), R.drawable.launch_logo);
        mLaunchChangan = BitmapFactory.decodeResource(context.getResources(), R.drawable.launch_changan);

//        BitmapShader bitmapShader = new BitmapShader(bitmap, TileMode.CLAMP,
//                TileMode.CLAMP);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
//        mPaint.setShader(bitmapShader);
//        选择我们的半径为较小的那个
        mWidth = Math.min(mBitmap.getWidth(), mBitmap.getHeight());
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(mLaunchBack,0,0, mPaint);
    }

    @Override
    public int getIntrinsicWidth() {
        return mWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return mWidth;
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

}
