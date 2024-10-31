package com.voxelengine.model;

public class Block {
    private static final float BLOCK_SIZE = 1.0f;
    private final BlockType type;
    private final TextureCoords[] textureCoords;
    
    public Block(BlockType type) {
        this.type = type;
        this.textureCoords = new TextureCoords[6]; // 6 сторон
        initializeTextureCoords();
    }
    
    private void initializeTextureCoords() {
        if (type == BlockType.DIRT) {
            // Все стороны блока земли используют одну и ту же текстуру
            TextureCoords dirtTexture = new TextureCoords(2, 0);
            for (int i = 0; i < 6; i++) {
                textureCoords[i] = dirtTexture;
            }
        }
    }
    
    public float[] generateVertices(float x, float y, float z) {
        if (type == BlockType.AIR) {
            return new float[0];
        }
        
        // 6 граней * 6 вершин на грань * 5 компонент на вершину (3 для позиции, 2 для текстуры)
        float[] vertices = new float[6 * 6 * 5];
        int offset = 0;
        
        // Верхняя грань (Y+)
        offset = addFace(vertices, offset,
                x, y + BLOCK_SIZE, z + BLOCK_SIZE,           // v1
                x + BLOCK_SIZE, y + BLOCK_SIZE, z + BLOCK_SIZE,  // v2
                x + BLOCK_SIZE, y + BLOCK_SIZE, z,              // v3
                x, y + BLOCK_SIZE, z,                       // v4
                textureCoords[0]);
                
        // Нижняя грань (Y-)
        offset = addFace(vertices, offset,
                x, y, z,                       // v1
                x + BLOCK_SIZE, y, z,              // v2
                x + BLOCK_SIZE, y, z + BLOCK_SIZE,     // v3
                x, y, z + BLOCK_SIZE,              // v4
                textureCoords[1]);
                
        // Передняя грань (Z+)
        offset = addFace(vertices, offset,
                x, y, z + BLOCK_SIZE,              // v1
                x + BLOCK_SIZE, y, z + BLOCK_SIZE,     // v2
                x + BLOCK_SIZE, y + BLOCK_SIZE, z + BLOCK_SIZE, // v3
                x, y + BLOCK_SIZE, z + BLOCK_SIZE,     // v4
                textureCoords[2]);
                
        // Правая грань (X+)
        offset = addFace(vertices, offset,
                x + BLOCK_SIZE, y, z + BLOCK_SIZE,     // v1
                x + BLOCK_SIZE, y, z,              // v2
                x + BLOCK_SIZE, y + BLOCK_SIZE, z,     // v3
                x + BLOCK_SIZE, y + BLOCK_SIZE, z + BLOCK_SIZE, // v4
                textureCoords[3]);
                
        // Задняя грань (Z-)
        offset = addFace(vertices, offset,
                x + BLOCK_SIZE, y, z,              // v1
                x, y, z,                       // v2
                x, y + BLOCK_SIZE, z,              // v3
                x + BLOCK_SIZE, y + BLOCK_SIZE, z,     // v4
                textureCoords[4]);
                
        // Левая грань (X-)
        offset = addFace(vertices, offset,
                x, y, z,                       // v1
                x, y, z + BLOCK_SIZE,              // v2
                x, y + BLOCK_SIZE, z + BLOCK_SIZE,     // v3
                x, y + BLOCK_SIZE, z,              // v4
                textureCoords[5]);
                
        return vertices;
    }
    
    private int addFace(float[] vertices, int offset,
                       float x1, float y1, float z1,
                       float x2, float y2, float z2,
                       float x3, float y3, float z3,
                       float x4, float y4, float z4,
                       TextureCoords texCoords) {
        // Первый треугольник (v1, v2, v3)
        // v1
        vertices[offset++] = x1;
        vertices[offset++] = y1;
        vertices[offset++] = z1;
        vertices[offset++] = texCoords.getU1();
        vertices[offset++] = texCoords.getV1();
        
        // v2
        vertices[offset++] = x2;
        vertices[offset++] = y2;
        vertices[offset++] = z2;
        vertices[offset++] = texCoords.getU2();
        vertices[offset++] = texCoords.getV1();
        
        // v3
        vertices[offset++] = x3;
        vertices[offset++] = y3;
        vertices[offset++] = z3;
        vertices[offset++] = texCoords.getU2();
        vertices[offset++] = texCoords.getV2();
        
        // Второй треугольник (v1, v3, v4)
        // v1
        vertices[offset++] = x1;
        vertices[offset++] = y1;
        vertices[offset++] = z1;
        vertices[offset++] = texCoords.getU1();
        vertices[offset++] = texCoords.getV1();
        
        // v3
        vertices[offset++] = x3;
        vertices[offset++] = y3;
        vertices[offset++] = z3;
        vertices[offset++] = texCoords.getU2();
        vertices[offset++] = texCoords.getV2();
        
        // v4
        vertices[offset++] = x4;
        vertices[offset++] = y4;
        vertices[offset++] = z4;
        vertices[offset++] = texCoords.getU1();
        vertices[offset++] = texCoords.getV2();
        
        return offset;
    }
    
    public BlockType getType() {
        return type;
    }
}