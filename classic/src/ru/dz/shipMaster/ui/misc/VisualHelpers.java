package ru.dz.shipMaster.ui.misc;

import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public abstract class VisualHelpers {
    private static final Logger log = Logger.getLogger(VisualHelpers.class.getName());

    static ImageCache cache = new ImageCache();
    
    static public BufferedImage loadImage(String shortName)
    {
    	BufferedImage img = cache.find(shortName);
    	if( img == null )
    	{
    		img = doLoadImage(shortName);
    		if(img != null)
    			cache.put(shortName, img);
    	}
    	return img;
    }
    
	static private BufferedImage doLoadImage(String shortName)
	{
		InputStream in = null;

		try {
			in = new FileInputStream("project.images/"+shortName);
		} catch (FileNotFoundException e1) {
			//log.severe(e1.toString());
		}
		
		if(in == null)
			in = VisualHelpers.class.getClassLoader().getResourceAsStream(shortName);
		
		if(in == null)
		{
			try {
				in = new FileInputStream(shortName);
			} catch (FileNotFoundException e1) {
				//log.severe(e1.toString());
			}
		}
		
		if(in == null) 
		{ 
			log.severe("Can't find "+shortName);
			return null; 
		}
		
		try {
			return ImageIO.read(in);
		} catch (IOException e) {
			log.log(Level.SEVERE,"Image read error", e);
			return null;
		}		
	}

	
	static public void showMessageDialog(JPanel referencePanel, String text)
	{
		Frame frame= JOptionPane.getFrameForComponent(referencePanel);
	    JOptionPane.showMessageDialog(frame, text);		
	}


	private static BufferedImage appIconImage = null;
	/**
	 * @return Application icon image.
	 */
	public static Image getApplicationIconImage() {
		if(appIconImage == null)
		{
			appIconImage = loadImage("gardemarineIcon.png");
		}
		
		return appIconImage;
	}
	
	
	public static BufferedImage createCompatibleBitmap(int width, int height, boolean hasAlpha)
	{
		// Create a buffered image with a format that's compatible with the screen
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            if (hasAlpha ) {
                transparency = Transparency.TRANSLUCENT; //Transparency.BITMASK;
            }
    
            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            return gc.createCompatibleImage( width, height, transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }
        return null;
	}
	
}
