package nautilus.lab.component;

public interface CommandListener {
	void onRotateCommand(double theta, double rvx, double rvy, double rvz);
	void onRotateCommand(double theta, double px, double py, double pz,
			double rvx, double rvy, double rvz);
	void onNFunctionChange(String strFunct, float[] boundaries);
	void onAddVertexCommand(double vx, double vy, double vz);
	void onAddLineCommand();
}
