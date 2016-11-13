package nautilus.lab.jogl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;

import com.jogamp.opengl.util.FPSAnimator;

import static javax.media.opengl.GL2.*;
import simplemath.math.ImageData;
import nautilus.util.GraphUtilites;

public class NLabScene extends GLCanvas {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7449127758004519581L;
	
	int fps = 48;
	GLU glu;
	FPSAnimator animator = null;
	MyGLEventListener glEventListener;
	
	/**  */
	ImageData imageData;
	short[] indices;
	int[] buffers;
	
//	public NLabScene(List<ImageData> _imageData) {
//		imageData = _imageData.get(0);
//		glEventListener = new MyGLEventListener();
//		this.addGLEventListener(glEventListener);
//	}
	
	public void stopAnimator(){
		if(animator!=null && animator.isStarted())
			animator.stop();
	}
	
	void initBuffers(GL2 gl) {
//		buffers = new int[2];
//		gl.glGenBuffers(2, buffers, 0);
//		indices = GraphUtilites.buildIndicesForGLLINEs(imageData.getVertexCount(), imageData.getRowInfo());
//		createVertexBuffer(gl, GL_ARRAY_BUFFER, imageData.getImage(), buffers[0]);
//		createIndexBuffer(gl, GL_ELEMENT_ARRAY_BUFFER, indices, buffers[1]);
	}

	private void createIndexBuffer(GL2 gl, int glElementArrayBuffer, short[] indices, int bufferId) {
		int size = indices.length * 2;
	    ShortBuffer sb = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder()).asShortBuffer();
	    sb.put(indices);
	    sb.position(0);
		
	    gl.glBindBuffer(glElementArrayBuffer, bufferId);
	    gl.glBufferData(glElementArrayBuffer, size, sb, GL_STATIC_DRAW);
	    gl.glBindBuffer(glElementArrayBuffer, 0);
	}

	private void createVertexBuffer(GL2 gl, int glArrayBuffer, float[] vertices, int bufferId) {
		int size = vertices.length * 4;
	    FloatBuffer fb = ByteBuffer.allocateDirect(4*vertices.length).order(ByteOrder.nativeOrder()).asFloatBuffer();
	    fb.put(vertices);
	    fb.position(0);
	
	    gl.glBindBuffer(glArrayBuffer, bufferId);
	    gl.glBufferData(glArrayBuffer, size, fb, GL_STATIC_DRAW);
	    gl.glBindBuffer(glArrayBuffer, 0);
	}

	class MyGLEventListener implements GLEventListener{
		private int k;
		/**
		* Called back by the animator to perform rendering.
		*/
		@Override
		public void display(GLAutoDrawable drawable) {
			if(!animator.isAnimating())
				return;
			
			final GL2 gl = drawable.getGL().getGL2();
			
			gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers
		    gl.glLoadIdentity();  // reset the model-view matrix
		    
		    gl.glEnableClientState(GL_VERTEX_ARRAY);
		    gl.glBindBuffer(GL_ARRAY_BUFFER, buffers[0]);
		    gl.glVertexPointer(3, GL_FLOAT, 0, 0);
		    gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, buffers[1]);
		    gl.glDrawElements(GL_LINES, indices.length, GL.GL_UNSIGNED_SHORT, 0);
		    
		    gl.glDisableClientState(GL_VERTEX_ARRAY);
		}

		@Override
		public void dispose(GLAutoDrawable arg0) {
			// TODO Auto-generated method stub
			
		}

		/**
		    * Called back immediately after the OpenGL context is initialized. Can be used
		    * to perform one-time initialization. Run only once.
		    */
		@Override
		public void init(GLAutoDrawable drawable) {
			GL2 gl = drawable.getGL().getGL2();
			
			//check for VBO support
			final boolean vboSupport = gl.isFunctionAvailable("glGenBuffersARB") 
					&& gl.isFunctionAvailable("glBindBufferARB")
					&& gl.isFunctionAvailable("glBufferDataARB")
					&& gl.isFunctionAvailable("glDeleteBuffersARB");

			glu = new GLU();
			gl.glClearDepthf(1.0f);
			gl.glEnable(GL_DEPTH_TEST);
			gl.glDepthFunc(GL_LEQUAL);
			gl.glShadeModel(GL_SMOOTH);
			gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
			gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);  // Black Background
			
			initBuffers(gl);
			
			// Start animator.
	        animator = new FPSAnimator(NLabScene.this, fps);
	        animator.start();
		}

		/**
		    * Call-back handler for window re-size event. Also called when the drawable is
		    * first set to visible.
		    */
		@Override
		public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
			GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
			 
			if (height == 0) height = 1;   // prevent divide by zero
			float aspect = (float)width / height;
		 
		    //Set the view port (display area) to cover the entire window
		    gl.glViewport(0, 0, width, height);
		 
		    // Setup perspective projection, with aspect ratio matches viewport
		    gl.glMatrixMode(GL_PROJECTION);  // choose projection matrix
		    gl.glLoadIdentity();             // reset projection matrix
		    glu.gluPerspective(45.0, aspect, 0.1, 10.0); // fovy(Field of view), aspect, zNear, zFar
		 
		    // Enable the model-view transform
		    gl.glMatrixMode(GL_MODELVIEW);
		    gl.glLoadIdentity(); // reset
		}
		
	}
}
