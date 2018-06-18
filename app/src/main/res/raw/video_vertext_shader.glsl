attribute vec4 vPosition;    //顶点着色器输入变量由attribute来声明
attribute vec4 vTexCoordinate;
//uniform表示一个变量的值由应用程序在着色器执行之前指定，
//并且在图元处理过程中不会发生任何变化。mat4表示一个4x4矩阵
uniform mat4 textureTransform;
varying vec2 v_TexCoordinate;   //片段着色器输入变量用arying来声明

void main () {
    v_TexCoordinate = (textureTransform * vTexCoordinate).xy;
    gl_Position = vPosition;
}