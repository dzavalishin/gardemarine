package ru.dz.shipMaster.ui.meter;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import ru.dz.shipMaster.ui.misc.VisualHelpers;

@SuppressWarnings("serial")
public class JustHand extends GeneralInstrumentWithRoundScale {
	BufferedImage imgHand = VisualHelpers.loadImage("justHandMeter_hand.png");
	private int center = 100;

	public JustHand() {
		setBorder(null);
	}

	@Override
	protected void recalcMeterParameters() {
		digitalMeterNChars = calculateDigitalMeterNChars();
		resetDashComponentBackgroundImage();
	}

	@Override
	protected void processResize() {
		setCenter();
	}

	protected void setCenter() {
		if(width < height)	center = width/2;
		else 				center = height/2;
	}

	/* * Degrees. * /
	protected double calcValRotation(double val) {
		//return baseRotationAngle + ((val-minimum)*scaleAngle/(maximum-minimum));
		return ((val-minimum)*360/(maximum-minimum));
	}*/


	@Override
	protected void paintDashComponentBackground(Graphics2D g2d) {
		// none
	}

	@Override
	protected void paintDashComponent(Graphics2D g2d) {
		double rot = Math.toRadians(calcValRotation(currVal));

		g2d.translate(center, center);

	    if(histogramVisible)
	    {
	    	g2d.setColor(vis.histogramColor);
	    	ds.drawCircularPoints(g2d, 0, 0, baseRotationAngle, scaleAngle, center/2);
	    }
	
		g2d.rotate(rot);

		g2d.drawImage(imgHand, -imgHand.getWidth()/2, -imgHand.getHeight()/2, null);

		g2d.rotate(-rot);
		g2d.translate(-center, -center);

	}

}
