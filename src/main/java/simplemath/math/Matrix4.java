package simplemath.math;

public class Matrix4 {
	static {
		System.loadLibrary("nmath");
	}
	
	public native void multiplyMM(float[] result, float[] m1, float[] m2);
	public native void multiplyMV(float[] result, float[] m, float[] v);
	public native void transpose(float[] result, float[] m);
}
