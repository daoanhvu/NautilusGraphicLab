#version 330

in vec3 aPosition;
in vec2 aUV;
uniform mat4 uMVP;
varying vec2 vUV;
void main(void) {
    gl_Position = uMVP * aPosition;
    vUV = aUV;
}