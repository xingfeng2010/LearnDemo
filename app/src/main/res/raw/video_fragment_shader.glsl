/**使用GL_OES_EGL_image_external扩展处理，来增强GLSL*/
#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES texture; //定义扩展的的纹理取样器amplerExternalOES
varying vec2 v_TexCoordinate;

void main () {
    vec4 color = texture2D(texture, v_TexCoordinate);
    float gray = color.r * 0.299 + color.g * 0.587 + color.b * 0.114;
    gl_FragColor = vec4(gray,gray,gray,color.w);
}