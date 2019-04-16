package ru.dz.shipMaster.ui.meter;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import ru.dz.shipMaster.ui.misc.VisualHelpers;

@SuppressWarnings("serial")
public class DirectionMeter extends GeneralMeter {

	private int center = 100;

	BufferedImage imgCenter = VisualHelpers.loadImage("dirMeter_center.png");
	BufferedImage imgOuter = VisualHelpers.loadImage("dirMeter_outer.png");
	BufferedImage imgNose = VisualHelpers.loadImage("dirMeter_nose.png");
	
	public DirectionMeter() {
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
	
	/** Degrees. */
	protected double calcValRotation(double val) {
		//return baseRotationAngle + ((val-minimum)*scaleAngle/(maximum-minimum));
		return ((val-minimum)*360/(maximum-minimum));
	}
	
	
	@Override
	protected void paintDashComponentBackground(Graphics2D g2d) {
		g2d.drawImage(imgCenter, center-imgCenter.getWidth()/2, center-imgCenter.getHeight()/2, null);
	}

	@Override
	protected void paintDashComponent(Graphics2D g2d) {
		double rot = Math.toRadians(calcValRotation(currVal));

		g2d.translate(center, center);
		g2d.rotate(-rot);

		g2d.drawImage(imgOuter, -imgOuter.getWidth()/2, -imgOuter.getHeight()/2, null);

		g2d.rotate(rot);
		g2d.translate(-center, -center);

		g2d.drawImage(imgNose, center-imgNose.getWidth()/2, center-imgNose.getHeight()/2, null);
	}
	
}
