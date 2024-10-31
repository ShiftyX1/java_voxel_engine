package com.voxelengine;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import com.voxelengine.engine.Camera;
import com.voxelengine.engine.Renderer;
import com.voxelengine.engine.TextureAtlas;
import com.voxelengine.model.Block;
import com.voxelengine.model.BlockType;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main {
    private static final int PLATEAU_SIZE = 16;
    
    private long window;
    private Camera camera;
    private Renderer gameRenderer; // переименовал renderer в gameRenderer
    private TextureAtlas textureAtlas;
    private Block dirtBlock;
    
    private boolean wPressed = false;
    private boolean aPressed = false;
    private boolean sPressed = false;
    private boolean dPressed = false;
    private double lastMouseX;
    private double lastMouseY;
    private boolean firstMouse = true;
    private final float MOUSE_SENSITIVITY = 0.1f;
    private final float MOVEMENT_SPEED = 0.1f;

    public void run() {
        System.out.println("Starting LWJGL " + org.lwjgl.Version.getVersion() + "!");
        
        try {
            init();
            loop();
        } catch (Exception e) {
            System.err.println("Error occurred: ");
            e.printStackTrace();
        } finally {
            cleanup();
        }
    }

    private void init() throws Exception {
        System.out.println("\n=== Starting Engine Initialization ===");
        
        // GLFW initialization
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }    

        System.out.println("GLFW initialized");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        }

        window = glfwCreateWindow(800, 600, "Voxel Engine", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        System.out.println("Window created");

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true);
            }
            
            if (key == GLFW_KEY_W) {
                wPressed = (action != GLFW_RELEASE);
            }
            if (key == GLFW_KEY_A) {
                aPressed = (action != GLFW_RELEASE);
            }
            if (key == GLFW_KEY_S) {
                sPressed = (action != GLFW_RELEASE);
            }
            if (key == GLFW_KEY_D) {
                dPressed = (action != GLFW_RELEASE);
            }
        });

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);

        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        
        glfwSetCursorPosCallback(window, (window, xpos, ypos) -> {
            if (firstMouse) {
                lastMouseX = xpos;
                lastMouseY = ypos;
                firstMouse = false;
                return;
            }
            
            float xoffset = (float) (xpos - lastMouseX);
            float yoffset = (float) (lastMouseY - ypos);
            lastMouseX = xpos;
            lastMouseY = ypos;
            
            xoffset *= MOUSE_SENSITIVITY;
            yoffset *= MOUSE_SENSITIVITY;
            
            if (camera != null) {
                camera.moveRotation(
                    (float) Math.toRadians(yoffset),
                    (float) Math.toRadians(xoffset)
                );
            }
        });

        System.out.println("Creating OpenGL context...");
        GL.createCapabilities();
        System.out.println("\nOpenGL Info:");
        System.out.println("Vendor: " + glGetString(GL_VENDOR));
        System.out.println("Version: " + glGetString(GL_VERSION));
        System.out.println("Renderer: " + glGetString(GL_RENDERER));
        
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        
        System.out.println("\n=== Initializing Components ===");
        
        System.out.println("\nInitializing Camera...");
        camera = new Camera();
        
        System.out.println("\nInitializing Renderer...");
        gameRenderer = new Renderer();
        gameRenderer.setCamera(camera);
        gameRenderer.init();
        
        System.out.println("\nInitializing TextureAtlas...");
        textureAtlas = new TextureAtlas();
        textureAtlas.init();
        
        int boundTexture = glGetInteger(GL_TEXTURE_BINDING_2D);
        System.out.println("Current bound texture after initialization: " + boundTexture);
        if (boundTexture == 0) {
            System.err.println("Warning: No texture bound after initialization!");
        }
        
        System.out.println("\nInitializing Blocks...");
        dirtBlock = new Block(BlockType.DIRT);
        
        System.out.println("\n=== Initialization Complete ===\n");
    }
    

    private void updatePosition() {
        if (camera != null) {
            float speed = MOVEMENT_SPEED;
            
            if (wPressed) {
                camera.movePosition(0, 0, -speed);
            }
            if (sPressed) {
                camera.movePosition(0, 0, speed);
            }
            if (aPressed) {
                camera.movePosition(-speed, 0, 0);
            }
            if (dPressed) {
                camera.movePosition(speed, 0, 0);
            }
        }
    }

    private void loop() {
        System.out.println("Entering main loop");
        glClearColor(0.529f, 0.808f, 0.922f, 0.0f);

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            
            updatePosition();
            
            try {
                textureAtlas.bind();
                
                for (int x = 0; x < PLATEAU_SIZE; x++) {
                    for (int z = 0; z < PLATEAU_SIZE; z++) {
                        gameRenderer.render(dirtBlock, x, 0, z);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error in render loop: ");
                e.printStackTrace();
                break;
            }

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
        
        System.out.println("Main loop ended");
    }

    private void cleanup() {
        System.out.println("Cleaning up...");
        
        if (gameRenderer != null) {
            try {
                gameRenderer.cleanup();
            } catch (Exception e) {
                System.err.println("Error cleaning up renderer: ");
                e.printStackTrace();
            }
        }
        
        if (textureAtlas != null) {
            try {
                textureAtlas.cleanup();
            } catch (Exception e) {
                System.err.println("Error cleaning up texture atlas: ");
                e.printStackTrace();
            }
        }
        
        System.out.println("Destroying window...");
        if (window != NULL) {
            glfwDestroyWindow(window);
        }
        
        glfwTerminate();
        GLFWErrorCallback callback = glfwSetErrorCallback(null);
        if (callback != null) {
            callback.free();
        }
        
        System.out.println("Cleanup completed");
    }

    public static void main(String[] args) {
        try {
            new Main().run();
        } catch (Exception e) {
            System.err.println("Fatal error: ");
            e.printStackTrace();
        }
    }
}