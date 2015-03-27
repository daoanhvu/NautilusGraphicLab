package nautilus.lab.graphics;

import java.awt.Image;

public interface IGraphics {

	void setColor(int r, int g, int b);

	void setColor(int r, int g, int b, int alpha);

	void fillRect(int l, int t, int width, int height);

	void drawLine(float x1, float y1, float x2, float y2, IPaint paint);

	void drawImage(Image image, int x, int y);

	void drawString(String str, float x, float y, IPaint paint);

}
