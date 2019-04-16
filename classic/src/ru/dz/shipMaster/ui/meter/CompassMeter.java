package ru.dz.shipMaster.ui.meter;

import java.awt.Graphics2D;
import java.util.logging.Logger;


public class CompassMeter extends GeneralCompassMeter {


	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(CompassMeter.class.getName()); 

	
	/**
	 * Eclipse id. 
	 */
	private static final long serialVersionUID = -2504011250151482509L;
	

	
	public CompassMeter() {}

	
	
	public void paintDashComponent(Graphics2D g2d) {
		g2d.translate(center, center);

	    if(histogramVisible)
	    {
	    	g2d.setColor(vis.histogramColor);
	    	ds.drawCircularPoints(g2d, 0, 0, baseRotationAngle, scaleAngle, center/2);
	    }

		
		//double valRotation = baseRotationAngle + ((currVal-minimum)*scaleAngle/(maximum-minimum));
		double valRotation = calcValRotation(currVal);
		
		g2d.rotate(Math.toRadians(valRotation));
		
		drawHand(g2d);
		
		g2d.rotate(-Math.toRadians(valRotation));
		drawHandCup(g2d, center/10);
		
		g2d.translate(-center, -center);
		
		drawHorLabels(g2d);
		drawDigitalMeter(g2d, currVal);
	}


	@Override
	protected void paintDashComponentBackground(Graphics2D g2d)
	{
		{
		Graphics2D ng2d = (Graphics2D) g2d.create();
		super.paintDashComponentBackground(ng2d);
		// bug - it uses fact of origin translation in super
		drawHorLabel(ng2d, units, 0, height/8, false, TextAnchorX.Center, TextAnchorY.Top);
		ng2d.dispose();
		}
		
		{
		Graphics2D ng2d = (Graphics2D) g2d.create();
		ng2d.translate(center, center);
		paintAlarmRegions(ng2d);
		ng2d.dispose();
		}
	}

}





  
