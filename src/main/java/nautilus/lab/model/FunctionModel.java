package nautilus.lab.model;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL3;
import nautilus.lab.graphics.Camera3D;
import nautilus.lab.graphics.IGraphics;
import nautilus.lab.graphics.IPaint;
import simplemath.math.Function;
import simplemath.math.ImageData;
import static nautilus.lab.formula.Constant.NO_ERROR;

public class FunctionModel extends SingleModel {

	private Function function = null;
	
	private Color color = new Color(104, 20, 19);
	private boolean updating = false;
	float[] boundaries;
	List<ImageData> imageData = new ArrayList<>();
	List<short[]> indices;
	int loop;
	
	public FunctionModel(String strFunction, float[] _boundaries){
		//initialize testing function
		function = new Function();
		if(function.setString(strFunction) == NO_ERROR) {
			boundaries = _boundaries;
			function.getSpace(boundaries, 0.1f, false, imageData);
		} else {
			updating = true;
		}
	}

	public void initGL(GL3 gl3, int posHandler, int colorHdl, int normalHandler, int useTextureHandler) {
		for (ImageData data : imageData) {
			data.initGL(gl3, posHandler, colorHdl, normalHandler, useTextureHandler);
		}
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

	public void draw(GL3 gl3, int posHandler, int normalHandler, int colorHandler, int textureHandler) {
		gl3.glUniform1i(textureHandler, 0);
		for (ImageData data : imageData) {
			data.render(gl3, posHandler, normalHandler, colorHandler, textureHandler);
		}
	}
	
	public boolean setFunction(String strFunct, float[] _boundaries) {
		int errorCode;
		if(function != null ) {
			errorCode = function.setString(strFunct);
			boundaries = _boundaries;
			updating = true;
      return errorCode == NO_ERROR;
		}
		return false;
	}
	
	public void dispose(GL3 gl3) {
		if(function != null ) {
			function.release();
		}
		for (ImageData data : imageData) {
			data.dispose(gl3);
		}
	}
}