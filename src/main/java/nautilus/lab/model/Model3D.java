package nautilus.lab.model;

import nautilus.lab.graphics.Camera3D;
import nautilus.lab.graphics.IGraphics;
import nautilus.lab.graphics.IPaint;

public abstract class Model3D {
	public abstract void draw(Camera3D camera, IGraphics g, IPaint paint);
	public abstract void save(String fileName);
}

