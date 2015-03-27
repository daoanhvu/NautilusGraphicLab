package nautilus.lab.formula;
/**
 * This class MUST BE synchronized with common module in native code
 * @author DaVu
 */
public final class Constant {
	public static final boolean DEBUG = true;
	
	public static final int APP_MODE_CALCULATOR = 0;
	public static final int APP_MODE_PLOTTER = 1;
	
	public static final int RAD_UNIT_MODE = 0;
	public static final int DEG_UNIT_MODE = 1;
	
	public static final int LINES = 0x1;
	public static final int LINE_STRIP = 0x3;
	public static final int TRIANGLE_STRIP = 0x5;
	
	public static final int COMMA 		= 0x0000002C;
	public static final int SEMI 		= 0x0000003B;
	public static final int SQRT 		= 0x0000221A;
	public static final int CBRT 		= 0x0000221B;
	public static final int FOURTHRT 	= 0x0000221C;
	public static final int DIV 		= 0x000000F7;
	public static final int LPAREN 		= 0x00000028;
	public static final int RPAREN 		= 0x00000029;
	public static final int LPRACKET 	= 0x0000005B;
	public static final int RPRACKET 	= 0x0000005D;
	public static final int ELEMENT_OF 	= 0x00002208;
	public static final int PLUS 		= 0x0000002B;
	public static final int MINUS 		= 0x0000002D;
	public static final int MULTIPLY 	= 0x000000D7;
	public static final int POWER 		= 0x0000005E;
	public static final int PI_TYPE 	= 0x000003C0;
	public static final int E_TYPE 		= 0x0000212F;
	public static final int NE 			= 0x00002260; // not equals !=
	public static final int AND 		= 0x00002227;
	public static final int OR 			= 0x00002228;
	public static final int GT			= 0x000000E3; //>
	public static final int LT			= 0x0000003C; //<
	public static final int GTE			= 0x00002265; //>=
	public static final int LTE			= 0x00002264; //<=
	public static final int EQ			= 0x0000003D; // equals =
	
	public static final int SEC 			= 0x000100E2;
	public static final int DOMAIN_NOTATION	= 0x000100E3;
	public static final int GT_LT			= 0x00010010;
	public static final int GTE_LT			= 0x00010011;
	public static final int GT_LTE			= 0x00010012;
	public static final int GTE_LTE			= 0x00010013;
	public static final int ABS				= 0x00010014;
	public static final int LN 				= 0x000100E0;
	public static final int LOG 			= 0x000100E1;
	public static final int IMPLY			= 0x00010000;
	public static final int RARROW			= 0x00010001;
	public static final int SIN 			= 0x000100F0;
	public static final int COS 			= 0x000100F1;
	public static final int TAN 			= 0x000100F2;
	public static final int COTAN 			= 0x000100F3;
	public static final int ASIN 			= 0x000100F4;
	public static final int ACOS 			= 0x000100F5;
	public static final int ATAN 			= 0x000100F6;
	public static final int NUMBER			= 0x00010002;
	public static final int NAME			= 0x00010003;
	public static final int VARIABLE 		= 0x00010004;
	
	public static final int NO_ERROR = 0;
	public static final int ERROR_DIV_BY_ZERO = -1;
	public static final int ERROR_TOO_MANY_PARENTHESE = -2;
	public static final int ERROR_OPERAND_MISSING = -3;
	public static final int ERROR_PARSE = -4;
	public static final int ERROR_TOO_MANY_FLOATING_POINT = -5;
	public static final int ERROR_PARENTHESE_MISSING = -6;
	public static final int ERROR_OUT_OF_DOMAIN = -7;
	public static final int ERROR_SYNTAX =-8;
	public static final int ERROR_NOT_AN_EXPRESSION = -9;
	public static final int ERROR_NOT_A_FUNCTION = -10;
	public static final int ERROR_BAD_TOKEN = -11;
	public static final int ERROR_LEXER = -12;
	public static final int ERROR_PARSING_NUMBER = -13;
	public static final int ERROR_MISSING_VARIABLE = -14;
	public static final int ERROR_LOG = -15;
	public static final int ERROR_MISSING_DOMAIN = -16;
	public static final int ERROR_NOT_ENOUGH_MEMORY = -17;
	public static final int ERROR_MISSING_FUNCTION_NOTATION = -18;
	public static final int ERROR_JNI_ERROR = -1000;
	
	public static final float DEG2RAD = 0.017453292519943295769236907685f;
	public static final float RAD2DEG = 57.29577951308232087679815481410f;
}
