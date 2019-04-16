package ru.dz.shipMaster.ui.meter;


/**
 * Will be lit if value is over the warning level.
 * @author dz
 */
public class WarningPictogram extends GeneralPictogram {

	/**
	 * eclipse
	 */
	private static final long serialVersionUID = -8347580765400674770L;

	public WarningPictogram( String pictogramIconName, String offMessage, String onMessage, Severity severity) {
		super(pictogramIconName, offMessage, onMessage, severity);
	}

	public WarningPictogram( String pictogramIconName, String offMessage, String onMessage ) {
		super(pictogramIconName, offMessage, onMessage, Severity.Warning);
	}

	public WarningPictogram()
	{
		severity = Severity.Warning;
	}
	
	@Override
	protected void determineIsOn(double newCurr) {
		//isOn = newCurr > warnVal; 
		if(nearStatus == null) 
			isOn = false;
		else
			isOn = nearStatus.nearWarningLevel > 0.999; 
		}
	
}
