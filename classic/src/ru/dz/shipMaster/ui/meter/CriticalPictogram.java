package ru.dz.shipMaster.ui.meter;

/**
 * Will be lit if value is over the critical level.
 * @author dz
 */
public class CriticalPictogram extends GeneralPictogram {

	/**
	 * eclipse
	 */
	private static final long serialVersionUID = -2812729817828108519L;

	public CriticalPictogram(String pictogramIconName, String offMessage, String onMessage, Severity severity) {
		super(pictogramIconName, offMessage, onMessage, severity);
	}

	public CriticalPictogram(String pictogramIconName, String offMessage, String onMessage ) {
		super(pictogramIconName, offMessage, onMessage, Severity.Critical);
	}

	public CriticalPictogram()
	{
		//super(db);
		severity = Severity.Critical;
	}
	
	@Override
	protected void determineIsOn(double newCurr) {
		if(nearStatus == null) 
			isOn = false;
		else
			isOn = nearStatus.nearCriticalLevel > 0.999; 
		}
	
}
