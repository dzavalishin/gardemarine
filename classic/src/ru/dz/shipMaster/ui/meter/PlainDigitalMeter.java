package ru.dz.shipMaster.ui.meter;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlainDigitalMeter extends GeneralMeter {
    /**
	 * 
	 */
	private static final long serialVersionUID = -6160908800934602254L;
	private static final Logger log = Logger.getLogger(PlainDigitalMeter.class.getName());
	private FontMetrics myMetrics;
	private Font myFont; // = getFont().deriveFont(24); 

	
	private void setupSize()
	{
		if(isPreferredSizeSet())
			return;
		//myPreferredSize = new Dimension(180,66);
	
		// XXX hack
		setPreferredSize(new Dimension(186,60));
	}
    
	public PlainDigitalMeter() {
		//doSetup();
		setupSize();
	}
	
	
    
    
	@Override
	protected void recalcMeterParameters() {
		//doSetup();
	}

	@Override
	protected void paintDashComponent(Graphics2D g2d) {
		doSetup(g2d);
		
		g2d.setColor(vis.digitalMeterColor);
		g2d.setFont(myFont);
		
		String txt = String.format("%.1f", currVal);
		FontRenderContext frc = g2d.getFontRenderContext();
		Rectangle2D stringBounds = myFont.getStringBounds(txt, frc);
		
		g2d.drawString(txt, 
				(int)(width-stringBounds.getWidth()), 
				(int)(height/2-(stringBounds.getCenterY())));
	}

	private void doSetup(Graphics2D g2d) {
		if(myFont == null )
		{
			try {			
				myFont = new Font("Verdana", Font.PLAIN, 48);				
			} catch(Throwable e)
			{
				log.log(Level.SEVERE, "Can't create font",e);
				myFont = new Font(Font.SANS_SERIF, Font.BOLD, 48);
			}
		}
		if(myMetrics==null && myFont != null) {
			
			myMetrics = getGraphics().getFontMetrics(myFont);
			Dimension myPreferredSize = new Dimension(myMetrics.charWidth('0')*6,myMetrics.getHeight());
			//myMinimumSize = myPreferredSize;
			//setSize(myPreferredSize);
			setPreferredSize(myPreferredSize);
			setMinimumSize(myPreferredSize);
			setMaximumSize(myPreferredSize);
			//setBounds(0, 0, myPreferredSize.width, myPreferredSize.height);
			setSize(myPreferredSize);
			//setStringMetrics();
			getParent().validate();
			getParent().repaint(100);
			//resetDashComponentBackgroundImage();
			//paintDashComponentBackground(g2d);
		}
	}

	@Override
	protected void paintDashComponentBackground(Graphics2D g2d) {
		doSetup(g2d);
		g2d.setColor(vis.bgColor);
		g2d.fillRect(0, 0, width, height);
		drawHorLabels(g2d);
	}

	@Override
	protected void processResize() {
		//doSetup();
	}

}
