package nautilus.game.model;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import nautilus.lab.component.CommandListener;
import nautilus.lab.jogl.Coordinator3D;
import nautilus.lab.jogl.GLShaderProgram;
import simplemath.math.Matrix4;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Ref: http://miksblog.capcaval.org/2013/07/jogl-gl3-sample/
 * @author vdao5
 *
 */

public class GameScene extends GLCanvas implements CommandListener {
	private static final long serialVersionUID = 155L;

	static final int FPS_INTERVAL = 30;

	/** Additional constants. */
	private static final int POSITION_HANDLE = 0;
	private static final int NORMAL_HANDLE = 1;
	private static final int COLOR_HANDLE = 2;
	private static final int UV_HANDLE = 3;

	private static final int POSITION_DATA_SIZE_IN_ELEMENTS = 3;
	private static final int NORMAL_DATA_SIZE_IN_ELEMENTS = 3;

	private final DefaultCamera camera = new DefaultCamera();
	private GLShaderProgram mProgramShader;

    Coordinator3D coord;
	BattleMap battleMap;

	private float[] background = {0.2f, 0.2f, 0.2f, 1.0f};
	private final float[] mModelView = new float[16];
	private final float[] mModel = new float[16];

	float[] lightPosInWorldSpace = {1.0f, 1.0f, 1.0f, 1.0f};
	float[] lightPosInEyeSpace = {1.0f, 1.0f, 1.0f, 1f};
	float rotX;
	float rotY;
    int difSampler;
	int uModelHandler;
	int uViewMatrixHandle;
    int uPerspectiveHandler;
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

	final Matrix4 matrixUtil = new Matrix4();

	//Animation control
	final FPSAnimator animator;

	public GameScene(GLCapabilities caps) {
		super(caps);
		
		mProgramShader = new GLShaderProgram();
		
		addGLEventListener(glListener);
		
		addMouseMotionListener(new MouseMotionListener(){
			
			public void mouseMoved(MouseEvent evt){
			}
			
			public void mouseDragged(MouseEvent evt) {
				float dx = evt.getX() - preMouseX;
				float dy = evt.getY() - preMouseY;
				
				rotX = dy /*pitchInRadian*/ * 0.0005f;
				rotY = dx /*yaw */ * 0.0005f;
				
				System.out.println("Rotate: " + rotX + "," + rotY);

                matrixUtil.invertM(mRotationInversion, mRotationM);
                matrixUtil.multiplyMV(mRotationAxisX, mRotationInversion, mWorldRotationAxisX);
                matrixUtil.rotateM(mRotationM, (float)Math.toDegrees(rotX), mRotationAxisX[0], mRotationAxisX[1], mRotationAxisX[2]);
                matrixUtil.invertM(mRotationInversion, mRotationM);
                matrixUtil.multiplyMV(mRotationAxisY, mRotationInversion, mWorldRotationAxisY);
                matrixUtil.rotateM(mRotationM, (float)Math.toDegrees(rotY), mRotationAxisY[0], mRotationAxisY[1], mRotationAxisY[2]);

                matrixUtil.multiplyMM(mModel, mTranslationM, mRotationM);
				
				preMouseX = evt.getX();
				preMouseY = evt.getY();
			}
		});

        matrixUtil.identity(mModel);
        matrixUtil.identity(mRotationM);

        coord = new Coordinator3D();

		int mapData[][] = {
                {1, 1, 1, 1, 1, 1, 1},
                {1, 2, 1, 1, 1, 1, 1},
                {1, 3, 1, 2, 2, 1, 1},
                {1, 1, 1, 1, 1, 1, 1}
        };
        battleMap = new BattleMap(7, 4, mapData);
		
		animator = new FPSAnimator(this, FPS_INTERVAL, true);
	}
	
	public void start() {
		animator.start();
	}
	
	public void stop() {
		animator.stop();
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
            gl3.glEnable(GL3.GL_CULL_FACE);
            gl3.glCullFace(GL3.GL_BACK);
            gl3.glFrontFace(GL3.GL_CW);
//            gl3.glDepthMask(true);

            camera.lookAt(0.00001f, 2.5f, 0.0f, 0, 0, 0, 0, 1.0f, 0);
            matrixUtil.identity(mRotationM);
            matrixUtil.identity(mTranslationM);
            matrixUtil.identity(mModel);
	        
	        mProgramShader.init(gl3, 
	        		"D:\\projects\\demo\\NautilusGraphicLab\\shaders\\vertex.shader",
	        		"D:\\projects\\demo\\NautilusGraphicLab\\shaders\\fragment.shader");
	        
//	        mProgramShader.init(gl3, 
//	        		"C:\\projects\\NautilusGraphicLab\\shaders\\vertex.shader", 
//	        		"C:\\projects\\NautilusGraphicLab\\shaders\\fragment.shader");
	        
	        //IMPORTANT: set position for attribute
	        mProgramShader.bindAttribLocation(gl3, POSITION_HANDLE,"aPosition");
	        mProgramShader.bindAttribLocation(gl3, NORMAL_HANDLE, "aNormal");
	        mProgramShader.bindAttribLocation(gl3, COLOR_HANDLE, "aColor");
            mProgramShader.bindAttribLocation(gl3, UV_HANDLE, "aUV");
			
	        gl3.glLinkProgram(mProgramShader.getProgramId());

	        //Now we can get handlers for uniforms
            difSampler = gl3.glGetUniformLocation(mProgramShader.getProgramId(), "difSampler");
            uViewMatrixHandle = gl3.glGetUniformLocation(mProgramShader.getProgramId(), "uViewMatrix");
            uPerspectiveHandler = gl3.glGetUniformLocation(mProgramShader.getProgramId(), "uPerspective");
            uModelHandler = gl3.glGetUniformLocation(mProgramShader.getProgramId(), "uModel");
            uLightPosHandle = gl3.glGetUniformLocation(mProgramShader.getProgramId(), "uLightPos");

            coord.initialize(gl3);

            battleMap.setHandlers(POSITION_HANDLE, UV_HANDLE);
	        battleMap.initialize(gl3, difSampler);
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
			//TODO: Should we do this in shader program???
            matrixUtil.multiplyMM(mModelView, camera.getViewMatrix(), mModel);
			
			//Setting the camera
			gl3.glUniformMatrix4fv(uModelHandler, 1, false, mModel, 0);
			gl3.glUniformMatrix4fv(uViewMatrixHandle, 1, false, camera.getViewMatrix(), 0);
            gl3.glUniformMatrix4fv(uPerspectiveHandler, 1, false, camera.getPerspective(), 0);

            matrixUtil.multiplyMV(lightPosInEyeSpace, mModelView, lightPosInWorldSpace);
			
			//Setting Light source
			gl3.glUniform3f(uLightPosHandle, lightPosInEyeSpace[0], lightPosInEyeSpace[1],lightPosInEyeSpace[2]);

            coord.render(gl3, POSITION_HANDLE, NORMAL_HANDLE, COLOR_HANDLE);
			battleMap.render(gl3);
			
			gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
			gl3.glDisableVertexAttribArray(POSITION_HANDLE);
		    gl3.glDisableVertexAttribArray(NORMAL_HANDLE);
		    gl3.glDisableVertexAttribArray(COLOR_HANDLE);
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
