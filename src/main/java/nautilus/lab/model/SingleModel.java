package nautilus.lab.model;

import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL3;
import nautilus.lab.graphics.Camera3D;
import nautilus.lab.graphics.IGraphics;
import nautilus.lab.graphics.IPaint;

public class SingleModel extends Model3D {
	protected ArrayList<VertexItem> vertices = new ArrayList<VertexItem>();

	protected float[] primitiveVertices;
	
	public void addVertice(VertexItem vertex){
		vertices.add(vertex);
	}

	public void initialize() {

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
	public void initGL(GL3 gl3, int posHandler, int colorHdl, int normalHandler, int useTextureHandler) {

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

	}
}
