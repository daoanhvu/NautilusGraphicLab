package nautilus.lab.component;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Toolkit;
import java.awt.AlphaComposite;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.util.Timer;
import java.util.TimerTask;

import nautilus.lab.common.ImageUtility;
import nautilus.lab.graphics.IGraphics;
import nautilus.lab.graphics.IPaint;
import nautilus.lab.graphics.NLabGraphics;
import nautilus.lab.model.FunctionModel;
import nautilus.lab.model.Model3D;
import nautilus.util.Camera;


public class StrategyCanvas extends Canvas implements IControlListener, CommandListener{
	
	private static final int INFO_PANE_WIDTH = 210;
	private static final int INFO_PANE_HEIGHT = 60;
	
	private BufferStrategy		strategy;
	private final Timer			timer;
	private TimerTask			renderTask;
	private Paint				backgroundGradient;
	private AffineTransform transform = new AffineTransform();
	
	private int command = 0x00000000;
	private IDrawPaneChangeListener drawPaneListener = null;
	
	private BufferedImage imageInfoPane;
	private Image imageCtxMenu;	
	private Camera coord;
	private IGraphics graphics;
	private IPaint paint;
	private IPaint paintX, paintY, paintZ;
	
	private double[] theta = {0, 0, 0, 0};
	private double[] rotateAxis = {-4, 3, 1, 1};
	
	private boolean isMousePressed = false;
	private boolean isShowCtxMenu = false;	
	private int preMouseX = 0, preMouseY = 0;
	
	/** Data used for testing */
	private Model3D testModel;
	
	static {
		//System.setProperty("sun.java2d.trace", "timestamp,log,count");
		System.setProperty("sun.java2d.transaccel", "True");
		//System.setProperty("sun.java2d.opengl", "True");
		// System.setProperty("sun.java2d.d3d", "false"); //default on windows
		// System.setProperty("sun.java2d.ddforcevram", "true");
	}
	
	public StrategyCanvas(){
		this.setIgnoreRepaint(true);
		timer = new Timer(); // used for the render thread
		setListeners();
		setPreferredSize(new Dimension(850, 450));
		initImageInfoPane();
		coord = new Camera();
		//System.out.println("[DEBUG] Screen(" + coord.getScreenX() + ", " + coord.getScreenY()+")");
		
		graphics = new NLabGraphics();
		//Test model
		testModel = new FunctionModel("f(x,y) = x+3*y^2 D:x<-1 AND y>0 OR x > 0", new float[]{-2, 1, 0, 1});
	}
	
	public void setDrawPaneListener(IDrawPaneChangeListener l){
		drawPaneListener = l;
	}
	
	private void initImageInfoPane(){
		Color fromC = new Color(12, 70, 23);
		Color toC = new Color(22, 73, 225);
		BufferedImage inputImage = new BufferedImage(INFO_PANE_WIDTH, INFO_PANE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)inputImage.getGraphics();
		g.setColor(new Color(22, 73, 225));
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
		g.fillRect(0, 0, inputImage.getWidth(), inputImage.getHeight());
		g.dispose();
		imageInfoPane = ImageUtility.applyTransparency(inputImage, fromC, toC);
		inputImage.flush();
		
		inputImage = new BufferedImage(INFO_PANE_WIDTH, INFO_PANE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		g = (Graphics2D)inputImage.getGraphics();
		g.setColor(new Color(22, 23, 225));
		//g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
		g.fillRect(0, 0, inputImage.getWidth(), inputImage.getHeight());
		g.dispose();
		
		imageCtxMenu = ImageUtility.makeTransparent(inputImage, 0.25f);
		inputImage.flush();
	}
	
	private void drawInfoPane(Graphics2D g2){
		int x = getWidth()-imageInfoPane.getWidth(null);
		int y = getHeight()-imageInfoPane.getHeight(null);
		g2.drawString("Theta X: " + theta[0], x + 5, y+10);
		g2.drawString("Theta Y: " + theta[1], x + 5, y+22);
		g2.drawString("Theta Z: " + theta[2], x + 5, y+34);
		g2.drawString("Theta : " + theta[3], x+5, y+46);
		g2.drawString("Rotate vector (" + rotateAxis[0] + "," + rotateAxis[1] + "," + rotateAxis[2] + ")",
				x+5, y+58);
		g2.drawImage(imageInfoPane, x, y, null);
	}
	
	public void render(){
		Graphics2D g2 = (Graphics2D)strategy.getDrawGraphics();
		g2.setPaint(backgroundGradient);
		g2.fillRect(0, 0, getWidth(), getHeight());
		
		((NLabGraphics)graphics).setGraphics(g2);

		// TODO: Draw your game world, or scene or anything else here.
		coord.drawCoordinator(graphics, paintX, paintY, paintZ);

		testModel.draw(coord, graphics, paint);
		
		if(isShowCtxMenu)
			g2.drawImage(imageCtxMenu, preMouseX, preMouseY, null);

		drawInfoPane(g2);
		// properly dispose of the backbuffer graphics object. Release resources
		// and cleanup.
		g2.dispose();

		// flip/draw the backbuffer to the canvas component.
		strategy.show();

		// synchronize with the display refresh rate.
		Toolkit.getDefaultToolkit().sync();
	}
	
	public void setup() {
		// create the background gradient paint object.
		backgroundGradient = new GradientPaint(0, 0, Color.gray, getWidth(),
				getHeight(), Color.lightGray.brighter());

		// create a strategy that uses two buffers, or is double buffered.
		this.createBufferStrategy(2);

		// get a reference to the strategy object, for use in our render method
		// this isn't necessary but it eliminates a call during rendering.
		strategy = this.getBufferStrategy();

		start();
	}
	
	/**
	 * Initialize the render and update tasks, to call the render method, do
	 * timing and FPS counting, handling input and canceling existing tasks.
	 */
	public void start() {
		// if the render task is already running stop it, this may cause an
		// exception to be thrown if the task is already canceled.
		if (renderTask != null) {
			renderTask.cancel();
		}

		// our main task for handling the rendering and for updating and
		// handling input and movement events. The timer class isn't the most
		// reliable for game updating and rendering but it will suffice for the
		// purpose of this snippet.
		renderTask = new TimerTask() {
			//long	lasttime	= System.currentTimeMillis();
			@Override
			public void run() {

				// get the current system time
				//long time = System.currentTimeMillis();
				// calculate the time passed in milliseconds
				//double dt = (time - lasttime) * 0.001;
				// save the current time
				//lasttime = time;

				render();
			}
		};

		// These will cap our frame rate but give us unexpected results if our
		// rendering or updates take longer than the 'period' time. It
		// is likely that we could have overlapping calls.
		// timer.schedule(renderTask, 0, 16);
		timer.schedule(renderTask, 0, 30);
	}

	/**
	 * Stops the rendering cycle so that the application can close gracefully.
	 */
	protected void stop() {
		renderTask.cancel();
	}
	
	private double[] rotM1 = new double[16];
	private double[] rotM2 = new double[16];
	private double[] rotM = new double[16];
	private void setListeners(){
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
					coord.setup(0,0, getWidth(), getHeight());
				}
				
				public void  componentShown(ComponentEvent e){
					
				}
		});
	}
	
	public void onFrustumChangeCmd(double l, double y, double z) {
	}
	
	public void onViewChangeCmd(double ex, double ey, double ez,
											double targetX, double targetY, double targetZ,
											double upX, double upY, double upZ,
											double neaf, double far) {
	}
	
	public void broastCastChanged(){
	}

	@Override
	public void onRotateCommand(double alp, double rvx, double rvy, double rvz) {
		// TODO Auto-generated method stub
		command = 1;
		rotateAxis[0] = rvx;
		rotateAxis[1] = rvy;
		rotateAxis[2] = rvz;
	}

	@Override
	public void onRotateCommand(double alp, double px, double py, double pz,
			double rvx, double rvy, double rvz) {
		command = 1;
		rotateAxis[0] = rvx;
		rotateAxis[1] = rvy;
		rotateAxis[2] = rvz;
		
	}

	@Override
	public void onAddVertexCommand(double vx, double vy, double vz) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAddLineCommand() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNFunctionChange(String strFunct, float[] boundaries) {
		if(testModel != null){
			((FunctionModel)testModel).setFunction(strFunct, boundaries);
		}
	}
}
