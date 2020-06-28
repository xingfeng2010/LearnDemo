package com.administrator.learndemo.opengl;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class OpenGLActivity extends AppCompatActivity {
    private GLSurfaceView mGLSurfaceView;
    private AirHockeyRenderer mAirHockeyRenderer;

    private boolean bRenderSet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGLSurfaceView = new GLSurfaceView(this);
        config(this);
        addTouchListener();

        setContentView(mGLSurfaceView);
    }

    private void addTouchListener() {
        mGLSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final float normalizedX = (motionEvent.getX() / (float) view.getWidth()) * 2 -1;
                final float normalizedY = -((motionEvent.getY() / (float) view.getHeight()) * 2 - 1);
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mGLSurfaceView.queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                mAirHockeyRenderer.handleTouchPress(normalizedX, normalizedY);
                            }
                        });
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mGLSurfaceView.queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                mAirHockeyRenderer.handleTouchDrag(normalizedX, normalizedY);
                            }
                        });
                        break;
                }
                return true;
            }
        });
    }

    private void config(OpenGLActivity openGLActivity) {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo info = activityManager.getDeviceConfigurationInfo();
        boolean supportES2 = info.reqGlEsVersion >= 0x20000;
        if (supportES2) {
            mGLSurfaceView.setEGLContextClientVersion(2);
            mAirHockeyRenderer = new AirHockeyRenderer(this);
            mGLSurfaceView.setRenderer(mAirHockeyRenderer);

            bRenderSet = true;
        } else {
            bRenderSet = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (bRenderSet) {
            mGLSurfaceView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (bRenderSet) {
            mGLSurfaceView.onResume();
        }
    }
}
