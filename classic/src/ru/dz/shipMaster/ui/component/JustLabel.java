package ru.dz.shipMaster.ui.component;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.dz.shipMaster.ui.DashComponent;
import ru.dz.shipMaster.ui.meter.GeneralMeter;
import ru.dz.shipMaster.ui.meter.IInstrumentWithFont;
import ru.dz.shipMaster.ui.meter.IInstrumentWithMessages;

@SuppressWarnings("serial")
public class JustLabel extends DashComponent implements IInstrumentWithFont, IInstrumentWithMessages 
{
	protected
	static final Logger log = Logger.getLogger(GeneralMeter.class.getName()); 

	private Font myFont; 
	private int fontSize = 24;
	protected FontMetrics myMetrics;

	private String labelText = "?";

	@Override
	protected void processResize() 
	{
	}

	
	@Override
	protected void paintDashComponent(Graphics2D g2d) {
		doSetup(g2d);
		
		//g2d.setColor(vis.digitalMeterColor);
		g2d.setColor(vis.labelColor);
		g2d.setFont(myFont);
		
		FontRenderContext frc = g2d.getFontRenderContext();
		Rectangle2D stringBounds = myFont.getStringBounds(labelText , frc);
		
		g2d.drawString(labelText, 
				(int)(width-stringBounds.getWidth()), 
				(int)(height/2-(stringBounds.getCenterY())));
	}
	
	
	
	private void doSetup(Graphics2D g2d) {
		if(myFont == null )
		{
			try {			
				myFont = new Font("Verdana", Font.PLAIN, fontSize);				
			} catch(Throwable e)
			{
				log.log(Level.SEVERE, "Can't create font",e);
				myFont = new Font(Font.SANS_SERIF, Font.BOLD, fontSize);
			}
		}
		if(myMetrics==null && myFont != null) {
			
			myMetrics = getGraphics().getFontMetrics(myFont);
			Dimension myPreferredSize = findSize();

			setPreferredSize(myPreferredSize);
			setMinimumSize(myPreferredSize);
			setMaximumSize(myPreferredSize);

			setSize(myPreferredSize);

			getParent().validate();
			getParent().repaint(100);
		}
	}

	protected Dimension findSize() {
		Dimension myPreferredSize = new 
			Dimension(
					myMetrics.stringWidth(labelText),
					myMetrics.getHeight());
		return myPreferredSize;
	}
	
	
	
	
	
	
	
	
	
	
	@Override
	public void setFontSize(int size) 
	{
		if( size <= 0 )
		{
			log.severe("Attempt to set negative font size");
			return;
		}
		this.fontSize = size;
		myFont = null;
	}

	@Override
	public int getFontSize() { return fontSize; }

	
	
	
	/**
	 * @return the offMessage
	 */
	public String getOffMessage() {		return labelText;	}

	/**
	 * @param offMessage the offMessage to set
	 */
	public void setOffMessage(String offMessage) {		this.labelText = offMessage;	}

	/**
	 * @return the onMessage
	 */
	public String getOnMessage() {		return labelText;	}

	/**
	 * @param onMessage the onMessage to set
	 */
	public void setOnMessage(String onMessage) {		this.labelText = onMessage;	}


	@Override
	public void setDrawMessage(boolean drawmessage) {
		// ignore		
	}


	@Override
	public boolean isDrawMessage() {
		return true;
	}
	
}
