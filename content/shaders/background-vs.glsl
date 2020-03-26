#version 300 es

in vec4 vertexPosition;

uniform struct{
    mat4 viewProjMatrix;
    mat4 rayDirMatrix;
} camera;

out vec4 rayDir;

void main(void) {
    gl_Position = vec4(vertexPosition.xy, 0.99999, vertexPosition.w);
    rayDir = normalize(vertexPosition * camera.rayDirMatrix);
}