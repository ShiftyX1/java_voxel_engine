#version 330 core

in vec2 TexCoord;
in vec3 debugColor;
out vec4 FragColor;

uniform sampler2D textureSampler;

void main()
{
    vec4 texColor = texture(textureSampler, TexCoord);
    // Смешиваем текстуру с отладочным цветом для визуализации текстурных координат
    FragColor = mix(texColor, vec4(debugColor, 1.0), 0.5);
}