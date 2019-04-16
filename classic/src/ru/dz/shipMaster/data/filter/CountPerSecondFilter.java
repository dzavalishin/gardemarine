package ru.dz.shipMaster.data.filter;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * Frequency calculations filter.
 * @author dz
 *
 */

public class CountPerSecondFilter extends GeneralFilterDataSource {
	private static final Logger log = Logger.getLogger(CountPerSecondFilter.class.getName()); 

	//private static final int ONCE_UPON_A_TIME = 10;
	private double lastValue;
	private boolean haveLastValue = false;

	public CountPerSecondFilter() {
	} 

	/*public CountPerSecondFilter(DataSource src, int averageWindowSize ) {
		super(src, src.getMin(), src.getMax());
	}*/

	//private int counter = 0;

	@Override
	public void performMeasure() {
		//if( ++counter > ONCE_UPON_A_TIME )
		{
			//counter = 0;
			calcFrequency(currInVal);
			//log.log(Level.SEVERE,"Freq = "+freq);
			translateToMeters(freq,null);
			//translateToMeters(freq);
		}
	}

	// ---------------------------------------------------------------------
	// workers
	// ---------------------------------------------------------------------

	private double freq = 0;
	private long lastTime;

	synchronized void calcFrequency(double val)
	{
		double diff = currInVal - lastValue;
		lastValue = currInVal;

		// Handle too low freq & wrap
		if(  (currInVal < lastValue) || (diff < 0.00001))
		//if( (!haveLastValue) )
		{
			lastTime = System.currentTimeMillis();
			haveLastValue = true;
			freq = 0;
			log.log(Level.FINER,String.format("restart diff %f val %f", diff, currInVal));
			return;
		}

		// Handle startup, wrap and too low freq
		if( (!haveLastValue) )
		//if( (!haveLastValue) )
		{
			lastTime = System.currentTimeMillis();
			haveLastValue = true;
			freq = 0;
			log.log(Level.FINER,String.format("restart diff %f val %f", diff, currInVal));
			return;
		}

		long now = System.currentTimeMillis();
		long timeDiffMsec = now - lastTime;

		freq = (diff/timeDiffMsec) * 1000;

		log.log(Level.FINER,String.format("diff %f tdiff %d val %f freq %f", diff, timeDiffMsec, currInVal, freq));

		lastTime = now;
	}


	//private CieLowPassFilter dialog = null;
	public void displayPropertiesDialog() {
		/*
		if(dialog == null)
			dialog = new CieFrequencyFilter(this);
		dialog.displayPropertiesDialog();
		*/		
	}


}
