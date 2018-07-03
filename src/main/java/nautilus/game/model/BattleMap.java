package nautilus.game.model;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.*;

public class BattleMap {

    private static final int TILE_SIZE_PIXEL = 32;
    private static final int NUM_OF_TILE = 15;

    /**
     * This is size of the map image in pixel
     * */
    private int height;
    private int width;

    private final BufferedImage[] mapTile = new BufferedImage[NUM_OF_TILE];
    private int[][] mapData;
    private BufferedImage mapBitmap = null;

    /**
     * Each vertex have data:
     * x, y, z, u, v so we have 5 floats each
     * we have 4 vertices
     * in total we have: 4 * 5 = 20 floats
     * */
    private final float[] vertexData = {
            -3.5f, -1f, 3f, 0f, 0f,    -3.5f, -1f, -3f, 0f, 1f,
            3.5f, -1f, -3f, 1f, 1f,    3.5f, -1f, 3f, 1f, 0f,
    };
    private short[] mIndice = {0, 2, 1, 3};
    private float y_offset;
    private float mapHeight;
    private float mapWidth;

    public BattleMap() {
    }

    /**
     *
     *
     * */
    public BattleMap(int w, int h, int[][] data) {
        mapData = data;
        int row = data.length;
        int col = data[0].length;

        loadMap();

        width = col * mapTile[0].getWidth();
        height = row * mapTile[0].getHeight();
        mapBitmap = new BufferedImage(width, height, mapTile[0].getType());
        Graphics2D g2d = mapBitmap.createGraphics();
        int x, y;
        for (int i = 0; i < row; ++i) {
            y = i * TILE_SIZE_PIXEL;
            for(int j=0; j<col; ++j) {
                x = j * TILE_SIZE_PIXEL;
                g2d.drawImage(mapTile[mapData[i][j]], x, y, null);
            }
        }

        //for testing purpose
//        try {
//            ImageIO.write(mapBitmap, "png", new File("D:\\mapBitmap.png"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void load(int[][] data, int w, int h) {
        mapData = data;
        height = h;
        width = w;
    }

    private void loadMap() {
        String imgPath = "D:\\projects\\demo\\NautilusGraphicLab\\images\\";
        try {
            for (int i = 1; i <= NUM_OF_TILE; i++) {
                mapTile[i-1] = ImageIO.read(new File(imgPath + "mapTile" + i + ".png"));
            }
        }catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    private int mPositionHandler = 0;
    private int mTextureHandler = 0;
    private int[] buffers = new int[2];
    private final int[] mTextureId = new int[1];
    private final int[] mSampler = new int[1];
    private final int STRIDE = 20; //5 * 4;
    private TextureData textureData;
    private Texture texture;
    private int sampleLoc;

    public void setHandlers(int posHandler, int textureHandler) {
        mPositionHandler = posHandler;
        mTextureHandler = textureHandler;
    }

    public void initialize(GL3 gl3, int texLoc) {
        sampleLoc = texLoc;
        //
        gl3.glGenBuffers(2, buffers, 0);

        //init vertex buffer
        int size = vertexData.length * Buffers.SIZEOF_FLOAT; //BYTES_PER_FLOAT
        FloatBuffer floatBuffer = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder()).asFloatBuffer();
        floatBuffer.put(vertexData);
        floatBuffer.position(0);
        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, buffers[0]);
        gl3.glBufferData(GL3.GL_ARRAY_BUFFER, size, floatBuffer, GL3.GL_STATIC_DRAW);
        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);

        //init index buffer
        size = mIndice.length * Buffers.SIZEOF_SHORT; //BYTES_PER_SHORT = 2
        ShortBuffer indexBuffer = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder()).asShortBuffer();
        indexBuffer.put(mIndice);
        indexBuffer.position(0);
        gl3.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, buffers[1]);
        gl3.glBufferData(GL3.GL_ELEMENT_ARRAY_BUFFER, size, indexBuffer, GL3.GL_STATIC_DRAW);
        gl3.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, 0);

        gl3.glPixelStorei ( GL3.GL_UNPACK_ALIGNMENT, 1 );
        gl3.glGenTextures ( 1, mTextureId, 0 );
        gl3.glBindTexture ( GL3.GL_TEXTURE_2D, mTextureId[0] );
        gl3.glGenSamplers(1, mSampler, 0);

        //Set the filtering mode
//        gl3.glTexParameteri( GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_MIN_FILTER, GL3.GL_NEAREST );
//        gl3.glTexParameteri( GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_MAG_FILTER, GL3.GL_NEAREST );
//        //gl3.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_WRAP_S, GL3.GL_TEXTURE);
//        //gl3.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_WRAP_T, GL3.GL_TEXTURE);
//        gl3.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_WRAP_S, GL3.GL_CLAMP_TO_EDGE);
//        gl3.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_WRAP_T, GL3.GL_CLAMP_TO_EDGE);

        textureData = AWTTextureIO.newTextureData(gl3.getGLProfile(), this.mapBitmap, false);
        texture = TextureIO.newTexture(textureData);
        texture.setTexParameteri(gl3, GL3.GL_TEXTURE_MIN_FILTER, GL3.GL_NEAREST );
        texture.setTexParameteri(gl3, GL3.GL_TEXTURE_MAG_FILTER, GL3.GL_NEAREST );
        texture.setTexParameteri(gl3, GL3.GL_TEXTURE_WRAP_S, GL3.GL_CLAMP_TO_EDGE );
        texture.setTexParameteri(gl3, GL3.GL_TEXTURE_WRAP_T, GL3.GL_CLAMP_TO_EDGE );


    }

    public void render(GL3 gl3) {

        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, buffers[0]);
        gl3.glEnableVertexAttribArray(mPositionHandler);
        gl3.glVertexAttribPointer(mPositionHandler, 3, GL3.GL_FLOAT, false, STRIDE, 0);
        int textureOffset = 12;
        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, buffers[0]);
        gl3.glEnableVertexAttribArray(mTextureHandler);
        gl3.glVertexAttribPointer(mTextureHandler, 2, GL3.GL_FLOAT, false, STRIDE, textureOffset);
        gl3.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, buffers[1]);

        gl3.glActiveTexture(GL3.GL_TEXTURE0);
        gl3.glBindTexture(GL3.GL_TEXTURE_2D, mTextureId[0]);
        texture.enable(gl3);
        texture.bind(gl3);
        gl3.glUniform1i(sampleLoc, 0);

        gl3.glDrawElements(GL3.GL_TRIANGLE_STRIP, mIndice.length, GL3.GL_UNSIGNED_SHORT, 0); // 2 triangles * 3 each



        texture.disable(gl3);
        gl3.glBindBuffer(GL3.GL_ELEMENT_ARRAY_BUFFER, 0);
        gl3.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
        gl3.glDisableVertexAttribArray(mPositionHandler);
        gl3.glDisableVertexAttribArray(mTextureHandler);
    }
}
