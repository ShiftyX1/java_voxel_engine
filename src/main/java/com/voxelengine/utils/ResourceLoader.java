package com.voxelengine.utils;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.stb.STBImage.*;

public class ResourceLoader {
    public static int loadTexture(String path) {
        System.out.println("=== Starting texture loading process ===");
        System.out.println("Looking for texture at path: " + path);
        
        int textureId = 0;
        
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            // Получаем InputStream из ресурсов
            InputStream inputStream = ResourceLoader.class.getClassLoader().getResourceAsStream(path);
            if (inputStream == null) {
                System.err.println("ERROR: Resource not found at: " + path);
                // Перечислим доступные ресурсы в classpath
                ClassLoader cl = ResourceLoader.class.getClassLoader();
                try {
                    java.net.URL resourceUrl = cl.getResource("");
                    if (resourceUrl != null) {
                        System.out.println("Available resources in root:");
                        java.io.File folder = new java.io.File(resourceUrl.toURI());
                        for (java.io.File file : folder.listFiles()) {
                            System.out.println(" - " + file.getName());
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error listing resources: " + e.getMessage());
                }
                throw new RuntimeException("Resource not found: " + path);
            }

            System.out.println("Resource found, reading data...");

            // Читаем данные в ByteBuffer
            ByteBuffer imageBuffer;
            try {
                byte[] bytes = inputStream.readAllBytes();
                System.out.println("Read " + bytes.length + " bytes from input stream");
                imageBuffer = BufferUtils.createByteBuffer(bytes.length);
                imageBuffer.put(bytes);
                imageBuffer.flip();
            } catch (Exception e) {
                System.err.println("ERROR: Failed to read texture file: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Failed to read texture file: " + path, e);
            }
            System.out.println("Loading image with STB...");
            stbi_set_flip_vertically_on_load(true);
            ByteBuffer image = stbi_load_from_memory(imageBuffer, width, height, channels, STBI_rgb_alpha);

            if (image != null) {
                System.out.println("Image loaded successfully!");
                System.out.println("Dimensions: " + width.get(0) + "x" + height.get(0));
                System.out.println("Channels: " + channels.get(0));

                textureId = glGenTextures();
                System.out.println("Generated OpenGL texture ID: " + textureId);
                
                glBindTexture(GL_TEXTURE_2D, textureId);
                System.out.println("Texture bound to GL_TEXTURE_2D");

                glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 
                           0, GL_RGBA, GL_UNSIGNED_BYTE, image);

                int error = glGetError();
                if (error != GL_NO_ERROR) {
                    System.err.println("OpenGL Error after texture loading: " + error);
                } else {
                    System.out.println("Texture data uploaded to GPU successfully");
                }

                stbi_image_free(image);
            } else {
                System.err.println("ERROR: STB image loading failed");
                System.err.println("STB Failure reason: " + stbi_failure_reason());
                throw new RuntimeException("Failed to load texture: " + path);
            }
        } catch (Exception e) {
            System.err.println("ERROR: Texture loading failed with exception:");
            e.printStackTrace();
            throw new RuntimeException("Error loading texture: " + path, e);
        }

        System.out.println("=== Texture loading process completed ===");
        return textureId;
    }
}