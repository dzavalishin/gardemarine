package ru.dz.shipMaster.dev;

import java.awt.Image;
import java.util.logging.Logger;

import ru.dz.shipMaster.data.DataGeneration;
import ru.dz.shipMaster.data.GeneralDataSource;

public class PortDataSource extends GeneralDataSource {
    @SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(PortDataSource.class.getName());
	private double currVal; 



	public PortDataSource(double min, double max, String legend, String units) {
		super(min, max, legend, units);
	}

	public void setValue(double value)
	{
		currVal = value;
	}
	
	public void setValue(Image val) {
		translateImageToMeters(val);		
	}
	
	public void setValue(String val) {
		translateStringToMeters(val);		
	}
	
	/** called from timer */
	@Override
	public void performMeasure() {
		translateToMeters(currVal,null);
	}

	/**
	 * Nobody above us, so we don't need to ask them to prepare data.
	 */
	protected void callSourceDataPumpEntry(DataGeneration currentGeneration)
	{
	}

	@Override
	public String getName() {
		return "port "+legend;
	}

	public double getCurrentValue() {
		return currVal;
	}



}
