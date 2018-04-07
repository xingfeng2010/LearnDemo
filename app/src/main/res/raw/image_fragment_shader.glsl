precision mediump float;

uniform sampler2D u_TextureUnit;
varying vec2 v_TextureCoordinates;

uniform vec4 u_Color;

void main()
{
    vec4 changeColor = vec4(0.3,0,0.5,1);
    vec4 color = texture2D(u_TextureUnit,v_TextureCoordinates);
    //float value = color.r * changeColor.r + color.g * changeColor.g+ color.b* changeColor.b;
    gl_FragColor = color;
}