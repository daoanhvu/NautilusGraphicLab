package nautilus.lab.jogl;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Coordinator3D {
    private float[] axesVertices = new float[]{
            0, 0, 0, 0, 1, 0, 1, 0, 0, 1,	1, 0, 0, 0, 1, 0, 1, 0, 0, 1,
            0, 0, 0, 1, 0, 0, 0, 1, 0, 1,	0, 1, 0, 1, 0, 0, 0, 1, 0, 1,
            0, 0, 0, 0, 1, 0, 0, 0, 1, 1,	0, 0, 1, 0, 1, 0, 0, 0, 1, 1
    };
    private int[] buffers = new int[1];
    private final int STRIDE = 10 * Buffers.SIZEOF_FLOAT;

    public void initialize(GL3 gl3) {
        gl3.glGenBuffers(1, buffers, 0);
        //init vertex buffer
        int size = axesVertices.length * Buffers.SIZEOF_FLOAT; //BYTES_PER_FLOAT
        FloatBuffer floatBuffer = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder()).asFloatBuffer();
        floatBuffer.put(axesVertices);
        floatBuffer.position(0);
        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, buffers[0]);
        gl3.glBufferData(GL3.GL_ARRAY_BUFFER, size, floatBuffer, GL3.GL_STATIC_DRAW);
        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
    }

    public void render(GL3 gl3,
                       int positionHandler,
                       int normalHandler,
                       int colorHandler) {

        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, buffers[0]);
        gl3.glEnableVertexAttribArray(positionHandler);
        gl3.glVertexAttribPointer(positionHandler, 3, GL3.GL_FLOAT, false, STRIDE, 0);

        gl3.glEnableVertexAttribArray(normalHandler);
        gl3.glVertexAttribPointer(normalHandler, 3, GL3.GL_FLOAT, false, STRIDE, 12);

        gl3.glEnableVertexAttribArray(colorHandler);
        gl3.glVertexAttribPointer(colorHandler, 4, GL3.GL_FLOAT, false, STRIDE, 24);

        gl3.glDrawArrays(GL3.GL_LINES, 0, axesVertices.length);

        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
    }
}