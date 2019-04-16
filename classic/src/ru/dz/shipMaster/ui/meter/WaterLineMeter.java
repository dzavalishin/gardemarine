package ru.dz.shipMaster.ui.meter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;

import ru.dz.shipMaster.ui.VisualSettings;
import ru.dz.shipMaster.ui.component.DashImage;
import ru.dz.shipMaster.ui.misc.AlarmRegion;
import ru.dz.shipMaster.ui.misc.VisualHelpers;
import ru.dz.shipMaster.ui.render.ColorifyFilter;
import ru.dz.shipMaster.ui.render.RoundGradientPaint;

@SuppressWarnings("serial")
public class WaterLineMeter extends GeneralMeter implements DashImage {


	protected int center;


	public WaterLineMeter() {
	}

	protected void setCenter() {
		if(width < height)	center = width/2;
		else 				center = height/2;
	}

	@Override
	protected void recalcMeterParameters() {
		double log10 = Math.log10(maximum-minimum);
		digitalMeterNChars = (int)(log10+1);

		if(minimum < 0)
			digitalMeterNChars++; // For '-' sign

		resetDashComponentBackgroundImage();
	}

	private Paint gradient;
	//protected BasicStroke handStroke = new BasicStroke(12*HAND_SCALE,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND );



	@Override 
	protected void processResize() {
		setCenter();

		int displ = (width*75)/200;
		int sz = (width*150)/200;

		gradient = new RoundGradientPaint(
				displ, displ, vis.scaleGlimpseColor, 
				new Point2D.Double(0, sz), vis.getClassScaleColor( this.getClass().getSimpleName() ) );			        

		//handStroke = new BasicStroke((center*HAND_SCALE*8)/200,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND );
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

		//double range = maximum-minimum;


		g2d.translate(center, center);



		{
			Graphics2D ng2d = (Graphics2D) g2d.create();
			ng2d.translate(center, center);
			paintAlarmRegions(ng2d);
			ng2d.dispose();
		}

	}


	private final static Stroke alarmStroke = new BasicStroke(2);  
	
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

		
		//log.log(Level.SEVERE,"baseRotationAngle = "+baseRotationAngle);

		//g2d.drawLine(0, 0, 0, 100);
		
		/*

			g2d.draw(new Arc2D.Double(
					center*(-1+delta),center*(-1+delta),
					center*(2-delta*2),center*(2-delta*2),
					-startAngle+angle_shift,	
					-endAngle+startAngle,
					Arc2D.OPEN
			));

		*/
		//g2d.translate(-center, -center);

		g2d.setStroke(oldStroke);
	}

	

	
	
	
	
	
	
	
	
	{
		setImageFile("ship_mask_140.png");		
	}
	
	static ImageFilter filter = new ColorifyFilter(VisualSettings.global.digitalMeterColor);
	
	private Image iShipPictogram = null;

	private String pictogramFileName;

	private void setImageFile(String name)
	{
		Image image = VisualHelpers.loadImage(name);

		if(image != null)
		{
			FilteredImageSource filteredSrc = new FilteredImageSource(image.getSource(), filter);
			iShipPictogram = Toolkit.getDefaultToolkit().createImage(filteredSrc);
		}
		else
			log.severe("Unable to load image "+name);

	}

	public void setPictogramFileName(String pictogramFileName)
	{
		if(pictogramFileName != null)
		{
			this.pictogramFileName = pictogramFileName;		
			setImageFile(pictogramFileName);
		}
		else
			iShipPictogram = null;
	}

	public String getPictogramFileName() {
		return pictogramFileName;
	}





	public void paintDashComponent(Graphics2D ing2d) {
		//Graphics2D g2d = (Graphics2D) ing2d.create();
		Graphics2D g2d = (Graphics2D) ing2d;
		g2d.translate(center, center);



		if(iShipPictogram != null)
		{

			g2d.setColor(VisualSettings.global.digitalMeterColor);
			g2d.drawLine(-center+10,0,center-10,0);

			g2d.drawImage(iShipPictogram, 
					-iShipPictogram.getWidth(null)/2, 
					-iShipPictogram.getHeight(null)/2 + ((int)currVal), null );
		}



	    /*if(histogramVisible)
	    {
	    	g2d.setColor(vis.histogramColor);
	    	ds.drawVertically(g2d, 0, 0, baseRotationAngle, scaleAngle, center*33/40);
	    }*/

		g2d.translate(-center, -center);

		drawHorLabels(g2d);
		drawDigitalMeter(g2d, currVal);
		//g2d.dispose();
	}



	
	
	
}
