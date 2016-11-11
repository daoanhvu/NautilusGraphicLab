package nautilus.lab.component;

import java.awt.Frame;
import java.awt.BorderLayout;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.MenuBar;
import java.awt.Menu;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class LabFrame extends Frame{

	private MenuBar mainMenu;
	private ToolBar mainToolBar;
	private Menu mnFile, mnTool, mnHelp;
	private MenuItem mniQuit, mniOpenCommandPane, mniHelp, mniAbout;
	private MenuItem mni3ModelLab;

	private AbstractStrategyCanvas canvas;
	private CoordinatorInfoPane infoPane;
	private CommandPane commandPane;
	
	public LabFrame(){
		super("Nautilus Lab 1.0");
		setLayout(new BorderLayout(5, 5));
		
		infoPane = new CoordinatorInfoPane();
		infoPane.setControlListener(canvas);
		
		commandPane = new CommandPane();
		commandPane.setCommandListener(canvas);
		
		this.add(commandPane, BorderLayout.SOUTH);
		
		//Menubar
		mainMenu = new MenuBar();
		mnFile = new Menu("File");
		mniQuit = new MenuItem("Quit");
		mniQuit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				infoPane.setControlListener(null);
				canvas.stop();
				System.exit(0);
				
			}
		});
		mnFile.add(mniQuit);
		
		mainMenu.add(mnFile);
		mnTool = new Menu("Tools");
		mniOpenCommandPane = new MenuItem("Command Pane");
		mniOpenCommandPane.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				CommandPane cmdPane = new CommandPane();
				LabFrame.this.remove(infoPane);
				LabFrame.this.add(cmdPane, BorderLayout.SOUTH);
				cmdPane.setVisible(true);
				invalidate();
			}
		});
		mnTool.add(mniOpenCommandPane);
		
		mainMenu.add(mnTool);
		mnHelp = new Menu("Help");
		mniHelp = new MenuItem("Help");
		mniAbout = new MenuItem("About");
		mnHelp.add(mniHelp);
		mnHelp.add(mniAbout);
		
		mainMenu.add(mnHelp);
		setMenuBar(mainMenu);
		
		initToolBar();
		
		//canvas = new StrategyCanvas();
		canvas = new Lab3DCanvas();
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				infoPane.setControlListener(null);
				canvas.stop();
				System.exit(0);
			}
			
			public void windowOpened(WindowEvent we){
				canvas.setup();
				//canvas.broastCastChanged();
			}
		});
		this.add(canvas, BorderLayout.CENTER);
		
		infoPane.setControlListener(canvas);
		commandPane.setCommandListener(canvas);
		canvas.setDrawPaneListener(infoPane);
		this.pack();
	}
	
	private void initToolBar(){
		ToolBar mainToolBar = new ToolBar();
		ToggleButton btnSelect;
		ImageButton btnCurve;
		
		try{
			btnSelect = new ToggleButton(getClass().getClassLoader().getResource("select32.png"));
			mainToolBar.add(btnSelect);
			btnCurve = new ImageButton(getClass().getClassLoader().getResource("curve32.png"));
			mainToolBar.add(btnCurve); // test
		}catch(NullPointerException e){
			
			try {
				btnSelect = new ToggleButton(ImageIO.read(new File("../src/resource/select32.png")));
				mainToolBar.add(btnSelect);
				btnCurve = new ImageButton(ImageIO.read(new File("../src/resource/curve32.png")));
				mainToolBar.add(btnCurve);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		add(mainToolBar, BorderLayout.NORTH);
	}
}