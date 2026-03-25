package comp3170.week5.sceneobjects;

import static comp3170.Math.TAU;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import comp3170.GLBuffers;
import comp3170.SceneObject;
import comp3170.Shader;
import comp3170.ShaderLibrary;
public class FlowerHead extends SceneObject {
	
	private static final String VERTEX_SHADER = "vertex.glsl";
	private static final String FRAGMENT_SHADER = "fragment.glsl";
	private static final float INNER_RADIUS = 0.75f;
	
	private Shader shader;

	private Vector3f petalColour = new Vector3f(1.0f,1.0f,1.0f);

	private Vector4f[] vertices;
	private int vertexBuffer;

	private int[] indices;
	private int indexBuffer;
	
	
	
	public FlowerHead(int nPetals, Vector3f colour) {
		
		// TODO: Create the flower head. (TASK 1)
		// Consider the best way to draw the mesh with the nPetals input. 
		// Note that this may involve moving some code OUT of this class!
	
		// 6 Petals
		// 12 Sides
		
		// Like circle but every 2nd outer radius is the inner radius
		
		vertices = new Vector4f[nPetals * 2 + 1];
		
		vertices[0] = new Vector4f(0, 0, 0, 1); // centre
		
		Matrix4f rotate = new Matrix4f();
		
		for (int i = 0; i < nPetals * 2; i++) {
			float angle = i * TAU / (nPetals * 2);

			rotate.rotationZ(angle); // R = R(angle)
			vertices[i + 1] = new Vector4f(i % 2 == 0 ? INNER_RADIUS : 1f, 0, 0, 1); // v = (1,0,0)
			vertices[i + 1].mul(rotate); // v = R v
		}

		vertexBuffer = GLBuffers.createBuffer(vertices);

		indices = new int[nPetals * 2 * 3]; // each side creates 1 triangle with 3 vertices

		int k = 0;
		for (int i = 1; i <= nPetals * 2; i++) {
			indices[k++] = 0;
			indices[k++] = i;
			indices[k++] = (i % (nPetals * 2)) + 1; // wrap around when i = NSIDES
		}

		indexBuffer = GLBuffers.createIndexBuffer(indices);
		
		shader = ShaderLibrary.instance.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);		
		
		petalColour = colour;
	
	}

	public void update(float dt) {
		// TODO: Make the flower head rotate. (TASK 5)
	}

	public void drawSelf(Matrix4f mvpMatrix) {
		shader.enable();
		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setAttribute("a_position", vertexBuffer);
		shader.setUniform("u_colour", petalColour);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
	}
}
