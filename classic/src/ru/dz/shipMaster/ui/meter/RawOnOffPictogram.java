package ru.dz.shipMaster.ui.meter;

import java.awt.Graphics2D;

public class RawOnOffPictogram extends GeneralPictogram {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5351975666986979398L;

	{
		isRaw = true;
		setOpaque(false); // we are not drawing all
	}
	
	public RawOnOffPictogram() {
		super();
		//isOn = true; // NO - it leads to erroneously displayed ones
		setBorder(null);
	}
	
	public void paintDashComponentBackground(Graphics2D g2d) {

		if(iPictogram != null && (isOn|editMode))
			g2d.drawImage(iPictogram, picX, picY, null);

	}
	
	/*
	@Override
	protected void determineIsOn(double newCurr) {
		super.determineIsOn(newCurr);
		if(newCurr > 50)
		{
			System.out.println("RawOnOffPictogram.determineIsOn() > 50");
		}
	}
	*/
}
