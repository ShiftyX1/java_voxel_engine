package com.voxelengine.engine;

import com.voxelengine.utils.ResourceLoader;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

public class TextureAtlas {
    private static final String TERRAIN_TEXTURE_PATH = "/textures/blocks/terrain.png";
    private int textureId;
    
    public void init() {
        System.out.println("Initializing TextureAtlas...");
        System.out.println("Loading texture from: " + TERRAIN_TEXTURE_PATH);
        textureId = ResourceLoader.loadTexture(TERRAIN_TEXTURE_PATH);
        System.out.println("Texture loaded with ID: " + textureId);
        
        // Проверяем, что текстура действительно загружена
        bind();
        int width = glGetTexLevelParameteri(GL_TEXTURE_2D, 0, GL_TEXTURE_WIDTH);
        int height = glGetTexLevelParameteri(GL_TEXTURE_2D, 0, GL_TEXTURE_HEIGHT);
        System.out.println("Loaded texture dimensions: " + width + "x" + height);
    }
    
    public void bind() {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureId);
    }
    
    public void cleanup() {
        if (textureId != 0) {
            glDeleteTextures(textureId);
        }
    }
}