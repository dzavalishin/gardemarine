package ru.dz.shipMaster.ui.logger;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Timer;

import ru.dz.shipMaster.ui.DashComponent;
import ru.dz.shipMaster.ui.bitFont.BitFont;
import ru.dz.shipMaster.ui.bitFont.ConstantBitFont;
import ru.dz.shipMaster.ui.bitFont.SimpleBitFontRenderer;

public abstract class GeneralLogWindow extends DashComponent {
	// -----------------------------------------------------------------------
	// Global parameters
	// -----------------------------------------------------------------------

	/**
	 * 
	 */
	private static final long serialVersionUID = -2587537531908014324L;
	protected int lineHeight = 12;
	protected boolean haveClock = false;

	// -----------------------------------------------------------------------
	// Global init
	// -----------------------------------------------------------------------

	/* (non-Javadoc)
	 * @see ru.dz.shipMaster.ui.DashComponent#processResize()
	 */
	@Override
	protected void processResize() {
	}


	public GeneralLogWindow()
	{
		//myPreferredSize.width = 350;
		//myMinimumSize.width = 300;
		setMinimumSize(new Dimension(300,getHeight()));
		setPreferredSize(new Dimension(350,getHeight()));
	}
	
	
	// -----------------------------------------------------------------------
	// State
	// -----------------------------------------------------------------------
	
	protected List<Item> items = new LinkedList<Item>();
	
	
	// -----------------------------------------------------------------------
	// Timer
	// -----------------------------------------------------------------------
	
	//public LogWindow()
	{
		setTimer();
	}
	
	private Timer timer;
	private ActionListener timerAction;
	private void setTimer()
	{
	
		timerAction = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				repaint();
			}	
		};	

		timer = new Timer(200, timerAction);
		timer.setInitialDelay(100);
		timer.start();
	
	}
	

	// -----------------------------------------------------------------------
	// Paint
	// -----------------------------------------------------------------------
	final static int FROMSIDE = 4;

	@Override
	protected void paintDashComponentBackground(Graphics2D g2d)
	{
        // Clear
		g2d.setColor(vis.bgColor);
		g2d.fillRect(0, 0, width, height);

		int lineY = 2;
		int bgSwitch = 0;
		while((lineY += lineHeight) < height)
		{			
			if(((bgSwitch++) % 2) == 0)
			{
				g2d.setColor(vis.labelBgColor);					
				g2d.fillRect( FROMSIDE, lineY-lineHeight, width-(FROMSIDE*2), lineHeight );
			}
			
		}
		drawComponentBorder(g2d);
	}

	
	@Override
	protected void paintDashComponent(Graphics2D g2d) {
        
		int lineY = 0;
		Item lastRenderedItem = null;
		synchronized(items)
		{
			for( Item i : items )
			{
				lineY += lineHeight;
				if( lineY > height )
					break;
				
				lastRenderedItem = i;
				
				g2d.setColor( i.calculateColor() );
				//g2d.drawString(""+i.itemNumber+": "+i.text, 10, lineY );
				g2d.drawString(i.text, 10, lineY );
			}
		}
		
		if(haveClock)
		{
			long lastRenderedTime = lastRenderedItem == null ? 20000 : (System.currentTimeMillis() - lastRenderedItem.creationTime);

			long size = lastRenderedTime * 4 / 30000;
			if( size > 4 ) size = 4;
			if( size < 1 ) size = 1;
			bfRender.setPixelSize((int)size);
			drawClock(g2d);
		}
	}
	
	private BitFont bitFont = new ConstantBitFont();
	private SimpleBitFontRenderer bfRender = new SimpleBitFontRenderer(bitFont);
	
	public static String intTo2Chars(int i)
	{
		String out = Integer.toString(i);
		if(out.length() == 1)
			out = "0"+out;
		return out;
	}
	
	private void drawClock(Graphics2D g2d) {
			final int NCHARS = 8;
			
			Calendar now = Calendar.getInstance();
			
			/*String valS = 
				Integer.toString(now.get(Calendar.HOUR_OF_DAY)) + ":" +
				Integer.toString(now.get(Calendar.MINUTE)) + ":" +
				Integer.toString(now.get(Calendar.SECOND))
				;*/
			
			final int clockY = height-FROMSIDE-bfRender.stringHeight();
			final int clockX = width-FROMSIDE-1;
			
			/*
			bfRender.render(g2d, 2, intTo2Chars(now.get(Calendar.HOUR_OF_DAY)), 
					clockX-bfRender.stringWidth(NCHARS), clockY);
		
			bfRender.render(g2d, 1, ":", 
					clockX-bfRender.stringWidth(NCHARS-2), clockY);

			bfRender.render(g2d, 2, intTo2Chars(now.get(Calendar.MINUTE)), 
					clockX-bfRender.stringWidth(NCHARS-3), clockY);

			bfRender.render(g2d, 1, ":", 
					clockX-bfRender.stringWidth(NCHARS-5), clockY);

			bfRender.render(g2d, 2, intTo2Chars(now.get(Calendar.SECOND)), 
					clockX-bfRender.stringWidth(NCHARS-6), clockY);
			*/
			
			String clock = 
				intTo2Chars(now.get(Calendar.HOUR_OF_DAY)) + ":" +
				intTo2Chars(now.get(Calendar.MINUTE)) + ":" +
				intTo2Chars(now.get(Calendar.SECOND));
			
			bfRender.render(g2d, 8, clock, clockX-bfRender.stringWidth(NCHARS), clockY);
	}

	
	
	// -----------------------------------------------------------------------
	// Item
	// -----------------------------------------------------------------------
	
	
	//static int itemCounter = 0;
	public class Item
	{
		String 		text;
		boolean		isCritical = false; 	// Critical message will wait for confirm!?
		boolean		isTimed = false;		// Will die soon
		long		actualUntil = 0;		// Time to live to, if isTimed == true
		long		creationTime = System.currentTimeMillis();
		//private int			itemNumber = itemCounter++;
		
		private MessageWindow owner = null;
		public void setOwner(MessageWindow owner) {
			this.owner = owner;
		}
		@Override
		protected void finalize() throws Throwable {
			if(owner != null)
				owner.removeItem(this);
			super.finalize();
		}
		
		public Item( String messageText ) 
		{
			text = messageText;
		}

		public Item( String messageText, boolean isCritical ) 
		{
			text = messageText;
			this.isCritical = isCritical;
		}

		public Item( String messageText, int secondsToLive ) 
		{
			text = messageText;
			isTimed = true;
			actualUntil = System.currentTimeMillis()+(secondsToLive*1000);
		}

		/**
		 * 
		 * @param min out min 
		 * @param max out max
		 * @param seconds input max (min is 0)
		 * @param spent input time value to map to min-max interval
		 * @param delay seconds to skip before start
		 * @return value == min if spent == 0 and max if spent == seconds*1000 
		 */
		private float map( float min, float max, int seconds, int delay, long spent )
		{
			spent -= (delay*1000);
			if( spent < 0 ) spent = 0;
			
			double out = min + ((spent/(seconds*1000.0)) * (max-min));
			if( out < min ) out = min;
			if( out > max ) out = max;
			return (float)out;
		}
		
		public Color calculateColor() {
			
			//if(isCritical) return Color.RED;
			
			float hue;
			float saturation;
			float brightness; 
			
			long now = System.currentTimeMillis();
			long spent = now-creationTime;
			
			final float HUE_LOW = 0.13f; 
			final float HUE_HI = 0.5f;
			
			hue = map( HUE_LOW, HUE_HI, 8, 0, spent );
			saturation = 0.6f - map( 0, 0.6f, 12, 8, spent );
			brightness = 1.0f - map( 0, 0.5f, 10, 0, spent );
			
			if(isCritical) 
				{
				hue = 1.0f; // RED
				brightness *= 1.5f; 
				if( brightness > 1.0 ) 
					brightness = 1.0f;
				saturation = 0.8f;
				}
			
			int rgb = Color.HSBtoRGB(hue, saturation, brightness);			
			return new Color(rgb);
		}

		public void renew() { creationTime = System.currentTimeMillis(); }
		public void setText(String string) { text = string; renew(); }

		public boolean isCritical() {			return isCritical;		}
		public void setCritical(boolean isCritical) {			this.isCritical = isCritical;		}
	}
	

}
