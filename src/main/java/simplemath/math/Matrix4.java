package simplemath.math;

/**
 * This matrix use column-major order
 * @author vdao5
 *
 */
public class Matrix4 {
	
	public void identity(float[] data) {
		//column 0
		data[0] = 1;
		data[1] = 0;
		data[2] = 0;
		data[3] = 0;

		//column 1
		data[4] = 0;
		data[5] = 1;
		data[6] = 0;
		data[7] = 0;

		//column 2
		data[8] = 0;
		data[9] = 0;
		data[10] = 1;
		data[11] = 0;

		//column 3
		data[12] = 0;
		data[13] = 0;
		data[14] = 0;
		data[15] = 1;
	}
	
	public void multiplyMM(float[] result, float[] m1, float[] m2) {
		result[0] = m1[0] * m2[0] + m1[4] * m2[1] + m1[8] * m2[2] + m1[12] * m2[3];
		result[1] = m1[1] * m2[0] + m1[5] * m2[1] + m1[9] * m2[2] + m1[13] * m2[3];
		result[2] = m1[2] * m2[0] + m1[6] * m2[1] + m1[10] * m2[2] + m1[14] * m2[3];
		result[3] = m1[3] * m2[0] + m1[7] * m2[1] + m1[11] * m2[2] + m1[15] * m2[3];

		result[4] = m1[0] * m2[4] + m1[4] * m2[5] + m1[8] * m2[6] + m1[12] * m2[7];
		result[5] = m1[1] * m2[4] + m1[5] * m2[5] + m1[9] * m2[6] + m1[13] * m2[7];
		result[6] = m1[2] * m2[4] + m1[6] * m2[5] + m1[10] * m2[6] + m1[14] * m2[7];
		result[7] = m1[3] * m2[4] + m1[7] * m2[5] + m1[11] * m2[6] + m1[15] * m2[7];

		result[8] = m1[0] * m2[8] + m1[4] * m2[9] + m1[8] * m2[10] + m1[12] * m2[11];
		result[9] = m1[1] * m2[8] + m1[5] * m2[9] + m1[9] * m2[10] + m1[13] * m2[11];
		result[10] = m1[2] * m2[8] + m1[6] * m2[9] + m1[10] * m2[10] + m1[14] * m2[11];
		result[11] = m1[3] * m2[8] + m1[7] * m2[9] + m1[11] * m2[10] + m1[15] * m2[11];

		result[12] = m1[0] * m2[12] + m1[4] * m2[13] + m1[8] * m2[14] + m1[12] * m2[15];
		result[13] = m1[1] * m2[12] + m1[5] * m2[13] + m1[9] * m2[14] + m1[13] * m2[15];
		result[14] = m1[2] * m2[12] + m1[6] * m2[13] + m1[10] * m2[14] + m1[14] * m2[15];
		result[15] = m1[3] * m2[12] + m1[7] * m2[13] + m1[11] * m2[14] + m1[15] * m2[15];
	}
	
	public void multiplyMV(float[] result, float[] m, float[] v) {
		result[0] = m[0] * v[0] + m[4] * v[1] + m[8] * v[2] + m[12] * v[3];
		result[1] = m[1] * v[0] + m[5] * v[1] + m[9] * v[2] + m[13] * v[3];
		result[2] = m[2] * v[0] + m[6] * v[1] + m[10] * v[2] + m[14] * v[3];
		result[3] = m[3] * v[0] + m[7] * v[1] + m[11] * v[2] + m[15] * v[3];
		
	}
	
	public void transpose(float[] result, float[] m) {
		
	}
	
	public boolean invertM(float[] invOut, float[] data) {
		float det;
		float[] inv = new float[16];
		int i;
		//T factor2233 = data[2][2] * data[3][3];
		//T factor1123 = data[1][1] * data[2][3];

		/*
			m[5]	m[9]	m[13]
			m[6]	m[10]	m[14]
			m[7]	m[11]	m[15]
		*/
		inv[0] = data[5] * data[10] * data[15] + data[9] * data[14] * data[7] + data[13] * data[6] * data[11]
					- data[7] * data[10] * data[13] - data[11] * data[14] * data[5] - data[15] * data[6] * data[9];

		/*
			m[4]	m[8]	m[12]
			m[6]	m[10]	m[14]
			m[7]	m[11]	m[15]
		*/
		inv[1] = -(data[4] * data[10] * data[15] + data[8] * data[14] * data[7] + data[12] * data[6] * data[11] 
					- data[7] * data[10] * data[12] - data[11] * data[14] * data[4] - data[15] * data[6] * data[8]);

		/*
			m[4]	m[8]	m[12]
			m[5]	m[9]	m[13]
			m[7]	m[11]	m[15]
		*/
		inv[2] = data[4] * data[9] * data[15] + data[8] * data[13] * data[7] + data[12] * data[5] * data[11] 
					- data[7] * data[9] * data[12] - data[11] * data[13] * data[4] - data[15] * data[5] * data[8];

		/*
			m[4]	m[8]	m[12]
			m[5]	m[9]	m[13]
			m[6]	m[10]	m[14]
		*/
		inv[3] = -(data[4] * data[9] * data[14] + data[8] * data[13] * data[6] + data[12] * data[5] * data[10] 
					- data[6] * data[9] * data[12] - data[10] * data[13] * data[4] - data[14] * data[5] * data[8]);

		/*
			m[1]	m[9]	m[13]
			m[2]	m[10]	m[14]
			m[3]	m[11]	m[15]
		*/
		inv[4] = -(data[1] * data[10] * data[15] + data[9] * data[14] * data[3] + data[13] * data[2] * data[11] 
					- data[3] * data[10] * data[13] - data[11] * data[14] * data[1] - data[15] * data[2] * data[9]);

		/*
			m[0]	m[8]	m[12]
			m[2]	m[10]	m[14]
			m[3]	m[11]	m[15]
		*/
		inv[5] = data[0] * data[10] * data[15] + data[8] * data[14] * data[3] + data[12] * data[2] * data[11] 
					- data[3] * data[10] * data[12] - data[11] * data[14] * data[0] - data[15] * data[2] * data[8];

		/*
			m[0]	m[8]	m[12]
			m[1]	m[9]	m[13]
			m[3]	m[11]	m[15]
		*/
		inv[6] = -(data[0] * data[9] * data[15] + data[8] * data[13] * data[3] + data[12] * data[1] * data[11] 
					- data[3] * data[9] * data[12] - data[11] * data[13] * data[0] - data[15] * data[1] * data[8]);

		/*
			m[0]	m[8]	m[12]
			m[1]	m[9]	m[13]
			m[2]	m[10]	m[14]
		*/
		inv[7] = data[0] * data[9] * data[14] + data[8] * data[13] * data[2] + data[12] * data[1] * data[10] 
					- data[2] * data[9] * data[12] - data[10] * data[13] * data[0] - data[14] * data[1] * data[8];

		/*
			m[1]	m[5]	m[13]
			m[2]	m[6]	m[14]
			m[3]	m[7]	m[15]
		*/
		inv[8] = data[1]  * data[6] * data[15] + data[5]  * data[14] * data[3] + data[13]  * data[2] * data[7] 
					- data[3]  * data[6] * data[13] - data[7] * data[14] * data[1] - data[15] * data[2] * data[5];

		/*
			m[0]	m[4]	m[12]
			m[2]	m[6]	m[14]
			m[3]	m[7]	m[15]
		*/
		inv[9] = -(data[0] * data[6] * data[15] + data[4] * data[14] * data[3] + data[12] * data[2] * data[7] 
					- data[3] * data[6] * data[12] - data[7] * data[14] * data[0] - data[15] * data[2] * data[4]);

		/*
			m[0]	m[4]	m[12]
			m[1]	m[5]	m[13]
			m[3]	m[7]	m[15]
		*/
		inv[10] = data[0]  * data[5] * data[15] + data[4]  * data[13] * data[3] + data[12]  * data[1] * data[7] 
					- data[3]  * data[5] * data[12] - data[7] * data[13] * data[0] - data[15] * data[1] * data[4];

		/*
			m[0]	m[4]	m[12]
			m[1]	m[5]	m[13]
			m[2]	m[6]	m[14]
		*/
		inv[11] = -(data[0] * data[5] * data[14] + data[4] * data[13] * data[2] + data[12] * data[1] * data[6] 
					- data[2] * data[5] * data[12] - data[6] * data[13] * data[0] - data[14] * data[1] * data[4]);

		/*
			m[1]	m[5]	m[9]
			m[2]	m[6]	m[10]
			m[3]	m[7]	m[11]
		*/
		inv[12] = -(data[1] * data[6] * data[11] + data[5] * data[10] * data[3] + data[9] * data[2] * data[7] 
					- data[3] * data[6] * data[9] - data[7] * data[10] * data[1] - data[11] * data[2] * data[5]);

		/*
			m[0]	m[4]	m[8]
			m[2]	m[6]	m[10]
			m[3]	m[7]	m[11]
		*/
		inv[13] = data[0] * data[6] * data[11] + data[4] * data[10] * data[3] + data[8] * data[2] * data[7] 
					- data[3] * data[6] * data[8] - data[7] * data[10] * data[0] - data[11] * data[2] * data[4];

		/*
			m[0]	m[4]	m[8]
			m[1]	m[5]	m[9]
			m[3]	m[7]	m[11]
		*/
		inv[14] = -(data[0] * data[5] * data[11] + data[4] * data[9] * data[3] + data[8] * data[1] * data[7] 
					- data[3] * data[5] * data[8] - data[7] * data[9] * data[0] - data[11] * data[1] * data[4]);

		/*
			m[0]	m[4]	m[8]
			m[1]	m[5]	m[9]
			m[2]	m[6]	m[10]
		*/
		inv[15] = data[0] * data[5] * data[10] + data[4] * data[9] * data[2] + data[8] * data[1] * data[6] 
					- data[2] * data[5] * data[8] - data[6] * data[9] * data[0] - data[10] * data[1] * data[4];

		det = data[0] * inv[0] + data[1] * inv[1] + data[2] * inv[2] + data[3] * inv[3];

		if (det == 0)
			return false;

		det = 1.0f / det;

		int j;
		for (i = 0; i < 4; i++) {
			j = i * 4;
			invOut[j+0] = inv[j] * det;
			invOut[j+1] = inv[j+1] * det;
			invOut[j+2] = inv[j+2] * det;
			invOut[j+3] = inv[j+3] * det;
		}
		
		return true;
	}
	
	public void rotateM(float[] m, float angleR, float rotx, float roty, float rotz) {
//		Vec3<T> axis, temp;
		float[] Rotate = new float[16];
		float[] Result = new float[16];
		
		float c = (float)Math.cos(angleR);
		float s = (float)Math.sin(angleR);

		//normalize the rotation axis
		float mag = (float)Math.sqrt(rotx*rotx + roty*roty + rotz*rotz);
		float[] axis = {rotx/mag, roty/mag, rotz/mag};
		float[]	temp = {axis[0] * (1.0f - c), axis[1] * (1.0f - c), axis[2] * (1.0f - c)};
		Rotate[0] = c + temp[0] * axis[0];
		Rotate[1] = temp[0] * axis[1] + s * axis[2];
		Rotate[2] = temp[0] * axis[2] - s * axis[1];

		Rotate[4] = temp[1] * axis[0] - s * axis[2];
		Rotate[5] = c + temp[1] * axis[1];
		Rotate[6] = temp[1] * axis[2] + s * axis[0];

		Rotate[8] = temp[2] * axis[0] + s * axis[1];
		Rotate[9] = temp[2] * axis[1] - s * axis[0];
		Rotate[10] = c + temp[2] * axis[2];

		//column 0 = column m[0] * rotate[0]
		Result[0] = m[0] * Rotate[0] + m[4] * Rotate[4] + m[8] * Rotate[8];
		Result[1] = m[1] * Rotate[0] + m[5] * Rotate[4] + m[9] * Rotate[8];
		Result[2] = m[2] * Rotate[0] + m[6] * Rotate[4] + m[10] * Rotate[8];
		Result[3] = m[3] * Rotate[0] + m[7] * Rotate[4] + m[11] * Rotate[8];
		
		Result[4] = m[0] * Rotate[4] + m[1] * Rotate[5] + m[2] * Rotate[6];
		Result[5] = m[0] * Rotate[4] + m[1] * Rotate[5] + m[2] * Rotate[6];
		Result[6] = m[0] * Rotate[4] + m[1] * Rotate[5] + m[2] * Rotate[6];
		Result[7] = m[0] * Rotate[4] + m[1] * Rotate[5] + m[2] * Rotate[6];
		
		Result[8] = m[0] * Rotate[8] + m[1] * Rotate[9] + m[2] * Rotate[10];
		Result[9] = m[0] * Rotate[8] + m[1] * Rotate[9] + m[2] * Rotate[10];
		Result[10] = m[0] * Rotate[8] + m[1] * Rotate[9] + m[2] * Rotate[10];
		Result[11] = m[0] * Rotate[8] + m[1] * Rotate[9] + m[2] * Rotate[10];
		
		//Result[3] = m[3];
		
		m[0] = Result[0];
		m[1] = Result[1];
		m[2] = Result[2];
		m[3] = Result[3];
		
		m[4] = Result[4];
		m[5] = Result[5];
		m[6] = Result[6];
		m[7] = Result[7];
		
		m[8] = Result[8];
		m[9] = Result[9];
		m[10] = Result[10];
		m[11] = Result[11];
	}
}
