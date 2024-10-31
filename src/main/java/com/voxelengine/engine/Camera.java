package com.voxelengine.engine;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    private Vector3f position;
    private float pitch;
    private float yaw;
    private final Matrix4f viewMatrix;
    private final Matrix4f projectionMatrix;
    
    public Camera() {
        position = new Vector3f(8.0f, 10.0f, 20.0f); // Позиция камеры над плато
        pitch = (float) Math.toRadians(-45.0f);  // Наклон камеры вниз
        yaw = (float) Math.toRadians(-90.0f);   // Поворот камеры к центру плато
        viewMatrix = new Matrix4f();
        projectionMatrix = new Matrix4f();
        updateProjectionMatrix(70.0f, 800.0f/600.0f);
    }
    
    public void updateProjectionMatrix(float fov, float aspectRatio) {
        float nearPlane = 0.1f;
        float farPlane = 100.0f;
        projectionMatrix.identity();
        projectionMatrix.perspective(
            (float) Math.toRadians(fov),
            aspectRatio,
            nearPlane,
            farPlane
        );
    }
    
    public void updateViewMatrix() {
        viewMatrix.identity()
            .rotateX(pitch)
            .rotateY(yaw)
            .translate(-position.x, -position.y, -position.z);
    }
    
    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }
    
    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }
    
    public void movePosition(float offsetX, float offsetY, float offsetZ) {
        if (offsetZ != 0) {
            position.x += (float) Math.sin(yaw) * -offsetZ;
            position.z += (float) Math.cos(yaw) * offsetZ;
        }
        if (offsetX != 0) {
            position.x += (float) Math.sin(yaw - Math.PI/2) * -offsetX;
            position.z += (float) Math.cos(yaw - Math.PI/2) * offsetX;
        }
        position.y += offsetY;
    }
    
    public void moveRotation(float offsetPitch, float offsetYaw) {
        pitch += offsetPitch;
        yaw += offsetYaw;
        
        // Ограничиваем углы поворота
        if (pitch > Math.PI/2) {
            pitch = (float) Math.PI/2;
        } else if (pitch < -Math.PI/2) {
            pitch = -(float) Math.PI/2;
        }
    }
}