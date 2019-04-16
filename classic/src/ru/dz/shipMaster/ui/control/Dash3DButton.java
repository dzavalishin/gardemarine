package ru.dz.shipMaster.ui.control;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.dz.shipMaster.ui.meter.IInstrumentWithFont;
import ru.dz.shipMaster.ui.misc.VisualHelpers;

@SuppressWarnings("serial")
public class Dash3DButton extends DashButton implements IInstrumentWithFont {
	private static final Logger log = Logger.getLogger(Dash3DButton.class.getName());
	
	private final BufferedImage iLeftOn = VisualHelpers.loadImage("3d_button/left_on.png"); 
	private final BufferedImage iMiddleOn = VisualHelpers.loadImage("3d_button/middle_on.png");
	private final BufferedImage iRightOn = VisualHelpers.loadImage("3d_button/right_on.png");

	private final BufferedImage iLeftOff = VisualHelpers.loadImage("3d_button/left_off.png"); 
	private final BufferedImage iMiddleOff = VisualHelpers.loadImage("3d_button/middle_off.png");
	private final BufferedImage iRightOff = VisualHelpers.loadImage("3d_button/right_off.png");

	private int fontSize = 20;


	public Dash3DButton() {
		super();
		isToggle = true;
		setBorder(null);
		setPreferredSize(new Dimension(200, iLeftOff.getHeight()));
	}

	
	@Override
	protected void paintDashComponentBackground(Graphics2D g2d) {

		g2d.setClip(0, 0, width, height);

		BufferedImage iLeft = iLeftOff; 
		BufferedImage iMiddle = iMiddleOff;
		BufferedImage iRight = iRightOff;

		if(pressed)
		{
			iLeft = iLeftOn; 
			iMiddle = iMiddleOn;
			iRight = iRightOn;
		}

		g2d.drawImage(iLeft, 0, 0, null);

		g2d.setClip(0, 0, width-iRight.getWidth(), height);
		g2d.drawImage(iMiddle, iLeft.getWidth(), 0, null);
		g2d.setClip(0, 0, width, height);

		g2d.drawImage(iRight, width-iRight.getWidth(), 0, null);

		drawText(g2d);
	}

	private Font myFont; // = getFont().deriveFont(24); 

	private void resetFont()
	{
		if(myFont == null)
		{
			try {			
				myFont = new Font("Tahoma", Font.PLAIN, fontSize);				
			} catch(Throwable e)
			{
				log.log(Level.SEVERE, "Can't create font",e);
				myFont = new Font(Font.SANS_SERIF, Font.BOLD, fontSize);
			}
		}
	}
	
	{
		resetFont();
	}

	private void drawText(Graphics2D g2d)
	{
		g2d.setColor(vis.digitalMeterColor);
		g2d.setFont(myFont);

		//String txt = (pressed||offMessage == null||offMessage.length() == 0) ? onMessage : offMessage;
		String txt = buttonText;
		FontRenderContext frc = g2d.getFontRenderContext();
		Rectangle2D stringBounds = myFont.getStringBounds(txt, frc);

		g2d.drawString(txt, 
				(int)(width-stringBounds.getWidth())/2, 
				(int)(height/2-(stringBounds.getCenterY())));

	}

	
	@Override
	public void setFontSize(int size) {
		if( size <= 0 ) return;
		myFont = null;
		this.fontSize = size;
		resetFont();
		}
	
	@Override
	public int getFontSize() { return fontSize; }
	
}
