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
import static nautilus.lab.formula.Constant.NUMBER;
import static nautilus.lab.formula.Constant.PI_TYPE;
import static nautilus.lab.formula.Constant.E_TYPE;
import static nautilus.lab.formula.Constant.NAME;
import static nautilus.lab.formula.Constant.VARIABLE;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

public class Token {
	
	static final float FUNCTION_FONT_SIZE_FACTOR = 1.1f;
	static final float ROOT_HEIGHT_FACTOR = 1.05f;
	static final float DIVIDER_SPACE = 2f;
	
	int type, column;
	int priority;
	float fontSize;
	String text = "";
	boolean visible = true;
	
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
	
	public Token getParentAST() {
		return mParent;
	}

	public void setParentAST(Token mParent) {
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
	
	public float getY() {
		return y;
	}
	
	public void setY(float _y) {
		float dy = y - _y;
		y = _y;
		baseline = baseline - dy;
		yDraw = yDraw - dy;
		
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
	
	public void setLeft(Token tkLeft) {
		mLeft = tkLeft;
		if(mLeft != null)
			mLeft.setParentAST(this);
	}
	
	public Token getLeft() {
		return mLeft;
	}
	
	public void setRight(Token tkRight) {
		mRight = tkRight;
		if(mRight != null)
			mRight.setParentAST(this);
	}
	
	public Token getRight() {
		return mRight;
	}
	
	public void layout(Graphics g, float startX, float startY, float fSize) {
		FontMetrics fm = g.getFontMetrics();
		Rectangle2D bounds;
		Font oldFont = null;
		float posX;
		x = startX + mSpace;
		baseline = y = yDraw = startY;
		ascent = fm.getAscent();
		descent = fm.getDescent();
		leading = fm.getLeading();
		
		switch(type) {
			case NUMBER:
			case NAME:
			case VARIABLE:
			case PI_TYPE:
			case E_TYPE:
				bounds = fm.getStringBounds(text, g);
				width = (float)bounds.getWidth();
				height = (float)bounds.getHeight();
				break;
			case DIV:
				mLeft.layout(g, startX, startY, fSize);
				mRight.layout(g, startX, startY, fSize);
				
				if(mLeft.getWidth() > mRight.getWidth()) {
					width = mLeft.getWidth();
					//set x for denominator
					float dx = mLeft.getWidth() - mRight.getWidth();
					mRight.setX(x + dx/2);
				} else if(mLeft.getWidth() < mRight.getWidth()) {
					width = mRight.getWidth();
					//set x for nominator
					float dx = mRight.getWidth() - mLeft.getWidth();
					mLeft.setX(x + dx/2);
				} else {
					width = mLeft.getWidth();
				}
				
				/* Vi tri cua dau ghach ngang chinh la baseline
					Y cua nominator = this.baseline - 2
				 * */
				yDraw -= fm.getDescent();
				mLeft.setY(yDraw - fm.getDescent());
				
				//y cua denominator = yDraw + denominator.height
				//mRight.setY(yDraw + mRight.height + fm.getDescent());
				mRight.setY(yDraw + mRight.height);
				
				this.height = mLeft.getHeight() + mRight.getHeight() + fm.getDescent();
				
				//Vi y la bottom line nen toa do y cua phep chia la y cua denominator
				this.y = mRight.getY();
				this.ascent = mLeft.ascent;
				this.descent = fm.getDescent();
				
				break;
				
			case SIN:
			case COS:
			case TAN:
			case ASIN:
			case ACOS:
			case ATAN:
				posX = startX;
				mLeft.layout(g, posX, startY, fSize);
				bounds = fm.getStringBounds(text, g);
				
				//check if child of this node is taller than 'cos(' 
				if(mLeft.height > bounds.getHeight()) {
					//We increase the font size for 'cos('
					oldFont = fm.getFont();
					float newSize = oldFont.getSize() * FUNCTION_FONT_SIZE_FACTOR;
					Font newFont = new Font(oldFont.getName(), oldFont.getStyle(), Math.round(newSize));
					g.setFont(newFont);
					bounds = g.getFontMetrics(newFont).getStringBounds(text, g);
				}
				mLeft.setX(startX + (float)bounds.getWidth() + mSpace);
				this.fontSize = g.getFont().getSize();
				Rectangle2D bounds1 = g.getFontMetrics().getStringBounds(")", g);
				width = mLeft.getWidth() + (float)(bounds.getWidth() + bounds1.getWidth());
				height = (float)(bounds.getHeight() > mLeft.height?bounds.getHeight():mLeft.height);
				this.ascent = g.getFontMetrics().getAscent();
				this.descent = g.getFontMetrics().getDescent();
				if(oldFont != null)
					g.setFont(oldFont);
				break;
			case SQRT:
			case CBRT:
				posX = startX;
				mLeft.layout(g, posX, startY, fSize);
				posX += mLeft.getWidth() + mSpace;
				width = posX - startX;
				//height = mLeft.getHeight() * ROOT_HEIGHT_FACTOR;
				height = mLeft.getHeight() * ROOT_HEIGHT_FACTOR + fm.getDescent();
				float newX = x + height/2;
				float dx = x - newX;
				width -= dx;
				mLeft.setX(x + height/2);
				this.y = mLeft.getY();
				mLeft.setY(mLeft.y - mLeft.descent);
				//height += fm.getDescent();
				descent = 0;
				leading = fm.getLeading();
				break;
			default:
				if( needParenthese ) {
					//Hien thi dau mo dong ngoac
					// tinh width cua dau mo ngoac
					bounds = fm.getStringBounds("(", g);
					posX = startX + (float)bounds.getWidth();
					mLeft.layout(g, posX, startY, fSize);
					posX += mLeft.getWidth() + 5;
					bounds = fm.getStringBounds(text, g);
					int textWidth = (int)Math.round(bounds.getWidth());
					mRight.layout(g, posX + textWidth + 5, startY, fSize);
					posX += mRight.getWidth() + textWidth + bounds.getWidth();
					xDraw = x + mLeft.getWidth() + (float)bounds.getWidth();
					width = posX - startX;
					height = mLeft.getHeight() > mRight.getHeight()? mLeft.getHeight(): mRight.getHeight();
					this.y = mLeft.getY() > mRight.getY()? mLeft.getY(): mRight.getY();
				} else {
					//Trong truong hop day la mot phep toan
					// + - hoac *
					
					posX = startX;
					mLeft.layout(g, posX, startY, fSize);
					posX += mLeft.getWidth() + mSpace;
					
					//toa do x de ve phep toan
					xDraw = posX;
					
					bounds = fm.getStringBounds(text, g);
					int textWidth = (int)Math.round(bounds.getWidth());
					//posX += textWidth + mSpace;
					posX += textWidth; //khong cong them space o day vi vao layout() cung se tang len
					mRight.layout(g, posX, startY, fSize);
					
					posX += mRight.getWidth();
					width = posX - startX;
					height = mLeft.getHeight() > mRight.getHeight()? mLeft.getHeight(): mRight.getHeight();
					this.y = mLeft.getY() > mRight.getY()? mLeft.getY(): mRight.getY();
				}
		}
		
	}
	
	public void draw(Graphics g) {
		int ix = Math.round(x);
		int iy = Math.round(y);
		int xwidth;
		
		switch(type) {
		case NAME:
		case VARIABLE:
			case NUMBER:
			case PI_TYPE:
			case E_TYPE:
				g.drawString(text, ix, iy);
				break;
			case DIV:
				mLeft.draw(g);
				/*
				 * */
				int ilineY = Math.round(yDraw);
				xwidth = Math.round(x + width);
				g.drawLine(ix, ilineY, xwidth, ilineY);
				mRight.draw(g);
				
				//testing
				Color old = g.getColor();
				g.setColor(Color.BLUE);
				g.drawRect(ix, Math.round(iy - height), (int)width, (int)height);
				g.setColor(old);
				//testing
				
				break;
			case SIN:
			case COS:
			case TAN:
			case ASIN:
			case ACOS:
			case ATAN:
				Font oldFont = g.getFont();
				Font newFont = new Font(oldFont.getName(), oldFont.getStyle(), Math.round(fontSize));
				g.setFont(newFont);
				g.drawString(text, ix, iy);
				g.setFont(oldFont);
				mLeft.draw(g);
				g.setFont(newFont);
				g.drawString(")", Math.round(mLeft.x + mLeft.width), iy);
				g.setFont(oldFont);
				
				//testing
//				Color old = g.getColor();
//				g.setColor(Color.BLUE);
//				g.drawRect(ix, Math.round(iy - height), (int)width, (int)height);
//				g.setColor(old);
				//testing
				
				break;
			case SQRT:
				mLeft.draw(g);
				drawSQRT(g, 2, x, y, width, height);
//				Color old = g.getColor();
//				g.setColor(Color.BLUE);
//				g.drawRect(ix, Math.round(iy - height), (int)width, (int)height);
//				g.setColor(old);
				break;
			case CBRT:
				mLeft.draw(g);
				drawSQRT(g, 3, x, y, width, height);
				break;
			default:
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
	
	private void drawSQRT(Graphics g, int th, float x, float y, float w, float h) {
		float w1 = h/2;
		int ix = Math.round(x);
		int iy = Math.round(y);
		int xw = Math.round(x + w);
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
			
			Font oldFont = g.getFont();
			int newFontSize = oldFont.getSize()/2;
			Font newFont = new Font(oldFont.getName(), oldFont.getStyle(), newFontSize);
			g.setFont(newFont);
			g.drawString(""+th,ix, yh2);
			g.setFont(oldFont);
		}
	}
	
	public void moveOffsetY(float dy) {
		baseline += dy;
		y += dy;
		if(mLeft != null) {
			mLeft.moveOffsetY(dy);
		}
		
		if(mRight != null) {
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
