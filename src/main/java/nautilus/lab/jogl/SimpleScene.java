package nautilus.lab.jogl;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import simplemath.math.Matrix4;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.nio.*;

/**
 * Ref: http://miksblog.capcaval.org/2013/07/jogl-gl3-sample/
 * @author vdao5
 *
 */

public class SimpleScene extends GLCanvas {
	private static final long serialVersionUID = 155L;

	static final int FPS_INTERVAL = 30;

	/** Additional constants. */
	private static final int POSITION_HANDLE = 0;
	private static final int COLOR_HANDLE = 1;

	private static final int POSITION_DATA_SIZE_IN_ELEMENTS = 3;

	//private Camera3D coord;
	private GLShaderProgram mProgramShader;

	private int[] axesBufferIndex = new int[1];
	private float[] axesVertices = new float[]{
			0, 0, 0, 1, 0, 0, 1,	1, 0, 0, 1, 0, 0, 1,
			0, 0, 0, 0, 1, 0, 1,	0, 1, 0, 0, 1, 0, 1,
			0, 0, 0, 0, 0, 1, 1,	0, 0, 1, 0, 0, 1, 1
			};

	private final float[] mPerspectiveMatrix = new float[16];
	private final float[] mModelView = new float[16];
	private final float[] mViewMatrix = new float[16];
	private final float[] mModel = new float[16];

	float[] lightPosInWorldSpace = {0f, 0f, 0f, 0f};
	float[] lightPosInEyeSpace = {0f, 0f, 0f, 0f};
	float rotX;
	float rotY;
    int uModelHandler;
    int uViewMatrixHandle;
    int uPerspectiveHandler;


	final float[] mRotationInversion = new float[16];
	final float[] mTranslationM = new float[16];
	final float[] mRotationM = new float[16];
	final float[] mRotationAxisX = new float[4];
	final float[] mRotationAxisY = new float[4];


	final float[] mWorldRotationAxisX = {1, 0, 0, 0};
	final float[] mWorldRotationAxisY = {0, 1, 0, 0};

	private float preMouseX;
	private float preMouseY;

	final Matrix4 mMatrix4 = new Matrix4();

	//Animation control
	final FPSAnimator animator;

	public SimpleScene(GLCapabilities caps) {
		super(caps);
		
		mProgramShader = new GLShaderProgram();
		
		//Init camera
		//coord = new Camera3D();
		
		addGLEventListener(glListener);
		
		addMouseMotionListener(new MouseMotionListener(){
			
			public void mouseMoved(MouseEvent evt){
			}
			
			public void mouseDragged(MouseEvent evt) {
				float dx = evt.getX() - preMouseX;
				float dy = evt.getY() - preMouseY;
				
				//coord.rotate(dx, dy, 0);
				
				rotX = -1 * dy /*pitchInRadian*/ * 0.05f;
				rotY = -1 * dx /*yaw */ * 0.05f;
				
				System.out.println("Rotate: " + rotX + "," + rotY);
				
				mMatrix4.invertM(mRotationInversion, mRotationM);
				mMatrix4.multiplyMV(mRotationAxisX, mRotationInversion, mWorldRotationAxisX);
				mMatrix4.rotateM(mRotationM, (float)Math.toDegrees(rotX), mRotationAxisX[0], mRotationAxisX[1], mRotationAxisX[2]);
				mMatrix4.invertM(mRotationInversion, mRotationM);
				mMatrix4.multiplyMV(mRotationAxisY, mRotationInversion, mWorldRotationAxisY);
				mMatrix4.rotateM(mRotationM, (float)Math.toDegrees(rotY), mRotationAxisY[0], mRotationAxisY[1], mRotationAxisY[2]);

				mMatrix4.multiplyMM(mModel, mTranslationM, mRotationM);
				
				preMouseX = evt.getX();
				preMouseY = evt.getY();
			}
		});
		
		mMatrix4.identity(mModel);
		mMatrix4.identity(mRotationM);
		
		animator = new FPSAnimator(this, FPS_INTERVAL, true);
	}
	
	public void start() {
		animator.start();
	}
	
	public void stop() {
		animator.stop();
	}
	
	void normalize(float[] result, float a, float b, float c) {
		float mag = (float)Math.sqrt(a*a + b*b + c*c);
		result[0] = a/mag;
		result[1] = b/mag;
		result[2] = c/mag;
	}
	
	void cross(float[] r, float[] v1, float[] v2) {
		r[0] = v1[1]*v2[2]-v1[2]*v2[1];
		r[1] = v1[2]*v2[0]-v1[0]*v2[2];
		r[2] = v1[0]*v2[1]-v1[1]*v2[0];
	}
	
	float dot(float[] v1, float[] v2) {
		return (v1[0]*v2[0] + v1[1]*v2[1] + v1[2]*v2[2]);
	}
	
	float eyeX;
	float eyeY;
	float eyeZ;
	float centerX;
	float centerY;
	float centerZ;
	
	void lookAt(float ex, float ey, float ez,
			float cx, float cy, float cz, float ux, float uy, float uz) {
		float[] f = {0, 0, 0};
		float[] cr = {0, 0, 0};
		float[] s = {0, 0, 0};
		float[] u = {0, 0, 0};
		normalize(f, cx-ex, cy-ey, cz-ez);
		float[] up = {ux, uy, uz};
		cross(cr, f, up);
		normalize(s, cr[0], cr[1], cr[2]);
		cross(u, s, f);
		float[] eye = {ex, ey, ez};

		centerX = cx;
		centerY = cy;
		centerZ = cz;

		eyeX = ex;
		eyeY = ey;
		eyeZ = ez;
		
		mViewMatrix[0] = 1;
		mViewMatrix[5] = 1;
		mViewMatrix[10] = 1;
		mViewMatrix[15] = 1;
		
		mViewMatrix[3] = 0;
		mViewMatrix[7] = 0;
		mViewMatrix[11] = 0;

		mViewMatrix[0] = s[0]; //s.x
		mViewMatrix[4] = s[1]; //s.y
		mViewMatrix[8] = s[2]; //s.z
		mViewMatrix[1] = u[0];
		mViewMatrix[5] = u[1];
		mViewMatrix[9] = u[2];
		mViewMatrix[2] =-f[0];
		mViewMatrix[6] =-f[1];
		mViewMatrix[10] =-f[2];
		mViewMatrix[12] =-dot(s, eye);
		mViewMatrix[13] =-dot(u, eye);
		mViewMatrix[14] = dot(f, eye);
	}
	
	private GLEventListener glListener = new GLEventListener() {
		
		@Override
		public void reshape(GLAutoDrawable drawable, int arg1, int arg2, int w, int h) {
			GL3 gl3 = drawable.getGL().getGL3();
			float near = 0.1f;
			float far = 9.0f;
			float range = near - far;
			float aspect = (w*1.0f)/h;
			float fovy = (float)Math.tan(0.5 * (Math.PI - Math.toRadians(40)));
			
			gl3.glViewport(0, 0, w, h);
			
			mPerspectiveMatrix[0] = fovy / aspect;
			mPerspectiveMatrix[1] = 0;
			mPerspectiveMatrix[2] = 0;
			mPerspectiveMatrix[3] = 0;

			mPerspectiveMatrix[4] = 0;
			mPerspectiveMatrix[5] = fovy;
			mPerspectiveMatrix[6] = 0;
	        mPerspectiveMatrix[7] = 0;

	        mPerspectiveMatrix[8] = 0;
	        mPerspectiveMatrix[9] = 0; 
	        mPerspectiveMatrix[10] = far / range;
	        mPerspectiveMatrix[11] = -1;

	        mPerspectiveMatrix[12] = 0;
	        mPerspectiveMatrix[13] = 0;
	        mPerspectiveMatrix[14] = near * far / range;
	        mPerspectiveMatrix[15] = 0;
		}
		
		@Override
		public void init(GLAutoDrawable drawable) {
			GL3 gl3 = drawable.getGL().getGL3();
	        gl3.glClearColor(0.2f, 0.2f, 0.29f, 1.0f);
	        gl3.glEnable(GL3.GL_DEPTH_TEST);
	        
	        lookAt(0.5f, 0.5f, 3.0f, 0, 0, 0, 0, 1.0f, 0);
	        mMatrix4.identity(mRotationM);
	        mMatrix4.identity(mTranslationM);
	        mMatrix4.identity(mModel);
	        
	        mProgramShader.init(gl3, 
	        		"D:\\projects\\demo\\NautilusGraphicLab\\shaders\\simple_vertex_shader.glsl",
	        		"D:\\projects\\demo\\NautilusGraphicLab\\shaders\\simple_fragment_shader.glsl");
	        
//	        mProgramShader.init(gl3, 
//	        		"C:\\projects\\NautilusGraphicLab\\shaders\\vertex.shader", 
//	        		"C:\\projects\\NautilusGraphicLab\\shaders\\fragment.shader");
	        
	        //IMPORTANT: set position for attribute
	        mProgramShader.bindAttribLocation(gl3, POSITION_HANDLE,"aPosition");
	        mProgramShader.bindAttribLocation(gl3, COLOR_HANDLE, "aColor");

	        gl3.glLinkProgram(mProgramShader.getProgramId());

            uViewMatrixHandle = gl3.glGetUniformLocation(mProgramShader.getProgramId(), "uViewMatrix");
            uPerspectiveHandler = gl3.glGetUniformLocation(mProgramShader.getProgramId(), "uPerspective");
            uModelHandler = gl3.glGetUniformLocation(mProgramShader.getProgramId(), "uModel");
	        
	        //initialize axes
	        gl3.glGenBuffers(1, axesBufferIndex, 0);
	        initVertexBuffer(gl3, GL3.GL_ARRAY_BUFFER, axesVertices, axesBufferIndex[0]);
		}
		
		@Override
		public void dispose(GLAutoDrawable drawable) {
			GL3 gl3 = drawable.getGL().getGL3();
			gl3.glUseProgram(0);
			mProgramShader.dispose(gl3);
		}
		
		@Override
		public void display(GLAutoDrawable drawable) {
			GL3 gl3 = drawable.getGL().getGL3();			
			gl3.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);
			gl3.glUseProgram(mProgramShader.getProgramId());
			
//			System.out.println("Display: ");
			
			//Draw tasks go here
			mMatrix4.multiplyMM(mModelView, mViewMatrix, mModel);
			//Setting the camera
			gl3.glUniformMatrix4fv(uModelHandler, 1, false, mModel, 0);
			gl3.glUniformMatrix4fv(uViewMatrixHandle, 1, false, mViewMatrix, 0);
            gl3.glUniformMatrix4fv(uPerspectiveHandler, 1, false, mPerspectiveMatrix, 0);

			/* Draw coordinator */
			int stride = 28;
			gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, axesBufferIndex[0]);
			gl3.glEnableVertexAttribArray(POSITION_HANDLE);
			gl3.glVertexAttribPointer(POSITION_HANDLE, 3, GL3.GL_FLOAT, false, stride, 0);
			gl3.glEnableVertexAttribArray(COLOR_HANDLE);
			gl3.glVertexAttribPointer(COLOR_HANDLE, 4, GL3.GL_FLOAT, false, stride, 12);
			gl3.glDrawArrays(GL3.GL_LINES, 0, 6);
			
			gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
			gl3.glDisableVertexAttribArray(POSITION_HANDLE);
		    gl3.glDisableVertexAttribArray(COLOR_HANDLE);
			/* End drawing the coordinator */
			
			gl3.glFlush();
			
		}
	};
	
	/**
	 * 
	 * @param target one of value GLES20.GL_ARRAY_BUFFER or GLES20.GL_ELEMENT_ARRAY_BUFFER
	 */
	static void initVertexBuffer(GL3 gl3, int target, float[] data, int bufferId){
		int size = data.length * Buffers.SIZEOF_FLOAT;
		FloatBuffer fb = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder()).asFloatBuffer();
		fb.put(data);
		fb.position(0);
		
		gl3.glBindBuffer(target, bufferId);
		gl3.glBufferData(target, size, fb, GL3.GL_STATIC_DRAW);
		gl3.glBindBuffer(target, 0);
	}
}
