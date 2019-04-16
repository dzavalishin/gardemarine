package ru.dz.shipMaster.ui.meter;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;

public class CameraImage extends GeneralMeter {
	/**
	 * UID 
	 */
	private static final long serialVersionUID = 900618158215771297L;
	
	private Image cameraImage;
	private int picX = 0;
	private int picY = 0;

	@Override
	public void receiveImage(Image val) {
		cameraImage = val;
		repaint(20);
	}
	
	
	public void paintDashComponent(Graphics2D g2d) {
		if(cameraImage != null)
			g2d.drawImage(cameraImage, picX, picY, null);

		drawComponentBorder(g2d);
		
	}


	@Override
	public void paintDashComponentBackground(Graphics2D g2d) {
		// None
	}
	
	@Override
	protected void recalcMeterParameters() {
		Dimension myPreferredSize = new Dimension();
		//myPreferredSize.height = 20;
		//myPreferredSize.width  = 20;

		myPreferredSize.height = 200;
		myPreferredSize.width  = 200;
		
		if(cameraImage != null)
		{
			myPreferredSize.height = Math.max( cameraImage.getHeight(null), 40 );
			myPreferredSize.width = Math.max( cameraImage.getWidth(null), 40 );
		}
		
		setPreferredSize(myPreferredSize);
	}




	@Override
	protected void processResize() {
		if( cameraImage != null )
		{
			picX = ((getSize().width)/2) - cameraImage.getWidth(null)/2;
			picY = ((getSize().height)/2) - cameraImage.getHeight(null)/2;
		}
	}
	
}
