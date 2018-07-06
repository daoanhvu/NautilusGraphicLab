package nautilus.lab.component;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class CommandPane extends JPanel{

	private static final long serialVersionUID = 100L;
	
	private JTextField txtCommand;
	private Font font = null;
	
	private CommandListener commandListener = null;
	
	public CommandPane(){
		setPreferredSize(new Dimension(400, 32));
		this.setLayout(new BorderLayout());
		txtCommand = new JTextField();
		txtCommand.addKeyListener(keyListener);
		add(txtCommand, BorderLayout.CENTER);
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream("ncc.ttf"));
			
			//txtCommand.setFont(font.deriveFont(Font.BOLD, 16));
			txtCommand.setFont(font.deriveFont(17f));
			txtCommand.setForeground(new Color(20, 23, 203));
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private KeyListener keyListener = new KeyListener(){

		@Override
		public void keyPressed(KeyEvent e) {
		}

		@Override
		public void keyReleased(KeyEvent evt) {
			if(evt.getKeyCode() == KeyEvent.VK_ENTER){
				String command = txtCommand.getText();
				commandListener.onNFunctionChange(command, new float[]{});
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
	
	};
	
	public void setCommandListener(CommandListener l){
		commandListener = l;
	}
	
	public CommandListener getCommandListener(){
		return commandListener;
	}
}
