package comp3170.week5.sceneobjects;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Matrix4f;

import comp3170.SceneObject;
import comp3170.InputManager;

public class Camera extends SceneObject {

	private float zoom = 10.0f; // You'll need this when setting up your projection matrix...
	private Matrix4f projectionMatrix = new Matrix4f();
	private Matrix4f viewMatrix = new Matrix4f();
	
	private static final float ZOOM_SPEED = 5f;
	
	private float aspectRatio = 1f;
	
	public Camera() {
		
	}
	
	public void resize(int w, int h) {
		aspectRatio = (float)w / (float)h;
	}
	
	public Matrix4f GetViewMatrix(Matrix4f dest) {
		viewMatrix = getMatrix();
		return viewMatrix.invert(dest);
	}
	
	public Matrix4f GetProjectionMatrix(Matrix4f dest) {
		return projectionMatrix.invert(dest);
	}
	
// TODO: Make the camera zoom in-and-out based on user input. (TASK 4)
// You'll need to move some code around!
	
	public void update(InputManager input, float deltaTime) {
		if (input.isKeyDown(GLFW_KEY_UP)) {
			zoom -= deltaTime * ZOOM_SPEED;
		}
			
		if (input.isKeyDown(GLFW_KEY_DOWN)) {
			zoom += deltaTime * ZOOM_SPEED;
		}
		
		// Change to aspect
		projectionMatrix.scaling(aspectRatio * zoom, zoom, 1.0f);
	}
}