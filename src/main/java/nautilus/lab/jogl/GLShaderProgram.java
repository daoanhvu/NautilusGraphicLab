package nautilus.lab.jogl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.jogamp.opengl.GL3;

public class GLShaderProgram {
	
	private int mProgramId;
	private int mVertextShaderId;
	private int mFragmentShaderId;
	
	public GLShaderProgram() {
		
	}
	
	public void init(GL3 gl3, File shaderFolder, String vShaderPath, String fShaderPath) {
		String vShaderCode = loadShaderFromFile(shaderFolder, vShaderPath);
		String fShaderCode = loadShaderFromFile(shaderFolder, fShaderPath);
		mProgramId = gl3.glCreateProgram();
		mVertextShaderId = loadShader(gl3, GL3.GL_VERTEX_SHADER, vShaderCode);
		mFragmentShaderId = loadShader(gl3, GL3.GL_FRAGMENT_SHADER, fShaderCode);
		gl3.glAttachShader(mProgramId, mVertextShaderId);
		gl3.glAttachShader(mProgramId, mFragmentShaderId);
	}
	
	private String loadShaderFromFile(File shaderFolder, String filePath) {
		StringBuilder result = new StringBuilder();
		BufferedReader br = null;
		try {
			String line;
			br = new BufferedReader(new FileReader(new File(shaderFolder, filePath)));
			while( (line = br.readLine()) != null ) {
				result.append(line);
				result.append("\n");
			}
		} catch(IOException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				if(br != null) br.close();
			}catch(IOException ex2) {
//				throw new RuntimeException(ex2);
                ex2.printStackTrace();
			}
		}
		
		return result.toString();
	}
	
	
	public int getProgramId() {
		return mProgramId;
	}
	
	private int loadShader(GL3 gl3, int type, String shaderCode) {
		int shader = gl3.glCreateShader(type);
		int[] compiled = {0};
		if(shader == 0) return 0;
		
		gl3.glShaderSource(shader, 1, new String[] {shaderCode}, null);
		gl3.glCompileShader(shader);
		
		gl3.glGetShaderiv(shader, GL3.GL_COMPILE_STATUS, compiled, 0);
		if(compiled[0] == 0) {
			int[] logLength = new int[1];
			gl3.glGetShaderiv(shader, GL3.GL_INFO_LOG_LENGTH, logLength, 0);
			byte[] log = new byte[logLength[0]];
			gl3.glGetShaderInfoLog(shader, logLength[0], null, 0, log, 0);
			String logMsg = new String(log);
			
			System.err.println("Compile Shader ERROR: " + logMsg);			
		}
		
		return shader;
	}
	
	public void bindAttribLocation(GL3 gl3, int positionHandle, String attrName) {
		gl3.glBindAttribLocation(mProgramId, positionHandle, attrName);
	}
	
	public void dispose(GL3 gl3) {
        //System.out.println("cleanup, remember to release shaders");
        gl3.glDetachShader(mProgramId, mVertextShaderId);
        gl3.glDeleteShader(mVertextShaderId);
        gl3.glDetachShader(mProgramId, mFragmentShaderId);
        gl3.glDeleteShader(mFragmentShaderId);
        gl3.glDeleteProgram(mProgramId);
    }
}
