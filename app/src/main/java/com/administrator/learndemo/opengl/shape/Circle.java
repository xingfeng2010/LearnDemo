package com.administrator.learndemo.opengl.shape;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.administrator.learndemo.R;
import com.administrator.learndemo.opengl.util.LoggerConfig;
import com.administrator.learndemo.opengl.util.ShaderHelper;
import com.administrator.learndemo.opengl.util.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Administrator on 2018/3/13.
 */

public class Circle implements Shape {
    private Context context;
    private FloatBuffer vertexData;
    // 定义圆心坐标
    private float x;
    private float y;
    // 半径
    private float r;
    // 三角形分割的数量
    private int count = 40;
    // 每个顶点包含的数据个数 （ x 和 y ）
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int BYTES_PER_FLOAT = 4;

    private static final String U_COLOR = "u_Color";
    private static final String A_POSITION = "a_Position";
    private int program;
    private int uColorLocation;
    private int aPositionLocation;

    /*
     * 第一步: 定义投影矩阵相关
     */
    private static final String U_MATRIX = "u_Matrix";
    private final float[] projectionMatrix = new float[16];
    private int uMatrixLocation;

    public Circle(Context context) {
        this.context = context;
        x = 0f;
        y = 0f;
        r = 0.6f;
        initVertexData();
    }

    private void initVertexData() {
        // 顶点的个数，我们分割count个三角形，有count+1个点，再加上圆心共有count+2个点
        final int nodeCount = count + 2;
        float circleCoords[] = new float[nodeCount * POSITION_COMPONENT_COUNT];
        // x y
        int offset = 0;
        circleCoords[offset++] = x;// 中心点
        circleCoords[offset++] = y;
        for (int i = 0; i < count + 1; i++) {
            float angleInRadians = ((float) i / (float) count)
                    * ((float) Math.PI * 2f);
            circleCoords[offset++] = x + r * (float) Math.sin(angleInRadians);
            circleCoords[offset++] = y + r * (float) Math.cos(angleInRadians);
        }
        // 为存放形状的坐标，初始化顶点字节缓冲
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (坐标数 * 4)float占四字节
                circleCoords.length * BYTES_PER_FLOAT);
        // 设用设备的本点字节序
        bb.order(ByteOrder.nativeOrder());
        // 从ByteBuffer创建一个浮点缓冲
        vertexData = bb.asFloatBuffer();
        // 把坐标们加入FloatBuffer中
        vertexData.put(circleCoords);
        // 设置buffer，从第一个坐标开始读
        vertexData.position(0);

        getProgram();

        uColorLocation = GLES20.glGetUniformLocation(program, U_COLOR);
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);

        /*
         * 第二步: 获取顶点着色器中投影矩阵的location
         */
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX);

        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT,
                GLES20.GL_FLOAT, false, 0, vertexData);

        GLES20.glEnableVertexAttribArray(aPositionLocation);
    }

    private void getProgram() {
        String vertexShaderSource = TextResourceReader
                .readTextFileFromResource(context, R.raw.color_vertex_shader);
        String fragmentShaderSource = TextResourceReader
                .readTextFileFromResource(context, R.raw.color_fragment_shader);

        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper
                .compileFragmentShader(fragmentShaderSource);

        program = ShaderHelper.linkProgram(vertexShader, fragmentShader);

        if (LoggerConfig.ON) {
            ShaderHelper.validateProgram(program);
        }

        GLES20.glUseProgram(program);
    }

    /**
     * 第三步 ： 根据屏幕的width 和 height 创建投影矩阵
     *
     * @param width
     * @param height
     */
    public void projectionMatrix(int width, int height) {
        final float aspectRatio = width > height ?
                (float) width / (float) height :
                (float) height / (float) width;
        if (width > height) {
            Matrix.orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
        } else {
            Matrix.orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
        }
    }

    public void draw() {
        GLES20.glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
        /*
         * 第四步：传入投影矩阵
         */
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, count + 2);
    }
}
