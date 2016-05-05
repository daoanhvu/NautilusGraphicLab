package nautilus.lab.component;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.util.Timer;
import java.util.TimerTask;

public abstract class AbstractStrategyCanvas extends Canvas {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected BufferStrategy		strategy;
	protected final Timer			timer;
	protected TimerTask			renderTask;
	protected Paint				backgroundGradient;
	protected AffineTransform transform = new AffineTransform();
	
	static {
		//System.setProperty("sun.java2d.trace", "timestamp,log,count");
		System.setProperty("sun.java2d.transaccel", "True");
		//System.setProperty("sun.java2d.opengl", "True");
		// System.setProperty("sun.java2d.d3d", "false"); //default on windows
		// System.setProperty("sun.java2d.ddforcevram", "true");
	}
	
	/**
	 * Must be called by subclasses
	 */
	public AbstractStrategyCanvas() {
		this.setIgnoreRepaint(true);
		timer = new Timer(); // used for the render thread
	}
	
	public abstract void render(Graphics2D g2);
	
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

				//internalRender();
				//==========Start gender==============
				Graphics2D g2 = (Graphics2D)strategy.getDrawGraphics();
				g2.setPaint(backgroundGradient);
				g2.fillRect(0, 0, getWidth(), getHeight());
				
				render(g2);
				
				// properly dispose of the backbuffer graphics object. Release resources
				// and cleanup.
				g2.dispose();
				
				// flip/draw the backbuffer to the canvas component.
				strategy.show();
				// synchronize with the display refresh rate.
				Toolkit.getDefaultToolkit().sync();
				//==========End gender==============
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

}
