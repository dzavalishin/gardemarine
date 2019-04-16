package ru.dz.shipMaster.dev.system;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.data.DataGeneration;
import ru.dz.shipMaster.data.GeneralDataSource;
import ru.dz.shipMaster.data.misc.CommunicationsException;
import ru.dz.shipMaster.dev.DriverPort;
import ru.dz.shipMaster.dev.ThreadedDriver;

/**
 * This thread reads incoming data periodically and passes it to meters.
 */

public class DataPump extends ThreadedDriver {
	private static final String DRIVER_NAME = "system.DataPump driver";
	private static final long DEFAULT_INTERVAL_MSEC = 200L;
	private static final int MIN_TIME = 100; 
	
	//private long interval = DEFAULT_INTERVAL_MSEC;
	
	public DataPump() {
		super(DEFAULT_INTERVAL_MSEC, Thread.MAX_PRIORITY, DRIVER_NAME);
	}


	@Override
	protected void doLoadPanelSettings() {
		updateFields();
	}

	@Override
	protected void doSavePanelSettings() {
		int newTime = Integer.parseInt( timeField.getText() );
		if( newTime < MIN_TIME ) newTime = MIN_TIME;
		//interval = newTime;
		//changeInterval();
		super.setInterval(newTime);
		updateFields();
	}

	private void updateFields() {
		//timeField.setText(Long.toString(interval));
		timeField.setText(Long.toString(super.getInterval()));
	}

	/*private void changeInterval() {
		super.setInterval(interval);		
	}*/

	private JTextField timeField = new JTextField();
	private DriverPort processorLoadAveragePort;
	private DriverPort memUsedPort;
	private DriverPort memLeftPort;
	
	@Override
	protected void setupPanel(JPanel panel) {
		panel.add(new JLabel("Update interval, msec:"), consL);
		panel.add(timeField, consR);
		timeField.setColumns(5);
	}

	@Override
	protected void doStartDriver() throws CommunicationsException {
	}

	@Override
	protected void doStopDriver() throws CommunicationsException {
	}

	@Override
	public String getDeviceName() { return DRIVER_NAME; }

	@Override
	public String getInstanceName() {		return "internal";	}

	@Override
	public boolean isAutoSeachSupported() { return false; }


	@Override
	protected void updatePorts(Vector<DriverPort> ports) {
		DriverPort.absentize(ports);
		DriverPort.sortPorts(ports);
		
		{
			processorLoadAveragePort = getPort(ports,0);
			processorLoadAveragePort.setDirection(Direction.Input);
			processorLoadAveragePort.setHardwareName("CPU load average");
			processorLoadAveragePort.setType(Type.Numeric);
		}
		{
			memUsedPort = getPort(ports,1);
			memUsedPort.setDirection(Direction.Input);
			memUsedPort.setHardwareName("Memory occupied");
			memUsedPort.setType(Type.Numeric);
		}
		{
			memLeftPort = getPort(ports,2);
			memLeftPort.setDirection(Direction.Input);
			memLeftPort.setHardwareName("Memory left");
			memLeftPort.setType(Type.Numeric);
		}
	}

	// -------------------------------------------------------------------------------
	// Driver
	// -------------------------------------------------------------------------------
	
	static private Set< WeakReference<GeneralDataSource> > sources = new HashSet< WeakReference<GeneralDataSource> >();
	
	private DataGeneration currentGeneration = new DataGeneration();
	
	@Override
	protected void doDriverTask() throws Throwable {
		currentGeneration = currentGeneration.getNext();
		
		synchronized (sources) {
			for(WeakReference<GeneralDataSource> s : sources)
			{
				GeneralDataSource ds = s.get(); 
				if( ds != null ) { ds.dataPumpEntry(currentGeneration); }
			}
		}
		
		if(processorLoadAveragePort != null)
			processorLoadAveragePort.sendDoubleData(getLoadLevelAverage());

		long freeMem = Runtime.getRuntime().freeMemory();
		long maxMem = Runtime.getRuntime().maxMemory();

		if(memUsedPort != null)
			memUsedPort.sendDoubleData(maxMem-freeMem);

		if(memLeftPort != null)
			memLeftPort.sendDoubleData(freeMem);

	}

	public static void addSource(WeakReference<GeneralDataSource> s) 
	{ 
		synchronized (sources) { sources.add(s); } 
	}
	
	public static void removeSource(WeakReference<GeneralDataSource> s) 
	{ 
		synchronized (sources) { sources.remove(s); } 
	}

	// Getters/setters

	//public Long getInterval() {		return interval;	}
	//public void setInterval(Long interval) {		this.interval = interval;	}
	
}
