package simplemath.math;

import java.util.List;
import static nautilus.lab.formula.Constant.NO_ERROR;

public class Function {
	
	static {
		System.loadLibrary("nmath");
	}
	
	private long jniAddress;
	private String mText;
	
	private native long jniInit();
	private native int jniSetString(long address, String str);
	private native void jniCalc(long address, String str, ReturnVal result);
	private native int derivative(long address, Function result);
	private native void jniGetSpace(long address, float[] bounds, float epsilon, boolean includeNormal, List<ImageData> imageDataList);
	private native void jniRelease(long adrress);
	
	public int setString(String str) {
		int result = jniSetString(jniAddress, str);
		
		if(result == NO_ERROR) {
			mText = str;
		}
		
		return result;
	}
	
	public void getSpace(float[] bounds, float epsilon, boolean includeNormal, List<ImageData> imageDataList) {
		jniGetSpace(jniAddress, bounds, epsilon, includeNormal, imageDataList);
	}

	public void release() {
		jniRelease(jniAddress);
	}
}
