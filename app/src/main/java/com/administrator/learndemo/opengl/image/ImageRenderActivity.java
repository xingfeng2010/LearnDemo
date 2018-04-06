package com.administrator.learndemo.opengl.image;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.administrator.learndemo.opengl.AirHockeyRenderer;
import com.administrator.learndemo.opengl.OpenGLActivity;

public class ImageRenderActivity extends AppCompatActivity {

    GLSurfaceView mGLSurfaceView;
    public boolean bRenderSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGLSurfaceView = new GLSurfaceView(this);
        config();

        setContentView(mGLSurfaceView);
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

    private void config() {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo info = activityManager.getDeviceConfigurationInfo();
        boolean supportES2 = info.reqGlEsVersion >= 0x20000;
        if (supportES2) {
            mGLSurfaceView.setEGLContextClientVersion(2);

            mGLSurfaceView.setRenderer(new TextureRender(this));

            bRenderSet = true;
        } else {
            bRenderSet = false;
        }
    }
}
