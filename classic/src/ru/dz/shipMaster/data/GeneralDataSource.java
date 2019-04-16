package ru.dz.shipMaster.data;

import java.awt.Image;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.dz.shipMaster.config.items.CliAlarm.NearStatus;

/**
 * Basic implementation of data source. Mostly - feeding of meters.
 * @author dz
 *
 */
public abstract class GeneralDataSource implements DataSource {
	private static final Logger log = Logger.getLogger(GeneralDataSource.class.getName()); 

	protected DataGeneration myGeneration = new DataGeneration();

	//@SuppressWarnings({"ThisEscapedInObjectConstruction"})
	private WeakReference<GeneralDataSource> weakToMe = new WeakReference<GeneralDataSource>(this);

	protected double min;
	protected double max;
	//protected double warn;
	//protected double crit;
	protected String legend;
	protected String units;

	/** For reflective instantiation */
	public GeneralDataSource() {
		//DataPumpThread.addSource(weakToMe);		
		ru.dz.shipMaster.dev.system.DataPump.addSource(weakToMe);
	} 

	public GeneralDataSource(double min, double max, String legend, String units)
	{
		this.min = min;
		this.max = max;
		//this.warn = warn;
		//this.crit = crit;
		this.legend = legend;
		this.units = units;		

		//DataPumpThread.addSource(weakToMe);
		ru.dz.shipMaster.dev.system.DataPump.addSource(weakToMe);
	}

	/**
	 * Must set meter characteristics.
	 * @param m Meter to setup.
	 */
	protected void setupMeter(DataSink m)
	{
		m.setLegend(getLegend());
		m.setUnits(getUnits());
		m.setMinimum(getMin()); // ParameterDS redefines this
		m.setMaximum(getMax()); // ParameterDS redefines this

		//DataPumpThread.addSource(weakToMe);
	}


	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		//DataPumpThread.removeSource(weakToMe);
		ru.dz.shipMaster.dev.system.DataPump.removeSource(weakToMe);
	}

	/** 
	 * Called from DataPumpThread timer 
	 * @param currentGeneration data generation number
	 *  
	 */
	public void dataPumpEntry(DataGeneration currentGeneration)
	{
		if(currentGeneration.equals(myGeneration))
			return;

		myGeneration = currentGeneration;

		callSourceDataPumpEntry(currentGeneration);

		performMeasure();
	}

	/**
	 * Must be implemented in classes that receive their data from other DataSource. Must call 
	 * source's dataPumpEntry.
	 * 
	 * @param currentGeneration
	 */
	protected abstract void callSourceDataPumpEntry(DataGeneration currentGeneration);



	public abstract void performMeasure();

	protected Set<DataSink> meters = new HashSet<DataSink>();

	public void addMeter(DataSink m) 
	{
		setupMeter(m);
		synchronized(meters) {
			meters.add(m); 
		}
	}

	public void removeMeter(DataSink m) 
	{ 
		synchronized(meters) {
			meters.remove(m);
		}
	}

	protected void translateToMeters(double value, NearStatus ns)
	{
		//log.log(Level.SEVERE, "ttm "+value);
		synchronized(meters) {
			for( DataSink m : meters )
			{
				try {	
					m.setCurrent(value,ns); 
				}
				catch(Throwable e)
				{
					log.log(Level.SEVERE,"setCurrent", e);
				}
			}
		}		
	}

	protected void resetupMeters()
	{
		synchronized(meters) {
			for( DataSink m : meters )
				setupMeter(m);
		}		
	}

	@Deprecated
	protected double normalize(double in, double inMin, double inMax)
	{
		double v = in;
		v -= inMin;
		v /= (inMax-inMin);

		v *= (max - min);
		v += min;

		double out = v;

		if( out > max ) out = max;
		if( out < min ) out = min;

		return out;
	}


	protected void translateImageToMeters(Image val)
	{
		//log.log(Level.SEVERE, "ttm "+value);
		synchronized(meters) {
			for( DataSink m : meters )
				m.receiveImage(val);
		}		
	}

	protected void translateStringToMeters(String val) {
		synchronized(meters) {
			for( DataSink m : meters )
				m.receiveString(val);
		}		
	}

	// ---------------------------------------------------------------------
	// getters
	// ---------------------------------------------------------------------

	public double getMin() { return min; }
	public double getMax() { return max; }

	//@Deprecated
	public String getLegend() { return legend; }
	//@Deprecated
	public String getUnits() { return units; }	

}
