package ru.dz.shipMaster.ui.meter;

import java.awt.Dimension;
import java.util.logging.Logger;

import ru.dz.shipMaster.ui.plaf.LinearVerticalMeterUI;
import ru.dz.shipMaster.ui.plaf.LinearVerticalMeterUI_silver;

public class LinearVerticalMeter extends GeneralLinearMeter {
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(LinearVerticalMeter.class.getName()); 


	/**
	 * Eclipse id. 
	 */
	private static final long serialVersionUID = -7425102678567881269L;


	public LinearVerticalMeter() {}

	/*public LinearVerticalMeter(DashBoard db ) { 
		super(db); 
	}

	public LinearVerticalMeter(DashBoard db, int min, int max) {
		super( db, min, max );
	}

	public LinearVerticalMeter(DashBoard db, int min, int max, float warn, float crit, String newLegend, String newUnits) {
		super( db, min, max, warn, crit, newLegend, newUnits );
	}*/

	{
		//setPreferredSize( new Dimension( 50, getHeight() ) );
		setMinimumSize(new Dimension( 30, 100 ));
		setPreferredSize( new Dimension( 50, 200 ) );
		vis.setPersonal_4_LinearVerticalMeter();
		//setGradient();
		updateUI();
	}

	/*private void setGradient() {
		gradient = new GradientPaint(
				width/4, 0, vis.scaleGlimpseColor, 
				width*3/4, 0, vis.scaleColor, 
				false); // true means to repeat pattern
	}*/

	@Override
	protected void recalcMeterParameters() {
		//warnLinePos = calcBarHeight(warnVal);//minVal+((maxVal-minVal)*warnPercent)); //(int)((height*warnPercent));
		//critLinePos = calcBarHeight(critVal);//(int)((height*critPercent));
	}
	
	@Override 
	protected void processResize() {
		//setGradient();
		//recalcMeterParameters();
		//warnLinePos = (int)((height*warnPercent));
		//critLinePos = (int)((height*critPercent));
	}

	/*public void paintDashComponent(Graphics2D g2d) {
		int barHeight = calcBarHeight(currVal);

		//g2d.setColor(myIndicatorColor);
		g2d.setColor(calcValueColor());
		if(false)
			paintDashedBar(g2d, barHeight);
		else
			g2d.fill3DRect(1, height-barHeight, width-2, barHeight,true);

		// No ticks - no unit

		if(histogramVisible)
		{
			g2d.setColor(vis.histogramColor);
			ds.drawVertically(g2d, 0, 0, width, height);
		}
		drawVerLabels(g2d);
	}*/


	/*private int calcBarHeight(double val) {

		if( val > maximum )
			val = maximum;

		if( val < minimum )
			val = minimum;

		return (int) (((float)(val-minimum)/(float)(maximum-minimum)) * height);
	}*/

	//protected final Color warnDarkColor = new Color((0x00FFFFFF & vis.warnColor.getRGB()) | 0xAA000000,true);
	//protected final Color critDarkColor = new Color((0x00FFFFFF & vis.critColor.getRGB()) | 0xAA000000,true);
	//protected final Color warnDarkColor = new Color((0x00FFFFFF & vis.warnColor.getRGB()) | 0x77000000,true);
	//protected final Color critDarkColor = new Color((0x00FFFFFF & vis.critColor.getRGB()) | 0x77000000,true);

	/*
	@Override
	protected void paintAlarmRegion(Graphics2D g2d, AlarmRegion r)
	{
		int h1 = calcBarHeight(r.getFrom());
		int h2 = calcBarHeight(r.getTo());
		g2d.setColor(r.isCritical() ? critDarkColor : warnDarkColor);		
		//g2d.fill3DRect(1, height-h1, width-2, height-h2, true);

		if(false)
		{
			g2d.fill3DRect(1, height-h2, 6, h2-h1, false);
		}
		else
		{
			g2d.fillRect(1, height-h2, 6, h2-h1 );
			g2d.setColor(r.isCritical() ? vis.critColor : vis.warnColor);		
			g2d.drawLine(6, height-h2, 6, height-h1-1);
		}

	}*/

	/*
	@Override
	public void paintDashComponentBackground(Graphics2D g2d) {
		g2d.setPaint(gradient);
		g2d.fillRect(0, 0, width, height);
		//g2d.setPaint(gradient);
		//g2d.fillRect(0, 0, width, height-calcBarHeight());

		g2d.setColor(warnDarkColor);
		g2d.drawLine(0, height-warnLinePos, width, height-warnLinePos );

		g2d.setColor(critDarkColor);
		g2d.drawLine(0, height-critLinePos, width, height-critLinePos );

		paintAlarmRegions(g2d);

		drawComponentBorder(g2d);
	}*/

	// -----------------------------------------------------------------------
	// PLAF support - strange and incomplete yet
	// -----------------------------------------------------------------------

	public void setUI(LinearVerticalMeterUI newUI) { super.setUI(newUI); }
	public LinearVerticalMeterUI getUI() {      return (LinearVerticalMeterUI)ui; }

	public void updateUI() {
		//setUI((SliderUI)UIManager.getUI(this);
		setUI(LinearVerticalMeterUI_silver.createUI(this)); // XXX wrong
	}

}
