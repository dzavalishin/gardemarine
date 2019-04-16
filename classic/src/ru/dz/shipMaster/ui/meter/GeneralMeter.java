package ru.dz.shipMaster.ui.meter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.logging.Logger;

import ru.dz.shipMaster.config.ConfigurationFactory;
import ru.dz.shipMaster.config.VisualConfiguration;
import ru.dz.shipMaster.config.items.CliAlarm.NearStatus;
import ru.dz.shipMaster.config.items.CliParameter;
import ru.dz.shipMaster.data.history.ParameterHistory;
import ru.dz.shipMaster.data.units.Unit;
import ru.dz.shipMaster.ui.DashComponent;
import ru.dz.shipMaster.ui.VisualSettings;
import ru.dz.shipMaster.ui.misc.AlarmRegion;
import ru.dz.shipMaster.ui.misc.AlarmRegionSet;
import ru.dz.shipMaster.ui.misc.VisualHelpers;
import ru.dz.shipMaster.ui.plaf.MeterUI;
import ru.dz.shipMaster.ui.render.RoundGradientPaint;

/**
 * General base class for controls, used to display a range of values
 * using some kind of a "meter" format.
 * <P>
 * Accessible Attributes:
 * <PRE>
 * minimum           Low end of the scale
 * maximum           High end of the scale
 * CurrentValue      Current setting (where the Pointer points)
 * Legend            What we're measuring (e.g., Speed)
 * Units             What units we're using (e.g., MPH)
 * </PRE>
 * <P>
 */


public abstract class GeneralMeter extends DashComponent implements Meter  {
	/**
	 * Generated UID.
	 */
	private static final long serialVersionUID = 7454075964512123037L;

	protected
	static final Logger log = Logger.getLogger(GeneralMeter.class.getName()); 

	protected static final int ALARM_FILL_ALPHA = 0x77000000;
	protected static final int ALARM_LINE_ALPHA = 0xAA000000;



	protected double minimum;
	protected double maximum;
	protected double currVal;



	protected String legend;
	protected String units;
	/** string representation of minimum value */
	protected String maxStr;
	/** string representation of maximum value */
	protected String minStr;

	protected DataStats	ds = new DataStats();

	// Getters/setters

	public double getMinimum() {		return minimum;	}
	public double getMaximum() {		return maximum;	}
	@Override public double getCurrentValue() {		return currVal;	}

	public String getLegend() {		return legend;	}
	public String getUnits() {		return units;	}

	/**
	 * Set value to display.
	 * @param newCurr New value
	 */
	public void setCurrent(double newCurr) {
		currVal = newCurr;
		ds.addValue(newCurr, minimum, maximum); // XXX data stats must be removed from meter either
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() { repaint(50); }
		});
		//repaint(50);
		//setToolTipText(String.format("%.1f", currVal));
	}

	public void setCurrent(double newCurr, NearStatus nearStatus) {
		this.nearStatus = nearStatus;
		setCurrent(newCurr);
	}


	protected NearStatus nearStatus;

	/**
	 * Units for the input value.
	 */
	@SuppressWarnings("unused")
	private Unit inputUnit;

	/**
	 * Units to display in.
	 */
	@SuppressWarnings("unused")
	private Unit displayUnit;

	/**
	 * Change the Gauge's Legend (what this Gauge is measuring; e.g., Speed or Swap Rate)
	 * @param newLegend	Legend's new value
	 */
	public void setLegend(String newLegend) {
		legend = newLegend;
		resetDashComponentBackgroundImage(); // legend is in background usually
		callRecalcMeterParameters();
		repaint(100);
	}
	/**
	 * Change meter's units (what value is measured in; e.g., KPH or RPM)
	 * @param newUnits Measure unit name
	 */
	//@Deprecated
	public void setUnits(String newUnits) {
		units = newUnits;
		resetDashComponentBackgroundImage(); // units label is in background usually
		callRecalcMeterParameters();
		repaint(100);
	}

	/**
	 * @param unit Measurement unit.
	 */
	public void setInputUnit(Unit unit) {
		this.inputUnit = unit;
		this.displayUnit = unit;
		if(unit != null)
			units = unit.getName();
		resetDashComponentBackgroundImage(); // units label is in background usually
		callRecalcMeterParameters();
		repaint(100);
	}


	/**
	 * Change the meter's Minimum Value 
	 * @param min New Minimum
	 */
	public void setMinimum(double min) {
		minimum = min;
		minStr = String.valueOf(minimum);
		callRecalcMeterParameters();
		repaint(100);
	}
	/**
	 * Change the meter's Maximum Value 
	 * @param max New Maximum
	 */
	public void setMaximum(double max) {
		maximum = max;
		maxStr = String.valueOf(maximum);
		callRecalcMeterParameters();
		repaint(100);
	}


	// -----------------------------------------------------------------------
	// Init and misc. helpers
	// -----------------------------------------------------------------------

	/**
	 * Called when min, max, warn or crit values are changed and after init().
	 */
	protected abstract void recalcMeterParameters();

	private void callRecalcMeterParameters()
	{
		recalcMeterParameters();
		if(ui != null)
			((MeterUI)ui).recalcMeterParameters();
	}

	protected static double d2r(int degs) {		return  Math.toRadians(degs);	}
	protected static int r2d(double rads) {		return (int)Math.toDegrees(rads);	}



	/**
	 * This one is for deserialization.  
	 */
	public GeneralMeter() { topInit(); }


	/**
	 * Constructs meter with specified Min and Max. 
	 * @param min Minimum display value
	 * @param max Maximum display value
	 */
	public GeneralMeter( double min, double max ) {
		topInit();
		setMinimum(min);
		setMaximum(max);
		setCurrent(min);
	}

	/**
	 * Constructs a Gauge with the specified Min, Max, Legend, and Units values.
	 * @param min		Minimum Value
	 * @param max		Maximum Value
	 * @param newLegend		Gauge Legend
	 * @param newUnits		Gauge Units
	 */
	//@SuppressWarnings({"InstanceVariableUsedBeforeInitialized"})
	public GeneralMeter( double min, double max, String newLegend, String newUnits) {
		topInit();
		setMinimum(min);
		setMaximum(max);
		//setCurrent(min);

		setLegend(newLegend);
		setUnits(newUnits);
	}

	{
		setCurrent(minimum);
	}

	/* *
	 * Must be implemented in children to initialize them
	 *
	 */
	//protected abstract void init();

	private final void topInit()
	{		
		recalcMeterParameters();
		setVisible(true);		
		//setOp
	}


	
    
    /** 
     * @return true if component is ready to be extended horizontally.
     */
    public boolean canExtendWidth() { return ConfigurationFactory.getVisualConfiguration().isGlobalExtendWidth() ? true : super.canExtendWidth(); }

    /** 
     * @return true if component is ready to be extended vertically.
     */
    public boolean canExtendHeight() { return ConfigurationFactory.getVisualConfiguration().isGlobalExtendHeight() ? true : super.canExtendHeight(); }	
	// -----------------------------------------------------------------------
	// Visibility
	// -----------------------------------------------------------------------

	protected boolean histogramVisible = true;

	public boolean isHistogramVisible() {		return histogramVisible;	}
	public void setHistogramVisible(boolean histogramVisible) {		this.histogramVisible = histogramVisible;	}


	// -----------------------------------------------------------------------
	// Render of default elements
	// -----------------------------------------------------------------------

	//@Deprecated
	protected void drawHorLabels(Graphics2D g2d) {
		// TODO: Why -2?
		drawHorLabel(g2d, legend, width-LABEL_FROM_SIDE+1, /*12+*/LABEL_FROM_SIDE-3, true, 
				TextAnchorX.Right, TextAnchorY.Top );
	}

	//@Deprecated
	public void drawVerLabels(Graphics2D g2d) {
		// TODO: Why -2?
		drawVerLabel(g2d, legend, width-LABEL_FROM_SIDE + 4, /*12+*/LABEL_FROM_SIDE, true, 
				TextAnchorX.Right, TextAnchorY.Top );
	}

	private BufferedImage meterBgImage = VisualHelpers.loadImage("aluminium_meter_bg.png");
	private final BufferedImage meterCenterImage = VisualHelpers.loadImage("aluminium_meter_center.png");

	protected void drawHandCup(Graphics2D g2d, int size) {
		if(size <= 0) size = 4;
		/*Paint cupGrad = new RoundGradientPaint(
				size/2, size/2, vis.bgColor, 
				new Point2D.Double(size,0), vis.cupColor 
		);*/

		if(meterCenterImage == null)
		{
			Paint cupGrad = new RoundGradientPaint(
					-size/2, -size/2, vis.bgColor, 
					new Point2D.Double(size,0), vis.cupColor 
			);
			g2d.setPaint(cupGrad);
			g2d.fillOval(-size, -size, size*2, size*2);
			g2d.setColor(vis.bgColor);
			g2d.drawOval(-size, -size, size*2, size*2);
		}
		else
		{
			//g2d.drawImage(meterCenterImage, width/2-size/2, height/2-size/2, width/2+size/2, height/2+size/2, 0, 0, meterCenterImage.getWidth(), meterCenterImage.getHeight(), null);
			g2d.drawImage(meterCenterImage, -size, -size, size, size, 0, 0, meterCenterImage.getWidth(), meterCenterImage.getHeight(), null);

		}
	}

	protected void drawDefaultBg(Graphics g) {
		if(meterBgImage != null)
		{
			//g.drawImage(meterBgImage, width/2-meterBgImage.getWidth()/2, height/2-meterBgImage.getHeight()/2, null);
			g.drawImage(meterBgImage, 0, 0, width, height, 0, 0, meterBgImage.getWidth(), meterBgImage.getHeight(), null);
			//meterBgImage, width/2-meterBgImage.getWidth()/2, height/2-meterBgImage.getHeight()/2,  null);
		}
		else
		{
			g.setColor(vis.bgColor);
			g.fillRect(0,0, width, height);
		}

	}


	// --------------------------------------------------------------------
	// This block calculates hand/other indicator color by value,
	// so tha color will change softly when value is approaching warning 
	// and critical levels 	 
	// --------------------------------------------------------------------


	/** 
	 * Defines what 'warning value' is. 
	 * @return 0 if value is 'far' from warning threshold (more then 10% from) 
	 * and 1 if equals or over. Chhanges from 0 to 1 in between.  
	 */
	protected double nearWarn(double value)
	{
		if(nearStatus != null)
			return nearStatus.nearWarningLevel;

		return 0;
	}

	/**
	 * Defines what 'critical value' is. 
	 * @return 0 if value is 'far' from critical threshold (more then 10% from) 
	 * and 1 if equals or over. Chhanges from 0 to 1 in between.  
	 */
	protected double nearCrit(double value)
	{
		if(nearStatus != null)
			return nearStatus.nearCriticalLevel;

		return 0;
	}

	private static float [] indicatorColorHSB = new float[3];
	static {
		VisualConfiguration vc = ConfigurationFactory.getVisualConfiguration();
		Color c = vc.getVisualSettings().getClassIndicatorColor(null);
		Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), indicatorColorHSB);
	}

	// XXX move to MeterUI
	public Color calcValueColor()
	{

		float hue = indicatorColorHSB[0];

		float warnDiff = VisualSettings.warnHue - hue;
		hue += warnDiff * nearWarn(currVal);

		float critDiff = VisualSettings.critHue - hue;
		hue += critDiff * nearCrit(currVal);


		return new Color( 
				(0x00FFFFFF & Color.HSBtoRGB(hue, indicatorColorHSB[1], indicatorColorHSB[2]))
				|
				//myIndicatorColor.getAlpha() << 24
				vis.getClassIndicatorColor("GeneralMeter").getAlpha() << 24
				,
				true);
	}

	/**
	 * 
	 * @return Number of characters needed to print our value, including possible minus sign.
	 */
	protected int calculateDigitalMeterNChars() {
		double log10 = Math.log10(maximum-minimum);
		int nChars = (int)(log10+1);
	
		if(minimum < 0)
			nChars++; // For '-' sign
		
		return nChars;
	}	
	

	// -----------------------------------------------------------------------
	// Debug
	// -----------------------------------------------------------------------

	public String toString() {
		String strBuf = getClass().getName();
		strBuf += "Rect: " +getBounds().toString();
		strBuf += ", Min : " +minimum+ " Max: " +maximum+ " Curr: " +currVal;
		strBuf += ", Lgnd: " +legend;
		strBuf += ", Unit: " +units;
		return strBuf;
	}

	// -----------------------------------------------------------------------
	// Parameter interface
	// -----------------------------------------------------------------------

	private CliParameter parameter = null;

	public CliParameter getParameter() {		return parameter;	}
	public void setParameter(CliParameter parameter) {		
		if(this.parameter != null)
			this.parameter.detachMeter(this);
		this.parameter = parameter;
		if(this.parameter != null)
			this.parameter.attachMeter(this);
	}

	private AlarmRegionSet regions = null;

	public void setAlarmRegions(AlarmRegionSet regions) {
		this.regions  = regions;
		callRecalcMeterParameters();
		repaint();
	}

	// XXX temp. made public for FLAF implementation -- must go to pluggable UI class
	public void paintAlarmRegions(Graphics2D g2d)
	{
		if(regions == null)
			return;

		if(ui != null)
		{
			// Paint criticals over warnings
			for( Iterator<AlarmRegion> i = regions.iterator(); i.hasNext();)
			{
				AlarmRegion region = i.next();
				if(region.isCritical()) continue;
				((MeterUI)ui).paintAlarmRegion(g2d,region);
			}
			for( Iterator<AlarmRegion> i = regions.iterator(); i.hasNext();)
			{
				AlarmRegion region = i.next();
				if(!region.isCritical()) continue;
				((MeterUI)ui).paintAlarmRegion(g2d,region);
			}
		}
		else
		{
			// Paint criticals over warnings
			// XXX alarm regions draw in Meter to be killed
			for( Iterator<AlarmRegion> i = regions.iterator(); i.hasNext();)
			{
				AlarmRegion region = i.next();
				if(region.isCritical()) continue;
				paintAlarmRegion(g2d,region);
			}
			for( Iterator<AlarmRegion> i = regions.iterator(); i.hasNext();)
			{
				AlarmRegion region = i.next();
				if(!region.isCritical()) continue;
				paintAlarmRegion(g2d,region);
			}
		}
	}

	//abstract 
	protected void paintAlarmRegion(Graphics2D g2d, AlarmRegion r) {}

	public DataStats getDs() {
		return ds;
	}

	protected ParameterHistory getHistory() {
		if(parameter == null) return null;
		return parameter.getHistory();
	}


	/**
	 * Receive image data. Most meters don't need it, others will override.
	 */

	@Override
	public void receiveImage(Image val) {
		// Ignored for most of meters		
	}

	/**
	 * Receive string data. Most meters don't need it, others will override.
	 */
	@Override
	public void receiveString(String val) {
		// Ignored for most of meters		
	}
}
