#version 330 core

// These attributes are passed from the renderer
// by calling bindAttribLocation
layout(location = 0) in vec4 aPosition;
layout(location = 1) in vec3 aNormal;
layout(location = 1) in vec4 aColor;

precision mediump float;
uniform mat4 uModelView;
uniform mat4 uMVP;
varying vec3 vPositionInE;
varying vec3 vNormalInE;
varying vec4 vColor;
void main() {
	//Transform the vertex into eye space
	vPositionInE = vec3(uModelView * aPosition);
	//Transform normal vector into eye space
	vNormalInE = vec3(uModelView * vec4(aNormal, 0.0));
	vColor = aColor;
	gl_Position = uMVP * aPosition;
}