package nautilus.lab.formula;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TestFormulaFrame extends JFrame {
	private static final long serialVersionUID = 10321L;
	
	private FormulaPane cvFormula;
	private JTextField tfInput;
	private JButton btnDraw;
	private Lexer mLexer = new Lexer();
	private Parser formulaLayout = new Parser();
	
	
	public TestFormulaFrame() {
		
		super("Nautilus Lab 1.0");
		setBounds(10, 10, 550, 300);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		initComponent();
	}
	
	private void initComponent() {
		Container c = getContentPane();
		c.setLayout(new BorderLayout(5, 5));
		
		JPanel pnInputPane = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		tfInput = new JTextField(20);
		//tfInput.setText("a+(c-d/f)/b");
		//tfInput.setText("a-(c-d/f)");
		tfInput.setText("sqrt(8)");
		//tfInput.setText("(t-23/6)/(m+9/6)-a");
		//tfInput.setText("a/(m+9/6)");
		btnDraw = new JButton("Draw");
		btnDraw.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String strForumla = tfInput.getText();
				mLexer.setText(strForumla);
				mLexer.lexicalAnalysis(false, 0);
				Token root = formulaLayout.parsing(mLexer);
				cvFormula.setToken(root);
				//cvFormula.invalidate();
				//cvFormula.revalidate();
				cvFormula.repaint();
			}
			
		});
		pnInputPane.add(tfInput);
		pnInputPane.add(btnDraw);
		
		c.add(pnInputPane, BorderLayout.NORTH);
		
		cvFormula = new FormulaPane();
		c.add(cvFormula, BorderLayout.CENTER);
	}
}

class FormulaPane extends JPanel {
	
	private Color drawingColor = Color.RED;
	private Token mToken = null;
	
	Font mFont = new Font("Arial", 12, 36);
	
	FormulaPane() {
		setBackground(Color.WHITE);
		setFont(mFont);
	}
	
	public void setToken(Token token) {
		mToken = token;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//Color bg = getBackground();
		g.setColor(drawingColor);
		//testDrawChar(g);
		if(mToken != null) {
			mToken.layout(g, 20, 120, mFont.getSize2D());
			mToken.draw(g);
		}
	}
	
	private void testDrawChar(Graphics g) {
		Font oldFont = g.getFont();
		g.setFont(mFont);
		g.drawString("A", 60, 120);
		
		g.drawLine(60, 120, 100, 120);
		
		g.setFont(oldFont);
	}
	
}
