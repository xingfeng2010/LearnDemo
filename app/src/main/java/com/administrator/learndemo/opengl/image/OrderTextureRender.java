package com.administrator.learndemo.opengl.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.administrator.learndemo.R;
import com.administrator.learndemo.opengl.util.ShaderHelper;
import com.administrator.learndemo.opengl.util.TextResourceReader;
import com.administrator.learndemo.opengl.util.TextureHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

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

public class OrderTextureRender implements GLSurfaceView.Renderer {
    static float squareCoords[] = {
            1, 1, 0,   // top right
            -1, 1, 0,  // top left
            -1, -1, 0, // bottom left
            1, -1, 0,  // bottom right
    };

    private static final short[] VERTEX_INDEX = { 0, 1, 2, 0, 2, 3 };

//    static float textureCoords[] = {
//            1.0f, 0,  // bottom right
//            0,    0,  // bottom left
//            0,    1.0f,  // top left
//            1.0f, 1.0f,  // top right
//    };

    static float textureCoords[] = {
            1.0f, 1.0f,  // bottom right
            1.0f,    0,  // bottom left
            0,    0.0f,  // top left
            0.0f, 1.0f,  // top right
    };

    float[] projectionMatrix = new float[16];

    int textureId;
    private Bitmap mBitmap;

    private FloatBuffer verxData;
    private FloatBuffer textureData;
    private ShortBuffer vertextIndexData;

    private int mProgram;

    private Context mContext;

    private int aPositionLocation;
    private int aTextureCoordinates;
    private int uTextureUnitLocation;
    private int uMatrixLocation;

    private int uColorLocation;


    public OrderTextureRender(Context context) {
        mContext = context;
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.fengj);

        resolveFlip();
        //resolveCrop(0.2f,0.2f,0.6f,0.6f);

        ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        verxData = bb.asFloatBuffer();
        verxData.put(squareCoords);

        ByteBuffer bb2 = ByteBuffer.allocateDirect(textureCoords.length * 4);
        bb2.order(ByteOrder.nativeOrder());
        textureData = bb2.asFloatBuffer();
        textureData.put(textureCoords);

        vertextIndexData = ByteBuffer.allocateDirect(VERTEX_INDEX.length * 2)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer()
                .put(VERTEX_INDEX);

        verxData.position(0);
        textureData.position(0);
        vertextIndexData.position(0);
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
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glEnable(GL_TEXTURE_2D);

        getProgram();

        uTextureUnitLocation = glGetUniformLocation(mProgram,
                "u_TextureUnit");
        uMatrixLocation = glGetUniformLocation(mProgram, "u_Matrix");
        aPositionLocation = glGetAttribLocation(mProgram, "a_Position");
        aTextureCoordinates = glGetAttribLocation(mProgram, "a_TextureCoordinates");
        uColorLocation = glGetUniformLocation(mProgram, "u_Color");

        textureId = TextureHelper.loadTexture(mContext, R.drawable.fengj);

        // Set the active texture unit to texture unit 0.
        glActiveTexture(GL_TEXTURE0);
        // Bind the texture to this unit.
        glBindTexture(GL_TEXTURE_2D, textureId);
        // Tell the texture uniform sampler to use this texture in the shader by
        // telling it to read from texture unit 0.
        glUniform1i(uTextureUnitLocation, 0);
        //传入顶点坐标和纹理坐标
        GLES20.glVertexAttribPointer(aPositionLocation, 3,
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
        GLES20.glViewport(0, 0, width, height);

        final float aspectRatio = width > height ?
                (float) width / (float) height :
                (float) height / (float) width;
        if (width > height) {
            Matrix.orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
        } else {
            Matrix.orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
        }
    }

    @Override
    public void onDrawFrame(GL10 glUnused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        //GLES20.glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);

        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, VERTEX_INDEX.length,
                GLES20.GL_UNSIGNED_SHORT, vertextIndexData);
    }

    private void resolveCrop(float x, float y, float width, float height) {
        float minX = x;
        float minY = y;
        float maxX = minX + width;
        float maxY = minY + height;

        // left bottom
        textureCoords[0] = minX;
        textureCoords[1] = minY;
        // right bottom
        textureCoords[2] = maxX;
        textureCoords[3] = minY;
        // left top
        textureCoords[4] = minX;
        textureCoords[5] = maxY;
        // right top
        textureCoords[6] = maxX;
        textureCoords[7] = maxY;
    }

    private void resolveFlip() {
//        swap(textureCoords, 1, 5);
//        swap(textureCoords, 3, 7);

//        swap(textureCoords, 0, 2);
//        swap(textureCoords, 4, 6);

//        swap(textureCoords, 0, 2);
//        swap(textureCoords, 4, 6);
//
//        swap(textureCoords, 1, 5);
//        swap(textureCoords, 3, 7);

//        float x = textureCoords[0];
//        float y = textureCoords[1];
//        textureCoords[0] = textureCoords[4];
//        textureCoords[1] = textureCoords[5];
//        textureCoords[4] = textureCoords[6];
//        textureCoords[5] = textureCoords[7];
//        textureCoords[6] = textureCoords[2];
//        textureCoords[7] = textureCoords[3];
//        textureCoords[2] = x;
//        textureCoords[3] = y;
    }

    private void swap(float[] arr, int index1, int index2) {
        float temp = arr[index1];
        arr[index1] = arr[index2];
        arr[index2] = temp;
    }
}
