package nautilus.lab.jogl;

import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import nautilus.game.model.GameScene;

public class GLFrame extends JFrame {
	private static final long serialVersionUID = 107L;
	
//	private NLabScene glCanvas;
	private GameScene glCanvas;
//	private SimpleScene glCanvas;

	private final int WIDTH = 1200;
	private final int HEIGHT = 800;
	
	public GLFrame() {
		initComponent();
	}
	
	private void initComponent() {
		Container c = this.getContentPane();
		this.setSize(WIDTH, HEIGHT);
//		c.setBounds(0, 0, WIDTH, HEIGHT);
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Set GLProfile
		GLProfile glProfile = GLProfile.get(GLProfile.GL3);
		GLCapabilities caps = new GLCapabilities(glProfile);
		
		//glCanvas
		glCanvas = new GameScene(caps);
//        glCanvas = new NLabScene(caps);
//		glCanvas = new SimpleScene(caps);
		glCanvas.requestFocusInWindow();
		
		c.add(glCanvas);
		
		addWindowListener(new WindowAdapter() {
			@Override 
	         public void windowClosing(WindowEvent e) {
	            // Use a dedicate thread to run the stop() to ensure that the
	            // animator stops before program exits.
	            new Thread() {
	               @Override 
	               public void run() {
	            	   glCanvas.stop(); // stop the animator loop
	            	   System.exit(0);
	               }
	            }.start();
	         }
			
			@Override 
	         public void windowOpened(WindowEvent e) {
				glCanvas.start();
	         }
			
		});

	}
	
	 
}
