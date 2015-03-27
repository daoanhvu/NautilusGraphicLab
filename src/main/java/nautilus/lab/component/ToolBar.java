package nautilus.lab.component;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Panel;
import java.util.ArrayList;

public class ToolBar extends Panel {
	private ArrayList<Button> buttons = new ArrayList<Button>();
	
	public ToolBar(){
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setPreferredSize(new Dimension(700, 40));
	}
	
	public void addButton(Button btn){
		buttons.add(btn);
	}
}
