package nautilus.lab.formula;

import static nautilus.lab.formula.Constant.CBRT;
import static nautilus.lab.formula.Constant.DIV;
import static nautilus.lab.formula.Constant.LPAREN;
import static nautilus.lab.formula.Constant.MINUS;
import static nautilus.lab.formula.Constant.MULTIPLY;
import static nautilus.lab.formula.Constant.PLUS;
import static nautilus.lab.formula.Constant.POWER;
import static nautilus.lab.formula.Constant.RPAREN;
import static nautilus.lab.formula.Constant.SIN;
import static nautilus.lab.formula.Constant.COS;
import static nautilus.lab.formula.Constant.SQRT;
import static nautilus.lab.formula.Constant.TAN;
import static nautilus.lab.formula.Constant.COTAN;
import static nautilus.lab.formula.Constant.LN;
import static nautilus.lab.formula.Constant.LOG;
import static nautilus.lab.formula.Constant.ASIN;
import static nautilus.lab.formula.Constant.ACOS;
import static nautilus.lab.formula.Constant.ATAN;
import static nautilus.lab.formula.Constant.NUMBER;
import static nautilus.lab.formula.Constant.NAME;
import static nautilus.lab.formula.Constant.VARIABLE;
import static nautilus.lab.formula.Constant.PI_TYPE;
import static nautilus.lab.formula.Constant.E_TYPE;

import static nautilus.lab.formula.Constant.AND;
import static nautilus.lab.formula.Constant.LT;
import static nautilus.lab.formula.Constant.GT;
import static nautilus.lab.formula.Constant.OR;
import static nautilus.lab.formula.Constant.LTE;
import static nautilus.lab.formula.Constant.GTE;
import static nautilus.lab.formula.Constant.SEMI;
import static nautilus.lab.formula.Constant.DOMAIN_NOTATION;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Parser {
	
	public Token parsing(List<Token> basicTokens) {
		Token tk;
		int size = basicTokens.size();
		int i = 0;
		boolean isEndExp = false;
		Token itm;
		ArrayList<Token> postfixList = new ArrayList<>();
		Stack<Token> opStack = new Stack<Token>();
		
		while( (i < size) && !isEndExp) {
			tk = basicTokens.get(i);
			switch(tk.type) {
				case PLUS:
				case MINUS:
				case MULTIPLY:
				case DIV:
				case POWER:
					while(!opStack.isEmpty() && Token.isOperator(opStack.peek().getType())
							&& opStack.peek().getPriority() >= tk.getPriority()) {
						itm = opStack.pop();
						addOperatorFunction(postfixList, itm);
					}
					
					//push tk
					opStack.push(tk);
					break;
				case CBRT:
				case SQRT:
				case SIN:
				case COS:
				case TAN:
				case ASIN:
				case ACOS:
				case ATAN:
				case LN:
				case LPAREN:
					opStack.push(tk);
					break;
				case RPAREN:
					while(!opStack.isEmpty() && !Token.isFunctionOrOpenParenthese(opStack.peek().type) ) {
						itm = opStack.pop();
						addOperatorFunction(postfixList, itm);
					}
					
					itm = opStack.pop();
					if(Token.isFunctionType(itm.type)) {
						addOperatorFunction(postfixList, itm);
					}
					
					break;
				case NUMBER:
				case NAME:
				case VARIABLE:
				case PI_TYPE:
				case E_TYPE:
					postfixList.add(tk);
					break;
					
				case SEMI:
				case DOMAIN_NOTATION:
					/** 	End of this expression, kindly stop the while loop, consume 
							this expression and start processing the next expression.
					*/
					isEndExp = true;
					break;
					
				default:
					//Error
					break;
			}
			i++;
		}
		
		while(!opStack.isEmpty()){
			itm = opStack.pop();
			addOperatorFunction(postfixList, itm);
		}
		
		return postfixList.get(0);
	}
	
	private void addOperatorFunction(List<Token> postfixList, Token itm) {
		int n = postfixList.size();
		Token operand1, operand2;
		switch (itm.getType()) {
		case PLUS:
			if(n >1) {
				operand2 = postfixList.remove(n-1);
				operand1 = postfixList.remove(n-2);
				if((itm.priority >= operand2.priority) && (operand2.type != DIV) )
					operand2.needParenthese = true;
				if((itm.priority >= operand1.priority ) && (operand1.type != DIV))
					operand1.needParenthese = true;
				itm.setLeft(operand1);
				itm.setRight(operand2);
				postfixList.add(itm);
			}
			break;
		case MINUS:
			if(n >1) {
				operand2 = postfixList.remove(n-1);
				operand1 = postfixList.remove(n-2);
				if((itm.priority >= operand2.priority) && (operand2.type != DIV) )
					operand2.needParenthese = true;
				if((itm.priority >= operand1.priority ) && (operand1.type != DIV))
					operand1.needParenthese = true;
				itm.setLeft(operand1);
				itm.setRight(operand2);
				postfixList.add(itm);
			}
			break;
			
		
		case DIV:
		case MULTIPLY:
		case POWER:
		case LT:
		case GT:
		case LTE:
		case GTE:
		case AND:
		case OR:
		case LOG:
			operand2 = postfixList.remove(n-1);
			operand1 = postfixList.remove(n-2);
			if((itm.priority >= operand2.priority) && (itm.type != DIV) )
				operand2.needParenthese = true;
			if((itm.priority >= operand1.priority ) && (itm.type != DIV))
				operand1.needParenthese = true;
			itm.setLeft(operand1);
			itm.setRight(operand2);
			postfixList.add(itm);
			break;
			
		case SIN:
		case COS:
		case TAN:
		case COTAN:
		case ASIN:
		case ACOS:
		case ATAN:
		case SQRT:
		case CBRT:
		case LN:
			operand1 = postfixList.remove(n-1);
			itm.setLeft(operand1);
			postfixList.add(itm);
			break;
		}
	}
}
