package ru.dz.shipMaster.data.filter;

import java.awt.Image;

import ru.dz.shipMaster.config.ConfigurationFactory;
import ru.dz.shipMaster.config.GeneralConfigBean;
import ru.dz.shipMaster.config.TransientState;
import ru.dz.shipMaster.config.items.CliAlarm;
import ru.dz.shipMaster.config.items.CliAlarm.AlarmProcessor;
import ru.dz.shipMaster.config.items.CliAlarm.NearStatus;
import ru.dz.shipMaster.config.items.CliParameter;
import ru.dz.shipMaster.data.AbstractMinimalDataSink;
import ru.dz.shipMaster.data.DataGeneration;
import ru.dz.shipMaster.data.DataSink;
import ru.dz.shipMaster.data.DataSource;
import ru.dz.shipMaster.data.GeneralDataSource;
import ru.dz.shipMaster.data.RandomDataSource;
import ru.dz.shipMaster.data.history.HistoryDataRecord;
import ru.dz.shipMaster.data.history.ParameterHistory;

/**
 * Used in parameters only, translates data to meters.
 * @author dz
 */
public class ParameterDataSource extends GeneralDataSource {

	private static final long HISTORY_INTERVAL_MSEC = 2*1000;
	protected double currInVal = 0; // Or else button-sourced parameter translates junk
	private CliParameter parameter;
	private RandomDataSource randomDataSource;
	private DataSource realDataSource;
	private double aperture;
	private boolean debug;

	/** inc on read, reset on write */
	private int timeoutCounter = 0;
	private static final int TIMEOUT_COUNT = 20;
	private boolean checkTimeout = false;
	
	private static final int HISTORY_SECONDS = 60*5; // 5 min
	private ParameterHistory history = new ParameterHistory(HISTORY_SECONDS*20);

	
	// Alarm related
	private CliAlarm alarm;
	private NearStatus ns;
	private AlarmProcessor ap;

	protected boolean simulationEnabled = true; // True if we can use random numbers as source when real ds is absent 

	private TransientState transientState;
	//private String parameterName;
	//private boolean isSimulation() { return true; }
	protected boolean isSimulation() { return (realDataSource == null) && simulationEnabled; }
	protected boolean isRealData() { return realDataSource != null; }
	/**
	 * Constructs data source for given parameter.
	 * @param p Our host parameter.
	 */
	public ParameterDataSource(CliParameter p)
	{
		parameter = p;
		checkTimeout = p.isCheckTimeout();
		reactivate(); 
	}

	/**
	 * To be called when parameter's settings are changed. Not used?
	 */
	public void reactivate()
	{
		//discard();

		simulationEnabled = ConfigurationFactory.getConfiguration().getGeneral().isSimulationMode();

		if(randomDataSource != null)
			randomDataSource.removeMeter(dsSimulated);
		
		randomDataSource = null;
		
		//parameter = p;
		if(simulationEnabled)
		{
			randomDataSource = new RandomDataSource( getMin(), getMax(), "", "");
			randomDataSource.addMeter(dsSimulated);
		}
		aperture = getMax()-getMin();
		alarm = parameter.getAlarm();
		if(alarm != null)
		{
			ns = alarm.new NearStatus();
			ap = alarm.new AlarmProcessor(parameter.getLegend());
		}
		transientState = ConfigurationFactory.getTransientState();
		//parameterName = parameter.getName();
	}

	/**
	 * Set a new actual input data source.
	 * @param ids Data source to listen to.
	 */
	public void setDataSource(DataSource ids)
	{
		if(realDataSource != null)
		{
			realDataSource.removeMeter(dsReal);
			if(GeneralConfigBean.debug && (realDataSource != null)) 
				System.out.println("ParameterDataSource.setDataSource() - Warning: reset data source, connected two sources? (param "+parameter.getName()+", ds1 "+realDataSource.getName()+", ds2 "+ids.getName()+")");
		}
		realDataSource = ids;
		if(realDataSource != null)
			realDataSource.addMeter(dsReal);
		resetupMeters();
	}


	/* (non-Javadoc)
	 * @see ru.dz.shipMaster.data.GeneralDataSource#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {		
		try { discard(); }
		finally { super.finalize(); }
	}

	/**
	 * Disassemble, disconnect from sources and stop.
	 */
	public void discard()
	{
		if(realDataSource != null)
			realDataSource.removeMeter(dsReal);
		realDataSource = null;

		if(randomDataSource != null)
			randomDataSource.removeMeter(dsSimulated);
		randomDataSource = null;
	}

	DataSink getDataSink() { return dsReal; }

	protected void callSourceDataPumpEntry(DataGeneration currentGeneration)
	{
		//if(realDataSource != null) realDataSource.performMeasure();
		//if( isSimulation() && simulationEnabled && (randomDataSource != null) ) randomDataSource.performMeasure();
		if(realDataSource != null) realDataSource.dataPumpEntry(currentGeneration);
		if( isSimulation() && (randomDataSource != null) ) randomDataSource.dataPumpEntry(currentGeneration);
	}


	private long lastHistoryTime = 0;
	@Override
	public void performMeasure() 
	{		
		long now = System.currentTimeMillis();
		if( now > lastHistoryTime+HISTORY_INTERVAL_MSEC)
		{
			lastHistoryTime = now;
			HistoryDataRecord rec = new HistoryDataRecord(parameter.getName(), currInVal);
			//history.addRecord(rec);
			transientState.addHistoryRecord( rec );	
		}
		history.put(now,currInVal);
		
		if( checkTimeout && (timeoutCounter++ > TIMEOUT_COUNT) )
		{
			currInVal = 0;
			timeoutCounter = 0;
		}
		
		if(ap != null)
			ap.processValue(currInVal, aperture, ns);
		translateToMeters(currInVal,ns);
	}

	DataSink dsReal = new AbstractMinimalDataSink() {

		@Override
		public void receiveImage(Image val) {
			if(isRealData())
				translateImageToMeters(val);
		}

		@Override
		public void receiveString(String val) {
			if(isRealData())
				translateStringToMeters(val);
		}

		public void setCurrent(double newCurr) {
			
			timeoutCounter = 0;
			
			if(isRealData())
			{
				currInVal = newCurr;
				//translateToMeters(currInVal);
				if(GeneralConfigBean.debug && debug )
					System.out
							.println("Parameter "+parameter.getName()+" = "+newCurr);
				
				
			}
		}

	};

	DataSink dsSimulated = new AbstractMinimalDataSink() {
		@Override
		public void receiveImage(Image val) {
			if(isSimulation())
				translateImageToMeters(val);
		}

		@Override
		public void receiveString(String val) {
			if(isSimulation())
				translateStringToMeters(val);
		}
		
		public void setCurrent(double newCurr) {
			if(isSimulation() ) 
			{
				currInVal = newCurr;
			}
		}};


		// ---------------------------------------------------------------------
		// workers
		// ---------------------------------------------------------------------
		@Deprecated
		@Override
		public String getLegend() 
		{
			String myLegend = parameter.getLegend();
			if(isSimulation() )
				return "SIM "+myLegend;
			else
				return myLegend;			
		}

		@Override
		public double 		getMin() { return parameter.getMinValue(); }

		@Override
		public double		getMax() { return parameter.getMaxValue(); }

		@Deprecated
		@Override
		public String		getUnits() { 
			String u = "?";
			if(parameter.getUnit() != null )
				u = parameter.getUnit().getName();
			return u; 
		}

		/**
		 * Get personal parameter's history.
		 * @return
		 */
		public ParameterHistory getHistory() {
			return history;
		}
		
		public DataSink getSink() {
			return dsReal;
		}

		public void setDebug(boolean debug) {
			this.debug = debug;
		}

		@Override
		public String getName() {
			return parameter.getName();
		}




}
