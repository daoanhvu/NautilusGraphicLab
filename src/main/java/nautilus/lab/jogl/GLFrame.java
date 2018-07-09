package nautilus.lab.jogl;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import nautilus.game.model.GameScene;
import nautilus.lab.component.CommandPane;
import nautilus.lab.component.Scene3D;

public class GLFrame extends JFrame {
	private static final long serialVersionUID = 107L;
	
	private Scene3D glCanvas;

	private final int WIDTH = 1200;
	private final int HEIGHT = 800;

	//UI design
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenu mnHelp;

	private JMenuItem mniSave;
    private JMenuItem mniSaveAs;
    private JMenuItem mniClose;
    private JMenuItem mniAbout;
    private JMenuItem mniHelp;

    private CommandPane commandPane;
	
	public GLFrame() {
		super("Nautilus Graphics Lab 1.0");
		initComponent();
	}

	private void setupMenu() {
	    Font menuFont = new Font("Sans-Serif", Font.PLAIN, 20);
	    UIManager.put("Menu.font", menuFont);
        UIManager.put("MenuItem.font", menuFont);

		menuBar = new JMenuBar();

		mnFile = new JMenu("File");
        mniSave = new JMenuItem("Save");
        mniSaveAs = new JMenuItem("Save as ...");
        mniClose = new JMenuItem("Close");
        mnFile.add(mniSave);
        mnFile.add(mniSaveAs);
        mnFile.add(mniClose);
		menuBar.add(mnFile);

		mnHelp = new JMenu("Help");
        mniAbout = new JMenuItem("About");
        mniHelp = new JMenuItem("Help");
        mnHelp.add(mniHelp);
        mnHelp.add(mniAbout);
		menuBar.add(mnHelp);

		this.setJMenuBar(menuBar);
	}
	
	private void initComponent() {
		Container c = this.getContentPane();
		this.setSize(WIDTH, HEIGHT);
//		c.setBounds(0, 0, WIDTH, HEIGHT);
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(5, 5));

		setupMenu();
		
		//Set GLProfile
		GLProfile glProfile = GLProfile.get(GLProfile.GL3);
		GLCapabilities caps = new GLCapabilities(glProfile);
		
		//glCanvas
//		glCanvas = new GameScene(caps);
        glCanvas = new NLabScene(caps);
//		glCanvas = new SimpleScene(caps);
		glCanvas.requestFocusInWindow();
		
		c.add(glCanvas, BorderLayout.CENTER);

        commandPane = new CommandPane();
        commandPane.setCommandListener(glCanvas);

        this.add(commandPane, BorderLayout.SOUTH);
		
		addWindowListener(new WindowAdapter() {
			@Override 
	         public void windowClosing(WindowEvent e) {
	            // Use a dedicate thread to run the stop() to ensure that the
	            // animator stops before program exits.
	            Thread t = new Thread() {
	               @Override 
	               public void run() {
	            	   glCanvas.stop(); // stop the animator loop
	            	   System.exit(0);

	               }
	            };
	            t.start();
                try {
                    t.join();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
			
			@Override 
	         public void windowOpened(WindowEvent e) {
				glCanvas.start();
	         }
			
		});

	}
	
	 
}
