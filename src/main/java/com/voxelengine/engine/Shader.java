package com.voxelengine.engine;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;
import java.nio.FloatBuffer;
import java.util.Map;
import java.util.HashMap;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private final int programId;
    private int vertexShaderId;
    private int fragmentShaderId;
    private final Map<String, Integer> uniforms;

    public Shader() throws Exception {
        programId = glCreateProgram();
        if (programId == 0) {
            throw new Exception("Could not create Shader");
        }
        uniforms = new HashMap<>();
    }

    public void createVertexShader(String shaderFile) throws Exception {
        System.out.println("Loading vertex shader from: " + shaderFile);
        String shaderCode = loadResource(shaderFile);
        System.out.println("Vertex shader code loaded: " + (shaderCode != null && !shaderCode.isEmpty()));
        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shaderFile) throws Exception {
        System.out.println("Loading fragment shader from: " + shaderFile);
        String shaderCode = loadResource(shaderFile);
        System.out.println("Fragment shader code loaded: " + (shaderCode != null && !shaderCode.isEmpty()));
        fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    private String loadResource(String fileName) throws Exception {
        try {
            var inputStream = getClass().getResourceAsStream(fileName);
            if (inputStream == null) {
                throw new Exception("Resource not found: " + fileName);
            }
            try (java.util.Scanner scanner = new java.util.Scanner(inputStream, "UTF-8").useDelimiter("\\A")) {
                return scanner.hasNext() ? scanner.next() : "";
            }
        } catch (Exception e) {
            System.err.println("Error loading shader resource: " + fileName);
            e.printStackTrace();
            throw e;
        }
    }

    private int createShader(String shaderCode, int shaderType) throws Exception {
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new Exception("Error creating shader. Type: " + shaderType);
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        glAttachShader(programId, shaderId);

        return shaderId;
    }

    public void link() throws Exception {
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024));
        }

        if (vertexShaderId != 0) {
            glDetachShader(programId, vertexShaderId);
            glDeleteShader(vertexShaderId);
        }
        if (fragmentShaderId != 0) {
            glDetachShader(programId, fragmentShaderId);
            glDeleteShader(fragmentShaderId);
        }

        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024));
        }
    }

    public void createUniform(String uniformName) throws Exception {
        int uniformLocation = glGetUniformLocation(programId, uniformName);
        if (uniformLocation < 0) {
            throw new Exception("Could not find uniform:" + uniformName);
        }
        uniforms.put(uniformName, uniformLocation);
    }

    public void setUniform(String uniformName, Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = stack.mallocFloat(16);
            value.get(fb);
            glUniformMatrix4fv(uniforms.get(uniformName), false, fb);
        }
    }

    public void setUniform(String uniformName, int value) {
        glUniform1i(uniforms.get(uniformName), value);
    }

    public void createUniforms() throws Exception {
        System.out.println("Creating uniforms...");
        createUniform("projectionMatrix");
        createUniform("viewMatrix");
        createUniform("textureSampler");
        // По умолчанию используем текстурный юнит 0
        bind();
        setUniform("textureSampler", 0);
        unbind();
        System.out.println("Uniforms created successfully");
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if (programId != 0) {
            glDeleteProgram(programId);
        }
    }
}