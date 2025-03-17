#include <iostream>
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
  NMathData *data = (NMathData *) address;
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
 * @param[OUT] resultSpaces, MUST be an ArrayList<DataImage> object
 * @return
 */
JNIEXPORT jint JNICALL Java_simplemath_math_Function_jniGetSpace(JNIEnv *env, jobject thiz,
    jlong nativeDataAddress,
    jfloatArray boundaries,
    jfloat epsilon,
    jboolean isNormal,
    jobject resultSpaces) {

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
    jfieldID rowsInfoField = env->GetFieldID(clsImageData, "rowsInfo", "[I");
    jfieldID normalOffsetField = env->GetFieldID(clsImageData, "normalOffset", "S");
    jfloatArray dataArr;
    jintArray rowsInfoArr;

    jclass java_util_ArrayList         = static_cast<jclass>(env->NewGlobalRef(env->FindClass("java/util/ArrayList")));
    jmethodID java_util_ArrayList_     = env->GetMethodID(java_util_ArrayList, "<init>", "(I)V");
    jmethodID java_util_ArrayList_size = env->GetMethodID (java_util_ArrayList, "size", "()I");
    jmethodID java_util_ArrayList_get  = env->GetMethodID(java_util_ArrayList, "get", "(I)Ljava/lang/Object;");
    jmethodID java_util_ArrayList_add  = env->GetMethodID(java_util_ArrayList, "add", "(Ljava/lang/Object;)Z");

    for (int i=0; i<spaces.size(); i++) {
      jobject spaceObject = env->NewObject(clsImageData, imgDataInitMethod);
      env->SetIntField(spaceObject, dimensionField, spaces[i]->getDimension());
      env->SetShortField(spaceObject, normalOffsetField, spaces[i]->getNormalOffset());
      std::cout << "[*** JNI DEBUG] Got here 1!\n";
      std::cout << "[*** JNI DEBUG] Vertex list size: " << spaces[i]->vertexListSize() << std::endl;
      jsize dataSize = spaces[i]->vertexListSize();
      dataArr = (jfloatArray)env->NewFloatArray(dataSize);
      if (dataArr == nullptr) {
        std::cout << "[*** JNI DEBUG] dataArr is null!\n";
        return 1100;
      }
      std::cout << "[*** JNI DEBUG] Got here 2!\n";
      env->SetFloatArrayRegion(dataArr, 0, spaces[i]->vertexListSize(), spaces[i]->getData());
      std::cout << "[*** JNI DEBUG] Got here 3!\n";
      env->SetObjectField(spaceObject, imageField, dataArr);
      std::cout << "[*** JNI DEBUG] Got here 4!\n";
      rowsInfoArr = (jintArray)env->NewIntArray(spaces[i]->getRowCount());
      env->SetIntArrayRegion(rowsInfoArr, 0, spaces[i]->getRowCount(), spaces[i]->getRowInfo());
      env->SetObjectField(spaceObject, rowsInfoField, rowsInfoArr);
      std::cout << "[*** JNI DEBUG] Got here 5!\n";
//      env->SetIntArrayRegion(rowsInfoArr, 0, spaces[i]->getRowCount(), spaces[i]->getRowInfo());
//      std::cout << "[*** JNI DEBUG] Got here 7!\n";

      // Add space object to resultSpaces
      env->CallBooleanMethod(resultSpaces, java_util_ArrayList_add, spaceObject);
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
