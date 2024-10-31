package com.voxelengine.model;

public class TextureCoords {
    // Размер текстуры блока в пикселях
    private static final float TEXTURE_SIZE = 16.0f;
    // Размер атласа в пикселях
    private static final float ATLAS_WIDTH = 64.0f;
    private static final float ATLAS_HEIGHT = 48.0f;
    
    private final float u1, v1, u2, v2;
    
    public TextureCoords(int x, int y) {
        // x и y - позиции текстуры в атласе (в блоках 16x16)
        this.u1 = (x * TEXTURE_SIZE) / ATLAS_WIDTH;
        this.v1 = (y * TEXTURE_SIZE) / ATLAS_HEIGHT;
        this.u2 = ((x + 1) * TEXTURE_SIZE) / ATLAS_WIDTH;
        this.v2 = ((y + 1) * TEXTURE_SIZE) / ATLAS_HEIGHT;
        
        System.out.println(String.format("Created texture coords for block (%d,%d): u1=%.3f, v1=%.3f, u2=%.3f, v2=%.3f",
            x, y, u1, v1, u2, v2));
    }
    
    public float getU1() { return u1; }
    public float getV1() { return v1; }
    public float getU2() { return u2; }
    public float getV2() { return v2; }
}