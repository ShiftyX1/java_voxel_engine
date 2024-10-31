package com.voxelengine.model;

public class Block {
    private static final float BLOCK_SIZE = 1.0f;
    private final BlockType type;
    private final TextureCoords[] textureCoords;
    
    public Block(BlockType type) {
        this.type = type;
        this.textureCoords = new TextureCoords[6];
        initializeTextureCoords();
    }
    
    private void initializeTextureCoords() {
        switch (type) {
            case DIRT:
                // Текстура земли находится в позиции (0,0)
                TextureCoords dirtTexture = new TextureCoords(0, 0);
                // Применяем одинаковую текстуру для всех граней
                for (int i = 0; i < 6; i++) {
                    textureCoords[i] = dirtTexture;
                }
                break;
                
            case STONE:
                // Булыжник находится в позиции (1,0)
                TextureCoords stoneTexture = new TextureCoords(1, 0);
                for (int i = 0; i < 6; i++) {
                    textureCoords[i] = stoneTexture;
                }
                break;
                
            default:
                // Для неизвестных типов используем текстуру земли
                TextureCoords defaultTexture = new TextureCoords(0, 0);
                for (int i = 0; i < 6; i++) {
                    textureCoords[i] = defaultTexture;
                }
                break;
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
            x, y + BLOCK_SIZE, z,              // V0
            x, y + BLOCK_SIZE, z + BLOCK_SIZE,     // V1
            x + BLOCK_SIZE, y + BLOCK_SIZE, z + BLOCK_SIZE, // V2
            x + BLOCK_SIZE, y + BLOCK_SIZE, z,     // V3
            textureCoords[0]);
        
        // Нижняя грань (Y-)
        offset = addFace(vertices, offset,
            x, y, z + BLOCK_SIZE,              // V0
            x, y, z,                       // V1
            x + BLOCK_SIZE, y, z,              // V2
            x + BLOCK_SIZE, y, z + BLOCK_SIZE,     // V3
            textureCoords[1]);
        
        // Передняя грань (Z+)
        offset = addFace(vertices, offset,
            x, y, z + BLOCK_SIZE,              // V0
            x + BLOCK_SIZE, y, z + BLOCK_SIZE,     // V1
            x + BLOCK_SIZE, y + BLOCK_SIZE, z + BLOCK_SIZE, // V2
            x, y + BLOCK_SIZE, z + BLOCK_SIZE,     // V3
            textureCoords[2]);
        
        // Задняя грань (Z-)
        offset = addFace(vertices, offset,
            x + BLOCK_SIZE, y, z,              // V0
            x, y, z,                       // V1
            x, y + BLOCK_SIZE, z,              // V2
            x + BLOCK_SIZE, y + BLOCK_SIZE, z,     // V3
            textureCoords[3]);
        
        // Правая грань (X+)
        offset = addFace(vertices, offset,
            x + BLOCK_SIZE, y, z + BLOCK_SIZE,     // V0
            x + BLOCK_SIZE, y, z,              // V1
            x + BLOCK_SIZE, y + BLOCK_SIZE, z,     // V2
            x + BLOCK_SIZE, y + BLOCK_SIZE, z + BLOCK_SIZE, // V3
            textureCoords[4]);
        
        // Левая грань (X-)
        offset = addFace(vertices, offset,
            x, y, z,                       // V0
            x, y, z + BLOCK_SIZE,              // V1
            x, y + BLOCK_SIZE, z + BLOCK_SIZE,     // V2
            x, y + BLOCK_SIZE, z,              // V3
            textureCoords[5]);
        
        return vertices;
    }
    
    private int addFace(float[] vertices, int offset,
                       float x0, float y0, float z0,  // V0
                       float x1, float y1, float z1,  // V1
                       float x2, float y2, float z2,  // V2
                       float x3, float y3, float z3,  // V3
                       TextureCoords texCoords) {
        // Первый треугольник (V0, V1, V2)
        vertices[offset++] = x0;
        vertices[offset++] = y0;
        vertices[offset++] = z0;
        vertices[offset++] = texCoords.getU1();
        vertices[offset++] = texCoords.getV1();
        
        vertices[offset++] = x1;
        vertices[offset++] = y1;
        vertices[offset++] = z1;
        vertices[offset++] = texCoords.getU2();
        vertices[offset++] = texCoords.getV1();
        
        vertices[offset++] = x2;
        vertices[offset++] = y2;
        vertices[offset++] = z2;
        vertices[offset++] = texCoords.getU2();
        vertices[offset++] = texCoords.getV2();
        
        // Второй треугольник (V0, V2, V3)
        vertices[offset++] = x0;
        vertices[offset++] = y0;
        vertices[offset++] = z0;
        vertices[offset++] = texCoords.getU1();
        vertices[offset++] = texCoords.getV1();
        
        vertices[offset++] = x2;
        vertices[offset++] = y2;
        vertices[offset++] = z2;
        vertices[offset++] = texCoords.getU2();
        vertices[offset++] = texCoords.getV2();
        
        vertices[offset++] = x3;
        vertices[offset++] = y3;
        vertices[offset++] = z3;
        vertices[offset++] = texCoords.getU1();
        vertices[offset++] = texCoords.getV2();
        
        return offset;
    }
    
    public BlockType getType() {
        return type;
    }
}