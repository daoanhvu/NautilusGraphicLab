package simplemath.math;

public class ImageData {
	
	private int dimension = 0;
	private float[] image = null;
	private int[] rowsInfo = null;
	private int normalOffset = -1;
	private short[] indices;
	
	public int getVertexCount() {
		return image.length / dimension;
	}

	public int[] getRowInfo() {
		return null;
	}

	public float[] getImage() {
		return image;
	}

	public int getDimension() {
		return dimension;
	}

}
