package nautilus.game.model;

public abstract class MovingObject extends GameObject implements Runnable {
    protected float x;
    protected float y;
    protected float z;

    protected float vX;
    protected float vY;
    protected float vZ;

    public void setVelocity(float vx, float vy, float vz) {
        this.vX = vx;
        this.vY = vy;
        this.vZ = vz;
    }

    public abstract void start();
    public abstract void stop();
}
