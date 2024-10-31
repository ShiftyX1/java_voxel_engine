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
        
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);
        
        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);
        
        glBindVertexArray(0);
    }
    
    public void render(Block block, float x, float y, float z) {
        shader.bind();
        
        // Обновляем матрицы
        camera.updateViewMatrix();
        shader.setUniform("viewMatrix", camera.getViewMatrix());
        shader.setUniform("projectionMatrix", camera.getProjectionMatrix());
        
        int currentProgram = glGetInteger(GL_CURRENT_PROGRAM);
        int currentTexture = glGetInteger(GL_TEXTURE_BINDING_2D);
        
        if (x == 0 && y == 0 && z == 0) {
            System.out.println("\nRendering debug info:");
            System.out.println("Current shader program: " + currentProgram);
            System.out.println("Current texture: " + currentTexture);
            
            int textureSamplerLocation = glGetUniformLocation(currentProgram, "textureSampler");
            System.out.println("textureSampler location: " + textureSamplerLocation);
            
            float[] vertices = block.generateVertices(x, y, z);
            System.out.println("Vertex data for first triangle:");
            for(int i = 0; i < 15; i += 5) {
                System.out.printf("V%d: pos(%.2f, %.2f, %.2f) tex(%.2f, %.2f)\n",
                    i/5, vertices[i], vertices[i+1], vertices[i+2], 
                    vertices[i+3], vertices[i+4]);
            }
        }
        
        glBindVertexArray(vaoId);
        
        float[] vertices = block.generateVertices(x, y, z);
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