package nautilus.lab.jogl;

public class LightSource {
    private final float[] position = {1, 1, 1};
    private final float[] color = {1, 1, 1};
    private float diffuse;
    private float ambient;
    private float specular;

    public float[] getPosition() {
        return position;
    }

    public void setPosition(float[] position) {
        this.position[0] = position[0];
        this.position[1] = position[1];
        this.position[2] = position[2];
    }

    public float[] getColor() {
        return color;
    }

    public void setColor(float[] color) {
        this.color[0] = color[0];
        this.color[1] = color[1];
        this.color[2] = color[2];
    }

    public float getDiffuse() {
        return diffuse;
    }

    public void setDiffuse(float diffuse) {
        this.diffuse = diffuse;
    }

    public float getAmbient() {
        return ambient;
    }

    public void setAmbient(float ambient) {
        this.ambient = ambient;
    }

    public float getSpecular() {
        return specular;
    }

    public void setSpecular(float specular) {
        this.specular = specular;
    }
}
