package ru.dz.shipMaster.data.filter;

//import java.util.logging.Level;
import java.util.logging.Logger;

public class TriggerFilter extends GeneralFilterDataSource {
    @SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(TriggerFilter.class.getName()); 
    
    private boolean state = false;
    private boolean prev = false;
    
	@Override
	public void performMeasure() {
		boolean val =  currInVal > 0.5; // Input is supposed to be boolean

		if( val != prev )
		{
			val = prev;
			if( val )
				state = !state;
		}
		
		translateToMeters(state ? 1.0 : 0.0,null);
	}


	@Override
	public void displayPropertiesDialog() {
	}

}
