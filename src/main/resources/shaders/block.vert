#version 330 core

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec2 aTexCoord;

out vec2 TexCoord;
out vec3 debugColor;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main()
{
    gl_Position = projectionMatrix * viewMatrix * vec4(aPos, 1.0);
    TexCoord = aTexCoord;
    debugColor = vec3(aTexCoord.x, aTexCoord.y, 0.0); // Визуализируем текстурные координаты как цвет
}