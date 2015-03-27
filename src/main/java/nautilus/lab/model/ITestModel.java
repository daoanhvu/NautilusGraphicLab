package nautilus.lab.model;

import java.awt.event.MouseEvent;
import java.awt.Graphics2D;

public interface ITestModel {
	public void render(Graphics2D g2);
	public void rotate();
	public void translate();
	public void dispose();
	
	/** Mouse listener */
	public void mouseClicked(MouseEvent arg0);
	public void mouseEntered(MouseEvent arg0);
	public void mouseExited(MouseEvent arg0);
	public void mousePressed(MouseEvent arg0);
	public void mouseReleased(MouseEvent arg0);
	
	/** Mouse motion listener */
	public void mouseMoved(MouseEvent evt);
	public void mouseDragged(MouseEvent evt);
}