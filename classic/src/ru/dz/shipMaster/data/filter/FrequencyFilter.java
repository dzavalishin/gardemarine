package ru.dz.shipMaster.data.filter;

import ru.dz.shipMaster.data.DataSource;
import ru.dz.shipMaster.ui.config.filter.CieLowPassFilter;

public class FrequencyFilter extends GeneralFilterDataSource {
	// Frequency calculations filter

	private static final int ONCE_UPON_A_TIME = 10;
	@SuppressWarnings("unused")
	private double lastValue;
	@SuppressWarnings("unused")
	private boolean haveLastValue = false;

	public FrequencyFilter() {
	} 

	public FrequencyFilter(DataSource src, int averageWindowSize ) {
		super(src, src.getMin(), src.getMax());
	}

	private int counter = 0;

	@Override
	public void performMeasure() {
		if( ++counter > ONCE_UPON_A_TIME )
		{
			counter = 0;
			calcFrequency(currInVal);
			translateToMeters(freq,null);
		}
	}

	// ---------------------------------------------------------------------
	// workers
	// ---------------------------------------------------------------------

	private double freq = 0;
	@SuppressWarnings("unused")
	private long lastTime;

	synchronized void calcFrequency(double val)
	{
		// this is not a freq calc! write anew!
		/*
		double diff = currInVal - lastValue;
		// Handle startup, wrap and too low freq
		if( (!haveLastValue) || (currInVal < lastValue) || (diff < 0.00001))
		{
			lastTime = System.currentTimeMillis();
			lastValue = currInVal;
			return;
		}
		lastValue = currInVal;
		long now = System.currentTimeMillis();
		long timeDiffMsec = now - lastTime;

		freq = (diff/timeDiffMsec) * 1000;

		lastTime = now;
		*/
	}


	@SuppressWarnings("unused")
	private CieLowPassFilter dialog = null;
	public void displayPropertiesDialog() {
		/*
		if(dialog == null)
			dialog = new CieFrequencyFilter(this);
		dialog.displayPropertiesDialog();
		*/		
	}


}
