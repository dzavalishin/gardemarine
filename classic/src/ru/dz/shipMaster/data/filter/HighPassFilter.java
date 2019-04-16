package ru.dz.shipMaster.data.filter;

import ru.dz.shipMaster.data.DataSource;
import ru.dz.shipMaster.ui.config.filter.CieHighPassFilter;

/**
 * Very simple differentiator.
 * @author dz
 */
public class HighPassFilter extends GeneralFilterDataSource {
	
	private double[] values;
	private int windowSize;

	
	// We fill all the buffer and when it's fill getCount will be
	// equal to the values.length and we will allways return average 
	// of all the buffered data.
	// On startup though we will use just [0,getCount[ elements.
	
	
	public HighPassFilter() { setWindowSize(4); }
	
	public HighPassFilter(DataSource src, int averageWindowSize ) {
		super(src, src.getMin()-src.getMax(),src.getMax()-src.getMin());
		setWindowSize(averageWindowSize);
	}

	public void setWindowSize(int s)
	{
		if( s <=0 )
			s = 1;

		this.windowSize = s;
		values = new double[s*2];
		
		for(int i = 0; i < values.length; i++)
			values[i] = 0;
	}

	public int getWindowSize() {
		return windowSize;
	}

	@Override
	protected double calculateOutMin( double inMin, double inMax ) { return inMin-inMax; }
	
	@Override
	protected double calculateOutMax( double inMin, double inMax ) { return inMax-inMin; }
	

	@Override
	public void performMeasure() {
		putAverage(currInVal);
		translateToMeters(getAverageDiff(),null);
	}
	
	// ---------------------------------------------------------------------
	// workers
	// ---------------------------------------------------------------------
	@Deprecated
	public String getLegend() {	return "∆"+src.getLegend();	}
/*	
	public int getMax() {		return src.getMax()-src.getMin();	}
	public int getMin() {		return src.getMin()-src.getMax();	}

	public String getLegend() {	return "∆"+src.getLegend();	}
	public String getUnits() {		return src.getUnits();	}

*/	
	// ---------------------------------------------------------------------
	// workers
	// ---------------------------------------------------------------------

	synchronized double getAverageDiff()
	{
		//if(getCount < values.length)
			//return 0;
		
		double sum1half = 0;
		double sum2half = 0;

		int getCount = values.length;
		
		int i;
		for( i = 0; i < getCount/2; i++)
			sum1half += values[i];

		for( ; i < getCount; i++)
			sum2half += values[i];
		
		return sum1half-sum2half;
	}
	
	synchronized void putAverage(double val)
	{
		System.arraycopy(values, 0, values, 1, values.length-1);
		values[0] = val;
	}


	private CieHighPassFilter dialog = null;
	public void displayPropertiesDialog() {
		if(dialog == null)
			dialog = new CieHighPassFilter(this);
		dialog.displayPropertiesDialog();		
	}


	//public String getName() { return "High pass filter"; }
}
