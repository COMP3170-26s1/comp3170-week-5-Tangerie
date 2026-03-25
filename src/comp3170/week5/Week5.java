package comp3170.week5;

import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL41.*;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.OpenGLException;
import comp3170.IWindowListener;
import comp3170.ShaderLibrary;
import comp3170.Window;
import comp3170.InputManager;

import java.io.File;
import java.io.IOException;


public class Week5 implements IWindowListener {
	private Window window;
	private int width = 600;
	private int height = 600;

	final private File DIRECTORY = new File("src/comp3170/week5/shaders/"); 

	private InputManager input;
	private long oldTime;
	
	private Scene scene;

	public Week5()  throws OpenGLException {		
		
		window = new Window("Flower field", width, height, this);
		window.setResizable(true);
	    window.run();
	}

	public void init() {
		input = new InputManager(window); // create an input manager to listen for keypresses and mouse events		
		oldTime = System.currentTimeMillis(); // initialise oldTime
		
		new ShaderLibrary(DIRECTORY);
		scene = new Scene();
	}
	
	private Vector2i position = new Vector2i();
		
	private void update() {
		long time = System.currentTimeMillis();
		float deltaTime = (time - oldTime) / 1000f;
		oldTime = time;		
		if (input.wasMouseClicked()) {
			// TODO: Get the mouse position into NDC, and then into world space. (TASK 2)
			input.getCursorPos(position); // This will get the mouse position in screen space.
																			// flip the y so -1 is the bottom
			Vector2f screenSpace = new Vector2f((float)position.x / width, -(float)position.y / height);
			screenSpace
				.mul(2f)
				.add(-1f, 1f);
			
			Matrix4f inv = new Matrix4f();
			mvpMatrix.invert(inv);
			
			Vector4f p = new Vector4f(screenSpace.x, screenSpace.y, 0f, 1f);
			p.mul(inv);
			
			scene.createFlower(p);
			System.out.println(String.format("%d %d", position.x, position.y));
			System.out.println(String.format("%f %f", screenSpace.x, screenSpace.y));
			System.out.println(String.format("%f %f %f", p.x, p.y, p.z));
		}
		
		input.clear(); // Run this to clear input before the next frame.
		scene.update(input, deltaTime); // Use update() for scene logic and draw() to...well, draw.
	}

	private Matrix4f viewMatrix  = new Matrix4f();
	private Matrix4f projectionMatrix  = new Matrix4f();
	private Matrix4f mvpMatrix = new Matrix4f();
	
	public void draw() {
		update();
	
		glClearColor(87.0f/255.0f, 60.0f/255.0f, 23.0f/255.0f, 1.0f); // Dirt brown
		glClear(GL_COLOR_BUFFER_BIT);		
		
		// TODO: Use the view and projection matricies to construct the mvpMatrix. (TASK 2)
		//			Then send it down the scene graph!
		scene.sceneCam().GetProjectionMatrix(projectionMatrix);
		scene.sceneCam().GetViewMatrix(viewMatrix);
		mvpMatrix.identity();
		mvpMatrix
			.mul(viewMatrix)
			.mul(projectionMatrix);
		
		scene.draw(mvpMatrix);
			
	}

	@Override
	public void resize(int width, int height) {
		// record the new width and height
		this.width = width;
		this.height = height;
		glViewport(0,0,width,height);
		
		scene.sceneCam().resize(width, height);
	}

	@Override
	public void close() {
		// nothing to do
	}

	public static void main(String[] args) throws IOException, OpenGLException {
		new Week5();
	}

}
