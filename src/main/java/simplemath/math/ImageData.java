package simplemath.math;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

public class ImageData {
	
	private int dimension = 0;
	/**
	 * The image data is stored in a float array. The number of elements in the array
	 * is equal to the number of vertices times the dimension of the vertices.
	 * For example, if the vertices are 3D, then the dimension is 3.
	 * The number of vertices is the number of elements in the array divided by the dimension.
	 * number of vertex is image.length / dimension
	 */
	private float[] image = null;
	/**
	 * The rowInfo is an array of integers. Each element in the array is the number of vertices
	 * in a row. The sum of all elements in the array is equal to the number of vertices.
	 */
	private int[] rowsInfo = null;
	private short normalOffset = -1;
	/**
	 * The indices are used to draw the image data in a triangle strip mode.
	 * We will use the rowInfo to generate the indices. The rows a connected by degeneration.
	 */
	private List<Short> indices;

	public ImageData() {
	}

	/**
	 *
	 */
	private float[] colors = null;
	private int positionHandler;
	private int colorHandler;
	private int normalHandler;
	private int useTextureHandler;
	private int[] glBuffers = new int[3];
	private int stride;
	private boolean glInitialized = false;
	
	public int getVertexCount() {
		return image.length / dimension;
	}

	public int[] getRowInfo() {
		return rowsInfo;
	}

	public float[] getImage() {
		return image;
	}

	public int getDimension() {
		return dimension;
	}

	public short getNormalOffset() {
		return normalOffset;
	}

	public boolean isGlInitialized() {
		return glInitialized;
	}

	public void setColors(float red, float green, float blue, float alpha) {
		if (image != null) {
			int vertexCount = image.length / dimension;
			colors = new float[vertexCount * 4];
			for (int i = 0; i < vertexCount; i++) {
				colors[i * 4] = red;
				colors[i * 4 + 1] = green;
				colors[i * 4 + 2] = blue;
				colors[i * 4 + 3] = alpha;
			}
		}
	}

	/**
	 * Get the indices of the image data. The indices are used to draw the image data in a triangle strip mode so that we
	 * can use TRIANGLE_STRIP mode in OpenGL to draw the image data.
	 * We use the rowInfo to generate the indices. The rows a connected by degeneration.
	 */
	void generateIndices() {
		indices = new ArrayList<>();
		int rowCount = rowsInfo.length;
		int vertexCount = 0;
		for(int i=0; i<rowCount - 1; i++) {
			int currentRowCount = rowsInfo[i];
			int nextRowCount = rowsInfo[i + 1];

			for(int j = 0; j < Math.max(currentRowCount, nextRowCount); j++) {
				if(j < currentRowCount) {
					indices.add((short) (vertexCount + j));
				}
				if(j < nextRowCount) {
					indices.add((short) (vertexCount + j));
				}
			}
			//Add degeneration
			if (i < rowCount - 2) {
				indices.add((short) (vertexCount + currentRowCount - 1));
				indices.add((short) (vertexCount + currentRowCount));
			}
			vertexCount += currentRowCount;
		}
	}

	public void initGL(GL3 gl3, int posHandler, int colorHdl, int normalHandler, int useTextureHandler) {
		/**
		 * Generate buffers for vertex data and indices
		 * The first buffer is for the vertex data.
		 * The second buffer is for color data.
		 * The third buffer is for the indices.
		 * The third parameter is the offset of the buffer where the normal data starts.
		 */
		gl3.glGenBuffers(3, glBuffers, 0);

		//init vertex buffer for position and normal
		int size = image.length * Buffers.SIZEOF_FLOAT;
		FloatBuffer floatBuffer = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder()).asFloatBuffer();
		floatBuffer.put(image);
		floatBuffer.position(0);
		gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, glBuffers[0]);
		gl3.glBufferData(GL3.GL_ARRAY_BUFFER, size, floatBuffer, GL3.GL_STATIC_DRAW);
		gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);

		//init color buffer
		if(colors == null) {
			// set default color
			setColors(0.0f, 0.5f, 1.0f, 1.0f);
		}
		size = colors.length * Buffers.SIZEOF_FLOAT;
		FloatBuffer colorBuffer = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder()).asFloatBuffer();
		colorBuffer.put(colors);
		colorBuffer.position(0);
		gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, glBuffers[1]);
		gl3.glBufferData(GL3.GL_ARRAY_BUFFER, size, colorBuffer, GL3.GL_STATIC_DRAW);
		gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);

		//init index buffer
		this.generateIndices();
		short[] indicesArr = new short[this.indices.size()];
		for(int i=0; i<indicesArr.length; i++) {
			indicesArr[i] = this.indices.get(i);
		}
		size = indicesArr.length * Buffers.SIZEOF_SHORT;
		ShortBuffer shortBuffer = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder()).asShortBuffer();
		shortBuffer.put(indicesArr);
		shortBuffer.position(0);
		gl3.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, glBuffers[2]);
		gl3.glBufferData(GL3.GL_ELEMENT_ARRAY_BUFFER, size, shortBuffer, GL3.GL_STATIC_DRAW);
		gl3.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, 0);

		this.positionHandler = posHandler;
		this.colorHandler = colorHdl;
		this.normalHandler = normalHandler;
		this.useTextureHandler = useTextureHandler;
		stride = dimension * Buffers.SIZEOF_FLOAT;

		glInitialized = true;
	}

	public void render(GL3 gl3, int positionHandler, int normalHandler, int colorHandler, int useTextureHandler) {
		if (!glInitialized) {
			initGL(gl3, positionHandler, colorHandler, normalHandler, useTextureHandler);
			return;
		}
		//Shut down texture
		gl3.glUniform1i(useTextureHandler, 0);

		gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, glBuffers[0]);
		gl3.glEnableVertexAttribArray(positionHandler);
		gl3.glVertexAttribPointer(positionHandler, dimension, GL3.GL_FLOAT, false, stride, 0);

		if (normalOffset >= 0) {
			gl3.glEnableVertexAttribArray(normalHandler);
			gl3.glVertexAttribPointer(normalHandler, 3, GL3.GL_FLOAT, false, stride, normalOffset * Buffers.SIZEOF_FLOAT);
		}

		gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, glBuffers[1]);
		gl3.glEnableVertexAttribArray(colorHandler);
		gl3.glVertexAttribPointer(colorHandler, 4, GL3.GL_FLOAT, false, 4 * Buffers.SIZEOF_FLOAT, 0);

		gl3.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, glBuffers[2]);
		gl3.glDrawElements(GL3.GL_TRIANGLE_STRIP, indices.size(), GL3.GL_UNSIGNED_SHORT, 0);

		// Unbind the buffers
		gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
		gl3.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, 0);

		// Disable vertex attributes
		gl3.glDisableVertexAttribArray(positionHandler);
		if (normalOffset >= 0) {
			gl3.glDisableVertexAttribArray(normalHandler);
		}
		gl3.glDisableVertexAttribArray(colorHandler);

		// Check for OpenGL errors
		int error = gl3.glGetError();
		if (error != GL3.GL_NO_ERROR) {
			System.err.println("OpenGL Error: " + error);
		}
	}

	public void dispose(GL3 gl3) {
		gl3.glDeleteBuffers(3, glBuffers, 0);
	}
}


