package nautilus.lab.component;

public interface IControlListener{
	public void onFrustumChangeCmd(double x, double y, double z);
	public void onViewChangeCmd(double ex, double ey, double ez,
			double targetX, double targetY, double targetZ,
			double upX, double upY, double upZ,
			double neaf, double far);
}