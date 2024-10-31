package com.voxelengine.model;

public class TextureCoords {
    // Размер текстуры блока в пикселях
    private static final float TEXTURE_SIZE = 16.0f;
    // Размер атласа в пикселях
    private static final float ATLAS_SIZE = 256.0f;
    
    private final float u1, v1, u2, v2;
    
    public TextureCoords(int x, int y) {
        float epsilon = 0.001f;
        
        // Вычисляем нормализованные координаты текстуры
        this.u1 = (x * TEXTURE_SIZE + epsilon) / ATLAS_SIZE;
        this.v1 = (y * TEXTURE_SIZE + epsilon) / ATLAS_SIZE;
        this.u2 = ((x + 1) * TEXTURE_SIZE - epsilon) / ATLAS_SIZE;
        this.v2 = ((y + 1) * TEXTURE_SIZE - epsilon) / ATLAS_SIZE;
        
        System.out.printf("Created texture coords for block at (%d,%d):%n", x, y);
        System.out.printf("u1=%.4f, v1=%.4f, u2=%.4f, v2=%.4f%n", u1, v1, u2, v2);
    }
    
    public float getU1() { return u1; }
    public float getV1() { return v1; }
    public float getU2() { return u2; }
    public float getV2() { return v2; }
}