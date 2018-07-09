#version 330

//lights
uniform vec3 lightPos1;
uniform vec3 lightPos2;
uniform vec3 lightPos3;
uniform vec3 lightPos4;

uniform vec3 lightColor1;
uniform vec3 lightColor2;
uniform vec3 lightColor3;
uniform vec3 lightColor4;

uniform int useTexture;
uniform int numLight;
uniform sampler2D difSampler;
in vec4 vColor;
in vec2 vUV;
//This is world position of the vertex
in vec3 vWPos;
out vec4 fragColor;

struct LightSource {
    vec3 wpos; //world position of the light
    vec3 direction_camera;
    vec3 lightColor;
    vec3 diffuse;
    vec3 ambient;
    vec3 specular;
};

LightSource light[4];

void main() {
    if( numLight > 0 ) {
        light[0].wpos = lightPos1;
        light[1].wpos = lightPos2;
        light[2].wpos = lightPos3;
        light[3].wpos = lightPos4;

        light[0].lightColor = lightColor1;
        light[1].lightColor = lightColor2;
        light[2].lightColor = lightColor3;
        light[3].lightColor = lightColor4;

        for(int i=0; i<numLight; ++i) {
            // Distance from light i to this vertex
            float distance = length( light[i].wpos - vWPos );
            float distance2 = distance * distance;
            vec3 l = normalize( light[i].direction_camera );
            fragColor = vColor;
        }
    } else {
        fragColor = vColor;
    }

    if( useTexture >= 1 ) {
        fragColor = fragColor * texture(difSampler, vUV).rgba;
    }
}