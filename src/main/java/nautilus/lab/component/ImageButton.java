package nautilus.lab.component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.net.URL;

/**
  * <P>This class implements an image button using only AWT features.
  *
  * @author Martina Hirsch, Martin Brunninger
  * @version 1.0
  */
public class ImageButton extends AbstractImageButton {
    /**
      * Constructor of ImageButton. Creates a new blank ImageButton.
      *
      * @see ImageButton#ImageButton(URL)
      * @see ImageButton#ImageButton(Image)
      */
    public ImageButton() {
        super();
        this.actionCommand = "ImageButton";
    }
    	
    /**
      * Constructor of ImageButton. Creates a new ImageButton and
      * loads the image from the given URL.
      *
      * @param url         the URL where the image is located
      *
      * @see ImageButton#ImageButton
      * @see ImageButton#ImageButton(Image)
      */
    public  ImageButton(URL url) {
        this();
        			
        Image image = getToolkit().getImage(url);
        MediaTracker mt = new MediaTracker(this);
        		
        mt.addImage(image, 0);
        try {
            mt.waitForAll();
        }
        catch (InterruptedException e) {
            // nothing
        }
           		
        this.image = image;
    }
    	
    /**
      * Constructor of ImageButton. Creates a new ImageButton using
      * the specified image.
      *
      * @param image        the image that should be displayed in button
      *
      * @see ImageButton#ImageButton
      * @see ImageButton#ImageButton(URL)
      */
    ImageButton(Image image) {
        this();
        this.image = image;
    }
    	
    /**
      * <P>Paints the ImageButton.
      *
      * @param g        the graphics object to draw on
      *
      * @see ImageButton#paint
      */
    public void paint(Graphics g) {
        Color colorUpperBorder, colorLowerBorder;
            // clear canvas
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, getSize().width, getSize().height);
        		
            // paint focus of button
        if (this.hasFocus == true) {
            g.setColor(BUTTON_FOCUS_COLOR);
            g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);	
        }
        		
            // draw the perimeter of the button
        if (this.isPressed == true && this.insideButton == true) {
            colorUpperBorder = BUTTON_DARK_COLOR;
            colorLowerBorder = BUTTON_BRIGHT_COLOR;
        }
        else {
            colorUpperBorder = BUTTON_BRIGHT_COLOR;
            colorLowerBorder = BUTTON_DARK_COLOR;
        }
        g.setColor(colorUpperBorder);
        g.drawLine(1, 1, getSize().width - 2, 1);
        g.drawLine(1, 1, 1, getSize().height - 2);
        g.setColor(colorLowerBorder);
        g.drawLine(getSize().width - 2, 2, getSize().width - 2, getSize().height - 2);
        g.drawLine(2, getSize().height - 2, getSize().width - 2, getSize().height - 2);
        		
            // paint the interior of the button
        if (this.image == null &&
            this.isPressed == true && this.insideButton == true)  {
            g.setColor(BUTTON_HIGHLIGHT_COLOR);
        }
        else {
            g.setColor(BACKGROUND_COLOR);
        }
        g.fillRect(2, 2, getSize().width - 4, getSize().height - 4);
        		
            // draw the image
        if (this.image != null) {
            g.drawImage(this.image,
                        (getSize().width - this.image.getWidth(this)) / 2,
                        (getSize().height - this.image.getHeight(this)) / 2,
                        this);
        }
            // is button disabled?
        if (isEnabled() == false)  {
            g.setColor(BACKGROUND_COLOR);
            g.setClip(2, 2, getSize().width - 4, getSize().height - 4);
            for(int i=2; i <= getSize().width + getSize().height - 4; i = i + 2) {
                g.drawLine(i, 2, i - getSize().height + 5, getSize().height - 3);
            }
        }
    }
    	    	 	
    /**
      * <P>Event-Handler for low level mouse events.
      */
    public void processMouseEvent(MouseEvent e) {
        switch(e.getID()) {
            case MouseEvent.MOUSE_PRESSED:
                if (isEnabled() == true) {
                        // process only if Button1 is pressed
                        // additional compare to 0 because of a bug in Netscape4.7-LINUX
                    if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0 ||
                    e.getModifiers() == 0) {
                            // mouse is always inside button when this
                            // event occurs
                        this.isPressed = true;
                        requestFocus();
                        repaint();
                    }
                }
                break;
            			
            case MouseEvent.MOUSE_RELEASED:
                if (isEnabled()) {
                        // process only if Button1 is released
                    if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0)  {
                            // CAUTION: mouse can be outside button
                            // when this event occurs
                            						
                            // send actionEvent only if MouseButton1 is
                            // released inside ImageButton
                        if((this.insideButton == true) && (this.actionListener != null)) {
                            this.actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, this.actionCommand));
                        }
                           					
                        this.isPressed = false;
                        repaint();
                    }
                }
                break;
            				
            case MouseEvent.MOUSE_ENTERED:
                this.insideButton = true;
                if (this.isPressed == true) {
                    repaint();
                }
                break;
            				
            case MouseEvent.MOUSE_EXITED:
                this.insideButton = false;
                if (this.isPressed == true) {
                    repaint();
                }
                break;
        }
        super.processMouseEvent(e);
    }
}
