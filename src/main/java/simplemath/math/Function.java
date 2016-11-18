package simplemath.math;

import java.util.List;

public class Function {
	private long jniAddress;
	
	private native long jniInit();
	private native void jniCalc(long address);
	private native Function derivative(long address);
	private native void jniGetSpace(long address, ImageData img);
	
	public int setString(String str) {
		return -1;
	}
	
	public List<ImageData> getSpace(float[] bounds, float step) {
		return null;
	}

	public void release() {
		// TODO Auto-generated method stub
		
	}
}
