package ru.dz.shipMaster.ui.render;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import ru.dz.shipMaster.ui.misc.VisualHelpers;

public class TransparantBackground {

	private static final BufferedImage upperLeftOrig = VisualHelpers.loadImage("Transparant_1_UL.png");
    private BufferedImage backgroungImage = null;
	private final int width;
	private final int height;

	//static final ImageFilter defaultFilter = new ColorifyFilter(VisualSettings.global.digitalMeterColor);
	static final ImageFilter defaultFilter = new ColorifyFilter(new Color(255, 60, 60 ));
	
	public TransparantBackground(int width, int height) {
		this.width = width;
		this.height = height;
		backgroungImage = VisualHelpers.createCompatibleBitmap(width, height, true);
		
		Graphics2D g2d = backgroungImage.createGraphics();
		drawMe(g2d, defaultFilter);	
		g2d.dispose();
	}


	/**
	 * Generate full background having just upper left corner.
	 * @param g2d where to paint to.
	 * @param filter how to preprocess 
	 */
	private void drawMe(Graphics2D g2d, ImageFilter filter) {
		
		FilteredImageSource filteredSrc = new FilteredImageSource(upperLeftOrig.getSource(), filter);
		Image upperLeft = Toolkit.getDefaultToolkit().createImage(filteredSrc);

		
		int cw = upperLeftOrig.getWidth(); 
		int ch = upperLeftOrig.getHeight(); 

		// upper left
		g2d.drawImage (upperLeft,
	             0, 0, cw, ch, 
	             0, 0, cw, ch,
	             null);

		// upper right
		g2d.drawImage (upperLeft,
	             width-cw, 0, width, ch,
	             cw, 0, 0, ch, // swap x 
	             null);

		// lower left
		g2d.drawImage (upperLeft,
	             0, height-ch, cw, height,
	             0, ch, cw, 0, 
	             null);
		
		// lower right
		g2d.drawImage (upperLeft,
	             width-cw, height-ch, width, height,
	             cw, ch, 0, 0, // swap x, y
	             null);


		// upper 
		g2d.drawImage (upperLeft,
	             cw, 0, width-cw, ch,
	             cw-1, 0, cw, ch, // stretch x 
	             null);
		// lower 
		g2d.drawImage (upperLeft,
	             cw, height, width-cw, height-ch,
	             cw-1, 0, cw, ch, // stretch x 
	             null);
		
		// left 
		g2d.drawImage (upperLeft,
	             0, ch, cw, height-ch,
	             0, ch-1, cw, ch, // stretch y 
	             null);
		
		// right 
		g2d.drawImage (upperLeft,
	             width, ch, width-cw, height-ch,
	             0, ch-1, cw, ch, // stretch y 
	             null);
		
		// center 
		g2d.drawImage (upperLeft,
	             cw, ch, width-cw, height-ch,
	             cw-1, ch-1, cw, ch, // stretch x, y 
	             null);
		
		
	}


	public static TransparantBackground get(int xs, int ys)
	{
		// TODO cache them
		return new TransparantBackground(xs,ys);
	}
	
	
    public static void main(String[] args) {
    	TransparantBackground tb = new TransparantBackground(130,70);


    	JFrame jf = new JFrame();
    	jf.add( new JLabel(new ImageIcon(tb.backgroungImage)) );
    	
    	jf.pack();
    	jf.setVisible(true);   	
    	
    }
	
}
