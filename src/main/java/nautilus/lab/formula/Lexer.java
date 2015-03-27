package nautilus.lab.formula;

import static nautilus.lab.formula.Constant.NO_ERROR;
import static nautilus.lab.formula.Constant.COMMA;
import static nautilus.lab.formula.Constant.SEMI;
import static nautilus.lab.formula.Constant.NUMBER;
import static nautilus.lab.formula.Constant.NAME;
import static nautilus.lab.formula.Constant.LPAREN;
import static nautilus.lab.formula.Constant.RPAREN;
import static nautilus.lab.formula.Constant.LPRACKET;
import static nautilus.lab.formula.Constant.RPRACKET;
import static nautilus.lab.formula.Constant.PLUS;
import static nautilus.lab.formula.Constant.MINUS;
import static nautilus.lab.formula.Constant.MULTIPLY;
import static nautilus.lab.formula.Constant.DIV;
import static nautilus.lab.formula.Constant.SQRT;
import static nautilus.lab.formula.Constant.CBRT;
import static nautilus.lab.formula.Constant.FOURTHRT;
import static nautilus.lab.formula.Constant.POWER;
import static nautilus.lab.formula.Constant.LOG;
import static nautilus.lab.formula.Constant.LN;
import static nautilus.lab.formula.Constant.SIN;
import static nautilus.lab.formula.Constant.COS;
import static nautilus.lab.formula.Constant.TAN;
import static nautilus.lab.formula.Constant.COTAN;
import static nautilus.lab.formula.Constant.ASIN;
import static nautilus.lab.formula.Constant.ACOS;
import static nautilus.lab.formula.Constant.ATAN;
import static nautilus.lab.formula.Constant.EQ;
import static nautilus.lab.formula.Constant.LT;
import static nautilus.lab.formula.Constant.GT;
import static nautilus.lab.formula.Constant.LTE;
import static nautilus.lab.formula.Constant.GTE;
import static nautilus.lab.formula.Constant.NE;
import static nautilus.lab.formula.Constant.AND;
import static nautilus.lab.formula.Constant.OR;
import static nautilus.lab.formula.Constant.ELEMENT_OF;
import static nautilus.lab.formula.Constant.DOMAIN_NOTATION;
import static nautilus.lab.formula.Constant.ERROR_TOO_MANY_FLOATING_POINT;
import static nautilus.lab.formula.Constant.PI_TYPE;
import static nautilus.lab.formula.Constant.E_TYPE;
import static nautilus.lab.formula.Constant.ERROR_OPERAND_MISSING;

import java.util.ArrayList;


public class Lexer extends ArrayList<Token> {
	static final String TAG = "LEXER";
	private static final long serialVersionUID = 98701L;
	
	private int outlen = 0;
	private int gErrorColumn = -1;
	private int gErrorCode = 0;
	private int mCurrentIndex = -1;
	
	private String mText;
	
	public Lexer() {
	}
	
	public int getCurrentIndex() {
		return mCurrentIndex;
	}

	public void setCurrentIndex(int mCurrentIndex) {
		this.mCurrentIndex = mCurrentIndex;
	}

	public int getErrorCode() {
		return gErrorCode;
	}

	public void setErrorCode(int gErrorCode) {
		this.gErrorCode = gErrorCode;
	}

	public int getErrorColumn() {
		return gErrorColumn;
	}

	public void setErrorColumn(int gErrorColumn) {
		this.gErrorColumn = gErrorColumn;
	}

	/**
		A NAME is a single character but not 'e' and placed at the end of inputString OR
		followed by (){}[];+-* / , ; > < ! space
	*/
	private boolean isAName(final CharSequence inputString, int length, int index) {
		char cc = inputString.charAt(index);
		char nextC;
	
		if(cc == 'e') return false;
			
		if(( cc>= 'a' && cc<='z' ) || (cc>= 'A' && cc<='Z')) {
			if((index < length-1)) {
				nextC = inputString.charAt(index + 1);
				if(nextC==' ' || nextC=='+' || nextC=='-' || nextC=='*' || nextC==(char)MULTIPLY 
						|| nextC=='/' || nextC == (char)DIV
						|| nextC=='^' 
						|| nextC=='='
						|| nextC=='(' || nextC==')' || nextC=='[' || nextC==']' || nextC=='<' || nextC=='>'
						|| nextC=='!'|| nextC==','|| nextC==';')
					return true;
			}else
				return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @return type of function or ZERO
	 */
	private int isFunctionName(final CharSequence str, int l, int index) {
		int result = 0;	
		char c0, c1, c2;
			
		if(index+2 < l){
			c0 = str.charAt(index);
			c1 = str.charAt(index+1);
			c2 = str.charAt(index+2);
		}else
			return result;
				
		if( (index+5 < l) && c0=='c' && c1=='o' && c2 =='t' && 
					(str.charAt(index+3)=='a')	&& (str.charAt(index+4)=='n') ){
			outlen = 5;
			result = COTAN;
		} else if(c0 == (char)SQRT){
			outlen = 1;
			result = SQRT;
		} else if(c0 == (char)CBRT){
			outlen = 1;
			result = CBRT;
		} else if(c0 == (char)FOURTHRT){
			outlen = 1;
			result = FOURTHRT;
		} else if((index+4 < l) && (c0=='s' && c1=='q' && c2=='r' && str.charAt(index+3)=='t' )){
			outlen = 4;
			result = SQRT;
		}else if((index+4 < l) && (c0=='a' && c1=='t' && c2=='a' && str.charAt(index+3)=='n')){
			outlen = 4;
			result = ATAN;
		}else if((index+4 < l) && (c0=='a' && c1=='s' && c2=='i' && str.charAt(index+3)=='n')){
			outlen = 4;
			result = ASIN;
		}else if((index+4 < l) && (c0=='a' && c1=='c' && c2=='o' && str.charAt(index+3)=='s')) {
			outlen = 4;
			result = ACOS;
		} else if((index+4 < l) && (c0=='c' && c1=='b' && c2=='r' && str.charAt(index+3)=='t')) {
			outlen = 4;
			result = CBRT;
		} else if((index+3 < l)&& (c0=='t' && c1=='a' && c2=='n')) {
			outlen = 3;
			result = TAN;
		}else if((index+3 < l) && (c0=='s' && c1=='i' && c2=='n')) {
			outlen = 3;
			result = SIN;
		}else if((index+3 < l) &&(c0=='c' && c1=='o' && c2=='s')) {
			outlen = 3;
			result = COS;
		}else if((index+3 < l) && (c0=='l' && c1=='o' && c2=='g')) {
			outlen = 3;
			result = LOG;
		}else if(c0=='l' && c1=='n') {
			outlen = 2;
			result = LN;
		}
		
		/*
			The following token of a function name MUST be an open parenthese
			TODO: I'm not sure if we need to check this
		*/
		if( (result!=0) && ( (outlen + index )>=0) && (str.charAt(outlen + index )!='(') ) {
			result = 0;
		}
		
		outlen++;

		return result;
	}
	
	private int checkNumericOperator(char c) {
		int result = 0;
		switch(c) {
			case '+':
				result = PLUS;
				break;
			case '-':
				result = MINUS;
				break;
			case '*':
			case 0xD7:
				result = MULTIPLY;
				break;
			case '/':
			case 0xF7:
				result = DIV;
				break;
			case '^':
				result = POWER;
				break;
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param c
	 * @return type of token or 0
	 */
	private int checkParenthesePrackets(char c) {
		int result = 0;

		switch(c) {

			case '(':
				result = LPAREN;
			break;
			
			case ')':
				result = RPAREN;
			break;
				
			case '[':
				result = LPRACKET;
			break;
				
			case ']':
				result = RPRACKET;
			break;
		}

		return result;
	}
	
	private int checkCommaSemi(char c) {
		int result = 0;

		switch(c) {
			case ',':
				result = COMMA;
			break;
			
			case ';':
				result = SEMI;
			break;
		}
			
		return result;
	}
	
	/**
	 * 
	 * @param c
	 * @return type of token or ZERO
	 */
	private int checkLogicOperator(char c) {
		int result = 0;
		switch(c){
			case '=':
				result = EQ;
				break;
				
			case '>':
				result = GT;
				break;
				
			case (char)0x2265:
				result = GTE;
				break;

			case '<':
				result = LT;
				break;
				
			case (char)0x2264:
				result = LTE;
				break;

			case (char)0x2260:
				result = NE;
				break;
		}
		return result;
	}
	
	private int getPriority(int type){
		switch(type){
			case PLUS:
			case MINUS:
				return 4;
				
			case MULTIPLY:
			case DIV:
				return 5;
				
			case POWER:
				return 6;
				
			case LT:
			case GT:
			case LTE:
			case GTE:
				return 3;

			case NE:
				return 7;
		}
		return 0;
	}

	void lexicalAnalysis(boolean appended, int charIndex) {
		int type, k = 0;
		boolean floatingPoint;
		char ch;
		Token token;
		
		gErrorColumn = -1;
		gErrorCode = NO_ERROR;
		clear();
		if(mText == null) {
			return;
		}
		
		//TODO: improve here to prevent analysising from begin of the text
		while( mText.length()>0 && charIndex < mText.length() ) {
			ch = mText.charAt(charIndex);
			
			if( (type=checkNumericOperator(ch)) != 0 ) {
					token = Token.getToken();
					token.column = charIndex;
					token.text = mText.substring(charIndex, charIndex+1);
					token.type = type;
					token.baseline = 0;
					token.needParenthese = false;
					switch(type) {
						case PLUS:
						case MINUS:
							token.priority = 4;
							break;
						
						case MULTIPLY:
						case DIV:
							token.priority = 5;
							break;
							
						case POWER:
							token.priority = 6;
							break;
					}
					add(token);
					charIndex += 1;
				}else if( (type=checkParenthesePrackets(ch)) != 0 ) {
					token = Token.getToken();
					token.priority = 0;
					token.column = charIndex;
					token.text = mText.substring(charIndex, charIndex+1);
					token.type = type;
					token.baseline = 0;
					token.needParenthese = false;
					add(token);
					charIndex++;
				}else if( (type=checkCommaSemi(ch)) != 0 ) {
					token = Token.getToken();
					token.column = charIndex;
					token.text = mText.substring(charIndex, charIndex+1);
					token.type = type;
					token.priority = 0;
					token.baseline = 0;
					token.needParenthese = false;
					add(token);
					charIndex++;
				}else if( (type=checkLogicOperator(ch)) != 0) {
					token = Token.getToken();
					token.column = charIndex;
					token.text = mText.substring(charIndex, charIndex+1);
					token.type = type;
					token.baseline = 0;
					token.needParenthese = false;
					switch(type) {
						case LT:
						case GT:
						case LTE:
						case GTE:
							token.priority = 3;
							break;
						case NE:
							token.priority = 7;
							break;
					}
					add(token);
					charIndex++;
				}else if( ch == (char)ELEMENT_OF ) {
					token = Token.getToken();
					token.priority = 0;
					token.column = charIndex;
					token.text = mText.substring(charIndex, charIndex+1);
					token.type = ELEMENT_OF;
					token.baseline = 0;
					token.needParenthese = false;
					add(token);
					charIndex++;
				}else if(Character.isDigit(ch)) {
					floatingPoint = false;
					for(k = charIndex+1; k < mText.length(); k++) {
						if(!Character.isDigit( mText.charAt(k) )) {
							if(mText.charAt(k) == '.') {
								//check if we got a floating point
								if(floatingPoint){ //<- ERROR: the second floating point
									gErrorColumn = k;
									gErrorCode = ERROR_TOO_MANY_FLOATING_POINT;
									return;
								}
								floatingPoint = true;
							} else {
								token = Token.getToken();
								token.priority = 0;
								token.column = charIndex;
								token.text = mText.substring(charIndex, k);
								token.type = NUMBER;
								token.baseline = 0;
								token.needParenthese = false;
								add(token);
								charIndex = k;
								break;
							}
						}
					}
					
					if(charIndex < k){
						token = Token.getToken();
						token.priority = 0;
						token.column = charIndex;
						token.text = mText.substring(charIndex, k);
						token.type = NUMBER;
						token.baseline = 0;
						token.needParenthese = false;
						add(token);
						charIndex = k;
					}
				}else if( (type=isFunctionName(mText, mText.length(), charIndex)) != 0 ) {
					token = Token.getToken();
					token.priority = 0;
					token.column = charIndex;
					token.text = mText.substring(charIndex, charIndex+outlen);
					token.type = type;
					token.baseline = 0;
					token.needParenthese = false;
					add(token);
					charIndex += outlen;
				}else if(charIndex>0 && (mText.charAt(charIndex-1)==' ') && (ch=='D') && (mText.charAt(charIndex+1)==':') ){
					token = Token.getToken();
					token.priority = 0;
					token.column = charIndex;
					token.text = "DOMAIN_NOTATION";
					token.type = DOMAIN_NOTATION;
					token.baseline = 0;
					token.needParenthese = false;
					add(token);
					charIndex += 2;
				}else if( isAName(mText, mText.length(), charIndex) ) {
					token = Token.getToken();
					token.priority = 0;
					token.column = charIndex;
					token.text = mText.substring(charIndex, charIndex+1);
					token.type = NAME;
					token.baseline = 0;
					token.needParenthese = false;
					add(token);
					charIndex++;
				}else if(ch=='o' || ch=='O') {
					if(mText.charAt(charIndex+1)=='r' || mText.charAt(charIndex+1)=='R') {
						token = Token.getToken();
						token.column = charIndex;
						token.text = mText.substring(charIndex, charIndex+2);
						token.type = OR;
						token.priority = 1;
						token.baseline = 0;
						token.needParenthese = false;
						add(token);
						charIndex += 2;
					}else{
						//TODO: maybe its a NAME
						charIndex++;
					}
				} else if(ch== (char)OR) {
					token = Token.getToken();
					token.priority = 1;
					token.column = charIndex;
					token.text = mText.substring(charIndex, charIndex+2);
					token.type = OR;
					token.baseline = 0;
					token.needParenthese = false;
					add(token);
					charIndex++;
				} else if(ch=='a' || ch=='A'){
					if(mText.charAt(charIndex+1)=='n' || mText.charAt(charIndex+1)=='N'){
						if(mText.charAt(charIndex+2)=='d' || mText.charAt(charIndex+2)=='D'){
							token = Token.getToken();
							token.column = charIndex;
							token.text = Character.toString((char)AND);
							token.type = AND;
							token.priority = 2;
							token.baseline = 0;
							token.needParenthese = false;
							add(token);
							charIndex += 3;
						}
					}else{
						//TODO: maybe its a NAME
						charIndex++;
					}
				} else if(ch==(char)AND) {
					token = Token.getToken();
					token.column = charIndex;
					token.text = Character.toString((char)AND);
					token.type = AND;
					token.priority = 2;
					token.baseline = 0;
					add(token);
					charIndex++;
				} else if( (charIndex+1 < mText.length() ) && (mText.charAt(charIndex)=='p' || mText.charAt(charIndex)=='P') && (mText.charAt(charIndex+1)=='i' || mText.charAt(charIndex+1)=='I')
								&& ( (charIndex+1 == mText.length()-1) /*|| !isLetter(mText.charAt(idx+2))*/ ) ) {
					token = Token.getToken();
					token.column = charIndex;
					token.text = Character.toString((char)PI_TYPE);
					token.type = OR;
					token.baseline = 0;
					add(token);
					charIndex += 2;
				} else if(ch == (char)PI_TYPE) {
					token = Token.getToken();
					token.column = charIndex;
					token.text = Character.toString((char)PI_TYPE);
					token.type = PI_TYPE;
					token.baseline = 0;
					add(token);
					charIndex++;
				} else if( ( ch==(char)E_TYPE || ch=='e') /*&& ((idx==length-1) || !isLetter(inStr[idx+1]))*/ ) {
					token = Token.getToken();
					token.column = charIndex;
					token.text = Character.toString((char)E_TYPE);
					token.type = E_TYPE;
					token.baseline = 0;
					add(token);
					charIndex++;
			} else
				charIndex++;
		}
	}

	public String getText() {
		return mText;
	}

	public void setText(String text) {
		mText = text;
		lexicalAnalysis(false, 0);
	}	
}
