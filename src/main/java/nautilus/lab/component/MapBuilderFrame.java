package nautilus.lab.component;

import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.MenuBar;
import java.awt.Menu;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import nautilus.lab.graphics.IGraphics;
import nautilus.lab.graphics.IPaint;
import nautilus.lab.graphics.NLabGraphics;
import nautilus.util.ImageOpenFilter;


public class MapBuilderFrame extends Frame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private MenuBar mainMenu;
	private ToolBar mainToolBar;
	private Menu mnFile, mnTool, mnHelp;
	private MenuItem mniQuit, mniOpenCommandPane, mniHelp, mniAbout;

	private MapCanvas canvas;
	private CoordinatorInfoPane infoPane;
	private CommandPane commandPane;
	
	short[][] mapData = {
			{0, 1, 3, 5, 4, 4, 0, 0},
			{2, 2, 1, 0, 3, 0, 0, 1},
			{0, 0, 0, 0, 2, 2, 0, 0},
			{0, 0, 0, 0, 1, 2, 0, 1},
			{0, 0, 2, 7, 5, 4, 1, 0}
	};
	
	public MapBuilderFrame(){
		super("Nautilus Lab 1.0 - Map builder");
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
				MapBuilderFrame.this.remove(infoPane);
				MapBuilderFrame.this.add(cmdPane, BorderLayout.SOUTH);
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
		
		canvas = new MapCanvas();
		canvas.loadFromFolder("D:\\projects\\TankGame\\artwork");
		canvas.setMapData(mapData);
		
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				infoPane.setControlListener(null);
				canvas.stop();
				System.exit(0);
			}
			
			public void windowOpened(WindowEvent we){
				canvas.setup();
				canvas.broastCastChanged();
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
	
	static class MapCanvas extends AbstractStrategyCanvas 
		implements IControlListener, CommandListener {
		
		private IGraphics graphics;
		private IPaint paint;
		private IPaint paintX, paintY, paintZ;
		private boolean isMousePressed = false;
		private boolean isShowCtxMenu = false;	
		private int preMouseX = 0, preMouseY = 0;
		
		final ImageOpenFilter mImageFilter = new ImageOpenFilter("mapTile");
		
		int X0 = 20;
		int Y0 = 50;
		
		private final List<BufferedImage> images = new ArrayList<>();
		short[][] mapData;
		int rowCount;
		int colCount;
		int tileWidth;
		int tileHeight;
		
		private IDrawPaneChangeListener drawPaneListener = null;
		
		public MapCanvas() {
			super();
			setListeners();
			setPreferredSize(new Dimension(850, 450));
			//initImageInfoPane();
			graphics = new NLabGraphics();
		}
		
		public void loadFromFolder(String pathFolder) {
			File[] imageFiles;
			BufferedImage img;
			try {
				File folder = new File(pathFolder);
				imageFiles = folder.listFiles(mImageFilter);
				for(File f: imageFiles) {
					img = ImageIO.read(f);
					images.add(img);
				}
					
				tileWidth = images.get(0).getWidth();
				tileHeight = images.get(0).getHeight();
				
			}catch(IOException ex) {
				ex.printStackTrace();
			}
		}
		
		public void setMapData(short[][] mapD) {
			mapData = mapD;
			rowCount = mapData[0].length;
			colCount = mapData.length;
		}
		
		@Override
		public void stop() {
			super.stop();
			for(BufferedImage img: images) {
				img.flush();
			}
		}
		
		private void setListeners() {
			addMouseListener(new MouseListener(){
				@Override
				public void mouseClicked(MouseEvent evt) {
					isShowCtxMenu = false;
					if(evt.getButton() == MouseEvent.BUTTON3)
						isShowCtxMenu = true;
				}

				@Override
				public void mouseEntered(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseExited(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mousePressed(MouseEvent evt) {
					isMousePressed = true;
					preMouseX = evt.getX();
					preMouseY = evt.getY();
					
				}

				@Override
				public void mouseReleased(MouseEvent arg0) {
					isMousePressed = false;
				}
			});
			
			addMouseMotionListener(new MouseMotionListener(){
			
				public void mouseMoved(MouseEvent evt){
				}
				
				public void mouseDragged(MouseEvent evt) {
					double dx = evt.getX() - preMouseX;
					double dy = evt.getY() - preMouseY;
					
					
					
					preMouseX = evt.getX();
					preMouseY = evt.getY();
				}
			});
			
			addComponentListener(new ComponentListener(){
				 	public void componentHidden(ComponentEvent e){
					}
					
					public void  componentMoved(ComponentEvent e){
					}
					
					public void  componentResized(ComponentEvent e){
						
					}
					
					public void  componentShown(ComponentEvent e){
						
					}
			});
		}
		
		public void setDrawPaneListener(IDrawPaneChangeListener l){
			drawPaneListener = l;
		}
		
		public void broastCastChanged() {
		}

		@Override
		public void render(Graphics2D g2) {
			// TODO Auto-generated method stub
			int x, y, i, j;
			
			for(i=0; i<images.size(); i++) {
				for(j=0; j<rowCount; j++) {
					x = getWidth()-10 - ((i % 2 + 1) * tileWidth + (i%2)*5 );
					y = i/2 * tileHeight + 10 + (i/2)*5;
					g2.drawImage(images.get(i), x, y, null);
				}
			}
			
			for(i=0; i<rowCount; i++) {
				for(j=0; j<colCount; j++) {
					x = i * tileWidth + X0;
					y = j * tileHeight + Y0;
					g2.drawImage(images.get(mapData[j][i]), x, y, null);
				}
			}
			
			if(isMousePressed) {
				g2.drawImage(images.get(0), preMouseX, preMouseY, null);
			}
		}

		@Override
		public void onFrustumChangeCmd(double x, double y, double z) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onViewChangeCmd(double ex, double ey, double ez, double targetX, double targetY, double targetZ,
				double upX, double upY, double upZ, double neaf, double far) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onRotateCommand(double theta, double rvx, double rvy, double rvz) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onRotateCommand(double theta, double px, double py, double pz, double rvx, double rvy, double rvz) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onNFunctionChange(String strFunct, float[] boundaries) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAddVertexCommand(double vx, double vy, double vz) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAddLineCommand() {
			// TODO Auto-generated method stub
			
		}
	}
}