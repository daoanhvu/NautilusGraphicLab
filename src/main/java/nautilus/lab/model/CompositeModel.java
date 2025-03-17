package nautilus.lab.model;

import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL3;
import nautilus.lab.graphics.Camera3D;
import nautilus.lab.graphics.IGraphics;
import nautilus.lab.graphics.IPaint;

public class CompositeModel extends Model3D {
	private List<Model3D> subModels = new ArrayList<Model3D>();
	
	public void addModel(Model3D mdl){
		subModels.add(mdl);
	}
	
	public boolean removeModel(Model3D mdl){
		return subModels.remove(mdl);
	}

	@Override
	public void initGL(GL3 gl3, int posHandler, int colorHdl, int normalHandler, int useTextureHandler) {
		for (Model3D mdl : subModels) {
			mdl.initGL(gl3, posHandler, colorHdl, normalHandler, useTextureHandler);
		}
	}

	@Override
	public void draw(Camera3D camera, IGraphics g, IPaint paint) {
		// TODO Auto-generated method stub
	}

	@Override
	public void draw(GL3 gl3, int posHandler, int normalHandler, int colorHandler, int textureHandler) {

	}

	@Override
	public void save(String fileName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose(GL3 gl3) {
		for(Model3D mdl : subModels){
			mdl.dispose(gl3);
		}
	}
}
