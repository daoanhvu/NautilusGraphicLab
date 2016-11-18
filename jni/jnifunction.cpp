#include "jnifunction.h"

#include <common.h>
#include <StringUtil.h>
#include <nmath_pool.h>
#include <nfunction.h>
#include <nlablexer.h>
#include <nlabparser.h>
#include <nfunction.h>
#include <criteria.h>
#include <SimpleCriteria.h>
#include <compositecriteria.h>

const int TOKEN_SIZE = 128;

using namespace nmath;

struct tagNMathData {
	NFunction f;
	NLabLexer lexer;
	NLabParser parser;
	Token tokens[TOKEN_SIZE];
};

typedef struct tagNMathData NMathData;

/*
 * Class:     simplemath_math_Function
 * Method:    jniInit
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_simplemath_math_Function_jniInit(JNIEnv *env, jobject thiz) {
	NMathData *data = new NMathData;
	return ((jlong)data);
}

/*
 * Class:     simplemath_math_Function
 * Method:    jniSetString
 * Signature: (JLjava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_simplemath_math_Function_jniSetString(JNIEnv *env, jobject thiz, jlong address, jstring str) {
	return -1;
}

JNIEXPORT void JNICALL Java_simplemath_math_Function_jniCalc(JNIEnv *env, jobject thiz, jlong address,
		jstring function_text, jobject ret) {
	NMathData *data = (NMathData*)address;
	const char* text = env->GetStringUTFChars(function_text, NULL);
	int textLen = env->GetStringUTFLength( function_text);
	int tokenNum, start = 0;
	jstring valueText;
	char outString[256];
	jint errorCode;
	DParam rp;
	NMAST *t;

	jclass returnValClass = env->GetObjectClass(ret);
	jfieldID returnValValueField = env->GetFieldID( returnValClass, "value", "D");
	jfieldID isNullField = env->GetFieldID( returnValClass, "isNull", "Z");
	jfieldID textField = env->GetFieldID( returnValClass, "valueText", "Ljava/lang/String;");

	tokenNum = (data->lexer).lexicalAnalysis(text, textLen, 0, data->tokens, TOKEN_SIZE/*capacity of mTokens*/, 0);
	errorCode = data->lexer.getErrorCode();
	if( errorCode == NMATH_NO_ERROR ) {
		t = data->parser.parseExpression(data->tokens, tokenNum, &start);
		errorCode = data->parser.getErrorCode();
		if( errorCode == NMATH_NO_ERROR) {
			rp.error = 0;
			rp.t = t;
			nmath::reduce_t((void*)&rp);
			errorCode = rp.error;
			if( errorCode == NMATH_NO_ERROR ) {
				start = 0;
				nmath::toString(rp.t, outString, &start, 256);
				outString[start] = 0;
				valueText = env->NewStringUTF(outString);
				env->SetDoubleField( ret, returnValValueField, rp.t->value);
				env->SetBooleanField( ret, isNullField, JNI_FALSE);
				env->SetObjectField( ret, textField, valueText);
			}
		}

		nmath::putIntoPool(rp.t);
	}

	//return errorCode;
}

/*
 * Class:     simplemath_math_Function
 * Method:    derivative
 * Signature: (J)Lsimplemath/math/Function;
 */
JNIEXPORT jint JNICALL Java_simplemath_math_Function_derivative(JNIEnv *env, jobject thiz, jlong address, jobject result) {
	return -1;
}

/*
 * Class:     simplemath_math_Function
 * Method:    jniGetSpace
 * Signature: (J[FFZLjava/util/List;)V
 */
JNIEXPORT void JNICALL Java_simplemath_math_Function_jniGetSpace(JNIEnv *env, jobject thiz, jlong address,
		jfloatArray bounds, jfloat params, jboolean includedNormal, jobject imageData) {

}

/*
 * Class:     simplemath_math_Function
 * Method:    jniRelease
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_simplemath_math_Function_jniRelease(JNIEnv *env, jobject thiz, jlong address) {
	jclass functionClass = env->GetObjectClass(thiz);
	jfieldID nativeAddrField = env->GetFieldID( functionClass, "jniAddress", "J");
	jlong nativeAddress = env->GetLongField( thiz, nativeAddrField);
	NMathData *data;
	if(nativeAddress > 0){
		data = (NMathData*)nativeAddress;
		data->f.release();
		delete data;
	}
	env->SetLongField( thiz, nativeAddrField, 0);
}
