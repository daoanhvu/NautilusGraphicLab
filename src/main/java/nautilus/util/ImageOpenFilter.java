package nautilus.util;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ImageOpenFilter extends FileFilter implements java.io.FileFilter {
	public final static String jpeg = "jpeg";
    public final static String jpg = "jpg";
    public final static String gif = "gif";
    public final static String tiff = "tiff";
    public final static String tif = "tif";
    public final static String png = "png";
    
    private String namePrefix = null;
    
    public ImageOpenFilter(String prefix) {
    	namePrefix = prefix;
    }
 
    //Accept all directories and all gif, jpg, tiff, or png files.
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        
        if(namePrefix != null) {
        	if(!f.getName().startsWith(namePrefix))
        		return false;
        }
 
        String extension = getExtension(f);
        if (extension != null) {
            return extension.equals(tiff) ||
                    extension.equals(tif) ||
                    extension.equals(gif) ||
                    extension.equals(jpeg) ||
                    extension.equals(jpg) ||
                    extension.equals(png);
        }
 
        return false;
    }
 
    //The description of this filter
    public String getDescription() {
        return "Just Images";
    }
    
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
}
