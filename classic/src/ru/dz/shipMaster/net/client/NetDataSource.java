package ru.dz.shipMaster.net.client;

import ru.dz.shipMaster.data.DataGeneration;
import ru.dz.shipMaster.data.GeneralDataSource;



public class NetDataSource extends GeneralDataSource {

	private double value;

	public NetDataSource(double min, double max, String legend, String units) {
		super(min, max, legend, units);
	}

	/** called from timer */
	@Override
	public void performMeasure() {
			translateData();		
	}

	

	private void translateData()
	{
		translateToMeters( value, null );
	}

	void setCurrentValue(double value) { this.value = value; }

	public void setInfo(double minValue, double maxValue, String label, String unit) {
		min = minValue;
		max = maxValue;
		legend = label;
		this.units = unit;
		resetupMeters();
	}

	/**
	 * Nobody above...
	 */
	protected void callSourceDataPumpEntry(DataGeneration currentGeneration)
	{
	}
	
	@Override
	public String getName() {
		return "Net "+legend;
	}
}
