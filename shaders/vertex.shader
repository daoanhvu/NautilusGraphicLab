#version 330

layout(location = 0)in vec3 aPosition;
layout(location = 1)in vec3 aNormal;
layout(location = 2)in vec4 aColor;
layout(location = 3)in vec2 aUV;
uniform mat4 uViewMatrix;
uniform mat4 uPerspective;
uniform mat4 uModel;
out vec2 vUV;
out vec4 vColor;
out vec3 vNormal;
void main(void) {
    gl_Position = uPerspective * uViewMatrix * uModel * vec4(aPosition, 1.0);
    vUV = aUV;
    vColor = aColor;
    vNormal = aNormal;
}