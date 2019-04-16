package ru.dz.shipMaster.ui.meter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;

import ru.dz.shipMaster.ui.element.PixelMeter;
import ru.dz.shipMaster.ui.misc.AlarmRegion;
import ru.dz.shipMaster.ui.render.RoundGradientPaint;

/**
 * The most complex meter ever. Shows data in analog, digital and graphics form. 
 */

public class HalfGaugeMeter extends GeneralMeter {
	//private static final Logger log = Logger.getLogger(HalfGaugeMeter.class.getName()); 


	/**
	 * Eclipse serial id. 
	 */
	private static final long serialVersionUID = -3213662001859291265L;

	/*
	 * Base attributes
	 */

	// Still internal-only:
	//private Color 		warnColor, critColor; //, normalColor;
	private int			separation = 4;
	private int 		margin = separation;
	private double 		minAngleRads; // = Math.PI; //Math.toRadians(180);//Math.PI;	// 180 degrees
	private double 		maxAngleRads; // = 0.0;	// zero degrees
	//private int			penRadius = 1;
	private int			tickSize = 8;
	private int			bigTick = 10;
	private int			littleTick = 5;

	private Point 		pivotLoc;
	private int   		pointerLen;
	private int 		minAngleDegs;
	private int 		maxAngleDegs;
	private Rectangle 	scaleRect;
	private FontMetrics myMetrics = null;
	private double		halfTick = Math.round(tickSize/2.0);

	private Paint 		gradient;

	private int miniGraphY = 10;
	private static boolean miniGraph = true; 

	private GeneralGraph	myMiniGraph;
	private PixelMeter 	myPixelMeter;



	/**
	 * Construct meter.  
	 */
	public HalfGaugeMeter() { /* nothing to do */
	}

	/* *
	 * Constructs a Gauge object. Uses all default values.  
	 * @see Gauge#init
	 * /
	public HalfGaugeMeter(DashBoard db) {
		super(db);
	}*/

	/**
	 * Constructs a Gauge object with specified Min and Max. 
	 * @param min Minimum Gauge value
	 * @param max Maximum Gauge Value
	 * @see Gauge#init
	 */
	public HalfGaugeMeter(int min, int max) {
		super(min,max);
	}

	/**
	 * Constructs a gauge with the specified Min, Max, Legend, and Units values.
	 * @param min			Minimum Value
	 * @param max			Maximum Value
	 * @param newLegend		Gauge Legend
	 * @param newUnits		Gauge Units
	 */
	public HalfGaugeMeter( int min, int max, String newLegend, String newUnits) {
		super( min, max, newLegend, newUnits);
	}

	/*
	 * Utility Routines
	 */

	// Based on current font, determine dimensions of all strings.
	// Reset PointerLen here.
	private void setStringMetrics() {
		if (myMetrics==null) return;

		margin = separation + myMetrics.getHeight();
		pointerLen = (int)Math.round(width/2.0) - margin - separation;
	}

	// Return rect into which the scale-arc will be drawn
	private Rectangle findScaleRect() {
		return new Rectangle(margin, pivotLoc.y - pointerLen, 2*pointerLen, pointerLen);
	}

	// Determine locations of Pivot and Scale, and len of Pointer
	// Called after any size-change
	private void partition() {
		miniGraphY  = height/2 + margin + separation;
		pivotLoc = new Point( width/2, height/2 );
		pointerLen = (int)Math.round(width/2.0) - margin - separation;
		scaleRect = findScaleRect();
	}

	/* *
	 * setup. Called by parent's constructor. 
	 * TO DO: why not in our constructor?
	 */
	//@Override
	//protected void init() 
	{

		vis.setPersonal_4_HalfGaugeMeter();

		minAngleRads = Math.PI; //Math.toRadians(180);//Math.PI;	// 180 degrees
		maxAngleRads = 0.0;	// zero degrees

		partition();

		minAngleDegs = (int)Math.toDegrees(minAngleRads);
		maxAngleDegs = (int)Math.toDegrees(maxAngleRads);

		setForeground(vis.getClassIndicatorColor("HalfGaugeMeter"));

		gradient = new RoundGradientPaint(75, 75, vis.scaleGlimpseColor, new Point2D.Double(0, 125), vis.getClassScaleColor( this.getClass().getSimpleName() ) );			        

		if( miniGraph ) {
			myMiniGraph = new GeneralGraph();
			add(myMiniGraph);
			setupMiniGraph();
			myMiniGraph.setVisible(true);
			//setComponentZOrder(myMiniGraph, 1);
			//resetDashComponentBackgroundImage();
		}

		myPixelMeter = new PixelMeter();
		add(myPixelMeter);
		setComponentZOrder(myPixelMeter, 0);
		myPixelMeter.setVisible(true);
	}


	@Override 
	protected void processResize() {
		partition();
		//repaint();
		if(myMiniGraph != null)
			setMinigraphBounds();
		if(myPixelMeter != null) myPixelMeter.setLocation( width-LABEL_FROM_SIDE-myPixelMeter.getWidth(), height-LABEL_FROM_SIDE-myPixelMeter.getHeight());
	}

	private void setMinigraphBounds() {
		myMiniGraph.setBounds(0+margin, miniGraphY, width-margin*2, height-miniGraphY-margin/2);
	}


	@Override
	public void setSize(Dimension d) {
		setSize(d.width,d.height);
	}

	@Override
	public void setSize(int width, int height) {
		int min = Math.min(width, height);
		//log.log(Level.SEVERE,"Setsize "+width+"x"+height+" -> "+min);
		super.setSize(min, min);
	}

	@Override
	public void setBounds(Rectangle r) {
		setBounds(r.x,r.y,r.width,r.height);
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		int min = Math.min(width, height);
		//log.log(Level.SEVERE,"Setsize "+width+"x"+height+" -> "+min);
		super.setBounds(x, y, min, min);
	}

	/**
	 * Called by superclass on meter min/max or other relevant
	 * values changed.
	 */
	@Override
	protected void recalcMeterParameters() {
		//warnVal = (int)Math.round(maxVal*warnPercent);
		//critVal = (int)Math.round(maxVal*critPercent);
		//minStr = String.valueOf(minVal);
		//maxStr = String.valueOf(maxVal);

		setStringMetrics();

		double log10 = Math.log10(maximum-minimum);
		bigTick = (int)Math.pow(10, log10-1)*2;
		littleTick = bigTick/2;

		if(myPixelMeter != null)
			myPixelMeter.setNChars((int)(log10+1));

		// Now pixelMeter size is changed
		processResize();
	}





	/**
	 * getValColor returns the color associated with the region into which the
	 * value falls.
	 *	@param val		value of interest
	 */
	public Color getValColor(int val) {
		//if (val < warnVal) {			return vis.indicatorColor; } 
		//else if (val < critVal) {		return vis.warnColor; } 
		//else {							return vis.critColor; }

		//return 	(val < warnVal) ? vis.indicatorColor :	((val < critVal) ? vis.warnColor : vis.critColor);

		return calcValueColor();

	}

	/**
	 * mapValToPoint returns a Point on the Scale for the specified value
	 */
	public Point mapValToPoint(double val) {
		double diffRads = minAngleRads - maxAngleRads;
		double valPcnt  = (double)(val-minimum)/(double)(maximum-minimum);
		double valRads  = minAngleRads - diffRads*valPcnt;
		return new Point((int)(pivotLoc.x + Math.round(Math.cos(valRads)*pointerLen)), 
				(int)(pivotLoc.y - Math.round(Math.sin(valRads)*pointerLen)));
	}

	/**
	 * mapValToPoint returns a Point "radius" units from the Pivot for the specified value
	 */
	public Point mapValToPoint(double val, double radius) {
		double diffRads = minAngleRads - maxAngleRads;
		double valPcnt  = (val-minimum)/(maximum-minimum);
		double valRads  = minAngleRads - diffRads*valPcnt;
		return new Point((int)(pivotLoc.x + Math.round(Math.cos(valRads)*radius)), 
				(int)(pivotLoc.y - Math.round(Math.sin(valRads)*radius)));
	}






	//public void update(Graphics g) { paint(g); };

	public String toString() {
		String strBuf = super.toString();
		strBuf += "MnAR: " +minAngleRads+ " MnAD: " +minAngleDegs+ '\n';
		strBuf += "MxAR: " +maxAngleRads+ " MxAD: " +maxAngleDegs+ '\n';
		strBuf += "PivotLoc: " +pivotLoc.toString()+ '\n';
		strBuf += "PtrLen: " +pointerLen+ '\n';
		strBuf += "SclRect : " +scaleRect.toString()+ '\n';
		return strBuf;
	}

	// ----------------------------------------------------------------------------
	// Painting
	// ----------------------------------------------------------------------------

	/**
	 * Paints moving parts and (why?) border
	 */
	@Override
	public void paintDashComponent(Graphics2D g2d) {
		if (myMetrics==null) {
			myMetrics = getGraphics().getFontMetrics();
			setStringMetrics();
		}

		drawPointer(g2d);
		drawComponentBorder(g2d);

		if(histogramVisible)
		{
			g2d.setColor(vis.histogramColor);
			ds.drawCircularPoints(g2d, width/2,height/2, -90, 180, Math.min(height, width)*5/16);
		}

	}


	/** Paints background. */
	@Override
	protected void paintDashComponentBackground(Graphics2D g2d)
	{
		drawDefaultBg(g2d);

		drawScale(g2d);
		drawStrings(g2d);
		drawHorLabels(g2d);
	}

	private static BasicStroke handStroke = new BasicStroke( 2,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND );
	private static BasicStroke normalStroke = new BasicStroke( 1 );


	/**
	 * drawPointer draws the Pivot and the Pointer
	 */
	public void drawPointer(Graphics2D g2d) {
		Point scalePt = mapValToPoint(currVal);
		//g.setColor(getValColor(currVal)); // TO DO use soft color change
		g2d.setColor(calcValueColor());
		g2d.setStroke( handStroke );
		//for (int i=-penRadius;i<=penRadius;i++) 
		g2d.drawLine(pivotLoc.x, pivotLoc.y, scalePt.x, scalePt.y);		
		g2d.setStroke( normalStroke );
		//g.setColor(vis.indicatorColor);
		//g.fillOval(pivotLoc.x-5, pivotLoc.y-5, 11, 11);
		g2d.translate(pivotLoc.x, pivotLoc.y);
		drawHandCup(g2d, width/20);
		g2d.translate(-pivotLoc.x, -pivotLoc.y);
	}



	/**
	 * drawTick draws one tick-mark from center of scale-arc _INWARDS_ 
	 * @param where		Value at which to draw the tick-mark
	 * @param offset		How long to make the tick-mark
	 */
	public void drawTick(Graphics g, double where, double offset) {
		int arcCenter = pointerLen+separation-8; 
		Point startPt = mapValToPoint(where, arcCenter-offset);
		Point endPt   = mapValToPoint(where, arcCenter);
		g.setColor(getValColor((int)where));
		//g.setColor(vis.tickColor);
		g.drawLine(startPt.x, startPt.y, endPt.x, endPt.y);
	}

	/**
	 * drawScale draws the ScaleArc itself, and the tick-marks
	 */
	public void drawScale(Graphics2D g) {

		// Draw the meter-interior.  Then draw the Arc itself.  Then, draw tickmarks.
		//g.setColor(scaleColor);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHints(getHints());

		g2d.setPaint(gradient);
		g.fillArc(margin, margin, 
				width - 2*margin, height - 2*margin,
				maxAngleDegs, (minAngleDegs-maxAngleDegs));

		paintAlarmRegions(g2d);

		//for (int i=-penRadius;i<=penRadius;i+=3) {
		{ 	
			int i = 4;
			int indWidth = width - 2*margin - 2*i;
			int indHeight = height - 2*margin - 2*i;

			Stroke oldStroke = g.getStroke();
			g.setStroke(new BasicStroke(2));

			g.setColor(vis.getClassIndicatorColor("HalfGaugeMeter"));
			g.drawArc(margin+i, margin+i, indWidth, indHeight, maxAngleDegs, minAngleDegs);

			i = 8;
			indWidth = width - 2*margin - 2*i;
			indHeight = height - 2*margin - 2*i;


			g.setColor(vis.getClassIndicatorColor("HalfGaugeMeter"));
			g.drawArc(margin+i, margin+i, indWidth, indHeight,
					maxAngleDegs, minAngleDegs);


			g.setStroke(oldStroke);
		} 

		for (double i=minimum;i<=maximum;i+=littleTick) {drawTick(g,i,halfTick);}
		for (double i=minimum;i<=maximum;i+=bigTick)    {drawTick(g,i,tickSize);}
	}

	protected double calcValRotation(double val) {
		return -90 + ((val-minimum)*180/(maximum-minimum));
	}


	private final static Stroke alarmStroke = new BasicStroke(2);  

	@Override
	protected void paintAlarmRegion(Graphics2D g2d, AlarmRegion r) {

		if( r.isCritical() )
			g2d.setColor(new Color((0x00FFFFFF & vis.critColor.getRGB()) | ALARM_FILL_ALPHA,true));
		else
			g2d.setColor(new Color((0x00FFFFFF & vis.warnColor.getRGB()) | ALARM_FILL_ALPHA,true));

		Stroke oldStroke = g2d.getStroke();
		g2d.setStroke(alarmStroke);

		//g2d.translate(center, center);
		//g2d.rotate(Math.toRadians(valRotation));

		//final double delta = 0.32; // inside the scale
		final double delta = 0.25;
		final int angle_shift = 90;
		double startAngle = calcValRotation(Math.max(r.getFrom(),minimum));
		double endAngle = calcValRotation(Math.min(r.getTo(),maximum));


		int center = Math.min(height, width)/2;

		g2d.draw(new Arc2D.Double(
				center*(delta),center*(delta),
				center*(2-delta*2),center*(2-delta*2),
				-startAngle+angle_shift,	
				-endAngle+startAngle,
				Arc2D.OPEN
		));

		g2d.setStroke(oldStroke);
	}


	/**
	 * drawStrings draws the Legend, Units, Min, and Max.
	 */
	public void drawStrings(Graphics g) {
		//g.setColor(vis.labelColor);
		//g.setFont(getFont());
		//drawLabel( g, legend, width/2, pivotLoc.y, false, TextAnchorX.Center, TextAnchorY.Top );
		drawHorLabel( g, units, width/2, pivotLoc.y*2/3, false, TextAnchorX.Center, TextAnchorY.Top );

		drawHorLabel( g, minStr, separation, pivotLoc.y /*+ minSize.height*/, false, TextAnchorX.Left, TextAnchorY.Top );
		drawHorLabel( g, maxStr, width - separation, pivotLoc.y /*+ maxSize.height*/, false, TextAnchorX.Right, TextAnchorY.Top );
	}

	// ----------------------------------------------------------------------------
	// Minigraph support
	// ----------------------------------------------------------------------------

	private void setupMiniGraph()
	{
		//myMiniGraph.setBounds(0+10, miniGraphY, width-10*2, height-miniGraphY);
		setMinigraphBounds();
		myMiniGraph.setMinimum(minimum);
		myMiniGraph.setMaximum(maximum);
		myMiniGraph.setCurrent(currVal);
		//myMiniGraph.setLogWarnAndCritical(false);
		myMiniGraph.setFont(getFont());
		myMiniGraph.setDrawMarkers(false);
	}


	@Override
	public void setMaximum(double max) {
		super.setMaximum(max);
		if(miniGraph && myMiniGraph != null) myMiniGraph.setMaximum(max);
	}

	@Override
	public void setMinimum(double min) {
		super.setMinimum(min);
		if(miniGraph && myMiniGraph != null) myMiniGraph.setMinimum(min);
	}

	@Override
	public void setCurrent(double newCurr) {
		super.setCurrent(newCurr);
		if( miniGraph && myMiniGraph != null)			myMiniGraph.setCurrent(newCurr);
		if( myPixelMeter != null) myPixelMeter.setValue(newCurr);
	}

}

