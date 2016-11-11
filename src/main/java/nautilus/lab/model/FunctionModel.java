package nautilus.lab.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import nautilus.lab.graphics.Camera3D;
import nautilus.lab.graphics.IGraphics;
import nautilus.lab.graphics.IPaint;
import simplemath.math.Function;
import simplemath.math.ImageData;
import static nautilus.lab.formula.Constant.NO_ERROR;

public class FunctionModel extends SingleModel{

	private Function function = null;
	
	private Color color = new Color(104, 20, 19);
	private boolean updating = false;
	float[] boundaries;
	List<ImageData> imageData;
	List<short[]> indices;
	int loop;
	
	public FunctionModel(String strFunction, float[] _boundaries){
		//initialize testing function
		function = new Function();
		if(function.setString(strFunction) == NO_ERROR){
			boundaries = _boundaries;
			imageData = function.getSpace(boundaries, 0.1f);
		}else
			updating = true;
	}
	
	@Override
	public void draw(Camera3D camera, IGraphics g2, IPaint paint){
		for(loop=0; loop<imageData.size(); loop++){
			if(imageData.get(loop).getDimension() > 2 ) {
				//camera.drawTriangleTrip(g2, paint, imageData.get(loop).getImage(), indices.get(loop));
			} else {
				//camera.drawLineTrip(g2, paint, imageData.get(loop).getImage(), indices.get(loop));
			}
		}
	}
	
	public boolean setFunction(String strFunct, float[] _boundaries){
		int errorCode;
		if(function != null )
			function.release();
		errorCode = function.setString(strFunct);
		boundaries = _boundaries;
		updating = true;
		if(errorCode == NO_ERROR){
			return true;
		}
		return false;
	}
	
	public void dispose(){
		if(function != null )
			function.release();
	}
}