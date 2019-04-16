package ru.dz.shipMaster.data;

import java.util.logging.Logger;

/**
 * Used for demo - generates random (but relatively smooth) values.
 * @author dz
 */
public class RandomDataSource extends GeneralDataSource {
    private static final Logger log = Logger.getLogger(RandomDataSource.class.getName());
    private static final double RANDOM_CENTER_VALUE = 0.5;

    private double step;
    /**
     * @param min Minimal value for output data.
     * @param max Maximal value for output data.
     * @param warn @deprecated - warning level coefficient. 
     * @param crit @deprecated - critical level coefficient. 
     * @param legend @deprecated Parameter name.
     * @param units @deprecated Unit name.
     */
    public RandomDataSource(double min, double max, String legend, String units) {
		super(min, max, legend, units);
		step = (max-min)/20;
		log.finest("Created. From "+min+" to "+max);
	}

    double value = min+max/2;
	
	@Override
	public void performMeasure() {
		value += (Math.random()- RANDOM_CENTER_VALUE)*step;
		if( value < min ) value = min;
		if( value > max ) value = max;
		translateToMeters(value,null);
	}

	/**
	 * Nobody above...
	 */
	protected void callSourceDataPumpEntry(DataGeneration currentGeneration)
	{
	}

	@Override
	public String getName() {
		return "Random";
	}


}
