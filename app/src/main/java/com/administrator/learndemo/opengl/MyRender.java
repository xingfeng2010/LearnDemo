package com.administrator.learndemo.opengl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.administrator.learndemo.opengl.shape.AirHockey;
import com.administrator.learndemo.opengl.shape.Shape;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2018/3/7.
 */

class MyRender implements GLSurfaceView.Renderer {
    private Shape mShape;
    private Context mContext;
    public MyRender(Context context) {
        mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(0.0f,0.0f,0.0f,0.0f);
        mShape = new AirHockey(mContext);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0,0,width,height);
        //设置投影矩阵
        mShape.projectionMatrix(width, height);

    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        mShape.draw();
    }
}
