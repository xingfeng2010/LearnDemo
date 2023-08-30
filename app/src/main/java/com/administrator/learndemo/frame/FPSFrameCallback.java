package com.administrator.learndemo.frame;

import android.util.Log;
import android.view.Choreographer;

/**
 * Created by Administrator on 2018/2/28.
 */

/**
 * 这个类用来作帧率监控
 */
public class FPSFrameCallback implements Choreographer.FrameCallback {
    private static final String TAG = "FPS_TEST";
    private long mLastFrameTimeNanos = 0;
    private long mFrameIntervalNanos;

    private static final long ONE_MINUTE_TIME = 1000000000;

    public FPSFrameCallback(long lastFrameTimeNanos) {
        mLastFrameTimeNanos = lastFrameTimeNanos;
        mFrameIntervalNanos = (long) (ONE_MINUTE_TIME / 60.0);
    }

    @Override
    public void doFrame(long frameTimeNanos) {
        //初始化时间
        if (mLastFrameTimeNanos == 0) {
            mLastFrameTimeNanos = frameTimeNanos;
        }
        final long jitterNanos = frameTimeNanos - mLastFrameTimeNanos;
        //Log.i(TAG, "frame cost time:" + jitterNanos / 1000000 + "ms");
        if (jitterNanos >= mFrameIntervalNanos) {
            final long skippedFrames = jitterNanos / mFrameIntervalNanos;
            if (skippedFrames > 30) {
                Log.i(TAG, "Skipped " + skippedFrames + " frames!  "
                        + "The application may be doing too much work on its main thread.");
            }
        }
        mLastFrameTimeNanos = frameTimeNanos;
        //注册下一帧回调
        Choreographer.getInstance().postFrameCallback(this);
    }
}
