package ru.dz.shipMaster.ui.meter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.util.logging.Logger;

import ru.dz.shipMaster.ui.VisualSettings;
import ru.dz.shipMaster.ui.component.DashImage;
import ru.dz.shipMaster.ui.misc.VisualHelpers;
import ru.dz.shipMaster.ui.render.ColorifyFilter;

public class ClinoMeter extends GeneralCompassMeter implements DashImage {
	private static final Logger log = Logger.getLogger(ClinoMeter.class.getName()); 

	static ImageFilter filter = new ColorifyFilter(VisualSettings.global.digitalMeterColor);

	/**
	 * Serial (eclipse).
	 */
	private static final long serialVersionUID = 1966045694875078139L;



	public ClinoMeter() {
		super();
		setMinimum(-15);
		setMaximum(15);
		baseRotationAngle = -90;
		scaleAngle = 180;
		setImageFile("ship_mask_140.png");
	}

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


	/** Given value is ignored. Allways 15. */
	@Override
	public void setMaximum(double max) {
		super.setMaximum(15);
	}

	/** Given value is ignored. Allways -15. */
	@Override
	public void setMinimum(double min) {
		super.setMinimum(-15);
	}



	public void paintDashComponent(Graphics2D ing2d) {
		//Graphics2D g2d = (Graphics2D) ing2d.create();
		Graphics2D g2d = (Graphics2D) ing2d;
		g2d.translate(center, center);

		double valRotation = calcValRotation(currVal);

		g2d.rotate(Math.toRadians(valRotation));

		if(iShipPictogram != null)
		{

			g2d.setColor(Color.BLACK);
			g2d.drawLine(-2,-2,2,2);

			g2d.drawImage(iShipPictogram, 
					-iShipPictogram.getWidth(null)/2, 
					-iShipPictogram.getHeight(null)/2, null );
		}

		drawHand(g2d);

		g2d.rotate(-Math.toRadians(valRotation));
		drawHandCup(g2d, center/10);

	    if(histogramVisible)
	    {
	    	g2d.setColor(vis.histogramColor);
	    	ds.drawCircularPoints(g2d, 0, 0, baseRotationAngle, scaleAngle, center*33/40);
	    }

		g2d.translate(-center, -center);

		drawHorLabels(g2d);
		drawDigitalMeter(g2d, currVal);
		//g2d.dispose();
	}


	@Override
	protected void paintDashComponentBackground(Graphics2D g2d)
	{
		{
			Graphics2D ng2d = (Graphics2D) g2d.create();
			super.paintDashComponentBackground(ng2d);
			// bug - it uses fact of origin translation in super
			//drawHorLabel(g2d, units, 0, height/8, false, TextAnchorX.Center, TextAnchorY.Top);
			ng2d.dispose();
		}
		{
			Graphics2D ng2d = (Graphics2D) g2d.create();
			ng2d.translate(center, center);
			paintAlarmRegions(ng2d);
			ng2d.dispose();
		}

	}



}


