#version 330

layout(location = 0)in vec3 aPosition;
layout(location = 1)in vec4 aColor;
layout(location = 2)in vec3 aNormal;
layout(location = 3)in vec2 aUV;
uniform int useNormal;
uniform mat4 uViewMatrix;
uniform mat4 uPerspective;
uniform mat4 uModel;
out vec3 vWPos;
out vec4 vColor;
out vec2 vUV;
out vec4 vertexInViewSpace;
void main() {
    vertexInViewSpace = uViewMatrix * uModel * vec4(aPosition, 1.0);
    gl_Position = uPerspective * vertexInViewSpace;
    if( useNormal == 1 ) {

    }
    vColor = aColor;
    vWPos = aPosition;
    vUV = aUV;
}