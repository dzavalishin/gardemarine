package ru.dz.shipMaster.data;

import java.awt.Image;

import ru.dz.shipMaster.config.items.CliAlarm.NearStatus;

/**
 * Object, able to receive data from datasource. Data will be delivered
 * by datasource by calling setCurrent method.
 * @author dz
 *
 */

public interface DataSink {

	public double getMinimum();
	public double getMaximum();
	/** 
	 * Not getCurent so that current value is not to be saved with XML serializer.
	 * Used in Plaf UI.
	 * 
	 * @return current value.
	 */
	public double getCurrentValue();

	//public double getWarningThreshold();
	//public double getCriticalThreshold();
	/* *
	 * Change the Gauge's Warning Threshold.  This is the percentage of the Maximum Value
	 * at which the Warning Region begins.
	 * @param newWarnPercnt	New Warning Threshold
	 * /
	public void setWarningThreshold(double newWarnPcnt);
	/**
	 * Change the Gauge's Critical Threshold.  This is the percentage of the Maximum Value
	 * at which the Critical Region begins.
	 * @param newCritPercnt	New Critical Threshold
	 * /
	public void setCriticalThreshold(double newCritPcnt);
	*/
	/**
	 * Receive (and, possibly, pass forward ASYNCHRONOUSLY) numeric data.
	 * @param newCurr New value
	 * @param ns informs about how near this value to warning or critical levels.
	 */
	public void setCurrent(double newCurr, NearStatus nearStatus);
	
	/**
	 * Receive (and, possibly, pass forward ASYNCHRONOUSLY) numeric data.
	 * @param newCurr New Current
	 */
	public void setCurrent(double newCurr);

	/**
	 * Receive (and, possibly, pass forward SYNCHRONOUSLY) image (picture) data.
	 * @param val Image to receive and/or pass further.
	 */
	public void receiveImage(Image val);

	/**
	 * Receive (and, possibly, pass forward SYNCHRONOUSLY) string data.
	 * @param val String to receive and/or pass further.
	 */
	public void receiveString(String val);
	
	/**
	 * Change the Gauge's Legend (what this Gauge is measuring; e.g., Speed or Swap Rate)
	 * @param newLegend	Legend's new value
	 */
	public void setLegend(String newLegend);
	/**
	 * Change the Gauge's Units (what Value is measured in; e.g., KPH or PPM)
	 * @param newUnits	Units's new value
	 */
	public void setUnits(String newUnits);
	/**
	 * Change the Gauge's Minimum Value 
	 * @param min New Minimum
	 */
	public void setMinimum(double min);
	/**
	 * Change the Gauge's Maximum Value 
	 * @param max New Maximum
	 */
	public void setMaximum(double max);
	
	
}
