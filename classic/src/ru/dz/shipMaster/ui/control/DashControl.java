package ru.dz.shipMaster.ui.control;

import ru.dz.shipMaster.config.items.CliParameter;

/**
 * Defines a visual element that provides user input.
 * @author dz
 *
 */

public interface DashControl {
	
	/* *
	 * Must be called by control implementation to translate its output
	 * data to the system.
	 * @param value Value to be sent as control's output. 
	 * Supposed to be 0 or 1 for buttons and switches and between
	 * 0 and 1 for sliders.
	 * /
	public void sendValue(double value);
	*/
	
	//public void setSink()

	public void setDestinationParameter(CliParameter parameter);

}
