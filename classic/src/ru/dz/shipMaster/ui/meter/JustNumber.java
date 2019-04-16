package ru.dz.shipMaster.ui.meter;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.logging.Level;

@SuppressWarnings("serial")
public class JustNumber extends GeneralMeter implements IInstrumentWithFont 
{
	private static final int N_CHARS = 6;
	protected FontMetrics myMetrics;
	private Font myFont; 
	private int fontSize = 48;

	public JustNumber() {
		setBorder(null);
	}
	
	@Override
	protected void recalcMeterParameters() {
		//digitalMeterNChars = calculateDigitalMeterNChars();
		//digitalMeterNChars = 6;
	}

	@Override
	protected void processResize() {
	}

	@Override
	protected void paintDashComponent(Graphics2D g2d) {
		doSetup(g2d);
		
		g2d.setColor(vis.digitalMeterColor);
		g2d.setFont(myFont);
		
		String txt = formatValue(currVal);
		FontRenderContext frc = g2d.getFontRenderContext();
		Rectangle2D stringBounds = myFont.getStringBounds(txt, frc);
		
		g2d.drawString(txt, 
				(int)(width-stringBounds.getWidth()), 
				(int)(height/2-(stringBounds.getCenterY())));
	}

	protected String formatValue(double value) {
		String txt = String.format("%.1f", value);
		return txt;
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
		Dimension myPreferredSize = new Dimension(myMetrics.charWidth('0')*N_CHARS,myMetrics.getHeight());
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
	public int getFontSize() {		return fontSize;	}

	
}
