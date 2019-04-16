package ru.dz.shipMaster.data.filter;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AngleFromDutyCycleFilter extends GeneralFilterDataSource {
	private static final Logger log = Logger.getLogger(AngleFromDutyCycleFilter.class.getName()); 
    
	@Override
	public void performMeasure() {
	
		log.log(Level.FINER, "in duty "+currInVal);
		
		double val =  currInVal/2048.0; // Input is supposed to be 0-1024-2048 for 0-90-180 degrees
		val -= 0.5;
		//val *= 4;
		val = Math.asin(val);
		//val -= Math.PI/2;
		val /= Math.PI;
		//val *= 180.0;
		val *= (720.0*3);
		
		log.log(Level.FINER, "out degrees "+val);

		translateToMeters(val,null);
	}


	@Override
	public void displayPropertiesDialog() {
	}

}
