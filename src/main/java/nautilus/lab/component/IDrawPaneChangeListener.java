package nautilus.lab.component;

public interface IDrawPaneChangeListener{
	public void onDrawPaneChanged(	double ex, double ey, double ez,
											double targetX, double targetY, double targetZ,
											double upX, double upY, double upZ,
											double neaf, double far);
}