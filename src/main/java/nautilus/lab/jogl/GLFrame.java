package nautilus.lab.jogl;

import java.awt.Container;

import javax.swing.JFrame;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;

import nautilus.lab.graphics.Camera3D;

public class GLFrame extends JFrame {
	private static final long serialVersionUID = 107L;
	
	private NLabScene glCanvas;
	
	
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
		glCanvas.requestFocusInWindow();
		
		c.add(glCanvas);
	}
}
