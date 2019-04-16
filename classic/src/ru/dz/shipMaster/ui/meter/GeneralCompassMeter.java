package ru.dz.shipMaster.ui.meter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;

import ru.dz.shipMaster.ui.misc.AlarmRegion;
import ru.dz.shipMaster.ui.render.RoundGradientPaint;

public abstract class GeneralCompassMeter extends GeneralInstrumentWithRoundScale implements IInstrumentWithTicks {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5064407329235796092L;
	//private static final int HAND_SCALE = 10;
	private static final int HAND_SCALE = 1;
	protected int numNumbers = 6;
	protected int numMinorTicks = 0;

	protected int center;


	public GeneralCompassMeter() {
		baseRotationAngle = -135; // base rotation of the meter
		scaleAngle = 270;
	}

	protected void setCenter() {
		if(width < height)	center = width/2;
		else 				center = height/2;
	}

	@Override
	protected void recalcMeterParameters() {
		digitalMeterNChars = calculateDigitalMeterNChars();
		resetDashComponentBackgroundImage();
	}

	private Paint gradient;
	protected BasicStroke handStroke = new BasicStroke(12*HAND_SCALE,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND );



	@Override 
	protected void processResize() {
		setCenter();

		int displ = (width*75)/200;
		int sz = (width*150)/200;

		gradient = new RoundGradientPaint(
				displ, displ, vis.scaleGlimpseColor, 
				new Point2D.Double(0, sz), vis.getClassScaleColor( this.getClass().getSimpleName() ) );			        

		handStroke = new BasicStroke((center*HAND_SCALE*8)/200,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND );
		resetDashComponentBackgroundImage();
	}

	{
		processResize();
		vis.setPersonal_4_CompassMeter();
	}


	@Override
	protected void paintDashComponentBackground(Graphics2D g2d)
	{
		//g2d.setColor(vis.bgColor);
		//g2d.fillRect(0, 0, width, height);

		drawDefaultBg(g2d);

		g2d.setPaint(gradient);
		g2d.fillOval(2, 2, 2*center-4,2*center-4);
		drawComponentBorder(g2d);

		double range = maximum-minimum;

		double divisor = range / scaleAngle;

		g2d.translate(center, center);

		double mySA = scaleAngle;
		if( mySA < 360 ) mySA += 1;

		for (double i = 0; i < mySA; i += (scaleAngle / numNumbers) )
		{
			int val = (int)(minimum + i * divisor);

			g2d.rotate(Math.toRadians(i+baseRotationAngle));

			g2d.setColor(vis.tickColor);
			if( nearCrit(val) > 0.999 ) g2d.setColor(vis.critColor);			
			else if( nearWarn(val) > 0.999 ) g2d.setColor(vis.warnColor);

			//g2d.drawLine( 0, -10, 0, -center * 7/10);
			//g2d.drawLine( 0, -center * 5/10, 0, -center * 7/10);

			//if(true)
				g2d.drawLine( 0, -center * 6/10, 0, -center * 7/10);
			/*else
			{
				Shape s = new Line2D.Double(0, -center * 6.0/10, 0, -center * 7.0/10);
				g2d.draw(s);
			}*/
			{
				g2d.setColor(vis.tickColor);
				Integer iv = val;			
				drawHorLabel(g2d,iv.toString(), 0, - center * 85 / 100, false, 
						TextAnchorX.Center, TextAnchorY.Bottom);
			}

			g2d.rotate(-Math.toRadians(i+baseRotationAngle));
		}

		if(numMinorTicks > 0)
		{
			for (double i = 0; i < mySA; i += (scaleAngle / numMinorTicks) )
			{
				//int val = (int)(minimum + i * divisor);

				g2d.rotate(Math.toRadians(i+baseRotationAngle));

				g2d.setColor(vis.tickColor);
				//if( nearCrit(val) > 0.999 ) g2d.setColor(vis.critColor);			
				//else if( nearWarn(val) > 0.999 ) g2d.setColor(vis.warnColor);

				//if(true)
					g2d.drawLine( 0, -center * 12/20, 0, -center * 13/20);
				/*else
				{
					Shape s = new Line2D.Double(0, -center * 12.0/20, 0, -center * 13.0/20);
					g2d.draw(s);
				}*/
				g2d.rotate(-Math.toRadians(i+baseRotationAngle));
			}
		}


	}

	private final static Stroke alarmStroke = new BasicStroke(3.f);  
	
	@Override
	protected void paintAlarmRegion(Graphics2D g2d, AlarmRegion r) {

		//if( r.isCritical() ) g2d.setColor(vis.critColor);			
		//else  g2d.setColor(vis.warnColor);

		if( r.isCritical() )
			g2d.setColor(new Color((0x00FFFFFF & vis.critColor.getRGB()) | ALARM_FILL_ALPHA,true));
		else
			g2d.setColor(new Color((0x00FFFFFF & vis.warnColor.getRGB()) | ALARM_FILL_ALPHA,true));

		Stroke oldStroke = g2d.getStroke();
		g2d.setStroke(alarmStroke);
		
		//g2d.translate(center, center);
		//g2d.rotate(Math.toRadians(valRotation));

		final double delta = 0.22;
		final int angle_shift = 90;
		double startAngle = calcValRotation(Math.max(r.getFrom(),minimum));
		double endAngle = calcValRotation(Math.min(r.getTo(),maximum));

		//if(startAngle < 0 ) startAngle = 0;
		//if(endAngle > startAngle+scaleAngle ) endAngle = startAngle+scaleAngle;
		
		//startAngle = baseRotationAngle;
		//endAngle = baseRotationAngle-10;
		
		//log.log(Level.SEVERE,"baseRotationAngle = "+baseRotationAngle);

		//g2d.drawLine(0, 0, 0, 100);
		
		//if(true)
			g2d.draw(new Arc2D.Double(
					center*(-1+delta),center*(-1+delta),
					center*(2-delta*2),center*(2-delta*2),
					-startAngle+angle_shift,	
					-endAngle+startAngle,
					Arc2D.OPEN
			));
		/*else
			g2d.drawArc(-center, -center, center*2, center*2,
					(int)startAngle, 
					(int)endAngle ); 
		*/
		//g2d.translate(-center, -center);

		g2d.setStroke(oldStroke);
	}

	protected void drawHand(Graphics2D in_g2d) {
		//Graphics2D g2d = (Graphics2D)in_g2d.create();
		Graphics2D g2d = (Graphics2D)in_g2d;
		
		int sizeBase = center*HAND_SCALE;
		g2d.scale(1f/HAND_SCALE, 1f/HAND_SCALE);

		//if(true)
		{
		// wide pen
		g2d.setStroke(handStroke); 
		g2d.setColor(calcValueColor()); 
		g2d.drawLine( 0, 0, 0, sizeBase / 4 - sizeBase );

		g2d.setStroke(defaultStroke);
		g2d.setColor(vis.indicatorHighlightColor); 
		g2d.drawLine( 0, 0, 0, sizeBase / 4 - sizeBase );
		}
		/*else
		{
			// must draw hand into the bitmap and draw bitmap here to prevent aliasing
			g2d.setColor(calcValueColor()); 
			g2d.drawLine( -3, 0, 0, sizeBase / 4 - sizeBase );
			g2d.drawLine(  3, 0, 0, sizeBase / 4 - sizeBase );
			
			//g2d.draw(new Line2D.Float( -3, 0, 0, sizeBase / 4 - sizeBase ) );
			//g2d.draw(new Line2D.Float( 3, 0, 0, sizeBase / 4 - sizeBase ) );
		}*/
	}




	/* (non-Javadoc)
	 * @see ru.dz.shipMaster.ui.meter.IInstrumentWithTicks#setNumNumbers(int)
	 */
	@Override
	public void setNumNumbers(int numNumbers) {
		this.numNumbers = numNumbers;
		if( this.numNumbers  <= 0 )
			this.numNumbers = 1;
	}
	/* (non-Javadoc)
	 * @see ru.dz.shipMaster.ui.meter.IInstrumentWithTicks#getNumNumbers()
	 */
	@Override
	public int getNumNumbers() { return numNumbers; }

	/* (non-Javadoc)
	 * @see ru.dz.shipMaster.ui.meter.IInstrumentWithTicks#getNumMinorTicks()
	 */
	@Override
	public int getNumMinorTicks() {		return numMinorTicks; }
	/* (non-Javadoc)
	 * @see ru.dz.shipMaster.ui.meter.IInstrumentWithTicks#setNumMinorTicks(int)
	 */
	@Override
	public void setNumMinorTicks(int numMinorTicks) { this.numMinorTicks = numMinorTicks; }

}
