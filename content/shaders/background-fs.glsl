#version 300 es

precision highp float;

in vec4 rayDir;
out vec4 fragmentColor;

uniform struct {
    samplerCube envTexture;
} material;




void main(void) {
    fragmentColor = texture ( material.envTexture, rayDir.xyz);
}
