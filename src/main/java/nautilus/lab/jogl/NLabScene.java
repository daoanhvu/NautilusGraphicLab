package nautilus.lab.jogl;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import com.jogamp.openal.sound3d.Vec3f;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.math.VectorUtil;
import com.jogamp.opengl.util.FPSAnimator;

import simplemath.math.ImageData;
import simplemath.math.Matrix4;
import nautilus.lab.graphics.Camera3D;
import nautilus.util.GraphUtilites;

/**
 * Ref: http://miksblog.capcaval.org/2013/07/jogl-gl3-sample/
 * @author vdao5
 *
 */

public class NLabScene extends GLCanvas {
	private static final long serialVersionUID = 155L;
	
	/** Additional constants. */
	private static final int POSITION_HANDLE = 0;
	private static final int NORMAL_HANDLE = 1;
	private static final int COLOR_HANDLE = 2;
	
	private static final int POSITION_DATA_SIZE_IN_ELEMENTS = 3;
	private static final int NORMAL_DATA_SIZE_IN_ELEMENTS = 3;
	public static final int BYTES_PER_FLOAT = 4;
	public static final int BYTES_PER_SHORT = 2;
	
	private Camera3D coord;
	private GLShaderProgram mProgramShader;
	
	private int[] axesBufferIndex = new int[1];
	private float[] axesVertices = new float[]{
			0, 0, 0, 0, 1, 0, 1, 0, 0, 1,	1, 0, 0, 0, 1, 0, 1, 0, 0, 1, 
			0, 0, 0, 1, 0, 0, 0, 1, 0, 1,	0, 1, 0, 1, 0, 0, 0, 1, 0, 1,
			0, 0, 0, 0, 1, 0, 0, 0, 1, 1,	0, 0, 1, 0, 1, 0, 0, 0, 1, 1
			};
	
	private final float[] mPerspectiveMatrix = new float[16];
	private final float[] mModelView = new float[16];
	private final float[] mViewMatrix = new float[16];
	private final float[] mMVP = new float[16];
	private final float[] mModel = new float[16];
	
	float[] lightPosInWorldSpace = {0f, 0f, 0f, 0f};
	float[] lightPosInEyeSpace = {0f, 0f, 0f, 0f};
	float rotX;
	float rotY;
	int uMVPHandle;
	int uModelViewMatrixHandle;
	int uLightPosHandle;
	int uNeedLightingHandle;
	
	
	final float[] mRotationInversion = new float[16];
	final float[] mTranslationM = new float[16];
	final float[] mRotationM = new float[16];
	final float[] mRotationAxisX = new float[4];
	final float[] mWorldRotationAxisX = new float[4];
	final float[] mRotationAxisY = new float[4];
	final float[] mWorldRotationAxisY = new float[4];
	
	private float preMouseX;
	private float preMouseY;
	
	final Matrix4 mMatrix4 = new Matrix4();

	public NLabScene(GLCapabilities caps) {
		super(caps);
		
		mProgramShader = new GLShaderProgram();
		
		//Init camera
		coord = new Camera3D();
		
		addGLEventListener(glListener);
		
		addMouseMotionListener(new MouseMotionListener(){
			
			public void mouseMoved(MouseEvent evt){
			}
			
			public void mouseDragged(MouseEvent evt) {
				float dx = evt.getX() - preMouseX;
				float dy = evt.getY() - preMouseY;
				
				coord.rotate(dx, dy, 0);
				
				rotX = -1 * dy /*pitchInRadian*/ * 0.05f;
				rotY = -1 * dx /*yaw */ * 0.05f;
				
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
	        gl3.glClearColor(0.392f, 0.584f, 0.929f, 1.0f);
	        gl3.glEnable(GL3.GL_DEPTH_TEST);
	        
	        coord.lookAt(0, 0, -7f, 0, 0, 0, 0, 1.0f, 0, mViewMatrix);
	        
//	        mProgramShader.init(gl3, 
//	        		"D:\\Documents\\NautilusGraphicLab\\shaders\\vertex.shader", 
//	        		"D:\\Documents\\NautilusGraphicLab\\shaders\\fragment.shader");
	        
	        mProgramShader.init(gl3, 
	        		"C:\\projects\\NautilusGraphicLab\\shaders\\vertex.shader", 
	        		"C:\\projects\\NautilusGraphicLab\\shaders\\fragment.shader");
	        
	        //IMPORTANT: set position for attribute
	        mProgramShader.bindAttribLocation(gl3, POSITION_HANDLE,"aPosition");
	        mProgramShader.bindAttribLocation(gl3, NORMAL_HANDLE, "aNormal");
	        mProgramShader.bindAttribLocation(gl3, COLOR_HANDLE, "aColor");
	        
			
	        gl3.glLinkProgram(mProgramShader.getProgramId());
	        
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
			
			//Draw tasks go here
			
			uModelViewMatrixHandle = gl3.glGetUniformLocation(mProgramShader.getProgramId(), "uModelView");
			uMVPHandle = gl3.glGetUniformLocation(mProgramShader.getProgramId(), "uMVP");
			uLightPosHandle = gl3.glGetUniformLocation(mProgramShader.getProgramId(), "uLightPos");
			uNeedLightingHandle = gl3.glGetUniformLocation(mProgramShader.getProgramId(), "uNeedLighting");
			
			mMatrix4.multiplyMM(mModelView, mViewMatrix, mModel);
			mMatrix4.multiplyMM(mMVP, mPerspectiveMatrix, mModelView);
			
//			Matrix.multiplyMM(mMVP, 0, mOrthoMatrix, 0, mViewMatrix, 0);
			
			//Setting the camera
			gl3.glUniformMatrix4fv(uMVPHandle, 1, false, mMVP, 0);
			gl3.glUniformMatrix4fv(uModelViewMatrixHandle, 1, false, mModelView, 0);
			
			// Calculate position of the light. Push into the distance.
//			Matrix.setIdentityM(lightModelMatrix, 0);
			//Matrix.translateM(lightModelMatrix, 0, 0.1f, 2.5f, -3.0f);
//			Matrix.multiplyMV(lightPosInWorldSpace, 0, lightModelMatrix, 0, lightPosInModelSpace, 0);
//			Matrix.multiplyMV(lightPosInEyeSpace, 0, mViewMatrix, 0, lightPosInWorldSpace, 0);
			mMatrix4.multiplyMV(lightPosInEyeSpace, mModelView, lightPosInWorldSpace);
			
			//Setting Light source
			gl3.glUniform3f(uLightPosHandle, lightPosInEyeSpace[0], lightPosInEyeSpace[1],lightPosInEyeSpace[2]);
			
			gl3.glUniform1i(uNeedLightingHandle, 0);
			
			/* Draw coordinator */
			gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, axesBufferIndex[0]);
			gl3.glEnableVertexAttribArray(POSITION_HANDLE);
			gl3.glVertexAttribPointer(POSITION_HANDLE, 3, GL3.GL_FLOAT, false, 40, 0);
			gl3.glEnableVertexAttribArray(NORMAL_HANDLE);
			gl3.glVertexAttribPointer(NORMAL_HANDLE, 3, GL3.GL_FLOAT, false, 40, 12);
			gl3.glEnableVertexAttribArray(COLOR_HANDLE);
			gl3.glVertexAttribPointer(COLOR_HANDLE, 4, GL3.GL_FLOAT, false, 40, 24);
			gl3.glDrawArrays(GL3.GL_LINES, 0, 6);
			
			gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
			gl3.glDisableVertexAttribArray(POSITION_HANDLE);
		    gl3.glDisableVertexAttribArray(NORMAL_HANDLE);
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
		int size = data.length * BYTES_PER_FLOAT;
		FloatBuffer fb = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder()).asFloatBuffer();
		fb.put(data);
		fb.position(0);
		
		gl3.glBindBuffer(target, bufferId);
		gl3.glBufferData(target, size, fb, GL3.GL_STATIC_DRAW);
		gl3.glBindBuffer(target, 0);
	}
	
	static void initIndexBuffer(GL3 gl3, int target, short[] data, int bufferId){
		int size = data.length * BYTES_PER_SHORT;
		ShortBuffer fb = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder()).asShortBuffer();
		fb.put(data);
		fb.position(0);
		
		gl3.glBindBuffer(target, bufferId);
		gl3.glBufferData(target, size, fb, GL3.GL_STATIC_DRAW);
		gl3.glBindBuffer(target, 0);
	}
}
