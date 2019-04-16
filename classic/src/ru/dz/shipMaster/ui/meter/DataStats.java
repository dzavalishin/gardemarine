package ru.dz.shipMaster.ui.meter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;
import java.util.logging.Logger;

/**
 * Statistics for some data stream
 * 
 * @author dz
 *
 */

public class DataStats {
	static final Logger log = Logger.getLogger("DataStats");

	public static final int RANGES = 36;
	//public static final int RANGES = 5;

	//private static final int DIM_LIMIT = 5;
	private static final float DIM_FACTOR = 0.999f;
	private static final int DIM_MSEC = 100;
	private long lastDimTime = System.currentTimeMillis();

	private double minValue = Double.MAX_VALUE;
	private double maxValue = 0;

	private double[] valueRangeCount = new double[RANGES+1]; // +1 i dont know why
	private double valueCountTotal = 0;
	//private int stepNumber = 0; // TO DO - use time instead?

	{
		for( int i = 0; i < RANGES; i++)
		{
			valueRangeCount[i] = 0;
		}
	}

	public void addValue(double val, double min, double max)
	{
		if( max <= min )
		{
			//log.severe( "min/max out of range: "+min+"-"+max );
			return;
		}

		if( val < minValue ) { minValue = val; }
		if( val > maxValue ) { maxValue = val; }

		double valD = val - min;
		valD = (valD/(max-min));

		// now it is [0,1[

		int pos = (int)(valD*(RANGES));

		if(pos == RANGES) pos--;

		if(pos < 0 && pos >= RANGES)
		{
			log.severe("pos out of range: "+pos);
			return;
		}

		// Time to time diminish older data to wear it's effect out
		//if(stepNumber++ > DIM_LIMIT)
		while( System.currentTimeMillis() - lastDimTime > DIM_MSEC )
		{
			//log.severe("------ dim at step: "+stepNumber);
			//stepNumber -= DIM_LIMIT;
			lastDimTime += DIM_MSEC;
			synchronized(valueRangeCount) {
				for( int i = 0; i < RANGES; i++)
					valueRangeCount[i] *= DIM_FACTOR;
				valueCountTotal *= DIM_FACTOR;
			}
			log.finest("valueCountTotal: "+valueCountTotal);
		}
		// now count in new data
		valueCountTotal += 1.0;

		try {
			if(pos >= 0 && pos <= valueRangeCount.length)
				valueRangeCount[pos] += 1.0;
		} catch(Throwable e) {
			// Catch array index out of bounds error
			//log.log( Level.FINE, "hystogram error, val = "+val, e);
		}

		//log.finest("count in pos: "+pos);

		/*for( int i = 0; i < RANGES; i++)
		{
			System.out.print( " "+valueRangeCount[i] );
		}
		System.out.println();*/

	}

	public void drawVertically( Graphics g, int x, int y, int width, int height )
	{
		Graphics2D g2d = (Graphics2D) g;
		//int lenRange = width;
		int posRange = height;

		/*double maxData = 0;
		for( int i = 0; i < RANGES; i++)
			if( valueRangeCount[i]/valueCountTotal > maxData ) maxData = valueRangeCount[i]/valueCountTotal;
		 */
		//if(true)
		{
			Color c = g2d.getColor();

			for( int i = 0; i < RANGES; i++)
			{
				//int len = (int)(calcBarLen(lenRange, i) * 1.5);
				int pos = calcBarPos(posRange, i, y);

				//if( len > 0)
				{
					//g.drawLine(x, pos, x+len, pos);

					float alpha = (float)Math.min(0.93f, calcBarLen(1, i)*8)+0.07f;
					g2d.setColor( new Color(c.getRed()/255.0f,c.getGreen()/255.0f,c.getBlue()/255.0f, alpha) );

					int xpos = x+3;
					int ypos = pos;

					double sz = 1;//len*4;
					g2d.fill(
							new Arc2D.Double(
									xpos-sz,ypos-sz,
									sz*2,sz*2,
									0, 360, Arc2D.OPEN
							));

				}
			}
		}

	}

	public void drawVerticallyGraph( Graphics g, int x, int y, int width, int height )
	{
		Graphics2D g2d = (Graphics2D) g;
		int lenRange = width;
		int posRange = height;

		if(true)
		{
			Color c = g2d.getColor();
			c = new Color(c.getRed()/255.0f,c.getGreen()/255.0f,c.getBlue()/255.0f,0.5f);
			g2d.setColor(c);
			Polygon s = new Polygon();

			//s.addPoint(x,y);
			s.addPoint(x,y+height-1);

			//s.addPoint(x+width,y);
			//s.addPoint(x+width,y+height-1);

			/* */
			for( int i = 0; i < RANGES; i++)
			{
				int len = (int)(calcBarLen(lenRange, i) * 1.5);
				int pos = calcBarPos(posRange, i, y);

				s.addPoint(x+len, pos);

			}
			/* */
			//s.addPoint(x,calcBarPos(posRange, RANGES-1, y));
			s.addPoint(x,y);


			g2d.fill(s);
		}
		/*
		else if(true)
		{
			for( int i = 1; i < RANGES; i++)
			{
				int len0 = (int)(calcBarLen(lenRange, i-1) * 1.5);
				int pos0 = calcBarPos(posRange, i-1, y);

				int len1 = (int)(calcBarLen(lenRange, i) * 1.5);
				int pos1 = calcBarPos(posRange, i, y);

				if(len0 > 0 && len1 >0)
					g.drawLine(x+len0, pos0, x+len1, pos1);
			}
		}
		else
		{
			for( int i = 0; i < RANGES; i++)
			{
				int len = (int)(calcBarLen(lenRange, i) * 1.5);
				int pos = calcBarPos(posRange, i, y);

				if( len > 0)
					g.drawLine(x, pos, x+len, pos);
			}
		}
		*/
	}


	private int calcBarPos(int posRange, int i, int y) {
		double barPos = (((double)i)/RANGES) * posRange;
		return y+posRange-((int)barPos);
	}

	private double calcBarLen(double lenRange, int i) {
		return lenRange * (valueRangeCount[i]/valueCountTotal);
	}

	protected double stepAngleDegrees(double scaleAngle) {
		return scaleAngle/RANGES;
	}


	protected double calcRangeRotationDegrees(int range, double baseRotationAngle, double scaleAngle) {
		double barPos = (((double)range)/RANGES);
		return baseRotationAngle + (barPos*scaleAngle);
	}

	protected double calcRangeRotation(int range, double baseRotationAngle, double scaleAngle) {
		return -Math.toRadians( 180 + calcRangeRotationDegrees(range, baseRotationAngle, scaleAngle) );
	}

	//private static RenderingHints hints = new RenderingHints(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);

	public void drawCircular(Graphics2D g2d, int x, int y,
			double baseRotationAngle, double scaleAngle, int height) {

		Path2D path = new Path2D.Double();

		int shift = 10;

		path.moveTo(x,y);

		//g2d.addRenderingHints(hints);

		for( int i = 0; i < RANGES; i++)
		{
			//int len0 = shift+(int)(calcBarLen(height, i-1) * 1.5);
			//double rot0 = calcRangeRotation(i-1, baseRotationAngle, scaleAngle);

			int len1 = shift+(int)(calcBarLen(height, i) * 1.5);
			double rot1 = calcRangeRotation(i, baseRotationAngle, scaleAngle);

			//if(len0 > 0 && len1 >0)
			{
				//path.moveTo( x+Math.sin(rot0)*len0, y+Math.cos(rot0)*len0 ); 
				//path.lineTo( x+Math.sin(rot1)*len1, y+Math.cos(rot1)*len1 );
				//path.moveTo(x,y);
				path.lineTo( x+Math.sin(rot1)*len1, y+Math.cos(rot1)*len1 );
			}
		}

		path.lineTo(x,y);


		g2d.draw(path);

	}


	public void drawCircularAlpha(Graphics2D g2d, int x, int y,
			double baseRotationAngle, double scaleAngle, int height) {

		//Path2D path = new Path2D.Double();		
		//int shift = 10;

		Color c = g2d.getColor();
		g2d.setColor( new Color(c.getRed()/255.0f,c.getGreen()/255.0f,c.getBlue()/255.0f, 0.6f) );

		Stroke oldStroke = g2d.getStroke();

		for( int i = 1; i < RANGES; i++)
		{
			double len0 = calcBarLen(1, i-1);
			double rot0 = -calcRangeRotationDegrees(i-1, baseRotationAngle, scaleAngle) + 90;

			double len1 = calcBarLen(1, i);
			double rot1 = -calcRangeRotationDegrees(i, baseRotationAngle, scaleAngle) + 90;

			float val = (float)((len0+len1)/2);

			g2d.setStroke(new BasicStroke(Math.min( val*20, 3) ));

			{
				/*
				if(false)
				g2d.draw( new Line2D.Double(
						x+Math.sin(rot0)*len0, y+Math.cos(rot0)*len0,
						x+Math.sin(rot1)*len1, y+Math.cos(rot1)*len1
						));
				 */
				//System.out.print(" rot="+rot0);
				g2d.draw(
						new Arc2D.Double(
								-height,-height,
								height*2,height*2,
								rot0,2*(rot1-rot0), Arc2D.OPEN
						));
			}
		}

		g2d.setStroke(oldStroke);

	}

	public void drawCircularPoints(Graphics2D g2d, int x, int y,
			double baseRotationAngle, double scaleAngle, int height) {

		Color c = g2d.getColor();
		g2d.setColor( new Color(c.getRed()/255.0f,c.getGreen()/255.0f,c.getBlue()/255.0f, 0.6f) );

		Stroke oldStroke = g2d.getStroke();

		double angleShift = Math.toRadians(stepAngleDegrees(scaleAngle))/2;

		for( int i = 0; i < RANGES; i++)
		{
			double len = calcBarLen(4, i);
			double rot = calcRangeRotation(i, baseRotationAngle, scaleAngle);

			//g2d.setStroke(new BasicStroke(Math.min( val*20, 3) ));

			rot -= angleShift; 

			{
				double xpos = x+Math.sin(rot)*height;
				double ypos = y+Math.cos(rot)*height;

				float alpha = (float)Math.min(0.93f, len*2)+0.07f;
				g2d.setColor( new Color(c.getRed()/255.0f,c.getGreen()/255.0f,c.getBlue()/255.0f, alpha) );

				double sz = 1;//len*4;
				g2d.fill(
						new Arc2D.Double(
								xpos-sz,ypos-sz,
								sz*2,sz*2,
								0, 360, Arc2D.OPEN
						));
			}
		}

		g2d.setStroke(oldStroke);

	}

}
