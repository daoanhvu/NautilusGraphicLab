package nautilus.lab.formula;

import java.util.Enumeration;

import javax.swing.JPanel;
import javax.swing.event.TreeModelEvent;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import nautilus.lab.formula.Token.TokenClass;

public class FormulaTreePane extends JPanel {

}

class FormulaTreeModelListener extends TreeModelEvent {

	public FormulaTreeModelListener(Object arg0, Object[] arg1) {
		super(arg0, arg1);
	}
}

class FormulaTreeNode implements MutableTreeNode {
	
	private Token mToken;
	
	@Override
	public Enumeration children() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getAllowsChildren() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public TreeNode getChildAt(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getChildCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getIndex(TreeNode arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public TreeNode getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isLeaf() {
		return (mToken.getTokenClass() == TokenClass.BASIC);
	}

	@Override
	public void insert(MutableTreeNode arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(MutableTreeNode arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeFromParent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setParent(MutableTreeNode arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUserObject(Object token) {
		// TODO Auto-generated method stub
		mToken = (Token)token;
	}
	
}