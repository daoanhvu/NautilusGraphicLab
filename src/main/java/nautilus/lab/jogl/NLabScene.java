package nautilus.lab.jogl;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.math.Quaternion;
import com.jogamp.opengl.math.Vec3f;
import com.jogamp.opengl.math.Vec4f;
import com.jogamp.opengl.math.Matrix4f;
import com.jogamp.opengl.util.FPSAnimator;
import nautilus.lab.component.CommandListener;
import nautilus.lab.component.Scene3D;
import nautilus.lab.model.Model3D;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;

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
	final Matrix4f mPerspectiveMatrix = new Matrix4f();
	final float[] tempMatrix = new float[16];
	Matrix4f mModel = new Matrix4f();
	private final float[] modelMatrix = new float[16];
	final Matrix4f viewMatrix = new Matrix4f();
	final Matrix4f mModelView = new Matrix4f();
	final Vec4f lightPosInWorldSpace = new Vec4f();
	final Vec4f lightPosInEyeSpace = new Vec4f();
	float rotX;
	float rotY;
	int uLightPosHandle;
	int uNeedLightingHandle;
	final Matrix4f mTranslationM = new Matrix4f();
	final Matrix4f mRotationMatrix = new Matrix4f();
	
	private float preMouseX;
	private float preMouseY;
	Quaternion currentRotation = new Quaternion();

	protected Model3D modelObject = null;

	public NLabScene(String shaderScriptFolder, GLCapabilities caps) {
		super(caps);

		mProgramShader = new GLShaderProgram();

		GLEventListener glListener = new GLEventListener() {

			@Override
			public void init(GLAutoDrawable drawable) {

				GL3 gl3 = drawable.getGL().getGL3();
				gl3.glClearColor(background[0], background[1], background[2], background[3]);
				gl3.glEnable(GL3.GL_DEPTH_TEST);

				Vec3f cameraPos = new Vec3f(0.3f, 0.1f, 3f);
				Vec3f center = new Vec3f(0, 0, 0);
				Vec3f upVector = new Vec3f(0, 1.0F, 0);
				viewMatrix.setToLookAt(cameraPos, center, upVector, viewMatrix);
				mRotationMatrix.loadIdentity();
				currentRotation.setIdentity();
				mTranslationM.loadIdentity();
				mModel.loadIdentity();

				File folder = new File(shaderScriptFolder);

				mProgramShader.init(gl3, folder, "simple_vertex_shader.glsl", "simple_fragment_shader.glsl");

				gl3.glLinkProgram(mProgramShader.getProgramId());
				//IMPORTANT: set position for attribute
				bindAttributeLocations(gl3);
				bindUniformHandlers(gl3);
				coord.initialize(gl3, positionHandler, colorHandler, uUseTextureHandler);
			}

			@Override
			public void reshape(GLAutoDrawable drawable, int arg1, int arg2, int width, int height) {
				GL3 gl3 = drawable.getGL().getGL3();
				gl3.setSwapInterval(1);
				gl3.glClearColor(background[0], background[1], background[2], background[3]);
				gl3.glEnable(GL3.GL_DEPTH_TEST);

				float left;
				float right;
				float top;
				float bottom;
				float ratio;
				if(width >= height) {
					ratio = (float)width / (float)height;
					left = -ratio;
					right = ratio;
					bottom = -1.0F;
					top = 1.0F;
				} else {
					ratio = (float)height / (float)width;
					left = -1.0F;
					right = 1.0F;
					bottom = -ratio;
					top = ratio;
				}
				final float w = right - left;
				final float h = top - bottom;

				gl3.glViewport(0, 0, width, height);
				mPerspectiveMatrix.setToPerspective((float)Math.toRadians(45F),  ratio, 0.1F, 10F);
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
				gl3.glUniformMatrix4fv(uViewMatrixHandle, 1, false, viewMatrix.get(tempMatrix), 0);
				gl3.glUniformMatrix4fv(uPerspectiveHandler, 1, false, mPerspectiveMatrix.get(tempMatrix), 0);
				gl3.glUniformMatrix4fv(uModelHandler, 1, false, mModel.get(modelMatrix), 0);

				mModelView.mulVec4f(lightPosInWorldSpace, lightPosInEyeSpace);

				//Setting LightSource source
				gl3.glUniform1i(uNumOfLightHandler, lightSources.size());

				/* Drawing the coordinator */
				coord.render(gl3);
				modelObject.draw(gl3, positionHandler, normalHandler, colorHandler, textureHandler);

				gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
				gl3.glDisableVertexAttribArray(positionHandler);
				gl3.glDisableVertexAttribArray(normalHandler);
				gl3.glDisableVertexAttribArray(colorHandler);
				gl3.glDisableVertexAttribArray(textureHandler);
				/* End drawing the coordinator */

				gl3.glFlush();
			}
		};
		addGLEventListener(glListener);

		addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent evt) {
				preMouseX = evt.getX();
				preMouseY = evt.getY();
			}

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}
		});
		
		addMouseMotionListener(new MouseMotionListener() {
			final float sensitivity = 0.1f;
			public void mouseMoved(MouseEvent evt){
			}

			final Quaternion quatX = new Quaternion();
			final Quaternion quatY = new Quaternion();
			public void mouseDragged(MouseEvent evt) {
				float dx = evt.getX() - preMouseX;
				float dy = evt.getY() - preMouseY;
				
				rotX = (float)Math.toRadians(-dy * sensitivity);
				rotY = (float)Math.toRadians(-dx * sensitivity);

				quatX.setIdentity();
				quatY.setIdentity();
				quatX.rotateByAngleNormalAxis(rotX, 1.0F, 0F, 0F);
				quatY.rotateByAngleNormalAxis(rotY, 0F, 1F, 0F);
				currentRotation = quatX.mult(currentRotation);
				currentRotation = quatY.mult(currentRotation);
				
				preMouseX = evt.getX();
				preMouseY = evt.getY();
				currentRotation.normalize();
				mModel = mTranslationM.mul(mRotationMatrix.setToRotation(currentRotation));
			}
		});
		
		mModel.loadIdentity();
		mRotationMatrix.loadIdentity();
		coord = new Coordinator3D();
		animator = new FPSAnimator(this, FPS_INTERVAL, true);
	}

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
