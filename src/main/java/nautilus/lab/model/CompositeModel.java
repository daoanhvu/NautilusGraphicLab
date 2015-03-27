package nautilus.lab.model;

import java.util.HashSet;

import nautilus.lab.graphics.IGraphics;
import nautilus.lab.graphics.IPaint;
import nautilus.util.Camera;

public class CompositeModel extends Model3D {
	private HashSet<Model3D> subModels = new HashSet<Model3D>();
	
	public void addModel(Model3D mdl){
		subModels.add(mdl);
	}
	
	public boolean removeModel(Model3D mdl){
		return subModels.remove(mdl);
	}

	@Override
	public void draw(Camera camera, IGraphics g, IPaint paint) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save(String fileName) {
		// TODO Auto-generated method stub
		
	}
}
