package nautilus.lab.jogl;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
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
	
	private Camera3D coord;
	private int mProgram;

	public NLabScene(GLCapabilities caps) {
		super(caps);
		
		//Init camera
		coord = new Camera3D();
		coord.lookAt(0, 0, -7f, 0, 0, 0, 0, 1.0f, 0);
		
		addGLEventListener(glListener);
	}
	
	private GLEventListener glListener = new GLEventListener() {
		
		@Override
		public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void init(GLAutoDrawable drawable) {
			GL3 gl3 = drawable.getGL().getGL3();
	        gl3.glClearColor(0.392f, 0.584f, 0.929f, 1.0f);
	        mProgram = gl3.glCreateProgram();
	        
			
		}
		
		@Override
		public void dispose(GLAutoDrawable drawable) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void display(GLAutoDrawable drawable) {
			GL3 gl3 = drawable.getGL().getGL3();
			
			gl3.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);
			gl3.glUseProgram(mProgram);
			
			//Draw tasks go here
			
			gl3.glFlush();
			
		}
	};
	
	private int loadShader(GL3 gl3, int type, String shaderCode) {
		int shader = gl3.glCreateShader(type);
		//int[] compiled = {0};
		if(shader == 0) return 0;
		
		gl3.glShaderSource(shader, 1, new String[] {shaderCode}, null);
		gl3.glCompileShader(shader);
		
//		gl3.glGetShaderiv(shader, GL3.GL_COMPILE_STATUS, compiled, 0);
//		if(compiled[0] == 0) {
//			System.out.println(gl3.glGetShaderInfoLog());
//		}
		
		return shader;
	}
}
