#include "nautilus_lab_graphics_Camera3D.h"
#include <camera.h>
#include <nmath/nmath.h>

using namespace fp;

JNIEXPORT jlong JNICALL initCamera(JNIEnv *env, jobject thisz) {
	fp::Camera *c;
	c = new fp::Camera();
	return ((jlong)c);
}

JNIEXPORT void JNICALL lookAt(JNIEnv *env, jobject thiz, jlong address, jfloat ex, jfloat ey, jfloat ez,
		jfloat cx, jfloat cy, jfloat cz, jfloat ux, jfloat uy, jfloat uz) {
	fp::Camera *c = (fp::Camera*)address;
	c->lookAt(ex, ey, ez, cx, cy, cz, ux, uy, uz);
}

JNIEXPORT void JNICALL perspective(JNIEnv *env, jobject thiz, jlong address, jint l, jint r, jint t, jint b,
			jfloat fov, jfloat znear, jfloat zfar) {
	fp::Camera *c = (fp::Camera*)address;
	c->setViewport(l, t, r, b);
	c->setPerspective(fov, znear, zfar);
}

JNIEXPORT void JNICALL project(JNIEnv *env, jobject thiz, jlong address, jfloatArray out,
		jfloat objX, jfloat objY, jfloat objZ) {
	static float tmp[3];
	fp::Camera *c = (fp::Camera*)address;
	c->project(tmp, objX, objY, objZ);
//#ifdef _DEBUG
//	LOGI(2, "After project %f %f %f", tmp[0], tmp[1], tmp[2]);
//#endif
	env->SetFloatArrayRegion(out, 0, 2, tmp);
}

JNIEXPORT void JNICALL projectOrthor(JNIEnv *env, jobject thiz, jlong address, jfloatArray out,
		jfloat objX, jfloat objY, jfloat objZ) {
	static float tmp[3];
	fp::Camera *c = (fp::Camera*)address;
	c->projectOrthor(tmp, objX, objY, objZ);
	env->SetFloatArrayRegion(out, 0, 3, tmp);
}

JNIEXPORT void JNICALL rotate(JNIEnv *env, jobject thiz, jlong address, jfloat yawR, jfloat pitchR, jfloat roll) {
	fp::Camera *c = (fp::Camera*)address;
	c->rotate(yawR, pitchR, roll);
}

JNIEXPORT void JNICALL jniReleaseCam(JNIEnv *env, jobject thiz, jlong address) {
	jclass cameraClass = env->GetObjectClass(thiz);
	jfieldID nativeAddrField = env->GetFieldID( cameraClass, "nativeCamera", "J");
	fp::Camera *c;
	if(address > 0){
		c = (fp::Camera*)address;
		delete c;
	}
	env->SetLongField( thiz, nativeAddrField, 0);
}

JNIEXPORT void JNICALL moveAlongForward(JNIEnv *env, jobject thiz, jlong address, jfloat distance) {
	fp::Camera *c = (fp::Camera*)address;
	c->moveAlongForward(distance);
}
