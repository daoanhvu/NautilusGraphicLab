package nautilus.lab.model;

import java.util.ArrayList;
import java.util.List;

import nautilus.lab.graphics.Camera3D;
import nautilus.lab.graphics.IGraphics;
import nautilus.lab.graphics.IPaint;

public class SingleModel extends Model3D {
	protected ArrayList<VertexItem> vertices = new ArrayList<VertexItem>();
	
	public void addVertice(VertexItem vertex){
		vertices.add(vertex);
	}
	
	public void addVertices(VertexItem[] vertices_){
		for(int i=0; i<vertices_.length; i++)
			vertices.add(vertices_[i]);
	}
	
	public void addVertices(List<VertexItem> vertices_){
		vertices.addAll(vertices_);
	}
	
	public VertexItem removeVertice(int index){
		return vertices.remove(index);
	}
	
	public VertexItem getVertex(int index){
		return vertices.get(index);
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
