package nautilus.lab.alg;

import java.util.Stack;

import nautilus.lab.model.VertexItem;
import nautilus.util.GraphUtilites;

public class IncrementalConvexHull3D extends AbstractAlg implements IAlgorithm{

	private VertexItem[] vertices;
	private Stack<Face> stackTetrahedrons = new Stack<Face>();
	
	public IncrementalConvexHull3D(){
	}
	
	public void setData(float[] vertexData){
		int count = vertexData.length;
		
		vertices = new VertexItem[count/3];
		
		for(int i=0; i<count; i++){
			vertices[i] = new VertexItem(vertexData[i*3], vertexData[i*3+1], vertexData[i*3+2]);
		}
	}
	
	private VertexItem[] findSuperCube(){
		float minx = vertices[0].x;
		float maxx = vertices[0].x;
		int minxIdx = 0;
		int maxxIdx = 0;
		
		float miny = vertices[0].y;
		float maxy = vertices[0].y;
		int minyIdx = 0;
		int maxyIdx = 0;
		
		float minz = vertices[0].z;
		float maxz = vertices[0].z;
		int minzIdx = 0;
		int maxzIdx = 0;
		
		for(int i=1; i<vertices.length; i++){
			if(vertices[i].x < minx){
				minx = vertices[i].x;
				minxIdx = i;
			}
			if(vertices[i].x > maxx){
				maxx = vertices[i].x;
				maxxIdx = i;
			}
			
			if(vertices[i].y < miny){
				miny = vertices[i].y;
				minyIdx = i;
			}
			if(vertices[i].y > maxy){
				maxy = vertices[i].y;
				maxyIdx = i;
			}
			
			if(vertices[i].z < minz){
				minz = vertices[i].z;
				minzIdx = i;
			}
			if(vertices[i].z > maxz){
				maxz = vertices[i].z;
				maxzIdx = i;
			}
		}
		
		VertexItem[] cube = new VertexItem[8];
		cube[0] = new VertexItem(minx, miny, minz);
		cube[1] = new VertexItem(minx, maxy, minz);
		cube[2] = new VertexItem(maxx, maxy, minz);
		cube[3] = new VertexItem(maxx, miny, minz);
		cube[4] = new VertexItem(minx, miny, maxz);
		cube[5] = new VertexItem(minx, maxy, maxz);
		cube[6] = new VertexItem(maxx, maxy, maxz);
		cube[7] = new VertexItem(maxx, miny, maxz);
		
		return cube;
	}
	
	public void run(){
		int i;
		VertexItem[] cube = findSuperCube();
		Tetrahedron newtet;
		Tetrahedron tet = new Tetrahedron(cube[0], cube[1], cube[2], cube[7]);		
		Face f;
		
		for(i=0; i<vertices.length; i++){
		
			//TODO: Locate the tetrahedron T containing vertices[i]
			
			if(inSphere(tet.v0.x, tet.v0.y, tet.v0.z,
												tet.v1.x, tet.v1.y, tet.v1.z,
												tet.v2.x, tet.v2.y, tet.v2.z,
												tet.v3.x, tet.v3.y, tet.v3.z,
												vertices[i].x, vertices[i].y, vertices[i].z) > 0){

				/** Connect vertices[i] into T by flip 14 */
				newtet = new Tetrahedron(tet.v0, tet.v1, tet.v2, vertices[i]);
				stackTetrahedrons.push(new Face(tet.v0, tet.v1, vertices[i]));
				
				newtet = new Tetrahedron(tet.v0, tet.v1, tet.v3, vertices[i]);
				//stackTetrahedrons.push(newtet);
				
				newtet = new Tetrahedron(tet.v0, tet.v2, tet.v3, vertices[i]);
				//stackTetrahedrons.push(newtet);
				
				newtet = new Tetrahedron(tet.v1, tet.v2, tet.v3, vertices[i]);
				//stackTetrahedrons.push(newtet);												
				
				while(!stackTetrahedrons.isEmpty()){
					f = stackTetrahedrons.pop();
				}
			}
		}
	}
	
	private int inSphere(float x, float y, float z, float x2, float y2,
			float z2, float x3, float y3, float z3, float x4, float y4,
			float z4, float x5, float y5, float z5) {
		// TODO Auto-generated method stub
		return 0;
	}

	class Face{
		VertexItem v0;
		VertexItem v1;
		VertexItem v2;
		
		Face(){}
		Face(VertexItem v0, VertexItem v1, VertexItem v2){
			this.v0 = v0;
			this.v1 = v1;
			this.v2 = v2;
		}
	}
	
	class Tetrahedron{
		VertexItem v0;
		VertexItem v1;
		VertexItem v2;
		VertexItem v3;
		
		Tetrahedron(){}
		Tetrahedron(VertexItem v0, VertexItem v1, VertexItem v2, VertexItem v3){
			this.v0 = v0;
			this.v1 = v1;
			this.v2 = v2;
			this.v3 = v3;
		}
		
		public boolean checkFace(Face face){
			return false;
		}
		
		public boolean checkFace(VertexItem v0, VertexItem v1, VertexItem v2){
			if ( this.v0 == v0 && this.v1 == v1 && this.v2==v2 )
				return true;
				
			if ( this.v0 == v0 && this.v1 == v1 && this.v3==v2 )
				return true;
				
			if ( this.v0 == v0 && this.v1 == v1 && this.v3==v2 )
				return true;
				
			return false;
		}
	}
	
}