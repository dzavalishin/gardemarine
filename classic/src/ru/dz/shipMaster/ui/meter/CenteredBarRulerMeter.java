package ru.dz.shipMaster.ui.meter;

import java.awt.Graphics2D;


public class CenteredBarRulerMeter extends GeneralRulerMeter {

	/**
	 * UID
	 */
	private static final long serialVersionUID = 1503440166576888907L;

	public CenteredBarRulerMeter() {}

	//public CenteredBarRulerMeter(DashBoard db) {		super(db);	}

	public void paintDashComponent(Graphics2D g2d) {		
	    int barHorPos = valToPos(currVal);


	    g2d.setColor(vis.getClassIndicatorColor(this.getClass().getSimpleName()));
	    //g2d.setStroke(handStroke);
	    // -1 to compensate for pen width
	    //g2d.drawLine(barHorPos-1, 0, barHorPos-1, height);
	    //g2d.setStroke(defaultStroke);
	    //g.fill3DRect(2, getSize().height-barHeight, getSize().width-3, barHeight,true);		

	    int hPos = height/2-height/6;
	    int barHeight = height/2;
	    
	    g2d.setColor(vis.transparentIndicatorColor);
	    if(currVal > 0)
	    {
	    	g2d.fill3DRect(valToPos(0), hPos, barHorPos-valToPos(0), barHeight, true);
	    }
	    else
	    {
	    	g2d.fill3DRect(barHorPos, hPos, valToPos(0)-barHorPos, barHeight, true);
	    }
	    
	    drawComponentBorder(g2d);
	}
	
}
