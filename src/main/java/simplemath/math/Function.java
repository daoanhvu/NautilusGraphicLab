package simplemath.math;

import java.util.List;
import static nautilus.lab.formula.Constant.NO_ERROR;

public class Function {

  static {
    System.loadLibrary("jni_nmath");
  }

  /**
   * This is the native address of the object.
   */
  private long jniAddress;

  /**
   * This is the text of the function.
   * Example: f(x,y) = x^2 + 2y + 1
   */
  private String mText;

  public Function() {
    jniAddress = jniInit();
  }

  private native long jniInit();
  private native int jniSetText(long address, String str);
  private native void jniCalc(long address, float[] varValues, ReturnVal result);
  private native int derivative(long address, Function result);
  private native int jniGetSpace(long address, float[] bounds, float epsilon, boolean includeNormal, List<ImageData> imageDataList);
  private native void jniRelease(long adrress);

  public int setString(String str) {
    int result = jniSetText(jniAddress, str);
    if(result == NO_ERROR) {
      mText = str;
    }
    return result;
  }

  public void getSpace(float[] bounds, float epsilon, boolean includeNormal, List<ImageData> imageDataList) {
    int errorCode = jniGetSpace(jniAddress, bounds, epsilon, includeNormal, imageDataList);
    if (errorCode != NO_ERROR) {
      throw new RuntimeException("Error in getSpace: " + errorCode);
    }
  }

  public void release() {
    jniRelease(jniAddress);
    jniAddress = 0L;
  }
}
