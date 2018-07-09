package nautilus.lab.component;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import nautilus.game.model.DefaultCamera;
import nautilus.lab.jogl.GLShaderProgram;
import nautilus.lab.jogl.LightSource;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene3D extends GLCanvas implements CommandListener {
    /**
     * All vertex shader should have attributes such as
     * position in world, normal vector in world, color 4, and texture
     * */
    protected int positionHandler = 0;
    protected int colorHandler = 1;
    protected int normalHandler = 2;
    protected int textureHandler = 3;

    protected int numOfLight = 0;
    protected boolean useTexture = false;

    //Uniform handlers
    protected int uDifSimpler;
    protected int uUseTextureHandler;
    protected int uNumOfLightHandler;
    protected int uLightPosHandle;
    protected int uViewMatrixHandle;
    protected int uPerspectiveHandler;
    protected int uModelHandler;

    //Tools for building scene
    protected final DefaultCamera camera = new DefaultCamera();
    protected GLShaderProgram mProgramShader;
    protected final List<LightSource> lightSources = new ArrayList<>();

    //Animation control
    protected FPSAnimator animator;

    protected float[] background = {0.2f, 0.2f, 0.2f, 1.0f};

    protected Scene3D(GLCapabilities caps) {
        super(caps);
    }

    public void start() {
        animator.start();
    }

    public void stop() {
        animator.stop();
    }

    public void bindAttributeLocations(GL3 gl3) {
        //IMPORTANT: set position for attribute
        mProgramShader.bindAttribLocation(gl3, positionHandler,"aPosition");
        mProgramShader.bindAttribLocation(gl3, colorHandler, "aColor");
        mProgramShader.bindAttribLocation(gl3, normalHandler, "aNormal");
        mProgramShader.bindAttribLocation(gl3, textureHandler, "aUV");
    }

    public void bindUniformHandlers(GL3 gl3) {
        gl3.glUseProgram(mProgramShader.getProgramId());
        //Now we can get handlers for uniforms
        uDifSimpler = gl3.glGetUniformLocation(mProgramShader.getProgramId(), "difSampler");
        uViewMatrixHandle = gl3.glGetUniformLocation(mProgramShader.getProgramId(), "uViewMatrix");
        uPerspectiveHandler = gl3.glGetUniformLocation(mProgramShader.getProgramId(), "uPerspective");
        uModelHandler = gl3.glGetUniformLocation(mProgramShader.getProgramId(), "uModel");
        uUseTextureHandler = gl3.glGetUniformLocation(mProgramShader.getProgramId(), "useTexture");
        uNumOfLightHandler = gl3.glGetUniformLocation(mProgramShader.getProgramId(), "numLight");
        uLightPosHandle = gl3.glGetUniformLocation(mProgramShader.getProgramId(), "lightPos1");
    }
}
