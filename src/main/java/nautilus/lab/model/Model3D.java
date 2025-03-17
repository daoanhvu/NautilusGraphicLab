package nautilus.lab.model;

import com.jogamp.opengl.GL3;
import nautilus.lab.graphics.Camera3D;
import nautilus.lab.graphics.IGraphics;
import nautilus.lab.graphics.IPaint;

public abstract class Model3D {
	public abstract void initGL(GL3 gl3, int posHandler, int colorHdl, int normalHandler, int useTextureHandler);
	public abstract void draw(Camera3D camera, IGraphics g, IPaint paint);
	public abstract void draw(GL3 gl3, int posHandler, int normalHandler, int colorHandler, int textureHandler);
	public abstract void save(String fileName);
	public abstract void dispose(GL3 gl3);
}

