#version 330

layout(location = 0)in vec3 aPosition;
layout(location = 1)in vec4 aColor;
uniform mat4 uViewMatrix;
uniform mat4 uPerspective;
uniform mat4 uModel;
out vec4 vColor;
void main(void) {
    gl_Position = uPerspective * uViewMatrix * uModel * vec4(aPosition, 1.0);
    vColor = aColor;
}