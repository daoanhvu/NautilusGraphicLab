package simplemath.math;

public class ImageData {
	
	private int dimension = 0;
	private float[] image = null;
	private int[] rowsInfo = null;
	private short normalOffset = -1;
	private short[] indices;
	
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

	public void generateIndices() {
		indices = new short[image.length];
		for(int i=0; i<image.length; i++) {
			indices[i] = (short) i;
		}
	}
}
