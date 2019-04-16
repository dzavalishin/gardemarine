package ru.dz.shipMaster.data.filter;

import ru.dz.shipMaster.data.DataSource;
import ru.dz.shipMaster.ui.config.filter.CieLowPassFilter;

public class LowPassFilter extends GeneralFilterDataSource {
	// Very simple running average filter
	private double[] values;
	//private final int averageWindowSize;

	
	// We fill all the buffer and when it's fill getCount will be
	// equal to the values.length and we will allways return average 
	// of all the buffered data.
	// On startup though we will use just [0,getCount[ elements.
	
	
	private int putPosition = 0;
	private int getCount = 0;
	private int windowSize;
	
	public LowPassFilter() {
		//values = new int[4]; // TO DO setWindowSize
		setWindowSize(4);
	} 
	
	public LowPassFilter(DataSource src, int averageWindowSize ) {
		super(src, src.getMin(), src.getMax());
		//this.averageWindowSize = averageWindowSize;
		//values = new int[averageWindowSize];
		setWindowSize(averageWindowSize);
	}

	public void setWindowSize(int size)
	{
		windowSize = size < 1 ? 1 : size;
		values = new double[size];		
		putPosition = 0;
		getCount = 0;
	}

	public int getWindowSize() {
		return windowSize;
	}
	

	@Override
	public void performMeasure() {
		putAverage(currInVal);
		translateToMeters(getAverage(),null);
	}
	
	// ---------------------------------------------------------------------
	// workers
	// ---------------------------------------------------------------------

	synchronized double getAverage()
	{
		if(windowSize == 1)
			return values[0];
		
		if(getCount == 0)
		{
			return 0;
		}
		
		double sum = 0;

		for( int i = 0; i < getCount; i++)
		{
			sum += values[i];
		}
		
		return sum/getCount;
	}
	
	synchronized void putAverage(double val)
	{
		values[putPosition] = val;
		putPosition++;
		putPosition %= values.length;
		if(putPosition > getCount)
		{
			getCount = putPosition;
		}
	}


	private CieLowPassFilter dialog = null;
	public void displayPropertiesDialog() {
		if(dialog == null)
			dialog = new CieLowPassFilter(this);
		dialog.displayPropertiesDialog();		
	}


	//public String getName() { return "Low pass filters"; }
}
