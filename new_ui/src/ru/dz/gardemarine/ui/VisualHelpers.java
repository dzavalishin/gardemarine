package ru.dz.gardemarine.ui;

import java.awt.Frame;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public abstract class VisualHelpers {
    private static final Logger log = Logger.getLogger(VisualHelpers.class.getName());

	static public BufferedImage loadImage(String shortName)
	{
		InputStream in = VisualHelpers.class.getClassLoader().getResourceAsStream(shortName);
		if(in == null) { return null; }
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

	public static boolean showConfirmDialog(JPanel referencePanel, String question) {
		//Frame frame= JOptionPane.getFrameForComponent(referencePanel);
	    int choice = JOptionPane.showConfirmDialog(referencePanel, question, "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE );
	    return choice == JOptionPane.YES_OPTION;
	}
	
	

	private static BufferedImage appIconImage;
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


	public static Icon loadIcon(String shortName) {
		BufferedImage image = loadImage(shortName);
		if(image == null)
			return null;
		
		return new ImageIcon(image);		
	}


}
