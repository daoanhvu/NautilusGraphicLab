package nautilus.lab.graphics;

public class Camera3D {
	
	static {
		System.loadLibrary("jni_nmath");
	}
	
	private long nativeCamera = 0;
	
	private float[] Ow0 = {0, 0, 0, 1};
	private float[] Xw0 = {2, 0, 0, 1};
	private float[] Yw0 = {0, 2, 0, 1};
	private float[] Zw0 = {0, 0, 2, 1};
	     
    private float eyeX = 0f, eyeY = 0f, eyeZ = -6.0f;
    private float centerX = 0, centerY = 0, centerZ = 0;
    
    //These are used for 
    private float scaleX, scaleY;
    private float XScreen0, YScreen0;
    
    public Camera3D() {
    	nativeCamera = initCamera();
    }
    
    /** Viewport attributes */
    private int left, right, top, bottom;
    
    private float[] p0 = {0, 0, 0};
	private float[] p = {0, 0, 0};
	private float[] p1 = {0, 0, 0};
	private float[] p2 = {0, 0, 0};
	private float[] pw0 = {0, 0, 0, 1};
	private float[] pw1 = {0, 0, 0, 1};
	private float[] pw2 = {0, 0, 0, 1};
	
	private native long initCamera();
	
	private native void lookAt(long address, float ex, float ey, float ez,
			float cx, float cy, float cz, float ux, float uy, float uz);
	
	/**
	 * 
	 * @param address
	 * @param ex
	 * @param ey
	 * @param ez
	 * @param cx
	 * @param cy
	 * @param cz
	 * @param ux
	 * @param uy
	 * @param uz
	 * @param view
	 */
	private native void lookAt(long address, float ex, float ey, float ez,
			float cx, float cy, float cz, float ux, float uy, float uz, float[] view);
	/**
	 * 
	 * @param address
	 * @param eye
	 * @param center
	 * @param up
	 * @param view [OUT]
	 */
	private native void lookAt(long address, float[] eye, float[] center, float[] up, float[] view);
	private native void perspective(long address, int l, int r, int t, int b, float fov, float near, float far);
	private native void project(long address, float[] out, float objX, float objY, float objZ);
	private native void projectOrthor(long address, float[] out, float objX, float objY, float objZ);
	private native void rotate(long address, float yawR, float pitchR, float roll);
	private native void rotate(long address, float[] ypr, float[] modelMatrix /*OUT*/);
	private native void moveAlongForward(long address, float distance);
	private native void jniRelease(long address);
	
	public void lookAt(float ex, float ey, float ez,
			float cx, float cy, float cz, float ux, float uy, float uz) {
		eyeX = ex;
		eyeY = ey;
		eyeZ = ez;
		lookAt(nativeCamera, ex, ey, ez, cx, cy, cz, ux, uy, uz);
	}
	
	public void lookAt(float ex, float ey, float ez,
			float cx, float cy, float cz, float ux, float uy, float uz, float[] viewMatrix) {
		eyeX = ex;
		eyeY = ey;
		eyeZ = ez;
		lookAt(nativeCamera, ex, ey, ez, cx, cy, cz, ux, uy, uz, viewMatrix);
	}
	
	public void perspective(int l, int r, int t, int b,
			float fov, float near, float far) {
		left = l;
		right = r;
		top = t;
		bottom = b;
		perspective(nativeCamera, l, r, t, b, fov, near, far);
	}
	
	public void project(float[] out, float objX, float objY, float objZ) {
		project(nativeCamera, out, objX, objY, objZ);
	}
	public void projectOrthor(float[] out, float objX, float objY, float objZ) {
		projectOrthor(nativeCamera, out, objX, objY, objZ);
	}
	
	public void rotate(float yawR, float pitchR, float roll) {
		rotate(nativeCamera, yawR, pitchR, roll);
	}
	
	public void moveAlongForward(float distance) {
		moveAlongForward(nativeCamera, distance);
	}
	
	public void drawCoordinator(IGraphics graphics, IPaint paintX, IPaint paintY, IPaint paintZ) {
		//Project the center of the coordinator
		project(nativeCamera, p0, Ow0[0], Ow0[1], Ow0[2]);
		project(nativeCamera, p, Xw0[0], Xw0[1], Xw0[2]);
		project(nativeCamera, p1, Yw0[0], Yw0[1], Yw0[2]);
		project(nativeCamera, p2, Zw0[0], Zw0[1], Zw0[2]);
		
		graphics.setColor(255, 0, 0);
		graphics.drawLine(p0[0], p0[1], p[0], p[1], paintX);
		graphics.setColor(0, 255, 0);
		graphics.drawLine(p0[0], p0[1], p1[0], p1[1], paintY);
		graphics.setColor(0, 0, 255);
		graphics.drawLine(p0[0], p0[1], p2[0], p2[1], paintZ);
	}
	
	private void release() {
		jniRelease(nativeCamera);
	}
}
