package ru.dz.shipMaster.ui.control;

import ru.dz.shipMaster.config.items.CliParameter;
import ru.dz.shipMaster.ui.DashComponent;

/**
 * Defines a visual element that provides user input.
 * @author dz
 *
 */

public abstract class GeneralControl extends DashComponent implements DashControl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7028623927725328476L;
	private CliParameter parameter = null;
	private ControlDataSource dataSource;

	{
		dataSource = new ControlDataSource(0, 1, "Control", "None");
	}
	
	public CliParameter getDestinationParameter() {		return parameter;	}
	public void setDestinationParameter(CliParameter parameter) {		
		//if(this.parameter != null)			this.parameter.detachMeter(this);
		
		//if(this.parameter != null)			dataSource.removeMeter(this.parameter.getSink());
		
		this.parameter = parameter;
		
		if(this.parameter != null)
		{
		// This prevents us from adding more than one button to parameter
		//parameter.setDataSource(dataSource);
		// we can't just turn in off 'cause then parameter thinks it has no data src - so give them some bogus one
		parameter.setDataSource(new ControlDataSource(0, 1, "Control", "None"));
		
		// TODO looks like hack
		dataSource.addMeter(parameter.getSink());
		
		//if(this.parameter != null)			this.parameter.attachMeter(this);
		}
	}
	
	/*@Override
	protected void finalize() throws Throwable {
		dataSource.removeMeter(parameter.getSink());
		super.finalize();
	}*/
	
	/**
	 * Must be called by control implementation to translate its output
	 * data to the system.
	 * @param value Value to be sent as control's output. 
	 * Supposed to be 0 or 1 for buttons and switches and between
	 * 0 and 1 for sliders.
	 */
	protected void sendValue(double value)
	{
		dataSource.setValue(value);
		
	}

}
