package nautilus.lab.component;

import java.awt.Panel;
import java.awt.TextField;
import java.awt.Button;
import java.awt.Label;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.GridBagConstraints;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CoordinatorInfoPane extends Panel implements IDrawPaneChangeListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 109L;
	
	private TextField txtCamX, txtCamY, txtCamZ;
	private TextField txtTargetX, txtTargetY, txtTargetZ;
	private TextField txtUpX, txtUpY, txtUpZ;
	private TextField txtL, txtT, txtR, txtB;
	private TextField txtNear, txtFar;
	private Button btnReset;
	private Matrix3DPane matrixPane;
	
	/** One control infor pane just control one drawing pane */
	private IControlListener controlListener = null;
	
	public CoordinatorInfoPane(){
		initComponent();
	}
	
	private void initComponent(){
		GridBagConstraints gbc = new GridBagConstraints();
		setLayout(new GridBagLayout());
		
		Label lb = new Label("Near");
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		add(lb, gbc);
		
		txtNear = new TextField();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		//gbc.weightx = 0.5;
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		add(txtNear, gbc);
		
		lb = new Label("Far");
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		add(lb, gbc);
		
		txtFar = new TextField();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		add(txtFar, gbc);
		
		lb = new Label("L/T/R/B");
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		add(lb, gbc);
		
		Panel pn = initLTRBPane();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		add(pn, gbc);
		
		//eye position, target, up vector
		lb = new Label("Eye ");
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 3;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		add(lb, gbc);
		
		txtCamX = new TextField();
		txtCamY = new TextField();
		txtCamZ = new TextField();
		pn = initTriplePane(FlowLayout.LEFT, txtCamX, txtCamY, txtCamZ);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 4;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		add(pn, gbc);
		
		lb = new Label("Target ");
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 3;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		add(lb, gbc);
		txtTargetX = new TextField();
		txtTargetY = new TextField();
		txtTargetZ = new TextField();
		pn = initTriplePane(FlowLayout.LEFT, txtTargetX, txtTargetY, txtTargetZ);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 4;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		add(pn, gbc);
		
		lb = new Label("Up vector ");
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 3;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		add(lb, gbc);
		txtUpX = new TextField();
		txtUpY = new TextField();
		txtUpZ = new TextField();
		pn = initTriplePane(FlowLayout.LEFT, txtUpX, txtUpY, txtUpZ);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 4;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		add(pn, gbc);
		
		lb = new Label("Projection matrix");
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 6;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		add(lb, gbc);
		
		matrixPane = new Matrix3DPane();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 7;
		gbc.gridy = 0;
		gbc.gridwidth = 4;
		gbc.gridheight = 3;
		add(matrixPane, gbc);
		
		btnReset = new Button("Reset");
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 11;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		add(btnReset, gbc);
		
		btnReset.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ea){
				double ex = Float.parseFloat(txtCamX.getText());
				double ey = Float.parseFloat(txtCamY.getText());
				double ez = Float.parseFloat(txtCamZ.getText());
				
				double targetX = Float.parseFloat(txtTargetX.getText());
				double targetY = Float.parseFloat(txtTargetY.getText());
				double targetZ = Float.parseFloat(txtTargetZ.getText());
				
				double upX = Float.parseFloat(txtUpX.getText());
				double upY = Float.parseFloat(txtUpY.getText());
				double upZ = Float.parseFloat(txtUpZ.getText());
				
				double near = 1f;
				double far = 10.0f;
				
				if(controlListener != null)
					controlListener.onViewChangeCmd(ex, ey, ez,
											targetX, targetY, targetZ,
											upX, upY, upZ,
											near, far);
			}
		});
	}
	
	private Panel initLTRBPane(){
		Panel pn = new Panel(new FlowLayout(FlowLayout.LEFT));
		txtL = new TextField();
		pn.add(txtL);
		txtT = new TextField();
		pn.add(txtT);
		txtR = new TextField();
		pn.add(txtR);
		txtB = new TextField();
		pn.add(txtB);
		return pn;
	}
	
	/** crete a flowlayout panel */
	private Panel initTriplePane(int align, TextField tf0, TextField tf1, TextField tf2){
		FlowLayout fl = new FlowLayout(align);
		Panel pn = new Panel(fl);
		pn.add(tf0);
		pn.add(tf1);
		pn.add(tf2);
		return pn;
	}
	
	public void setControlListener(IControlListener l){
		controlListener = l;
	}
	
	public void onDrawPaneChanged(	double ex, double ey, double ez,
											double targetX, double targetY, double targetZ,
											double upX, double upY, double upZ,
											double near, double far) {
		txtCamX.setText(ex + "");
		txtCamY.setText(ey + "");
		txtCamZ.setText(ez + "");
		txtTargetX.setText(targetX + "");
		txtTargetY.setText(targetY + "");
		txtTargetZ.setText(targetZ + "");
		txtUpX.setText(upX + "");
		txtUpY.setText(upY + ""); 
		txtUpZ.setText(upZ + "");
		txtNear.setText(near + "");
		txtFar.setText(far + "");
	}
}

class Matrix3DPane extends Panel{
	
	private double[] data = new double[16];
	private TextField[] txtData;
	private int gap = 6;
	
	public Matrix3DPane(){
		GridLayout layout = new GridLayout(4,4, gap/2, gap/2);
		setLayout(layout);
		txtData = new TextField[16];
		for(int i=0; i<16; i++){
			txtData[i] = new TextField();
			add(txtData[i]);
		}
	}
	
	public void setData(double[] dt){
		System.arraycopy(dt, 0, data, 0, 16);
	}
	
	public double[] getData(){
		return data;
	}
}