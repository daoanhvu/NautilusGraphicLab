package nautilus.lab.jogl;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.math.VectorUtil;
import com.jogamp.opengl.util.FPSAnimator;

import nautilus.game.model.DefaultCamera;
import nautilus.lab.component.CommandListener;
import nautilus.lab.component.Scene3D;
import simplemath.math.ImageData;
import simplemath.math.Matrix4;
import nautilus.lab.graphics.Camera3D;
import nautilus.util.GraphUtilites;

/**
 * Ref: http://miksblog.capcaval.org/2013/07/jogl-gl3-sample/
 * @author vdao5
 *
 */

public class NLabScene extends Scene3D implements CommandListener {
	private static final long serialVersionUID = 155L;
	
	static final int FPS_INTERVAL = 30;
	
	private static final int POSITION_DATA_SIZE_IN_ELEMENTS = 3;
	private static final int NORMAL_DATA_SIZE_IN_ELEMENTS = 3;
	public static final int BYTES_PER_FLOAT = 4;
	public static final int BYTES_PER_SHORT = 2;

	private Coordinator3D coord;

	private final float[] mModelView = new float[16];
	private final float[] mModel = new float[16];
	
	float[] lightPosInWorldSpace = {0f, 0f, 0f, 0f};
	float[] lightPosInEyeSpace = {0f, 0f, 0f, 0f};
	float rotX;
	float rotY;
	int uLightPosHandle;
	int uNeedLightingHandle;

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

	public NLabScene(GLCapabilities caps) {
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

        coord = new Coordinator3D();
		
		animator = new FPSAnimator(this, FPS_INTERVAL, true);
	}

	private GLEventListener glListener = new GLEventListener() {
		
		@Override
		public void reshape(GLAutoDrawable drawable, int arg1, int arg2, int w, int h) {
			GL3 gl3 = drawable.getGL().getGL3();
			gl3.glViewport(0, 0, w, h);
            camera.setPerspectiveMatrix(0.1f, 9.0f, w, h);
		}
		
		@Override
		public void init(GLAutoDrawable drawable) {
			GL3 gl3 = drawable.getGL().getGL3();
			gl3.glClearColor(background[0], background[1], background[2], background[3]);
	        gl3.glEnable(GL3.GL_DEPTH_TEST);
	        
	        camera.lookAt(0.3f, 0.1f, 3f, 0, 0, 0, 0, 1.0f, 0);
	        mMatrix4.identity(mRotationM);
	        mMatrix4.identity(mTranslationM);
	        mMatrix4.identity(mModel);
	        
	        mProgramShader.init(gl3, 
	        		"D:\\projects\\demo\\NautilusGraphicLab\\shaders\\simple_vertex_shader.glsl",
	        		"D:\\projects\\demo\\NautilusGraphicLab\\shaders\\simple_fragment_shader.glsl");

            gl3.glLinkProgram(mProgramShader.getProgramId());
	        //IMPORTANT: set position for attribute
            bindAttributeLocations(gl3);
            bindUniformHandlers(gl3);

            coord.initialize(gl3, positionHandler, colorHandler,
                    uUseTextureHandler);
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

			//Setting the camera
			gl3.glUniformMatrix4fv(uViewMatrixHandle, 1, false, camera.getViewMatrix(), 0);
			gl3.glUniformMatrix4fv(uPerspectiveHandler, 1, false, camera.getPerspective(), 0);
            gl3.glUniformMatrix4fv(uModelHandler, 1, false, mModel, 0);
			
			// Calculate position of the light. Push into the distance.
//			Matrix.setIdentityM(lightModelMatrix, 0);
			//Matrix.translateM(lightModelMatrix, 0, 0.1f, 2.5f, -3.0f);
//			Matrix.multiplyMV(lightPosInWorldSpace, 0, lightModelMatrix, 0, lightPosInModelSpace, 0);
//			Matrix.multiplyMV(lightPosInEyeSpace, 0, mViewMatrix, 0, lightPosInWorldSpace, 0);
			mMatrix4.multiplyMV(lightPosInEyeSpace, mModelView, lightPosInWorldSpace);
			
			//Setting LightSource source
            gl3.glUniform1i(uNumOfLightHandler, lightSources.size());
//			gl3.glUniform3f(uLightPosHandle, lightPosInEyeSpace[0], lightPosInEyeSpace[1],lightPosInEyeSpace[2]);
//			gl3.glUniform1i(uNeedLightingHandle, 0);

            coord.render(gl3);

			gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
			gl3.glDisableVertexAttribArray(positionHandler);
		    gl3.glDisableVertexAttribArray(normalHandler);
		    gl3.glDisableVertexAttribArray(colorHandler);
			gl3.glDisableVertexAttribArray(textureHandler);
			/* End drawing the coordinator */
			
			gl3.glFlush();
			
		}
	};

	@Override
	public void onRotateCommand(double theta, double rvx, double rvy, double rvz) {

	}

	@Override
	public void onRotateCommand(double theta, double px, double py, double pz, double rvx, double rvy, double rvz) {

	}

	@Override
	public void onNFunctionChange(String strFunct, float[] boundaries) {

	}

	@Override
	public void onAddVertexCommand(double vx, double vy, double vz) {

	}

	@Override
	public void onAddLineCommand() {

	}
}
