package com.voxelengine.utils;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.stb.STBImage.*;

public class ResourceLoader {
    public static int loadTexture(String path) {
        int textureId = 0;
        
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            // Загружаем изображение
            stbi_set_flip_vertically_on_load(true);
            ByteBuffer image = stbi_load(path.substring(1), width, height, channels, STBI_rgb_alpha);

            if (image != null) {
                System.out.println("Texture loaded: " + path);
                System.out.println("Dimensions: " + width.get(0) + "x" + height.get(0));
                System.out.println("Channels: " + channels.get(0));

                // Создаем текстуру OpenGL
                textureId = glGenTextures();
                glBindTexture(GL_TEXTURE_2D, textureId);

                // Устанавливаем параметры текстуры
                glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

                // Загружаем данные изображения в текстуру
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 
                           0, GL_RGBA, GL_UNSIGNED_BYTE, image);

                // Проверяем ошибки OpenGL
                int error = glGetError();
                if (error != GL_NO_ERROR) {
                    System.err.println("OpenGL Error after texture loading: " + error);
                }

                stbi_image_free(image);
            } else {
                System.err.println("Failed to load texture: " + path);
                System.err.println("STB Reason: " + stbi_failure_reason());
            }
        }

        return textureId;
    }
}