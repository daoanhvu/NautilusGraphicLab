package nautilus.lab.jogl;

import com.jogamp.openal.sound3d.Vec3f;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.math.Matrix4;
import com.jogamp.opengl.math.VectorUtil;
import com.jogamp.opengl.util.FPSAnimator;

import simplemath.math.ImageData;
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
	
	private Camera3D coord;
	private GLShaderProgram mProgramShader;
	
	private final float[] mPerspectiveMatrix = new float[16];
	private final float[] mModelView = new float[16];
	private final float[] mViewMatrix = new float[16];
	private final float[] mMVP = new float[16];
	private final float[] mModel = new float[16];
	
	private final Matrix4 mPerspective = new Matrix4();

	public NLabScene(GLCapabilities caps) {
		super(caps);
		
		mProgramShader = new GLShaderProgram();
		
		//Init camera
		coord = new Camera3D();
		coord.lookAt(0, 0, -7f, 0, 0, 0, 0, 1.0f, 0);
		
		addGLEventListener(glListener);
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
	        
	        mProgramShader.init(gl3, 
	        		"D:\\Documents\\NautilusGraphicLab\\shaders\\vertex.shader", 
	        		"D:\\Documents\\NautilusGraphicLab\\shaders\\fragment.shader");
	        
	        //IMPORTANT: set position for attribute
	        mProgramShader.bindAttribLocation(gl3, POSITION_HANDLE,"aPosition");
	        mProgramShader.bindAttribLocation(gl3, NORMAL_HANDLE, "aNormal");
	        mProgramShader.bindAttribLocation(gl3, COLOR_HANDLE, "aColor");
	        
			
	        gl3.glLinkProgram(mProgramShader.getProgramId());
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
			
			gl3.glFlush();
			
		}
	};
	
	private float centerX;
	private float centerY;
	private float centerZ;
	private float eyeX;
	private float eyeY;
	private float eyeZ;
	
	private void lookAt(float ex, float ey, float ez,
			float cx, float cy, float cz, float ux, float uy, float uz) {
		float[] normalV = normalize3f(cx-ex, cy-ey, cz-ez);
		Vec3f f = new Vec3f(normalV[0], normalV[1], normalV[2]);
		Vec3f up = new Vec3f(ux, uy, uz);
		Vec3f s = normalize3f(cross(f, up));
		Vec3f u = cross(s, f);
		Vec3f eye = new Vec3f(ex, ey, ez);
		int i, j;
		Matrix4 view = new Matrix4();
	
		centerX = cx;
		centerY = cy;
		centerZ = cz;
	
		eyeX = ex;
		eyeY = ey;
		eyeZ = ez;
	
		view.loadIdentity();
	
		mViewMatrix[0] = s.v1; //s.x
		mViewMatrix[3] = s.v2; //s.y
//		view[2][0] = s[2]; //s.z
//		view[0][1] = u[0];
//		view[1][1] = u[1];
//		view[2][1] = u[2];
//		view[0][2] =-f[0];
//		view[1][2] =-f[1];
//		view[2][2] =-f[2];
//		view[3][0] =-gm::dot(s, eye);
//		view[3][1] =-gm::dot(u, eye);
//		view[3][2] = gm::dot(f, eye);
	}
	
	private float[] normalize3f(float[] v) {
		float[] r = {0, 0, 0};
		float mag = (float)Math.sqrt(v[0]*v[0] + v[1]*v[1] + v[2]*v[2]);
		r[0] = v[0]/mag;
		r[1] = v[1]/mag;
		r[2] = v[2]/mag;
		return r;
	}
	
	private float[] normalize3f(float v0, float v1, float v2) {
		float[] r = {0, 0, 0};
		float mag = (float)Math.sqrt(v0*v0 + v1*v1 + v2*v2);
		r[0] = v0/mag;
		r[1] = v1/mag;
		r[2] = v2/mag;
		return r;
	}
	
	private Vec3f normalize3f(Vec3f v) {
		float mag = (float)Math.sqrt(v.v1*v.v1 + v.v2*v.v2 + v.v3*v.v3);
		return new Vec3f(v.v1/mag, v.v2/mag, v.v3/mag);
	}
	
	private float[] cross(float[] v1, float[] v2) {
		return new float[]{v1[1]*v2[2]-v1[2]*v2[1],
					v1[2]*v2[0]-v1[0]*v2[2],
					v1[0]*v2[1]-v1[1]*v2[0]};
	}
	
	private Vec3f cross(Vec3f v3f1, Vec3f v3f2) {
		float[] v1 = {v3f1.v1, v3f1.v2, v3f1.v3};
		float[] v2 = {v3f2.v1, v3f2.v2, v3f2.v3};
		return new Vec3f(v1[1]*v2[2]-v1[2]*v2[1],
					v1[2]*v2[0]-v1[0]*v2[2],
					v1[0]*v2[1]-v1[1]*v2[0]);
	}
}
