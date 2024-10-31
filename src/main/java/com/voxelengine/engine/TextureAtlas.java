package com.voxelengine.engine;

import com.voxelengine.utils.ResourceLoader;
import java.net.URL;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

public class TextureAtlas {
    private static final String TERRAIN_TEXTURE_PATH = "textures/blocks/terrain.png";
    private int textureId;
    
    public void init() {
        System.out.println("\n=== TextureAtlas Initialization ===");
        System.out.println("Loading texture from: " + TERRAIN_TEXTURE_PATH);
        
        // Try to locate the resource
        URL resourceUrl = getClass().getClassLoader().getResource(TERRAIN_TEXTURE_PATH);
        if (resourceUrl == null) {
            System.err.println("ERROR: Could not find texture file at: " + TERRAIN_TEXTURE_PATH);
            System.out.println("Working directory: " + System.getProperty("user.dir"));
            return;
        }
        System.out.println("Found texture at: " + resourceUrl);
        
        textureId = ResourceLoader.loadTexture(TERRAIN_TEXTURE_PATH);
        System.out.println("Loaded texture ID: " + textureId);
        
        glBindTexture(GL_TEXTURE_2D, textureId);
        
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        int minFilter = glGetTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER);
        int magFilter = glGetTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER);
        System.out.println("Texture parameters:");
        System.out.println("MIN_FILTER: " + (minFilter == GL_NEAREST ? "GL_NEAREST" : "OTHER"));
        System.out.println("MAG_FILTER: " + (magFilter == GL_NEAREST ? "GL_NEAREST" : "OTHER"));
        
        int width = glGetTexLevelParameteri(GL_TEXTURE_2D, 0, GL_TEXTURE_WIDTH);
        int height = glGetTexLevelParameteri(GL_TEXTURE_2D, 0, GL_TEXTURE_HEIGHT);
        System.out.println("Texture dimensions: " + width + "x" + height);
        
        System.out.println("=== TextureAtlas Initialization Complete ===\n");
    }
    
    public void bind() {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureId);
        
        int currentTexture = glGetInteger(GL_TEXTURE_BINDING_2D);
        if (currentTexture != textureId) {
            System.err.println("ERROR: Failed to bind texture! Expected: " + textureId + ", Got: " + currentTexture);
        }
    }
    
    public void cleanup() {
        if (textureId != 0) {
            glDeleteTextures(textureId);
            System.out.println("Texture " + textureId + " deleted");
            textureId = 0;
        }
    }
}