package nautilus.game.model;

import simplemath.math.Matrix4;

/**
 * @deprecated Please use camera API from OpenGL and GLM instead
 */
public class DefaultCamera {

    private float eyeX;
    private float eyeY;
    private float eyeZ;
    private float centerX;
    private float centerY;
    private float centerZ;

    private final Matrix4 matrixUtil = new Matrix4();
    private final float[] mPerspectiveMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    public void initialize() {
        matrixUtil.identity(mViewMatrix);
        matrixUtil.identity(mPerspectiveMatrix);
    }

    public float[] getViewMatrix() {
        return mViewMatrix;
    }

    public float[] getPerspective() {
        return mPerspectiveMatrix;
    }

    public void lookAt(float ex, float ey, float ez,
                float cx, float cy, float cz,
                float ux, float uy, float uz) {
        float[] f = {0, 0, 0};
        float[] cr = {0, 0, 0};
        float[] s = {0, 0, 0};
        float[] u = {0, 0, 0};
        matrixUtil.normalize(f, cx-ex, cy-ey, cz-ez);
        float[] up = {ux, uy, uz};
        matrixUtil.cross(cr, f, up);
        matrixUtil.normalize(s, cr[0], cr[1], cr[2]);
        matrixUtil.cross(u, s, f);
        float[] eye = {ex, ey, ez};

        centerX = cx;
        centerY = cy;
        centerZ = cz;

        eyeX = ex;
        eyeY = ey;
        eyeZ = ez;

        mViewMatrix[0] = 1;
        mViewMatrix[5] = 1;
        mViewMatrix[10] = 1;
        mViewMatrix[15] = 1;

        mViewMatrix[3] = 0;
        mViewMatrix[7] = 0;
        mViewMatrix[11] = 0;

        mViewMatrix[0] = s[0]; //s.x
        mViewMatrix[4] = s[1]; //s.y
        mViewMatrix[8] = s[2]; //s.z
        mViewMatrix[1] = u[0];
        mViewMatrix[5] = u[1];
        mViewMatrix[9] = u[2];
        mViewMatrix[2] =-f[0];
        mViewMatrix[6] =-f[1];
        mViewMatrix[10] =-f[2];
        mViewMatrix[12] =-matrixUtil.dot(s, eye);
        mViewMatrix[13] =-matrixUtil.dot(u, eye);
        mViewMatrix[14] = matrixUtil.dot(f, eye);
    }

    public void setPerspectiveMatrix(float near, float far, float w, float h) {

        float range = near - far;
        float aspect = (w)/h;
        float fovy = (float)Math.tan(0.5 * (Math.PI - Math.toRadians(40)));

        mPerspectiveMatrix[0] = fovy / aspect;
        mPerspectiveMatrix[1] = 0;
        mPerspectiveMatrix[2] = 0;
        mPerspectiveMatrix[3] = 0;

        mPerspectiveMatrix[4] = 0;
        mPerspectiveMatrix[5] = fovy;
        mPerspectiveMatrix[6] = 0;
        mPerspectiveMatrix[7] = 0;

        mPerspectiveMatrix[8] = 0;
        mPerspectiveMatrix[9] = 0;
        mPerspectiveMatrix[10] = far / range;
        mPerspectiveMatrix[11] = -1;

        mPerspectiveMatrix[12] = 0;
        mPerspectiveMatrix[13] = 0;
        mPerspectiveMatrix[14] = near * far / range;
        mPerspectiveMatrix[15] = 0;
    }

}
