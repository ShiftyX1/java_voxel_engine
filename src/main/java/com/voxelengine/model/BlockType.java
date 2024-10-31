package com.voxelengine.model;

public enum BlockType {
    AIR(0),
    DIRT(1),
    GRASS(2),
    STONE(3);
    
    private final int id;
    
    BlockType(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
}