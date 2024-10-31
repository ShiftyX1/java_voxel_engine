#version 330 core

in vec2 fragTexCoord;

uniform sampler2D textureSampler;

out vec4 fragColor;

void main() {
    // Получаем цвет из текстуры
    vec4 texColor = texture(textureSampler, fragTexCoord);
    
    // Отладочная визуализация: показываем разными цветами разные проблемы
    if(fragTexCoord.x < 0.0 || fragTexCoord.x > 1.0 || 
       fragTexCoord.y < 0.0 || fragTexCoord.y > 1.0) {
        // Красный цвет если координаты текстуры вне диапазона [0,1]
        fragColor = vec4(1.0, 0.0, 0.0, 1.0);
    }
    else if(texColor.a < 0.1) {
        // Зеленый цвет если текстура прозрачная
        fragColor = vec4(0.0, 1.0, 0.0, 1.0);
    }
    else if(texColor == vec4(0.0)) {
        // Синий цвет если текстура черная
        fragColor = vec4(0.0, 0.0, 1.0, 1.0);
    }
    else {
        // Используем цвет из текстуры
        fragColor = texColor;
        
        // Добавляем небольшое изменение цвета в зависимости от координат
        // для визуализации правильности наложения текстуры
        fragColor.rgb *= 0.8 + 0.2 * fragTexCoord.x;
    }
}