package ru.dz.shipMaster.ui.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import ru.dz.shipMaster.ui.DashComponent;
import ru.dz.shipMaster.ui.bitFont.BitFont;
import ru.dz.shipMaster.ui.bitFont.ConstantBitFont;
import ru.dz.shipMaster.ui.bitFont.SimpleBitFontRenderer;


@SuppressWarnings("serial")
public class Clock extends DashComponent {
	private final int NCHARS = 8;
	private final BitFont bitFont = new ConstantBitFont();
	private final SimpleBitFontRenderer bfRender = new SimpleBitFontRenderer(bitFont);

	Timer t = new Timer(this.getClass().getName(), true);

	TimerTask clockTask = new TimerTask() {
		
		@Override
		public void run() {
			resetDashComponentBackgroundImage();
			repaint(100);

		}
	}; 

	
	TimerTask syncTask = new TimerTask() {
		
		@Override
		public void run() {

			t.scheduleAtFixedRate(clockTask, 0, 1000);
		}
	}; 
	
	public Clock() {
		processResize();
		setBorder(null);
		//t.schedule(syncTask, 10);
		t.scheduleAtFixedRate(clockTask, 0, 1000);
	}
	
	@Override
	protected void finalize() throws Throwable {
		t.cancel();
		super.finalize();
	}
	
	//private long lastTime = System.currentTimeMillis();
	
	@Override
	protected void paintDashComponent(Graphics2D g2d) {
		/*
		long now = System.currentTimeMillis();
		
		if( now > lastTime+1000 || ( (now % 1000) == 0 )  )
		{
			resetDashComponentBackgroundImage();
			lastTime = now;
			repaint(100);
		}
		*/
		
	}

	
	private static Color glassColor = new Color(0,true); // completely transparent
	
	@Override
	protected void paintDashComponentBackground(Graphics2D g2d) {
		
		// Fill all with transparent color, or else it will be 
		// impossible to move clock with mouse in abs panel
		g2d.setColor(glassColor);
		g2d.drawRect(0, 0, width, height);
		
		Calendar now = Calendar.getInstance();
		
		final int clockY = 0; //height-FROMSIDE-bfRender.stringHeight();
		final int clockX = 0; //width-FROMSIDE-1;
		
		
		String clock = String.format("%02d:%02d:%02d", now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND));
		
		bfRender.render(g2d, 8, clock, clockX, clockY);
	}
	
	@Override
	protected void processResize() {
		Dimension preferredSize = new Dimension(bfRender.stringWidth(NCHARS), bfRender.stringHeight());
		setPreferredSize(preferredSize);
		setSize(preferredSize);
	}
	
}
