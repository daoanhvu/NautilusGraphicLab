package nautilus.lab;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;



import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;




//import javax.media.opengl.GLCapabilities;
//import javax.media.opengl.GLProfile;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import simplemath.math.Function;
import simplemath.math.ImageData;
import nautilus.lab.component.LabFrame;
import nautilus.lab.formula.TestFormulaFrame;
import nautilus.lab.jogl.NLabScene;

/**
 * Dao Anh Vu
 */
public class App {
    public static void main( String[] args ){
    	//startJogl();
    	//buildIndicesForTriangleStrip(3, 3);
    	testFormula();
    }
    
    static void testFormula() {
    	try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					TestFormulaFrame testFormulaFrm = new TestFormulaFrame();
					testFormulaFrm.setVisible(true);
				}
			});
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    static void buildIndicesForTriangleStrip(int yLength, int xLength) {
    	// Now build the index data
    	int numStripsRequired = yLength - 1;
    	int numDegensRequired = 2 * (numStripsRequired - 1);
    	int verticesPerStrip = 2 * xLength;
    	short[] heightMapIndexData = new short[(verticesPerStrip * numStripsRequired) + numDegensRequired];
    	int offset = 0;
    	for (int y = 0; y < yLength - 1; y++) {
    		if (y > 0) {
    			// Degenerate begin: repeat first vertex
    			heightMapIndexData[offset++] = (short)(y * yLength);
    		}
    		for (int x = 0; x < xLength; x++) {
    			// One part of the strip
    			heightMapIndexData[offset++] = (short)((y * yLength) + x);
    			heightMapIndexData[offset++] = (short)(((y + 1) * yLength) + x);
    		}
    		if (y < yLength - 2) {
    			// Degenerate end: repeat last vertex
    			heightMapIndexData[offset++] = (short)(((y + 1) * yLength) + (xLength - 1));
    		}
    	}

    	for (int i = 0; i<offset; i++) {
    		System.out.print(heightMapIndexData[i] + "  ");
    	}
    	System.out.print("\n");
    }

    
    public static void startJogl(){
    	//GLProfile profile = GLProfile.getDefault();
    	//GLCapabilities caps = new GLCapabilities(profile);
    	Function f = new Function();
    	int errorCode = f.jniSetFunctionString("f(x,y)=sin(x)+cos(y)");
    	
    	ArrayList<ImageData> list = f.jniGetSpace(new float[]{-2, 2, -2, 2}, .2f);
    	final NLabScene scene = new NLabScene(list);
    	JFrame frame = new JFrame("AWT Window Test");
    	frame.setSize(300, 300);
    	frame.getContentPane().add(scene, BorderLayout.CENTER);
    	frame.setVisible(true);
    	
    	frame.addWindowListener(new WindowAdapter(){
    		public void windowClosing(WindowEvent e){
    			Thread thread = new Thread(new Runnable(){
    				public void run() {
    					scene.stopAnimator();
    	    			System.exit(0);
    				}
    			});
    			thread.start();
    		}
    	});
    }
    
    public static void startAWT(){
    	LabFrame frm = new LabFrame();
		frm.setVisible(true);
    }
}
