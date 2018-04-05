package com.administrator.learndemo.opengl.shape;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.administrator.learndemo.R;
import com.administrator.learndemo.opengl.util.LoggerConfig;
import com.administrator.learndemo.opengl.util.MatrixHelper;
import com.administrator.learndemo.opengl.util.ShaderHelper;
import com.administrator.learndemo.opengl.util.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


/**
 * Created by Administrator on 2018/3/7.
 */

public class AirHockey implements Shape {
    private static final int BYTES_PER_FLOAT = 4;
    private static final int POSITION_COMPONENT_COUNT = 4;
    private static final int COLOR_COMPONENT_COUNT = 3;
    //国为我们在同一个数据数组里面既有位置又有颜色属性，OpenGL不能再假定下一个位置是紧跟着前一个位置的。
    //一旦OpenGL读入了一个顶点的位置，如果它想读入下一个顶点的位置，它不得不跳过当前顶点的颜色数据。我们用那个
    //跨距告诉OpenGL每个位置之间有多少个字节，这样它就知道需要跳过多少了。
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;
    private FloatBuffer vertexData;
    private static final String A_POSITION = "a_Position";
    private static final String A_COLOR = "a_Color";
    private static final String U_MATRIX = "u_Matrix";

    private Context mContext;
    private int program;

    private int aPositionLocation;
    private int aColorLocation;
    private int uMatrixLocation;

    private final float[] projectionMatrix = new float[16];
    private final float[] modeMatrix = new float[16];

    private float[] mTableVertices = {
            0f, 0f, 0f, 1.5f, 1f, 1f, 1f,
            -0.5f, -0.8f, 0f, 1f, 0.7f, 0.7f, 0.7f,
            0.5f, -0.8f, 0f, 1f, 0.7f, 0.7f, 0.7f,
            0.5f, 0.8f, 0f, 2f, 0.7f, 0.7f, 0.7f,
            -0.5f, 0.8f, 0f, 2f, 0.7f, 0.7f, 0.7f,
            -0.5f, -0.8f, 0f, 1f, 0.7f, 0.7f, 0.7f,

            // Line 1
            -0.5f, 0f, 0f, 1.5f, 1f, 0f, 0f,
            0.5f, 0f, 0f, 1.5f, 1f, 0f, 0f,

            // Mallets
            0f, -0.25f, 0f, 1.25f, 0f, 0f, 1f,
            0f, 0.25f, 0f, 1.75f, 1f, 0f, 0f
    };

    public AirHockey(Context context) {
        mContext = context;
        initVertexData();
    }

    public void initVertexData() {
        vertexData = ByteBuffer.allocateDirect(mTableVertices.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        vertexData.put(mTableVertices);

        String vertexShaderSource = TextResourceReader
                .readTextFileFromResource(mContext, R.raw.color_vertex_shader);
        String fragmentShaderSource = TextResourceReader
                .readTextFileFromResource(mContext, R.raw.color_fragment_shader);

        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);

        program = ShaderHelper.linkProgram(vertexShader, fragmentShader);

        if (LoggerConfig.ON) {
            ShaderHelper.validateProgram(program);
        }

        GLES20.glUseProgram(program);

        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
        aColorLocation = GLES20.glGetAttribLocation(program, A_COLOR);
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX);

        // Bind our data, specified by the variable vertexData, to the vertex
        // attribute at location A_POSITION_LOCATION.
        vertexData.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
                false, STRIDE, vertexData);
        GLES20.glEnableVertexAttribArray(aPositionLocation);

        vertexData.position(POSITION_COMPONENT_COUNT);
        GLES20.glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GLES20.GL_FLOAT,
                false, STRIDE, vertexData);
        GLES20.glEnableVertexAttribArray(aColorLocation);
    }

    @Override
    public void projectionMatrix(int width, int height) {
//        final float aspectRatio = width > height ? (float)width / (float) height:(float)height / (float) width;
//        if (width > height) {
//            Matrix.orthoM(projectionMatrix,0,-aspectRatio, aspectRatio,-1f,1f,-1f,1f);
//        } else {
//            Matrix.orthoM(projectionMatrix,0,-1f, 1f,-aspectRatio, aspectRatio,-1f,1f);
//        }

        MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width / (float) height, 1f, 10f);
        Matrix.setIdentityM(modeMatrix, 0);
        Matrix.translateM(modeMatrix, 0, 0f, 0f, -2f);
        Matrix.rotateM(modeMatrix,0, -45f,1f,0f,0f);

        //不论什么时候把两个矩阵相乘，都需要一个临时变量来存储其结果。如果尝试直接写入这个结果，
        //这个结果将是未定义的！
        final float[] temp = new float[16];
        Matrix.multiplyMM(temp, 0, projectionMatrix, 0, modeMatrix, 0);
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);
    }

    public void draw() {
        GLES20.glClear(GLES20.GL_DEPTH_BITS);

        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);

        // Draw the table.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);

        // Draw the center dividing line.
        GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2);

        // Draw the first mallet blue.
        GLES20.glDrawArrays(GLES20.GL_POINTS, 8, 1);

        // Draw the second mallet red.
        GLES20.glDrawArrays(GLES20.GL_POINTS, 9, 1);

        GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2);
    }
}
