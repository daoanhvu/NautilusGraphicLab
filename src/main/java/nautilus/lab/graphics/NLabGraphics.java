package nautilus.lab.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;

public class NLabGraphics implements IGraphics{

  private Graphics2D g2;
  private Color color = new Color(100, 100, 100, 200);
  private double[] p = {0, 0};
  private double[] p1 = {0, 0};
  private double[] p2 = {0, 0};

  public void setGraphics(Graphics2D g){
    g2 = g;
  }

  public NLabGraphics(){}

  @Override
  public void setColor(int r, int g, int b) {
    // TODO Auto-generated method stub
    g2.setColor(new Color(r, g, b));
  }

  @Override
  public void setColor(int r, int g, int b, int alpha) {
    // TODO Auto-generated method stub
    g2.setColor(new Color(r, g, b, alpha));
  }

  @Override
  public void fillRect(int l, int t, int width, int height) {
    // TODO Auto-generated method stub

  }

  @Override
  public void drawLine(float x1, float y1, float x2, float y2, IPaint paint) {
    g2.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
  }

  @Override
  public void drawImage(Image image, int x, int y) {
    g2.drawImage(image, x, y, null);
  }

  @Override
  public void drawString(String str, float x, float y, IPaint paint) {
    // TODO Auto-generated method stub

  }
}
