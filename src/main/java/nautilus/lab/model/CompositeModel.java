package nautilus.lab.model;

import java.util.ArrayList;
import java.util.List;

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
	public void draw(Camera3D camera, IGraphics g, IPaint paint) {
		// TODO Auto-generated method stub
	}

	@Override
	public void save(String fileName) {
		// TODO Auto-generated method stub
		
	}
}
