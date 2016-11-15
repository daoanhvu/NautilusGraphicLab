package nautilus.lab.jogl;

import java.awt.Container;

import javax.swing.JFrame;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;

import nautilus.lab.graphics.Camera3D;

public class GLFrame extends JFrame implements GLEventListener {
	private static final long serialVersionUID = 107L;
	
	private NLabScene glCanvas;
	private Camera3D coord;
	
	private final int WIDTH = 800;
	private final int HEIGHT = 600;
	
	public GLFrame() {
		initComponent();
	}
	
	private void initComponent() {
		Container c = this.getContentPane();
		this.setSize(WIDTH, HEIGHT);
//		c.setBounds(0, 0, WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Set GLProfile
		GLProfile glProfile = GLProfile.get(GLProfile.GL3);
		GLCapabilities caps = new GLCapabilities(glProfile);
		
		//glCanvas
		glCanvas = new NLabScene(caps);
		glCanvas.addGLEventListener(this);
		glCanvas.requestFocusInWindow();
		
		c.add(glCanvas);
		
		//Init camera
		coord = new Camera3D();
		coord.lookAt(0, 0, -7f, 0, 0, 0, 0, 1.0f, 0);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL3 gl3 = drawable.getGL().getGL3();
		
		gl3.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);
		
		//Draw tasks go here
		
		gl3.glFlush();
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL3 gl3 = drawable.getGL().getGL3();
        gl3.glClearColor(0.392f, 0.584f, 0.929f, 1.0f);

	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub
		
	}
	

}
