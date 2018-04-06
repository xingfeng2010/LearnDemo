package com.administrator.learndemo.opengl.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import com.administrator.learndemo.R;
import com.administrator.learndemo.opengl.util.LoggerConfig;
import com.administrator.learndemo.opengl.util.ShaderHelper;
import com.administrator.learndemo.opengl.util.TextResourceReader;
import com.administrator.learndemo.opengl.util.TextureHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES10.GL_TEXTURE0;
import static android.opengl.GLES10.glActiveTexture;
import static android.opengl.GLES10.glBindTexture;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;

/**
 * Created by lishixing on 2018/4/5.
 */

public class TextureRender implements GLSurfaceView.Renderer{
    static float squareCoords[] = { //以三角形扇的形式绘制
            //x   y
            -1.0f,  1.0f ,
            -1.0f,  -1.0f,
            1.0f, 1.0f  ,
            1.0f, -1.0f};  // bottom left

    static float textureCoords[] = { //以三角形扇的形式绘制
            //s   t
            0.0f , 0.0f , // top left
            0.0f , 1.0f ,// top right
            1.0f , 0.0f ,// bottom right
            1.0f , 1.0f};  // bottom left

    float[] projectionMatrix = new float[16];

    int textureId;
    private Bitmap mBitmap;

    private FloatBuffer verxData;
    private FloatBuffer textureData;

    private int mProgram;

    private Context mContext;

    private int aPositionLocation;
    private int aTextureCoordinates;
    private int uTextureUnitLocation;
    private int uMatrixLocation;

    private int uColorLocation;


    public TextureRender(Context context) {
        mContext = context;
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.fengj);

        ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        verxData = bb.asFloatBuffer();
        verxData.put(squareCoords);

        ByteBuffer bb2 = ByteBuffer.allocateDirect(squareCoords.length * 4);
        bb2.order(ByteOrder.nativeOrder());
        textureData = bb2.asFloatBuffer();
        textureData.put(textureCoords);

        verxData.position(0);
        textureData.position(0);
    }

    private void getProgram() {
        //获取顶点着色器文本
        String vertexShaderSource = TextResourceReader
                .readTextFileFromResource(mContext, R.raw.image_vertex_shader);
        //获取片段着色器文本
        String fragmentShaderSource = TextResourceReader
                .readTextFileFromResource(mContext, R.raw.image_fragment_shader);
        //获取program的id
        mProgram = ShaderHelper.buildProgram(vertexShaderSource, fragmentShaderSource);
        GLES20.glUseProgram(mProgram);
    }


    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(0.0f,0.0f,0.0f,0.0f);
        GLES20.glEnable(GL_TEXTURE_2D);

        getProgram();

        uTextureUnitLocation = glGetUniformLocation(mProgram,
                "u_TextureUnit");
        uMatrixLocation = glGetUniformLocation(mProgram, "u_Matrix");
        aPositionLocation = glGetAttribLocation(mProgram, "a_Position");
        aTextureCoordinates = glGetAttribLocation(mProgram, "a_TextureCoordinates");
        uColorLocation = glGetUniformLocation(mProgram, "u_Color");

        textureId = TextureHelper.loadTexture(mContext,R.drawable.fengj);

        // Set the active texture unit to texture unit 0.
        glActiveTexture(GL_TEXTURE0);
        // Bind the texture to this unit.
        glBindTexture(GL_TEXTURE_2D, textureId);
        // Tell the texture uniform sampler to use this texture in the shader by
        // telling it to read from texture unit 0.
        glUniform1i(uTextureUnitLocation, 0);
        //传入顶点坐标和纹理坐标
        GLES20.glVertexAttribPointer(aPositionLocation, 2,
                GLES20.GL_FLOAT, false, 0, verxData);
        GLES20.glEnableVertexAttribArray(aPositionLocation);
//        //设置从第二个元素开始读取，因为从第二个元素开始才是纹理坐标
//        verxData.position(2);
        GLES20.glVertexAttribPointer(aTextureCoordinates, 2,
                GLES20.GL_FLOAT, false, 0, textureData);
        GLES20.glEnableVertexAttribArray(aTextureCoordinates);
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        GLES20.glViewport(0,0,width,height);

        final float aspectRatio = width > height ?
                (float) width / (float) height :
                (float) height / (float) width;
        if(width > height){
            Matrix.orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
        }else{
            Matrix.orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
        }
    }

    @Override
    public void onDrawFrame(GL10 glUnused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);
        //GLES20.glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);

        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }
}
