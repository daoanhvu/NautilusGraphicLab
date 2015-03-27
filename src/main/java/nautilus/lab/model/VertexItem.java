package nautilus.lab.model;

public class VertexItem{
	public float x, y, z;
	
	public VertexItem(float xx, float yy, float zz){
		x = xx;
		y = yy;
		z = zz;
	}
	
	@Override
	public boolean equals(Object obj) {
		if( !VertexItem.class.isInstance(obj) )
			return false;
		
		VertexItem other = (VertexItem)obj;
		
		if(other.x == x && other.y == y
				&& other.z == z)
			return true;
		
		return false;
	}
}