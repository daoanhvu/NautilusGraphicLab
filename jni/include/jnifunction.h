#ifndef _NMATH_FP_NFUNCTION
#define _NMATH_FP_NFUNCTION

#include <cstdlib>
#include <cstring>
#include <vector>
#include <jni.h>
#include <cmath>
#include "nmath_pool.hpp"
#include "imagedata.hpp"

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jlong JNICALL
Java_simplemath_math_Function_jniInit(JNIEnv *env, jobject thiz);

JNIEXPORT void JNICALL
Java_simplemath_math_Function_jniParse(JNIEnv *env, jobject thiz);

JNIEXPORT jint JNICALL
Java_simplemath_math_Function_jniSetText(JNIEnv *env, jobject thiz, jlong address, jstring text);

JNIEXPORT jint JNICALL
Java_simplemath_math_Function_jniCalc(JNIEnv *env, jobject thiz, jlong address, jfloatArray varValues, jobject ret);

JNIEXPORT jint JNICALL Java_simplemath_math_Function_jniGetSpace(JNIEnv *env, jobject thiz, jlong nativeAddress,
        jfloatArray boundaries, jfloat epsilon,
        jboolean isNormal, jobjectArray resultSpaces);

JNIEXPORT jint JNICALL Java_simplemath_math_Function_jniGetDerivative(JNIEnv *env, jobject thiz, jlong nativeAddress);

JNIEXPORT void JNICALL Java_simplemath_math_Function_jniRelease(JNIEnv *env, jobject thiz, jlong nativeAddress);

JNIEXPORT jlong JNICALL Java_simplemath_math_Function_jniPostfixNativeAddress(JNIEnv *env, jobject thiz, jlong nativeAddress);

#ifdef __cplusplus
}
#endif

#endif