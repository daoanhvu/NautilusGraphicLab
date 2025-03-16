#include "jnifunction.h"
#include "nfunction.hpp"

typedef struct tagNMathData {
  nmath::NFunction<float> f;
  std::vector<nmath::ImageData<float>*> imageData;
  nmath::NLabLexer lexer;
  nmath::NLabParser<float> parser;
  int errorCode;
  int errorColumn;
} NMathData;


/*****************************************************************************************************************/
#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jlong JNICALL
Java_simplemath_math_Function_jniInit(JNIEnv *env, jobject thiz) {
  NMathData *data = new NMathData;
  return ((jlong) data);
}

JNIEXPORT void JNICALL
Java_simplemath_math_Function_jniParse(JNIEnv *env, jobject thiz) {

}

/**
 *
 * @param env
 * @param thiz
 * @param text
 * @return
 */
JNIEXPORT jint JNICALL
Java_simplemath_math_Function_jniSetText(JNIEnv *env, jobject thiz, jlong address, jstring text) {
  auto *data = (NMathData *) address;
  const char *pText = env->GetStringUTFChars(text, nullptr);
  auto len = (unsigned int)strlen(pText);

  // Release all memory allocated before setting new text
  data->f.release();

  data->f.parse(pText, len, &(data->lexer), &(data->parser));
  jint errorCode = (data->f).getErrorCode();
  env->ReleaseStringUTFChars(text, pText);
  return errorCode;
}

JNIEXPORT jint JNICALL
Java_simplemath_math_Function_jniCalc(JNIEnv *env, jobject thiz, jlong address, jfloatArray varValues, jobject returnObject) {
  NMathData *data = (NMathData *) address;
  const jfloat *fValues = env->GetFloatArrayElements(varValues, nullptr);
  jint errorCode;
  jfloat value = (data->f).calc(fValues);
  errorCode = data->f.getErrorCode();

  jclass returnClass = env->GetObjectClass(returnObject);
  jfieldID returnValValueFieldID = env->GetFieldID(returnClass, "value", "F");
  jfieldID returnValErrorCodeFieldID = env->GetFieldID(returnClass, "errorCode", "I");

  if (errorCode == NMATH_NO_ERROR) {
    // After calculation, set the return value
    env->SetFloatField(returnObject, returnValValueFieldID, value);
  }
  env->SetIntField(returnObject, returnValErrorCodeFieldID, errorCode);
  return errorCode;
}

/**
 *
 * @param env
 * @param thiz
 * @param nativeAddress
 * @param jtokenArr
 * @param boundaries
 * @param epsilon
 * @param isNormal
 * @param[OUT] resultSpaces
 * @return
 */
JNIEXPORT jint JNICALL Java_simplemath_math_Function_jniGetSpace(JNIEnv *env, jobject thiz,
    jlong nativeDataAddress,
    jfloatArray boundaries,
    jfloat epsilon,
    jboolean isNormal,
    jobjectArray resultSpaces) {

  jfloat *bdarr = env->GetFloatArrayElements(boundaries, nullptr);
  std::vector<nmath::ImageData<float>*> spaces;
  jint errorCode;
  NMathData *data = (NMathData *) nativeDataAddress;
  // Get function's value space, we don't need to normalize the normal vectors so
  // the last parameter is false
  spaces = data->f.getSpace(bdarr, epsilon, isNormal, false);
  errorCode = data->f.getErrorCode();
  if (errorCode == NMATH_NO_ERROR) {
    jclass clsImageData = env->FindClass("simplemath/math/ImageData");
    jmethodID imgDataInitMethod = env->GetMethodID(clsImageData, "<init>", "()V");
    jfieldID dimensionField = env->GetFieldID(clsImageData, "dimension", "I");
    jfieldID imageField = env->GetFieldID(clsImageData, "image", "[F");
    jfieldID imageField = env->GetFieldID(clsImageData, "image", "[F");
    jfieldID normalOffsetField = env->GetFieldID(clsImageData, "normalOffset", "I");

    for (int i=0; i<spaces.size(); i++) {
      jobject spaceObject = env->NewObject(clsImageData, imgDataInitMethod);
      env->SetIntField(spaceObject, dimensionField, spaces[i]->getDimension());
      env->SetIntField(spaceObject, normalOffsetField, spaces[i]->getNormalOffset());
      jfloatArray dataArr = (jfloatArray)env->GetObjectField(spaceObject, imageField);
      env->SetFloatArrayRegion(dataArr, 0, spaces[i]->vertexListSize(), spaces[i]->getData());
    }
  }
  env->ReleaseFloatArrayElements(boundaries, bdarr, 0);
  return errorCode;
}

/**
 * @Return
 * 		native address of derivative
 */
JNIEXPORT jint JNICALL Java_simplemath_math_Function_jniGetDerivative(JNIEnv *env, jobject thiz, jlong nativeAddress) {
  auto data = (NMathData *) nativeAddress;
  return 0;
}

JNIEXPORT void JNICALL Java_simplemath_math_Function_jniRelease(JNIEnv *env, jobject thiz, jlong nativeAddress) {
  NMathData *data;
  if (nativeAddress > 0L) {
    data = (NMathData *) nativeAddress;
    data->f.release();
    delete data;
  }
}

JNIEXPORT jlong JNICALL Java_simplemath_math_Function_jniPostfixNativeAddress(JNIEnv *env, jobject thiz, jlong nativeAddress) {
  NMathData *data = (NMathData *) nativeAddress;
  return (jlong)(data->f.getPrefix(0));
}

#ifdef __cplusplus
}
#endif
