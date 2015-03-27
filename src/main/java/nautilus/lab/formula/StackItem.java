package nautilus.lab.formula;

import java.util.ArrayList;

class StackItem {
	
	static ArrayList<StackItem> POOL = new ArrayList<StackItem>();
	
	float caretX;
	float caretY;
	float caretHeight;
	float size;
	float spacing;
	Token token;
	
	StackItem(float _x, float _y, float crHeight, float _size, float _spacing, Token _token) {
		caretX = _x;
		caretY = _y;
		size = _size;
		token = _token;
		spacing = _spacing;
		caretHeight = crHeight;
	}
}