package nautilus.game.model;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.math.Matrix4;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by davu on 5/3/2016.
 */
public class Tank extends MovingObject {
    private BufferedImage mBitmap;
    private Thread mThread;
    private float xDirection;
    private float yDirection;
    private float mDirectionMag;
    private float mDirectionAngle;
    private boolean moving = false;

    final Matrix4 mMatrix = new Matrix4();
    private final Object displayMutex = new Object();

    @Override
    public boolean load() {
        return false;
    }

    @Override
    public void queueEvent(Runnable task) {
        synchronized (queueMutex) {
            taskQueue.add(task);
            queueMutex.notifyAll();
        }
    }

    public boolean loadImage(InputStream fis) throws IOException {
        mBitmap = ImageIO.read(fis);
        width = mBitmap.getWidth();
        height = mBitmap.getHeight();
        return true;
    }

    /**
     * This method run on drawing thread
     * */
    public void render(GL3 gl3) {
        synchronized (displayMutex) {

        }
    }

    public void changeDirection(float dx, float dy) {
        synchronized (displayMutex) {
            mDirectionMag = (float) Math.sqrt((dx * dx) + (dy * dy));
            xDirection = (float) (dx / mDirectionMag);
            yDirection = (float) (dy / mDirectionMag);
            mDirectionAngle = (float) Math.asin(yDirection / mDirectionMag);
            displayMutex.notifyAll();
        }
    }

    @Override
    public void run() {
        float oldx, oldy;
        try {
            Runnable event = null;
            while (moving) {
                synchronized (queueMutex) {
                    if (!taskQueue.isEmpty()) {
                        event = taskQueue.remove(0);
                    }
                    queueMutex.notifyAll();
                }

                if(event != null) {
                    event.run();
                    event = null;
                    continue;
                }

                oldx = x - width/2.0f;
                oldy = y - height/2.0f;
                x += xDirection;
                y += yDirection;
                synchronized (displayMutex) {
                    displayMutex.notifyAll();
                }
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void start() {
        if(!moving) {
            moving = true;
            mThread = new Thread(this);
            mThread.start();
        }
    }

    @Override
    public void stop() {
        Runnable stop = () -> moving = false;
        synchronized (queueMutex) {
            taskQueue.add(stop);
            queueMutex.notifyAll();
        }
    }
}
