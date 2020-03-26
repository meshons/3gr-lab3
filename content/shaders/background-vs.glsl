#version 300 es

in vec4 vertexPosition;

uniform struct{
    mat4 viewProjMatrix;
    mat4 rayDirMatrix;
} camera;

out vec4 rayDir;

void main(void) {
    gl_Position = vertexPosition;
    rayDir = vertexPosition * camera.viewProjMatrix * camera.rayDirMatrix;
}