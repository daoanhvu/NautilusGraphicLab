#include "jnicamera.h"
#include <camera.h>

#ifdef _DEBUG
#include <iostream>
using namespace std;
#endif

using namespace fp;

JNIEXPORT jlong JNICALL Java_nautilus_lab_graphics_Camera3D_initCamera(JNIEnv *env, jobject thisz) {
	fp::Camera *c;
	c = new fp::Camera();
	return ((jlong)c);
}

JNIEXPORT void JNICALL Java_nautilus_lab_graphics_Camera3D_lookAt__JFFFFFFFFF(JNIEnv *env, jobject thiz, jlong address, jfloat ex, jfloat ey, jfloat ez,
		jfloat cx, jfloat cy, jfloat cz, jfloat ux, jfloat uy, jfloat uz) {
	fp::Camera *c = (fp::Camera*)address;
	c->lookAt(ex, ey, ez, cx, cy, cz, ux, uy, uz);
}

/**
 * Class:     nautilus_lab_graphics_Camera3D
 * Method:    lookAt
 * Signature: (JFFFFFFFFF[F)V
 */
JNIEXPORT void JNICALL Java_nautilus_lab_graphics_Camera3D_lookAt__JFFFFFFFFF_3F
  (JNIEnv *env, jobject thiz, jlong address, jfloat ex, jfloat ey, jfloat ez,
		  jfloat cx, jfloat cy, jfloat cz,
		  jfloat ux, jfloat uy, jfloat uz, jfloatArray view /* OUT */) {
	jfloat arrView[16];
	fp::Camera *c = (fp::Camera*)address;
	c->lookAt(ex, ey, ez, cx, cy, cz, ux, uy, uz);
	c->getView(arrView);
	env->SetFloatArrayRegion(view, 0, 16, arrView);
}

/*
 * Class:     nautilus_lab_graphics_Camera3D
 * Method:    lookAt
 * Signature: (J[F[F[F[F)V
 */
JNIEXPORT void JNICALL Java_nautilus_lab_graphics_Camera3D_lookAt__J_3F_3F_3F_3F(JNIEnv *env, jobject thiz,
		jlong address, jfloatArray eye, jfloatArray center, jfloatArray up, jfloatArray view) {
	fp::Camera *c = (fp::Camera*)address;

	jfloat *pEye = env->GetFloatArrayElements(eye, 0);
	jfloat *pCenter = env->GetFloatArrayElements(center, 0);
	jfloat *pUp = env->GetFloatArrayElements(up, 0);
	jfloat arrView[16];

	c->lookAt(pEye[0], pEye[1], pEye[2], pCenter[0], pCenter[1], pCenter[2], pUp[0], pUp[1], pUp[2]);
	c->getView(arrView);

	env->ReleaseFloatArrayElements(eye, pEye, 0);
	env->ReleaseFloatArrayElements(center, pCenter, 0);
	env->ReleaseFloatArrayElements(up, pUp, 0);
	env->SetFloatArrayRegion(view, 0, 16, arrView);
}


JNIEXPORT void JNICALL Java_nautilus_lab_graphics_Camera3D_perspective(JNIEnv *env, jobject thiz, jlong address, jint l, jint r, jint t, jint b,
			jfloat fov, jfloat znear, jfloat zfar) {
	fp::Camera *c = (fp::Camera*)address;
	c->setViewport(l, t, r, b);
	c->setPerspective(fov, znear, zfar);
}

JNIEXPORT void JNICALL Java_nautilus_lab_graphics_Camera3D_project(JNIEnv *env, jobject thiz, jlong address, jfloatArray out,
		jfloat objX, jfloat objY, jfloat objZ) {
	static float tmp[3];
	fp::Camera *c = (fp::Camera*)address;
	c->project(tmp, objX, objY, objZ);
#ifdef _DEBUG
	cout << tmp[0] << ", " << tmp[1] << endl;
#endif
//#ifdef _DEBUG
//	LOGI(2, "After project %f %f %f", tmp[0], tmp[1], tmp[2]);
//#endif
	env->SetFloatArrayRegion(out, 0, 2, tmp);
}

JNIEXPORT void JNICALL Java_nautilus_lab_graphics_Camera3D_projectOrthor(JNIEnv *env, jobject thiz, jlong address, jfloatArray out,
		jfloat objX, jfloat objY, jfloat objZ) {
	static float tmp[3];
	fp::Camera *c = (fp::Camera*)address;
	c->projectOrthor(tmp, objX, objY, objZ);
	env->SetFloatArrayRegion(out, 0, 3, tmp);
}

JNIEXPORT void JNICALL Java_nautilus_lab_graphics_Camera3D_rotate(JNIEnv *env, jobject thiz, jlong address, jfloat yawR, jfloat pitchR, jfloat roll) {
	fp::Camera *c = (fp::Camera*)address;
	c->rotate(yawR, pitchR, roll);
}

JNIEXPORT void JNICALL Java_nautilus_lab_graphics_Camera3D_moveAlongForward(JNIEnv *env, jobject thiz, jlong address, jfloat distance) {
	fp::Camera *c = (fp::Camera*)address;
	c->moveAlongForward(distance);
}

JNIEXPORT void JNICALL Java_nautilus_lab_graphics_Camera3D_jniRelease(JNIEnv *env, jobject thiz, jlong address) {
	jclass cameraClass = env->GetObjectClass(thiz);
	jfieldID nativeAddrField = env->GetFieldID( cameraClass, "nativeCamera", "J");
	fp::Camera *c;
	if(address > 0){
		c = (fp::Camera*)address;
		delete c;
	}
	env->SetLongField( thiz, nativeAddrField, 0);
}
