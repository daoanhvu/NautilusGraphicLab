package nautilus.lab.component;

import java.awt.AWTEvent;
import java.awt.AWTEventMulticaster;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public abstract class AbstractImageButton extends Component{
	protected static final Color BACKGROUND_COLOR = new Color(200, 200, 200);
	protected static final Color BUTTON_FOCUS_COLOR = new Color(200, 230, 230);
	protected static final Color BUTTON_DARK_COLOR = new Color(190, 190, 190);
	protected static final Color BUTTON_BRIGHT_COLOR = new Color(220, 220, 220);
	protected static final Color BUTTON_HIGHLIGHT_COLOR = new Color(240, 240, 220);
	
	protected ActionListener actionListener;
	protected Image image;
	protected boolean hasFocus = false;
	protected boolean isPressed = false;
	protected boolean insideButton = false;
	protected String actionCommand;
	protected Image dbImage;
	
	protected AbstractImageButton(){
		enableEvents(AWTEvent.MOUSE_EVENT_MASK |
                AWTEvent.KEY_EVENT_MASK |
                AWTEvent.FOCUS_EVENT_MASK);
	}
	
	/**
     * Returns the current button image.
     *
     * @return     the current button image
     *
     * @see ImageButton#setImage
     */
   public Image getImage() {
       return this.image;
   }
   	
   /**
     * Sets a new button image.
     *
     * @param image    the new image
     *
     * @see ImageButton#getImage
     */
   public void setImage(Image image) {
       this.image = image;
       invalidate();
       repaint();
   }
   
   /**
    * <P>Returns the current action command string.
    *
    * <P>Returns the string that is sent to a registered
    *    listener as an ACTION_PREFERED-Event.
    *
    * @return         the current action command string
    *
    * @see ImageButton#setActionCommand
    */
  public String getActionCommand() {
      return this.actionCommand;
  }
  		
  /**
    * <P>Sets a new action command string.
    *
    * <P>Sets the string that is sent to a registered
    *    listener as an ACTION_PREFERED-Event.
    *
    * @param command  the new action command string
    *
    * @see ImageButton#getActionCommand
    */
	  public void setActionCommand(String command) {
	      this.actionCommand = command;
	  }
	  
	  /**
	   * <P>Add a new action listener.
	   *
	   * @param listener     Object which wants to listen to action events from AVLAnimator
	   *
	   * @see ImageButton#removeActionListener
	   */
	 public void addActionListener(ActionListener listener)  {
	     this.actionListener = AWTEventMulticaster.add(this.actionListener, listener);
	 }
	 	
	 /**
	   * <P>Remove an existing action listener.
	   *
	   * @param listener     Object which no longer wants to listen to action events from AVLAnimator
	   *
	   * @see ImageButton#addActionListener
	   */
	 public void removeActionListener(ActionListener listener) {
	     this.actionListener = AWTEventMulticaster.remove(this.actionListener, listener);
	 }
 	
	 /**
	  * <P>This method is used from the JRE to determine if the button wants to gain
	  *    the focus using the TAB-Key
	  *
	  * @return         true if the button wants to get focus, false otherwise
	  */
	public boolean isFocusTraversable() {
	    return true;
	}
	
	/**
     * <P>This method is used from the JRE to determine if a mouse event happened
     *    inside the button.
     *
     * <P>This method decides what is inside and what is outside.
     *
     * @param x        x-coordinate relative to the buttons origin
     * @param y        y-coordinate relative to the buttons origin
     * @return         true if mouse event happened inside button, false otherwise
     */
   public boolean contains (int x, int y)  {
       return ((x >= 0) &&
               (y >= 0) &&
               (x < getSize().width) &&
               (y < getSize().height));
   }
   
   /**
    * <P>Sets button state.
    *
    * <P>Disabled buttons are not usable and are displayed
    *    grayed. To disable the button you have to set the
    *    button state to false.
    *
    * @param flag     the new button state
    */
  public void setEnabled(boolean flag) {
      boolean oldflag = isEnabled();
      super.setEnabled(flag);
      if (oldflag != flag) {
          repaint();
      }
      if (flag == false && this.hasFocus == true) {
          transferFocus();
      }
  }
  
	  /**
	   * <P>Prepares painting of ImageButton.
	   *
	   * <P>Some method calls for double buffering support are stored here.
	   *
	   * @param g        the graphics object to draw on
	   *
	   * @see ImageButton#paint
	   */
	 public void update(Graphics g) {
	     Graphics dbg;
	     		
	     if (dbImage == null) {
	         dbImage = createImage(getSize().width, getSize().height);
	     }
	     
	     if (dbImage != null) {
	         dbg = dbImage.getGraphics();
	         if (dbg != null) {
	             paint(dbg);
	             g.drawImage(dbImage, 0, 0, this);
	             dbg.dispose();
	             return;
	         }
	     }
	     paint(g);
	 }
  
  /**
   * <P>Event-Handler for low level focus events.
   */
 public void processFocusEvent(FocusEvent e){
     switch(e.getID()) {
         case FocusEvent.FOCUS_GAINED:
             this.hasFocus = true;
             repaint();
             break;
         			
         case FocusEvent.FOCUS_LOST:
             this.hasFocus = false;
             repaint();
             break;
     }
     super.processFocusEvent(e);
 }
 
	 /**
	  * <P>Event-Handler for low level key events.
	  */
	public void processKeyEvent(KeyEvent e) {
	    switch(e.getKeyCode()) {
	        case KeyEvent.VK_SPACE:
	                // ignore events other than key pressed
	            if (e.getID() != KeyEvent.KEY_PRESSED) {
	                break;
	            }
	               				
	                // simulate MOUSE_PRESSED - MOUSE_RELEASED
	            boolean oldState;
	            oldState = this.insideButton;
	            				
	            this.insideButton = true;
	            processMouseEvent(
	                new MouseEvent(this,
	                               MouseEvent.MOUSE_PRESSED,
	                               0,
	                               InputEvent.BUTTON1_MASK,
	                               1, 1,
	                               1,
	                               false));
	            repaint();
	            processMouseEvent(
	                new MouseEvent(this,
	                               MouseEvent.MOUSE_RELEASED,
	                               0,
	                               InputEvent.BUTTON1_MASK,
	                               1, 1,
	                               1,
	                               false));
	            if (this.insideButton == true) {
	                    // just to handle insideButton correct
	                    // if mouse has left Button during this event
	                this.insideButton = oldState;
	            }
	            break;
	    }
	    super.processKeyEvent(e);
	}
	
    /**
     * <P>Returns preferred size for this shadow object
     *
     * @return     the preferred size of the object
     *
     * @see ImageButton#getMinimumSize
     */
   public Dimension getPreferredSize() {
       if (this.image != null) {
           int width = this.image.getWidth(this);
           if (width == 0) {
               width = 20;
               System.err.println("Detected size 0 for image width! Maybe there is an error!");
           }
           int height = this.image.getHeight(this);
           if (height == 0) {
               height = 20;
               System.err.println("Detected size 0 for image height! Maybe there is an error!");
           }
           return new Dimension(width + 4, height + 4);
       }
       
       //return new Dimension(40, 40);
       return super.getPreferredSize();
       
   }

   /**
     * <P>Returns minimum size for this shadow object
     *
     * @return     the minimum size of the object
     *
     * @see ImageButton#getPreferredSize
     */
   public Dimension getMinimumSize()  {
       if (this.image != null) {
           int width = this.image.getWidth(this);
           if (width == 0)      {
               width = 20;
               System.err.println("Detected size 0 for image width! Maybe there is an error!");
           }
           int height = this.image.getHeight(this);
           if (height == 0) {
               height = 20;
               System.err.println("Detected size 0 for image height! Maybe there is an error!");
           }
           return new Dimension(width + 4, height + 4);
       }
       
        return new Dimension(20, 20);
   }
}
