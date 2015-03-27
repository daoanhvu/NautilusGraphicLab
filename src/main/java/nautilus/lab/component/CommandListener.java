package nautilus.lab.component;

public interface CommandListener {
	public void onRotateCommand(double theta, double rvx, double rvy, double rvz);
	public void onRotateCommand(double theta, double px, double py, double pz, 
			double rvx, double rvy, double rvz);
	public void onNFunctionChange(String strFunct, float[] boundaries);
	public void onAddVertexCommand(double vx, double vy, double vz);
	public void onAddLineCommand();
}
