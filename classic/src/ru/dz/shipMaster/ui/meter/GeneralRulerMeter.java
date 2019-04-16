package ru.dz.shipMaster.ui.meter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.util.logging.Logger;

import ru.dz.shipMaster.ui.VisualSettings;
import ru.dz.shipMaster.ui.misc.AlarmRegion;

public class GeneralRulerMeter extends GeneralMeter {
    @SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(GeneralRulerMeter.class.getName()); 

	
	/**
	 * Eclipse id. 
	 */
	private static final long serialVersionUID = 2089714855535952612L;
	
	// TO DO move strokes to VisualSettings too
	protected static BasicStroke handStroke = new BasicStroke( 3,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND );

	//private int warnLinePos = 0;
	//private int critLinePos = 0;
	private Paint gradient;
	//private float stepPixels = 50;
	private double stepValue = 10;

	
	
	public GeneralRulerMeter() { }

	/*public GeneralRulerMeter( DashBoard db ) { super(db); }
	
	public GeneralRulerMeter(DashBoard db, double min, double max) {
		super(db, min, max);
		}
	
	public GeneralRulerMeter(DashBoard db, double min, double max, double warn, double crit, String newLegend, String newUnits) {
		super(db, min, max, warn, crit, newLegend, newUnits);
		}*/

	
	

	/* (non-Javadoc)
	 * @see ru.dz.shipMaster.ui.DashComponent#canExtendWidth()
	 */
	@Override
	public boolean canExtendWidth() {
		return true;
	}

	private void setGradient() {
		gradient = new GradientPaint(
				0, height/4, vis.scaleGlimpseColor, 
				0, height*3/4, vis.getClassScaleColor( this.getClass().getSimpleName() ), 
				false); // true means to repeat pattern
	}
	
	{
		vis.setPersonal_4_RulerMeter();		
		setGradient();
	}
	


	@Override
	protected void recalcMeterParameters() {
		Dimension myPreferredSize = new Dimension();
		Dimension myMinimumSize = new Dimension();
		myPreferredSize.height = 40;
		//myPreferredSize.width = 760;
		myPreferredSize.width = 700;
		myMinimumSize.height = myPreferredSize.height; 

		setMinimumSize(myMinimumSize);
		setPreferredSize(myPreferredSize);
		
		//warnLinePos = (int)((width*warnPercent));
		//critLinePos = (int)((width*critPercent));
		stepValue = (maximum-minimum)/6;
	}

	
	
	
	@Override 
	protected void processResize() {
		setGradient();
		//warnLinePos = (int)((width*warnPercent));
		//critLinePos = (int)((width*critPercent));
		float sWidth = 6 * width / 800.0f;
		
		if(sWidth < 1) { sWidth = 1; }
		
		handStroke = new BasicStroke( sWidth ,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND );
		//stepPixels = width/12;
		//if(stepPixels <= 0.1) stepPixels = 50;
		resetDashComponentBackgroundImage();
	}
	
	//private int oldBarHorPos = -1;
	
	public void paintDashComponent(Graphics2D g2d) {		
	    int barHorPos = valToPos(currVal);


	    g2d.setColor(vis.getClassIndicatorColor("GeneralRulerMeter"));
	    g2d.setStroke(handStroke);
	    // -1 to compensate for pen width
	    g2d.drawLine(barHorPos-1, 0, barHorPos-1, height);
	    g2d.setStroke(defaultStroke);
	    //g.fill3DRect(2, getSize().height-barHeight, getSize().width-3, barHeight,true);		

	    drawComponentBorder(g2d);
	}

	protected int valToPos(double val) {
		return (int) (((val-minimum)/((float)maximum-minimum)) * width);
	}

	protected double posToVal(double pos)
	{
		double percent = pos / width;
		return  minimum+(percent*((float)maximum-minimum));
	}
	
	@Override
	public void paintDashComponentBackground(Graphics2D g2d) {
        g2d.setPaint(gradient);
		g2d.fillRect(0, 0, width, height);

		g2d.setStroke(VisualSettings.PLAIN_STROKE);

		/*
		g2d.setColor(vis.tickColor);				
		drawTicks(g2d,stepPixels/10,4);
		drawTicks(g2d,stepPixels/2,8);
		drawTicks(g2d,stepPixels,height);
		g2d.setColor(vis.labelColor);
		drawTickLabels(g2d, stepPixels);		
	    */

		drawTicks(g2d, stepValue/10, 4, false);
		drawTicks(g2d, stepValue/2, 8, false);
		drawTicks(g2d, stepValue, height, true);
		
	    //g2d.setColor( new Color((0x00FFFFFF & vis.warnColor.getRGB()) | 0xAA000000,true) );
	    //g2d.drawLine( warnLinePos, 0, warnLinePos, height );
	    
	    //g2d.setColor( new Color((0x00FFFFFF & vis.critColor.getRGB()) | 0xAA000000,true) );
	    //g2d.drawLine( critLinePos, 0, critLinePos, height );

	    drawHorLabels(g2d);
	    
	    paintAlarmRegions(g2d);
	}

	
	@Override
	protected void paintAlarmRegion(Graphics2D g2d, AlarmRegion r) {
		if( r.isCritical() )
			g2d.setColor(new Color((0x00FFFFFF & vis.critColor.getRGB()) | ALARM_LINE_ALPHA,true));
		else
			g2d.setColor(new Color((0x00FFFFFF & vis.warnColor.getRGB()) | ALARM_LINE_ALPHA,true));
		
		int from = valToPos(r.getFrom());
		int to = valToPos(r.getTo());
		
		if( from < -1 ) from = -1;
		if( to < -1 ) to = -1;
		
		if( from > width ) from = width;
		if( to > width ) to = width;
		
		//g2d.fillRect(0, Math.min(from, to), width, Math.abs(to-from));

		g2d.drawLine( from, 0, from, height );
	    g2d.drawLine( to, 0, to, height );

	}
	
	/*
	private void drawTicks( Graphics2D g2d , float stepPixels, int tickHeight )
	{
		double tickPos = 0;
		while( (tickPos += stepPixels) < width )
			g2d.drawLine((int)tickPos, height, (int)tickPos, height-tickHeight);
	}

	private void drawTickLabels( Graphics2D g2d , float stepPixels )
	{
		float tickPos = 0;
		while( (tickPos += stepPixels) < width )
		{
			//Integer iv = (int)((tickPos+1)*scale);
			Integer iv = (int)posToVal(tickPos+1.0f);
			g2d.drawString(iv.toString(), 4+(int)tickPos, 11);
		}
	}
	*/

	private void drawTicks( Graphics2D g2d , double stepValue, int tickHeight, boolean putLabel )
	{
		double valuePos = minimum;
		while( (valuePos += stepValue) <= maximum )
		{
			int tickPos = (int)valToPos(valuePos);
			g2d.setColor(vis.tickColor);				
			g2d.drawLine((int)tickPos, height, (int)tickPos, height-tickHeight);

			if(putLabel)
			{
				Integer iv = (int)valuePos;
				g2d.setColor(vis.labelColor);
				g2d.drawString(iv.toString()+units, 4+(int)tickPos, 11);
			}
		}
	}

	
}
