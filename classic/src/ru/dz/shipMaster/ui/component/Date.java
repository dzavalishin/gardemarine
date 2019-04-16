package ru.dz.shipMaster.ui.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import ru.dz.shipMaster.ui.DashComponent;

@SuppressWarnings("serial")
public class Date extends DashComponent {
	private static final int FONT_SIZE = 32;
	private FontMetrics myMetrics;
	private Font myFont; // = getFont().deriveFont(24); 

	public Date() {
		processResize();
		setBorder(null);
		//t.schedule(syncTask, 10);
		t.scheduleAtFixedRate(clockTask, 0, 1000*15);
	}
	
	
	Timer t = new Timer(this.getClass().getName(), true);
	Calendar now = Calendar.getInstance();
	int lastDate = -1;
	
	TimerTask clockTask = new TimerTask() {
		
		@Override
		public void run() {
			now = Calendar.getInstance();
			
			int date = now.get(Calendar.DAY_OF_MONTH);
			
			if(date == lastDate)
				return;

			lastDate = date;
			
			resetDashComponentBackgroundImage();
			repaint(1000); // No rush :)
		}
	}; 
	
	@Override
	protected void processResize() {
	}

	private static Color glassColor = new Color(0,true); // completely transparent

	@Override
	protected void paintDashComponentBackground(Graphics2D g2d) {
		// Fill all with transparent color, or else it will be 
		// impossible to move clock with mouse in abs panel
		g2d.setColor(glassColor);
		g2d.drawRect(0, 0, width, height);
		
		Calendar now = Calendar.getInstance();
		
		String dateString = String.format("%02d.%02d.%02d", now.get(Calendar.DAY_OF_MONTH), now.get(Calendar.MONTH), now.get(Calendar.YEAR));
		
		doSetup(g2d);
		
		g2d.setColor(vis.digitalMeterColor);
		g2d.setFont(myFont);
				
		//g2d.drawString(dateString, 0, 0 );
		
		FontRenderContext frc = g2d.getFontRenderContext();
		Rectangle2D stringBounds = myFont.getStringBounds(dateString, frc);
		
		g2d.drawString(dateString, 
				(int)(width/2-stringBounds.getWidth()/2), 
				(int)(height/2-(stringBounds.getCenterY())));
		
	}

	
	
	
	private void doSetup(Graphics2D g2d) {
		if(myFont == null )
		{
			try {			
				myFont = new Font("Verdana", Font.PLAIN, FONT_SIZE);				
			} catch(Throwable e)
			{
				//log.log(Level.SEVERE, "Can't create font",e);
				myFont = new Font(Font.SANS_SERIF, Font.BOLD, FONT_SIZE);
			}
		}
		if(myMetrics==null && myFont != null) {
			
			myMetrics = getGraphics().getFontMetrics(myFont);
			Dimension myPreferredSize = new Dimension(myMetrics.charWidth('0')*10,myMetrics.getHeight());
			setPreferredSize(myPreferredSize);
			setMinimumSize(myPreferredSize);
			setMaximumSize(myPreferredSize);
			setSize(myPreferredSize);
			getParent().validate();
			getParent().repaint(100);
		}
	}

}
