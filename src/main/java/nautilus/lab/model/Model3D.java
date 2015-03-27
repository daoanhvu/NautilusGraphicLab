package nautilus.lab.model;

import nautilus.lab.graphics.IGraphics;
import nautilus.lab.graphics.IPaint;
import nautilus.util.Camera;

public abstract class Model3D {
	public abstract void draw(Camera camera, IGraphics g, IPaint paint);
	public abstract void save(String fileName);
}

