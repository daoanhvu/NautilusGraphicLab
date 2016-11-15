package nautilus.lab.jogl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import simplemath.math.ImageData;
import nautilus.util.GraphUtilites;

public class NLabScene extends GLCanvas {
	private static final long serialVersionUID = 155L;

	public NLabScene(GLCapabilities caps) {
		super(caps);
	}
}
