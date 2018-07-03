package nautilus.game.model;

import com.jogamp.opengl.GL3;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davu on 5/3/2016.
 */
public abstract class GameObject {
    protected float x;
    protected float y;
    protected int width;
    protected int height;

    protected float[] vertices;
    protected final float[] modelMatrix = new float[16];
    protected final List<Runnable> taskQueue = new ArrayList<>();
    protected final Object queueMutex = new Object();
    public abstract boolean load();

    public abstract void queueEvent(Runnable task);

    public void setX(float x_) {
        x = x_;
    }

    public float getX(){ return x; }

    public void setY(float y_) {
        y = y_;
    }

    public float getY(){ return y; }

    public void setWidth(int w) {
        width = w;
    }

    public int getWidth(){ return width; }

    public void setHeight(int h) {
        height = h;
    }

    public int getHeight(){ return height; }

    public void moveTo(float x_, float y_) {
        x = x_;
        y = y_;
    }

    public abstract void render(GL3 gl3);
}
