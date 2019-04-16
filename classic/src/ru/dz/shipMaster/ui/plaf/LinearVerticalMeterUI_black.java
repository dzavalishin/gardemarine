package ru.dz.shipMaster.ui.plaf;

import java.awt.GradientPaint;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import ru.dz.shipMaster.ui.DashComponent;
import ru.dz.shipMaster.ui.misc.AlarmRegion;

public class LinearVerticalMeterUI_black extends LinearVerticalMeterUI {
	public static LinearVerticalMeterUI createUI(JComponent c) { return new LinearVerticalMeterUI_black(); }

	
	
	protected int calcBarHeight(double val) {

		if( val > maximum )
			val = maximum;

		if( val < minimum )
			val = minimum;

		return (int) (((float)(val-minimum)/(float)(maximum-minimum)) * height);
	}

	@Override
	public void paintAlarmRegion(Graphics2D g2d, AlarmRegion r)
	{
		int h1 = calcBarHeight(r.getFrom());
		int h2 = calcBarHeight(r.getTo());
		g2d.setColor(r.isCritical() ? critDarkColor : warnDarkColor);		
		//g2d.fill3DRect(1, height-h1, width-2, height-h2, true);

		/*if(false)
		{
			g2d.fill3DRect(1, height-h2, 6, h2-h1, false);
		}
		else*/
		{
			g2d.fillRect(1, height-h2, 6, h2-h1 );
			g2d.setColor(r.isCritical() ? vis.critColor : vis.warnColor);		
			g2d.drawLine(6, height-h2, 6, height-h1-1);
		}

	}
	
	
	@Override
	public void paintDashComponent(Graphics2D g2d, DashComponent dc) {
		int barHeight = calcBarHeight(m.getCurrentValue());

		g2d.setColor(m.calcValueColor());
		//if(false) paintDashedBar(g2d, barHeight);
		//else
			g2d.fill3DRect(1, height-barHeight, width-2, barHeight,true);

		// No ticks - no unit

		if(m.isHistogramVisible())
		{
			g2d.setColor(vis.histogramColor);
			m.getDs().drawVertically(g2d, 0, 0, width, height);
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
		//warnLinePos = calcBarHeight(m.getWarnVal());
		//critLinePos = calcBarHeight(m.getCritVal());
	
		gradient = new GradientPaint(
				m.getWidth()/4, 0, vis.scaleGlimpseColor, 
				m.getWidth()*3/4, 0, vis.getClassScaleColor( this.getClass().getSimpleName() ), 
				false); // true means to repeat pattern
	
	}
	
	
	@Override
	public void paintDashComponentBackground(Graphics2D g2d, DashComponent dc) {
		g2d.setPaint(gradient);
		g2d.fillRect(0, 0, width, height);
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
	
}
