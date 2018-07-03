#version 330

uniform sampler2D difSampler;
uniform vec3 uLightPos;
in vec4 vColor;
in vec3 vNormal;
in vec2 vUV;
out vec4 fragColor;
void main() {
    fragColor = texture(difSampler, vUV).rgba;
}