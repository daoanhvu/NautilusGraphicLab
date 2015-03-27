package nautilus.lab.formula;

import static nautilus.lab.formula.Constant.ACOS;
import static nautilus.lab.formula.Constant.ASIN;
import static nautilus.lab.formula.Constant.ATAN;
import static nautilus.lab.formula.Constant.CBRT;
import static nautilus.lab.formula.Constant.COS;
import static nautilus.lab.formula.Constant.DIV;
import static nautilus.lab.formula.Constant.LN;
import static nautilus.lab.formula.Constant.LOG;
import static nautilus.lab.formula.Constant.LPAREN;
import static nautilus.lab.formula.Constant.MINUS;
import static nautilus.lab.formula.Constant.MULTIPLY;
import static nautilus.lab.formula.Constant.PLUS;
import static nautilus.lab.formula.Constant.POWER;
import static nautilus.lab.formula.Constant.SIN;
import static nautilus.lab.formula.Constant.SQRT;
import static nautilus.lab.formula.Constant.TAN;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

public class Token {
	int type, column;
	int priority;
	float fontSize;
	String text = "";
	boolean visible = true;
	TokenClass mTokenClass;
	
	Token mParent;
	Token mLeft;
	Token mRight;
	
	//members are used for doing layout
	float width;
	float height;
	float x, y;
	float xDraw, yDraw; //used for operator token
	float advance;
	float descent, ascent, leading;
	float baseline;
	float mSpace = 5;
	boolean needParenthese = false;
	
	public enum TokenClass {
		BASIC, COMPOSITE
	}
	
	Token() {
		mParent = null;
		mLeft = null;
		mRight = null;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int _type) {
		type = _type;
	}
	
	public Token getParent() {
		return mParent;
	}

	public void setParent(Token mParent) {
		this.mParent = mParent;
	}

	public int getPriority() {
		return priority;
	}
	
	public void setPriority(int _priority) {
		priority = _priority;
	}
	
	public int getColumn() {
		return column;
	}
	
	public void setColumn(int _column) {
		column = _column;
	}
	
	public float getWidth() {
		return width;
	}
	
	public void setWidth(float w) {
		width = w;
	}
	
	public float getHeight() {
		return height;
	}
	
	public void setHeight(float h) {
		height = h;
	}
	
	public float getX() {
		return x;
	}
	
	public void setX(float _x) {
		float dx = x - _x;
		x = _x;
		xDraw = xDraw - dx;
		if(mTokenClass == TokenClass.COMPOSITE) {
			float newX;
			if(mLeft != null){
				newX = mLeft.x - dx;
				mLeft.setX(newX);
			}
			if(mRight != null){
				newX = mRight.x - dx;
				mRight.setX(newX);
			}
		}
	}
	
	public float getY() {
		return y;
	}
	
	public void setY(float _y) {
		float dy = y - _y;
		y = _y;
		baseline = baseline - dy;
		yDraw = yDraw - dy;
		if(mTokenClass == TokenClass.COMPOSITE) {
			float newY;
			if(mLeft != null){
				newY = mLeft.getY() - dy;
				mLeft.setY(newY);
			}
			if(mRight != null){
				newY = mRight.getY() - dy;
				mRight.setY(newY);
			}
		}
	}
	
	public void setLeft(Token tkLeft) {
		mLeft = tkLeft;
		if(mLeft != null)
			mLeft.setParent(this);
	}
	
	public Token getLeft() {
		return mLeft;
	}
	
	public void setRight(Token tkRight) {
		mRight = tkRight;
		if(mRight != null)
			mRight.setParent(this);
	}
	
	public Token getRight() {
		return mRight;
	}
	
	public void setTokenClass(TokenClass cls) {
		mTokenClass = cls;
	}
	
	public TokenClass getTokenClass() {
		return mTokenClass;
	}
	
	public void layout(Graphics g, float startX, float startY, float fSize) {
		FontMetrics fm = g.getFontMetrics();
		Rectangle2D bounds;
		Font oldFont = null;
		x = startX + mSpace;
		baseline = y = yDraw = startY;
		if(mTokenClass == TokenClass.BASIC) {
			bounds = fm.getStringBounds(text, g);
			width = (float)bounds.getWidth();
			height = (float)bounds.getHeight();
		} else {
			if(type == DIV) {
				mLeft.layout(g, startX, startY, fSize);
				mRight.layout(g, startX, startY, fSize);
				
				if(mLeft.getWidth() > mRight.getWidth()) {
					width = mLeft.getWidth();
					//set x for denominator
					float dx = mLeft.getWidth() - mRight.getWidth();
					//mRight.setX(startX + dx/2);
					mRight.setX(x + dx/2);
				} else if(mLeft.getWidth() < mRight.getWidth()) {
					width = mRight.getWidth();
					//set x for nominator
					float dx = mRight.getWidth() - mLeft.getWidth();
					//mLeft.setX(startX + dx/2);
					mLeft.setX(x + dx/2);
				} else {
					width = mLeft.getWidth();
				}
				
				/* Vi tri cua dau ghach ngang chinh la baseline
					Y cua nominator = this.baseline - 2
				 * */
				yDraw -= fm.getDescent() + fm.getLeading();
				mLeft.setY(yDraw - 2);
				
				//y cua denominator = baseline + 2 + denominator.height
				mRight.setY(yDraw + mRight.height - fm.getDescent() - fm.getLeading() );
				
				this.height = mLeft.getHeight() + 2 + mRight.getHeight();
				//Vi y la bottom line nen toa do y cua phep chia la y cua denominator
				this.y = mRight.getY();
				
			} else if(type == COS) {
				float posX = startX;
				mLeft.layout(g, posX, startY, fSize);
				bounds = fm.getStringBounds("cos(", g);
				
				//check if child of this node is taller than 'cos(' 
				if(mLeft.height > bounds.getHeight()) {
					//We increase the font size for 'cos('
					oldFont = fm.getFont();
					float newSize = oldFont.getSize() * 1.5f;
					Font newFont = new Font(oldFont.getName(), oldFont.getStyle(), Math.round(newSize));
					g.setFont(newFont);
					bounds = g.getFontMetrics(newFont).getStringBounds("cos(", g);
				}
				mLeft.setX(startX + (float)bounds.getWidth());
				this.fontSize = g.getFont().getSize();
				Rectangle2D bounds1 = g.getFontMetrics().getStringBounds(")", g);
				width = mLeft.getWidth() + (float)(bounds.getWidth() + bounds1.getWidth());
				height = (float)(bounds.getHeight() > mLeft.height?bounds.getHeight():mLeft.height);
				if(oldFont != null)
					g.setFont(oldFont);
				
			} else if(type == SQRT) {
				float posX = startX;
				mLeft.layout(g, posX, startY, fSize);
				posX += mLeft.getWidth() + mSpace;
				width = posX - startX;
				height = mLeft.getHeight() * 1.05f ;
				float newX = x + height/2;
				float dx = x - newX;
				width -= dx;
				mLeft.setX(x + height/2);
				this.y = mLeft.getY();
				mLeft.setY(mLeft.y - (fm.getDescent() + fm.getLeading() ) );
			} else {
				if( needParenthese ) {
					//Hien thi dau mo dong ngoac
					// tinh width cua dau mo ngoac
					bounds = fm.getStringBounds("(", g);
					float posX = startX + (float)bounds.getWidth();
					mLeft.layout(g, posX, startY, fSize);
					posX += mLeft.getWidth() + 5;
					bounds = fm.getStringBounds(text, g);
					int textWidth = (int)Math.round(bounds.getWidth());
					mRight.layout(g, posX + textWidth + 5, startY, fSize);
					posX += mRight.getWidth() + textWidth + bounds.getWidth();
					xDraw = x + mLeft.getWidth() + (float)bounds.getWidth();
					width = posX - startX + mSpace + (float)bounds.getWidth();
					height = mLeft.getHeight() > mRight.getHeight()? mLeft.getHeight(): mRight.getHeight();
					this.y = mLeft.getY() > mRight.getY()? mLeft.getY(): mRight.getY();
				} else {
					//Trong truong hop day la mot phep toan
					// + - hoac *
					
					float posX = startX;
					mLeft.layout(g, posX, startY, fSize);
					posX += mLeft.getWidth() + mSpace;
					
					//toa do x de ve phep toan
					xDraw = posX;
					
					bounds = fm.getStringBounds(text, g);
					int textWidth = (int)Math.round(bounds.getWidth());
					posX += textWidth + mSpace;
					mRight.layout(g, posX, startY, fSize);
					
					posX += mRight.getWidth();
					width = posX - startX;
					height = mLeft.getHeight() > mRight.getHeight()? mLeft.getHeight(): mRight.getHeight();
					this.y = mLeft.getY() > mRight.getY()? mLeft.getY(): mRight.getY();
				}
			}
		}
	}
	
	public void draw(Graphics g) {
		int ix = Math.round(x);
		int iy = Math.round(y);
		int xwidth;
		if(mTokenClass == TokenClass.BASIC) {
			g.drawString(text, ix, iy);
		} else {
			if(type == DIV) {
				mLeft.draw(g);
				/*
				 * */
				int ilineY = Math.round(yDraw);
				xwidth = Math.round(x + width);
				g.drawLine(ix, ilineY, xwidth, ilineY);
				mRight.draw(g);
			} else if(type == COS) {
				Font oldFont = g.getFont();				
				Font newFont = new Font(oldFont.getName(), oldFont.getStyle(), Math.round(fontSize));
				g.setFont(newFont);
				g.drawString("cos(", ix, iy);
				g.setFont(oldFont);
				mLeft.draw(g);
				g.setFont(newFont);
				g.drawString(")", Math.round(mLeft.x + mLeft.width), iy);
				g.setFont(oldFont);
			} else if(type == SQRT) {
				mLeft.draw(g);
				drawSQRT(g, 2, x, y, width, height);
				//g.drawString(Character.toString((char)0x0000221A), ix, iy);
			} else {
				if(needParenthese) {
					//Draw for other types
					int iBaseline = Math.round(mLeft.baseline);
					g.drawString("(", ix, iBaseline);
					mLeft.draw(g);
					xwidth = Math.round(xDraw);
					g.drawString(text, xwidth, Math.round(mLeft.baseline));
					mRight.draw(g);
					xwidth = Math.round(mRight.x + mRight.width);
					g.drawString(")", xwidth, iBaseline);
				} else {
					//Draw for other types
					mLeft.draw(g);
					xwidth = Math.round(x + mLeft.getWidth());
					g.drawString(text, xwidth, Math.round(mLeft.baseline));
					mRight.draw(g);
					xwidth = Math.round(x + width);
				}
			}
		
			//For testing purpose
//			Color old = g.getColor();
//			g.setColor(Color.BLUE);
//			g.drawRect(ix, Math.round(iy - height), (int)width, (int)height);
//			g.setColor(old);
		}
	}
	
	private void drawSQRT(Graphics g, int th, float x, float y, float w, float h, float stroke) {
		int ix = Math.round(x);
		int iy = Math.round(y);
		int xw = Math.round(x + w);
		int xw13 = Math.round(x + w/3);
		int yh = Math.round(y - h);
		int yh2 = Math.round(y - h*2.0f/3);
		g.drawString(Character.toString((char)0x0000221A), ix, iy);
		g.drawLine(xw13, yh2, xw, yh2);
		if(th > 2) {
//			p.getTextWidths(""+th, widths);
//			float size = p.getTextSize();
//			size = w3*size/widths[0];
//			p.setTextSize(size);
			g.drawString(""+th,ix, yh2);
		}
	}
	
	private void drawSQRT(Graphics g, int th, float x, float y, float w, float h) {
		float w1 = h/2;
		int ix = Math.round(x);
		int iy = Math.round(y);
		int xw = Math.round(x + w);
		int xw13 = Math.round(x + w/3);
		int yh = Math.round(y - h);
		int yh2 = Math.round(y - h*2.0f/3);
		g.drawLine(ix, yh2, ix + Math.round(w1*2.0f/3), iy);
		g.drawLine(ix, yh2+1, ix + Math.round(w1*2.0f/3), iy+1);
		g.drawLine(ix + Math.round(w1*2.0f/3), iy, ix + Math.round(w1), yh);
		g.drawLine(ix + Math.round(w1), yh, xw, yh);
		if(th > 2) {
//			p.getTextWidths(""+th, widths);
//			float size = p.getTextSize();
//			size = w3*size/widths[0];
//			p.setTextSize(size);
			g.drawString(""+th,ix, yh2);
		}
	}
	
	public void moveOffsetY(float dy) {
		baseline += dy;
		if(mTokenClass == TokenClass.BASIC) {
			y += dy;
		} else {
			y += dy;	
			mLeft.moveOffsetY(dy);
			mRight.moveOffsetY(dy);
		}
	}
	
	public static boolean isOperator(int type) {
		if(type == PLUS || type == MINUS || type == MULTIPLY || type == DIV || type ==POWER)
			return true;
		return false;
	}
	
	public static boolean isFunctionOrOpenParenthese(int type) {
		if(type == LPAREN || type == SQRT || type==CBRT || type == LN || type == LOG || type ==SIN
				|| type == COS || type == TAN || type == ASIN || type == ACOS || type ==ATAN )
			return true;
		return false;
	}
	
	public static boolean isFunctionType(int type) {
		if(type == SQRT || type==CBRT || type == LN || type == LOG || type ==SIN
				|| type == COS || type == TAN || type == ASIN || type == ACOS || type ==ATAN )
			return true;
		return false;
	}
	
	public static Token getToken() {
		Token token = new Token();
		token.baseline = 0;
		token.needParenthese = false;
		return token;
	}
	
	@Override
	public String toString() {
		return text + "; (" + x + ", " + y + "); size:" + width + "/" + height +"; visible: " + visible + ")";
	}

	public float getBaseline() {
		return baseline;
	}

	public void setBaseline(float baseline) {
		this.baseline = baseline;
	}
}
