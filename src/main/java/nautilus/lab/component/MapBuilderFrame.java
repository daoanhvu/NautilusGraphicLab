package nautilus.lab.component;

import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.BorderLayout;
import java.awt.Color;
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
			{0, 1, 3, 5, 4, 4, 0, 0, 0, 0, 0, 0, 0},
			{2, 2, 1, 0, 3, 0, 0, 1, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 1, 2, 0, 1, 0, 0, 0, 0, 0},
			{0, 0, 2, 7, 5, 4, 1, 0, 0, 0, 0, 0, 0}
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
		//canvas.loadFromFolder("D:\\projects\\TankGame\\artwork");
		canvas.loadFromFolder("D:\\projects\\my-tank-game\\artwork");
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
		int selectedTile = -1;
		int selectedTileRow;
		int selectedTileCol;
		
		final ImageOpenFilter mImageFilter = new ImageOpenFilter("mapTile");
		
		int mapX0 = 20;
		int mapY0 = 50;
		
		int tilePaneX0;
		int tilePaneY0;
		
		private final List<BufferedImage> images = new ArrayList<>();
		short[][] mapData;
		int rowCount;
		int colCount;
		int tileWidth;
		int tileHeight;
		
		final int MARGIN = 10;
		static final int TILE_GAP = 2;
		
		private IDrawPaneChangeListener drawPaneListener = null;
		
		public MapCanvas() {
			super();
			setListeners();
			setPreferredSize(new Dimension(850, 450));
			//initImageInfoPane();
			graphics = new NLabGraphics();
			
			tileWidth = 0;
			tileHeight = 0;
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
				
				if((this.getWidth()>0) && (this.getHeight() > 0)) {
					tilePaneX0 = getWidth() - (2 * tileWidth + TILE_GAP*4+1);
					tilePaneY0 = 50;
				}
				
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
				
				/**
				 * @TODO please rework on formulas in this method
				 */
				@Override
				public void mousePressed(MouseEvent evt) {
					isMousePressed = true;
					preMouseX = evt.getX();
					preMouseY = evt.getY();
					int temp;
					//test if mouse is on a tile in panel
					if( (preMouseX > tilePaneX0) && (preMouseX < tilePaneX0 + 2*tileWidth + TILE_GAP*4+1) ) {
						if( (preMouseY > tilePaneY0) && 
								(preMouseY < tilePaneY0 + tileHeight*(images.size()/2+1) + TILE_GAP*2*(images.size()/2+1)) ) {
							//drawPaneListener.
							int offx = preMouseX - tilePaneX0;
							int c = offx / (tileWidth + 2*TILE_GAP);
							int offy = preMouseY - tilePaneY0; 
							int r = offy / (tileHeight + TILE_GAP);
							temp = r * 2 + c;
							if(temp < 15) {
								selectedTileRow = r;
								selectedTileCol = c;
								selectedTile = temp;
							}
							System.out.println("row: " + r + " column: " + c + "; Selected Tile: " + selectedTile);
						}
					}
					
				}

				@Override
				public void mouseReleased(MouseEvent me) {
					isMousePressed = false;
					
					int mx = me.getX();
					int my = me.getY();
					
					int temp;
					if( (mx > mapX0) && (mx < mapX0 + colCount*tileWidth) ) {
						if( (my > mapY0) && (my < mapY0 + tileHeight*rowCount) ) {
							//drawPaneListener.
							int offx = mx - mapX0;
							int c = offx / tileWidth;
							int offy = my - mapY0; 
							int r = offy / tileHeight;
							//temp = r * rowCount + c;
							mapData[r][c] = (short)selectedTile;
							
//							System.out.println("row: " + r + " column: " + c + "; Selected Tile: " + selectedTile);
						}
					}
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
						
						if((e.getComponent().getWidth()>0) && (e.getComponent().getHeight() > 0)
								&& (tileWidth>0) && (tileHeight>0)) {
							tilePaneX0 = e.getComponent().getWidth() - (2 * tileWidth + TILE_GAP*4+1 + MARGIN);
							tilePaneY0 = 50;	
						}
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
			int gapx2 = TILE_GAP * 2;
			for(i=0; i<images.size(); i++) {
				x = tilePaneX0 + ((i % 2) * tileWidth + (i%2)*TILE_GAP + (i%2)*gapx2 + (i%2) );
				y = tilePaneY0 + i/2 * tileHeight + (i/2)*TILE_GAP;
				g2.drawImage(images.get(i), x, y, null);
			}
			Color oldC = g2.getColor();
			Color newC = Color.darkGray;
			if(selectedTile > 0) {
				x = tilePaneX0 + selectedTileCol*tileWidth + TILE_GAP;
				y = tilePaneY0 + selectedTileRow * tileHeight + TILE_GAP;
				g2.setColor(newC);
				g2.drawRect(x, y, tileWidth, tileHeight);
				g2.setColor(oldC);
			}
			
			for(i=0; i<rowCount; i++) {
				for(j=0; j<colCount; j++) {
					x = i * tileWidth + mapX0;
					y = j * tileHeight + mapY0;
					g2.drawImage(images.get(mapData[j][i]), x, y, null);
				}
			}
			
			if(isMousePressed && (selectedTile>=0) ) {
				g2.drawImage(images.get(selectedTile), preMouseX, preMouseY, null);
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