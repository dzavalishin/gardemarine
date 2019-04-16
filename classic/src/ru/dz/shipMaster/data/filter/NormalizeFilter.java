package ru.dz.shipMaster.data.filter;

import java.util.logging.Logger;

import ru.dz.shipMaster.data.DataSource;

public class NormalizeFilter extends GeneralFilterDataSource {
    @SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(NormalizeFilter.class.getName());
	
	private final double inMin, inMax, outMin, outMax;

	/** limit values */
	private final boolean limitInput;

	public NormalizeFilter(DataSource src, 
			double inMin, double inMax, double outMin, double outMax, boolean limitInput)
	{
		super(src);
		this.inMin = inMin;
		this.inMax = inMax;
		this.outMin = outMin;
		this.outMax = outMax;
		this.limitInput = limitInput;		
	}
	
	@Override
	public void performMeasure() {
		if(limitInput)
		{
			if( currInVal > inMax ) currInVal = inMax;
			if( currInVal < inMin ) currInVal = inMin;
		}
		double normIn = (currInVal-inMin)/(inMax-inMin);
		double normOut = (normIn*(outMax-outMin))+outMin;
		//log.log(Level.SEVERE, "normalize from "+val+" to "+normOut);
		translateToMeters(normOut,null);
	}

	//@Override	protected void pushValue(double val) {	}

	public void displayPropertiesDialog() {
	}

}
