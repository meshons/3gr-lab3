#version 300 es

in vec4 vertexPosition;

uniform struct{
  mat4 modelMatrix;
} gameObject;

uniform struct{
  mat4 viewProjMatrix;
} camera;

out vec4 tex;

void main(void) {
  gl_Position = vertexPosition * gameObject.modelMatrix * camera.viewProjMatrix;
  tex = vec4(vertexPosition.x, -1.0f * vertexPosition.y, vertexPosition.zw);
}