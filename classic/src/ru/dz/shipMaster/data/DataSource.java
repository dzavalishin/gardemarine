package ru.dz.shipMaster.data;

/**
 * Actively pushes data to receivers. Calls all set* methods on start
 * and setCurrent on timer (see DataPumpThread).
 * @author dz
 *
 */

public interface DataSource {

	public String getName();
	
	/**
	 * Add listener to this source's data.
	 * @param m Listener (meter or other data source, or something else).
	 */
	public void addMeter( DataSink m );

	/**
	 * Remove listener.
	 * @param m Listener to remove.
	 */
	public void removeMeter( DataSink m );
	
	/**
	 * Get actual value of this item and translate to all the meters. Called by dataPumpEntry()
	 * after source's perfromMeasure is called. 
	 */
	public void performMeasure();

	/**
	 * Special method to be called by DataPump. 
	 */
	public void dataPumpEntry(DataGeneration currentGeneration);

	
	/**
	 * Get minimal value of this source's data.
	 * @return Minimal possible value.
	 */
	double 		getMin();
	/**
	 * Get maximal value of this source's data.
	 * @return Maximal possible value.
	 */
	double		getMax();
	
	
	//@Deprecated
	String		getLegend();
	//@Deprecated
	String		getUnits();
}
