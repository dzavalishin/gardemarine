package ru.dz.shipMaster.ui.plaf;

import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import ru.dz.shipMaster.ui.DashComponent;
import ru.dz.shipMaster.ui.misc.AlarmRegion;
import ru.dz.shipMaster.ui.misc.VisualHelpers;



public class LinearVerticalMeterUI_silver extends LinearVerticalMeterUI {

	public static LinearVerticalMeterUI createUI(JComponent c) { return new LinearVerticalMeterUI_silver(); }

	private int actualMeterLeftX;
	private int actualMeterRightX;

	protected int calcBarHeight(double val) {

		if( val > maximum )
			val = maximum;

		if( val < minimum )
			val = minimum;

		int gap = actualMeterLeftX;
		
		return (int)(((val-minimum)/(maximum-minimum)) * (height-(2*gap)));
	}

	
	@Override
	public void paintDashComponent(Graphics2D g2d, DashComponent dc) {
		int barHeight = calcBarHeight(m.getCurrentValue());

		//g2d.setColor(myIndicatorColor);
		g2d.setColor(m.calcValueColor());
		/*if(false)			
			paintDashedBar(g2d, barHeight);
		else*/
		{
			g2d.fill3DRect(actualMeterLeftX, height-barHeight, actualMeterRightX-actualMeterLeftX, barHeight-actualMeterLeftX,true);
		}

		// No ticks - no unit

		if(m.isHistogramVisible())
		{
			g2d.setColor(vis.histogramColor);
			m.getDs().drawVertically(g2d, actualMeterLeftX, actualMeterLeftX+1, actualMeterRightX-actualMeterLeftX, height-2-actualMeterLeftX*2);
		}
		m.drawVerLabels(g2d);
	}

	/*
	private void paintDashedBar(Graphics2D g2d, int barHeight) {
		int stepSize = 10;
		int startHeight = 0;
		for( startHeight = 0; startHeight+stepSize < barHeight; startHeight += stepSize  )
			g2d.fill3DRect(2, height-startHeight+stepSize, width-3, stepSize*5/6 ,true);

		g2d.fill3DRect(2, height-startHeight-barHeight, width-3, barHeight-startHeight,true);		
	}
	*/
	
	@Override
	public void processResize(DashComponent dc) 
	{
		super.processResize(dc);

		actualMeterLeftX = width/8;
		actualMeterRightX = width*5/8;
		
		gradient = new GradientPaint(
				m.getWidth()/4, 0, vis.scaleGlimpseColor, 
				m.getWidth()*2/4, 0, vis.getClassScaleColor( this.getClass().getSimpleName() ), 
				false); // true means to repeat pattern
	
	}
	
	//private BufferedImage verticalMeterBgImage = VisualHelpers.loadImage("vertical_meter_bg_bad.bmp");
	private BufferedImage verticalMeterBgImage = VisualHelpers.loadImage("aluminium_meter_bg.png"); 
	
	@Override
	public void paintDashComponentBackground(Graphics2D g2d, DashComponent dc) {
		if(verticalMeterBgImage != null)
		{
			//g2d.drawImage(verticalMeterBgImage, 0, 0, null);
			g2d.drawImage(verticalMeterBgImage, 0, 0, width, height, 0, 0, verticalMeterBgImage.getWidth(), verticalMeterBgImage.getHeight(), null);
		}
		else
		{
			g2d.setColor(vis.bgColor);
			g2d.fillRect(0, 0, width, height);
		}
		
		g2d.setPaint(gradient);
		g2d.fillRect(actualMeterLeftX, actualMeterLeftX, actualMeterRightX-actualMeterLeftX, height-actualMeterLeftX*2);
		//g2d.setPaint(gradient);
		//g2d.fillRect(0, 0, width, height-calcBarHeight());

		/*
		g2d.setColor(warnDarkColor);
		g2d.drawLine(0, height-warnLinePos, width, height-warnLinePos );

		g2d.setColor(critDarkColor);
		g2d.drawLine(0, height-critLinePos, width, height-critLinePos );
		 */
		m.paintAlarmRegions(g2d);

		m.drawComponentBorder(g2d);

	}

	
	@Override
	public void paintAlarmRegion(Graphics2D g2d, AlarmRegion r)
	{
		int h1 = calcBarHeight(r.getFrom());
		int h2 = calcBarHeight(r.getTo());
		g2d.setColor(r.isCritical() ? critDarkColor : warnDarkColor);		
		//g2d.fill3DRect(1, height-h1, width-2, height-h2, true);

		//int leftSide = actualMeterLeftX;
		//int rightSide = leftSide+5;

		int leftSide = actualMeterRightX-3;
		int rightSide = actualMeterRightX;
		
		/*if(false)
		{
			g2d.fill3DRect(leftSide, height-h2, rightSide, h2-h1, false);
		}
		else*/
		{
			g2d.fillRect(leftSide, height-h2, rightSide-leftSide, h2-h1 );
			g2d.setColor(r.isCritical() ? vis.critColor : vis.warnColor);		
			//g2d.drawLine(rightSide, height-h2, rightSide, height-h1-1);
			g2d.drawLine(leftSide, height-h2, leftSide, height-h1-1);
		}

	}
	
}
