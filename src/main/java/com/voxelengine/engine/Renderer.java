package com.voxelengine.engine;

import com.voxelengine.model.Block;
import org.lwjgl.system.MemoryUtil;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Renderer {
    private int vaoId;
    private int vboId;
    private Shader shader;
    private Camera camera;
    
    public void init() throws Exception {
        shader = new Shader();
        shader.createVertexShader("/shaders/block.vert");
        shader.createFragmentShader("/shaders/block.frag");
        shader.link();
        shader.createUniforms();
        
        // Create the VAO
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);
        
        // Create the VBO
        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        
        // Position attribute
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        
        // Texture attribute
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);
        
        // Unbind VAO
        glBindVertexArray(0);
    }
    
    public void render(Block block, float x, float y, float z) {
        shader.bind();
        
        // Update view and projection matrices
        camera.updateViewMatrix();
        shader.setUniform("viewMatrix", camera.getViewMatrix());
        shader.setUniform("projectionMatrix", camera.getProjectionMatrix());
        
        glBindVertexArray(vaoId);
        
        float[] vertices = block.generateVertices(x, y, z);
        
        // Debug output for vertices
        if (x == 0 && y == 0 && z == 0) {
            System.out.println("Rendering block at (0,0,0)");
            System.out.println("Number of vertices: " + vertices.length / 5);
            System.out.println("First vertex position: (" + 
                vertices[0] + ", " + vertices[1] + ", " + vertices[2] + ")");
            System.out.println("First vertex texture coords: (" + 
                vertices[3] + ", " + vertices[4] + ")");
        }
        
        FloatBuffer verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
        verticesBuffer.put(vertices).flip();
        
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_DYNAMIC_DRAW);
        
        glDrawArrays(GL_TRIANGLES, 0, vertices.length / 5);
        
        glBindVertexArray(0);
        
        MemoryUtil.memFree(verticesBuffer);
        
        shader.unbind();
    }
    
    public void setCamera(Camera camera) {
        this.camera = camera;
    }
    
    public void cleanup() {
        if (shader != null) {
            shader.cleanup();
        }
        
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboId);
        
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }
}