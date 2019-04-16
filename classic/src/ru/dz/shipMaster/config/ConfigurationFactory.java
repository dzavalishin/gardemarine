package ru.dz.shipMaster.config;

import java.awt.Frame;
import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import ru.dz.shipMaster.config.items.CliAlarm;
import ru.dz.shipMaster.config.items.CliBus;
import ru.dz.shipMaster.config.items.CliButtonGroup;
import ru.dz.shipMaster.config.items.CliDriver;
import ru.dz.shipMaster.config.items.CliNetHost;
import ru.dz.shipMaster.config.items.CliNetInput;
import ru.dz.shipMaster.config.items.CliParameter;
import ru.dz.shipMaster.config.items.CliRight;
import ru.dz.shipMaster.config.items.CliSystemDriver;
import ru.dz.shipMaster.config.items.CliWindow;
import ru.dz.shipMaster.data.history.HistoryEventRecord;
import ru.dz.shipMaster.data.history.HistoryRecord;
import ru.dz.shipMaster.data.units.Unit;
import ru.dz.shipMaster.dev.AbstractDriver;
import ru.dz.shipMaster.dev.IBus;
import ru.dz.shipMaster.net.server.NetServer;
import ru.dz.shipMaster.ui.AlarmsFrame;
import ru.dz.shipMaster.ui.ConfigFrame;
import ru.dz.shipMaster.ui.config.HistoryViewList;
import ru.dz.shipMaster.ui.misc.GardemarineClassLoader;


/**
 * Factory class to access all the setup and runtime globals for the system.
 * @author dz
 */
public class ConfigurationFactory {
	private static final Logger log = Logger.getLogger(ConfigurationFactory.class.getName()); 

	private static final String SETUP_FN = "Setup.XML"; //$NON-NLS-1$
	private static final String VISUAL_SETUP_FN = "VisualSetup.XML"; //$NON-NLS-1$
	private static final String NEW_SFX = ".new"; //$NON-NLS-1$
	private static final String OLD_SFX = ".old"; //$NON-NLS-1$

	private static Configuration configuration = null;
	private static Object sync = new Object();

	/**
	 * Get object, that holds all the configurable things.
	 * @return Configuration object.
	 */
	public static Configuration getConfiguration() { 
		synchronized(sync)
		{
			if(configuration == null)
			{
				configuration = new Configuration();

				// Perform initialization

				//configuration.setSystemDriverItems(getConstantState().getSystemDriverItems());

				try 
				{ 
					load();
				}
				catch(Throwable e) {/** TODO: ignore? */}

				mergeConstantState();

			}
			return configuration;
		}
	}

	/**
	 * Add data from const state to data loaded from config, where applicable
	 */
	protected static void mergeConstantState() {

		{
			// System drivers - if const has some new ones, add them
			Vector<CliSystemDriver> systemDriverItems = configuration.getSystemDriverItems();
			needLoop: for(CliSystemDriver need : getConstantState().getSystemDriverItems())
			{
				for(CliSystemDriver have : systemDriverItems)
				{
					if(have.getDriver().getClass().equals( need.getDriver().getClass() ) )
						continue needLoop;
				}
				systemDriverItems.add(need);
			}
		}

		// Same with rights
		{
			Vector<CliRight> rightItems = configuration.getRightsItems();
			needLoop: for(CliRight need : getConstantState().getRightsItems())
			{
				for(CliRight have : rightItems)
				{
					if(have.equals(need) )
						continue needLoop;
				}
				rightItems.add(need);
			}
		}

		// Same with units
		{
			Vector<Unit> unitItems = configuration.getUnitItems();
			needLoop: for(Unit need : getConstantState().getUnitItems())
			{
				for(Unit have : unitItems)
				{
					if(have.equals(need) )
						continue needLoop;
				}
				unitItems.add(need);
			}
		}
		
		// Same with button groups
		{
			Vector<CliButtonGroup> buttonGroupItems = configuration.getButtonGroupsItems();
			needLoop: for(CliButtonGroup need : getConstantState().getButtonGroupsItems())
			{
				for(CliButtonGroup have : buttonGroupItems)
				{
					if(have.equals(need) )
						continue needLoop;
				}
				buttonGroupItems.add(need);
			}
		}

		
		
	}

	private static TransientState transientState = null;
	/** 
	 * TransientState is what not saved but recreated after start.
	 * 
	 * @return System-wide instance of transient state.
	 */
	static public TransientState getTransientState()
	{
		synchronized(sync)
		{
			if(transientState == null)
			{
				transientState = new TransientState();
			}
			return transientState;
		}
	}

	private static ConstantState constantState = null;
	/** 
	 * TransientState is what not saved but created by code and supposed to be mostly the same 
	 * from start to start.
	 * 
	 * @return System-wide instance of constant state.
	 */
	public static ConstantState getConstantState()
	{
		synchronized(sync)
		{
			if(constantState == null)
			{
				constantState = new ConstantState();
			}
			return constantState;
		}

	}

	private static final Object startStopMonitor = new Object();

	/**
	 * Save configuration.
	 * @throws FileNotFoundException
	 */
	public static void save() throws FileNotFoundException {

		if(systemStarted)
			return;

		if(systemStartingOrStopping)
			return;

		// Now again and for sure
		synchronized (startStopMonitor) {
			if(systemStarted)
				return;
			if(systemStartingOrStopping)
				return;
		}

		File tmp = new File(SETUP_FN+NEW_SFX);
		FileOutputStream fout = new FileOutputStream(tmp);
		BufferedOutputStream bout = new BufferedOutputStream(fout, 128*1024);
		XMLEncoder encoder = new XMLEncoder(bout);

		encoder.setOwner(getConstantState());

		encoder.setExceptionListener(new ExceptionListener() {
			public void exceptionThrown(Exception exception) {
				exception.printStackTrace();
			}
		});

		encoder.writeObject(configuration);

		encoder.flush();
		encoder.close();
		try {
			bout.close();
			File killOld = new File(SETUP_FN+OLD_SFX);
			killOld.delete();

			File old = new File(SETUP_FN);
			old.renameTo(new File(SETUP_FN+OLD_SFX));
			tmp.renameTo(new File(SETUP_FN));
			
			JOptionPane.showMessageDialog(null,Messages.getString("ConfigurationFactory.2")); //$NON-NLS-1$

			
		} catch (IOException e) {
			log.log(Level.SEVERE,Messages.getString("ConfigurationFactory.3"),e); //$NON-NLS-1$
			JOptionPane.showMessageDialog(null,Messages.getString("ConfigurationFactory.4")+e); //$NON-NLS-1$
		}
	}

	private static int loadCounter = 0;
	/**
	 * Load configuration file. Must be called early and once.
	 * @throws FileNotFoundException
	 */
	private static void load() throws FileNotFoundException {
		if(loadCounter > 0)
		{
			log.log(Level.SEVERE,Messages.getString("ConfigurationFactory.5")); //$NON-NLS-1$
			return;
		}
		loadCounter++;

		//getConfiguration(); // This is needed to 

		GardemarineClassLoader gcl = new GardemarineClassLoader();
		
		FileInputStream fin = new FileInputStream(new File(SETUP_FN));
		BufferedInputStream bin = new BufferedInputStream(fin,256*1024);
		
		ExceptionListener eListener = new ExceptionListener() {
			
			@Override
			public void exceptionThrown(Exception e) {
				log.log(Level.SEVERE,"Config load exception",e);
				
			}
		};
		
		XMLDecoder decoder = new XMLDecoder(bin,getConstantState(),eListener,gcl);

		// http://www.ftponline.com/javapro/2004_09/magazine/features/kgauthier/page5.aspx
		// recommends not to setOwner, 'cause it's too late.
		//decoder.setOwner(getConstantState()); 

		// Don't try lo load ones after some is failed.
		try {
			configuration = (Configuration) decoder.readObject();
		} catch (Throwable e) {
			log.log(Level.SEVERE,Messages.getString("ConfigurationFactory.6"),e); //$NON-NLS-1$
		}

		decoder.close();
		try {
			bin.close();
		} catch (IOException e) {
			log.log(Level.SEVERE,Messages.getString("ConfigurationFactory.7"),e); //$NON-NLS-1$
		}

	}

	
	

	
	
	
	
	
	private static VisualConfiguration visualConfiguration = null;
	
	private static void loadVisualConfiguration() throws FileNotFoundException {

		FileInputStream fin = new FileInputStream(new File(VISUAL_SETUP_FN));
		BufferedInputStream bin = new BufferedInputStream(fin,256*1024);
		XMLDecoder decoder = new XMLDecoder(bin,getConstantState());

		// http://www.ftponline.com/javapro/2004_09/magazine/features/kgauthier/page5.aspx
		// recommends not to setOwner, 'cause it's too late.
		//decoder.setOwner(getConstantState()); 

		// Don't try lo load ones after some is failed.
		try {
			visualConfiguration = (VisualConfiguration) decoder.readObject();
		} catch (Throwable e) {
			log.log(Level.SEVERE,Messages.getString("ConfigurationFactory.8"),e); //$NON-NLS-1$
		}

		decoder.close();
		try {
			bin.close();
		} catch (IOException e) {
			log.log(Level.SEVERE,Messages.getString("ConfigurationFactory.9"),e); //$NON-NLS-1$
		}

	}
	
	
	/**
	 * Save visual configuration.
	 * @throws FileNotFoundException
	 */
	public static void saveVisualConfiguration() throws FileNotFoundException {

		if(systemStarted || systemStartingOrStopping)
			return;

		File tmp = new File(VISUAL_SETUP_FN+NEW_SFX);
		FileOutputStream fout = new FileOutputStream(tmp);
		BufferedOutputStream bout = new BufferedOutputStream(fout, 128*1024);
		XMLEncoder encoder = new XMLEncoder(bout);

		encoder.setOwner(getConstantState());

		encoder.setExceptionListener(new ExceptionListener() {
			public void exceptionThrown(Exception exception) {
				exception.printStackTrace();
			}
		});

		encoder.writeObject(visualConfiguration);

		encoder.flush();
		encoder.close();
		try {
			bout.close();
			File killOld = new File(VISUAL_SETUP_FN+OLD_SFX);
			killOld.delete();

			File old = new File(VISUAL_SETUP_FN);
			old.renameTo(new File(VISUAL_SETUP_FN+OLD_SFX));
			tmp.renameTo(new File(VISUAL_SETUP_FN));
		} catch (IOException e) {
			log.log(Level.SEVERE,Messages.getString("ConfigurationFactory.10"),e); //$NON-NLS-1$
		}
	}
	
	
	
	/**
	 * Get object, that holds all the configurable things.
	 * @return Configuration object.
	 */
	public static VisualConfiguration getVisualConfiguration() { 
		synchronized(sync)
		{
			if(visualConfiguration == null)
			{
				visualConfiguration = new VisualConfiguration();

				// Perform initialization

				//configuration.setSystemDriverItems(getConstantState().getSystemDriverItems());

				try 
				{ 
					loadVisualConfiguration();
				}
				catch(Throwable e) {/** TODO: ignore? */}

				//mergeConstantState();

			}
			return visualConfiguration;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private static ConfigFrame cf = null;
	/**
	 * Get configuration frame object. What for?
	 * @return frame
	 */
	private static ConfigFrame getConfigFrame()
	{
		if(cf == null)
			cf = new ConfigFrame();
		return cf;
	}

	private static boolean systemStarted = false;
	private static boolean systemStartingOrStopping = false;

	/**
	 * Start the whole system.
	 */
	public static void startSystem()
	{
		log.severe("System start requested"); //$NON-NLS-1$
		new HistoryEventRecord(ConfigurationFactory.class.getName(),"system.start.begin","Begin starting system").store(); //$NON-NLS-1$ //$NON-NLS-2$
		synchronized (startStopMonitor) {
			if(loadCounter <= 0)
				return;

			if(systemStarted)
				return;

			if(systemStartingOrStopping)
				return;

			systemStartingOrStopping = true;
		}

		try { doStartSystem(); }
		finally {
			systemStartingOrStopping = false;
			if(systemStarted)
				new HistoryEventRecord(ConfigurationFactory.class.getName(),"system.start.done","System started").store(); //$NON-NLS-1$ //$NON-NLS-2$
			else
				new HistoryEventRecord(ConfigurationFactory.class.getName(),"system.start.fail","Start failed").store(); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	private static void doStartSystem()
	{		
		// First stop everything
		doStopSystem();

		getConfigFrame().enableConfigTabs(false);

		try
		{
			getTransientState().getDashBoard().getMessageWindow().clear();
			startSystemDrivers();
			startBuses();
			startDrivers();
			startParameters();
			startWindows();
			startNetwork();
			// connect all parameters, etc
			systemStarted = true;
		}
		catch(Throwable e)
		{
			// Start failed. Report and stop.
			reportStartProblem(e);
			doStopSystem();
		}
		getTransientState().updateTrayData();
	}


	private static void reportStartProblem(Throwable e) {
		log.log(Level.SEVERE,"Problem starting system",e); //$NON-NLS-1$
	}

	/**
	 * Stop the system.
	 */
	public static void stopSystem()
	{
		log.severe("System stop requested"); //$NON-NLS-1$
		new HistoryEventRecord(ConfigurationFactory.class.getName(),"system.stop.begin","Begin stopping system").store(); //$NON-NLS-1$ //$NON-NLS-2$
		synchronized (startStopMonitor) {
			if(!systemStarted)
				return;

			if(systemStartingOrStopping)
				return;

			systemStartingOrStopping = true;
		}

		try { doStopSystem(); }
		finally
		{
			systemStartingOrStopping = false;
			getTransientState().updateTrayData();
			if(systemStarted)
				new HistoryEventRecord(ConfigurationFactory.class.getName(),"system.stop.fail","Stop failed").store(); //$NON-NLS-1$ //$NON-NLS-2$
			else
				new HistoryEventRecord(ConfigurationFactory.class.getName(),"system.stop.done","System stopped").store(); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	private static void doStopSystem()
	{
		boolean hadProblems = false;

		try { stopNetwork(); } catch(Throwable e) { hadProblems = true; reportStopProblem(e); }
		try { stopWindows(); } catch(Throwable e) { hadProblems = true; reportStopProblem(e); }
		try { stopParameters(); } catch(Throwable e) { hadProblems = true; reportStopProblem(e); }
		try { stopDrivers(); } catch(Throwable e) { hadProblems = true; reportStopProblem(e); }
		try { stopBuses(); } catch(Throwable e) { hadProblems = true; reportStopProblem(e); }
		try { stopSystemDrivers(); } catch(Throwable e) { hadProblems = true; reportStopProblem(e); }

		try { CliAlarm.stopSound(); } catch(Throwable e) { hadProblems = true; reportStopProblem(e); }
		try { AlarmsFrame.stopSound(); } catch(Throwable e) { hadProblems = true; reportStopProblem(e); }
		
		if(hadProblems)
			return;

		getConfigFrame().enableConfigTabs(true);

		systemStarted = false; // in fact, we are not sure here.
	}

	private static void reportStopProblem(Throwable e) {
		log.log(Level.SEVERE,"Problem stopping system",e); //$NON-NLS-1$
	}

	/**
	 * Start the configuration UI.
	 */
	public static void startConfig()
	{
		getConfigFrame().setVisible(true);
		getConfigFrame().toFront();
		getConfigFrame().setExtendedState(Frame.NORMAL);
	}

	/**
	 * Stop the configuration UI.
	 */
	public static void stopConfig()
	{
		getConfigFrame().setExtendedState(Frame.ICONIFIED);
		getConfigFrame().setVisible(false);
	}


	// Start/stop workers

	private static void startSystemDrivers() {
		Vector<CliSystemDriver> systemDrivers = getConfiguration().getSystemDriverItems();
		for( CliSystemDriver d : systemDrivers)
		{
			AbstractDriver driver = d.getDriver();
			if( driver.isAutoStart() )
			{
				driver.start();
				driver.connectPorts();
			}
		}
	}


	private static void stopSystemDrivers() {
		Vector<CliSystemDriver> systemDrivers = getConfiguration().getSystemDriverItems();
		for( CliSystemDriver d : systemDrivers)
		{
			AbstractDriver driver = d.getDriver();
			driver.disconnectPorts();
			driver.stop();
		}
	}





	private static void startBuses() {

		if(getConfiguration().getGeneral().isDemoMode())
		{
			log.log(Level.SEVERE,Messages.getString("ConfigurationFactory.27")); //$NON-NLS-1$
			return;
		}

		for( CliBus d : getConfiguration().getBusItems())
		{
			IBus bus = d.getBus();
			if( bus.isAutoStart() )
				bus.start();
		}
	}


	private static void startDrivers() {

		if(getConfiguration().getGeneral().isDemoMode())
		{
			log.log(Level.SEVERE,Messages.getString("ConfigurationFactory.28")); //$NON-NLS-1$
			return;
		}

		Vector<CliDriver> srivers = getConfiguration().getDriverItems();
		for( CliDriver d : srivers)
		{
			AbstractDriver driver = d.getDriver();
			if( driver.isAutoStart() )
			{
				driver.start();
				driver.connectPorts();
			}
		}
	}

	private static void startParameters()
	{
		for( CliParameter p : getConfiguration().getParameterItems() )
		{
			p.start();
		}
	}

	private static void stopParameters()
	{
		for( CliParameter p : getConfiguration().getParameterItems() )
		{
			p.stop();
		}
	}

	private static void stopDrivers() throws Throwable {
		Vector<CliDriver> srivers = getConfiguration().getDriverItems();
		Throwable latsEx = null;
		for( CliDriver d : srivers)
		{
			try
			{
			AbstractDriver driver = d.getDriver();
			driver.disconnectPorts();
			driver.stop();
			}
			catch(Throwable e)
			{
				latsEx = e;
			}			
		}
		if(latsEx != null)
			throw latsEx;
	}

	private static void stopBuses() throws Throwable {
		Throwable latsEx = null;

		for( CliBus d : getConfiguration().getBusItems())
		{
			try { d.getBus().stop(); }
			catch(Throwable e)
			{
				latsEx = e;
			}
		}
		
		if(latsEx != null)
			throw latsEx;
	}




	private static void startWindows() {
		Vector<CliWindow> windows = getConfiguration().getWindowItems();
		for( CliWindow w : windows)
		{
			if( w.isAutostart() )
			{
				w.startWindow();
			}
		}
	}


	private static void stopWindows() {
		Vector<CliWindow> windows = getConfiguration().getWindowItems();
		for( CliWindow w : windows)
		{
			w.stopWindow();
		}
	}


	private static NetServer netServer = null;

	protected Object senderItemName;  

	private static void startNetwork() {
		if(!getConfiguration().getGeneral().isNetworkActive())
		{
			log.log(Level.SEVERE,Messages.getString("ConfigurationFactory.29")); //$NON-NLS-1$
			return;
		}
		log.log(Level.SEVERE,"Starting network");		 //$NON-NLS-1$
		if(netServer == null)
		{
			try {
				netServer = new NetServer("unnamedNode") { //$NON-NLS-1$
					@Override
					protected void newItemEvent(String senderHostName, String senderItemName) 
					{ 
						registerNewNetworkItem(senderHostName, senderItemName);
						registerNewNetworkHost(senderHostName);
					}
				};
			} catch (SocketException e) {
				log.log(Level.SEVERE,"Network start error", e); //$NON-NLS-1$
			} catch (IOException e) {
				log.log(Level.SEVERE,"Network start error", e); //$NON-NLS-1$
			}  
		}

		if(netServer == null)
		{
			log.log(Level.SEVERE,"Network startup failure"); //$NON-NLS-1$
			return;
		}

		netServer.setNodeName(getConfiguration().getGeneral().getNetNodeName());
		netServer.setRunning(true);

		// Send - connect
		for( CliParameter p : getConfiguration().getParameterItems() )
		{
			if(!p.isTranslateToNet())
				continue;
			netServer.addSendListItem(p.getName(), p.getDataSource());
		}

		for( CliNetInput in : getConfiguration().getNetInputItems())
		{
			CliParameter target = in.getTarget();
			if(target == null || !in.isEnabled())
				continue;
			netServer.connectReceiveItem(in);
		}
	}

	protected static void registerNewNetworkHost(String senderHostName)
	{
		for( CliNetHost h : getConfiguration().getNetHostItems())
		{
			if(h.getHostName().equalsIgnoreCase(senderHostName))
				return;
		}

		CliNetHost newHost = new CliNetHost();
		newHost.setHostName(senderHostName);

		getConfiguration().getNetHostItems().add(newHost);
	}

	protected static void registerNewNetworkItem(String senderHostName,
			String senderItemName) {

		for( CliNetInput in : getConfiguration().getNetInputItems() )
		{
			if(
					in.getHostName().equalsIgnoreCase(senderHostName) &&
					in.getItemName().equalsIgnoreCase(senderItemName)
			)
				return; // we have it
		}

		CliNetInput newIn = new CliNetInput();
		newIn.setEnabled(false);
		newIn.setHostName(senderHostName);
		newIn.setItemName(senderItemName);

		getConfiguration().getNetInputItems().add(newIn);
	}

	private static void stopNetwork() {
		if(netServer == null)
			return;
		log.log(Level.SEVERE,"Stopping network");		 //$NON-NLS-1$
		netServer.setRunning(false);
		netServer.clearSendList();
		// todo clear receive list connections to parameters too
	}

	/**
	 * Is system started?
	 * @return true if gardemarine is active.
	 */
	public static boolean isStarted() {
		return systemStarted;
	}

	public static boolean isNetworkActive()
	{
		return netServer != null && netServer.isRunning();
	}

	
	private static JDialog eventLogWindowDialog = null;
	static HistoryViewList eventLogListPanel = null;
	private static void makeEventLogWindow()
	{
		if(eventLogWindowDialog != null)
			return;

		Frame frame= JOptionPane.getFrameForComponent(null);
		eventLogWindowDialog = new JDialog(frame);
		eventLogWindowDialog.setTitle(Messages.getString("ConfigurationFactory.36")); //$NON-NLS-1$

		/*
		List<HistoryRecord> history = ConfigurationFactory.getTransientState().getAlarms();
		// X XX new Vector
		//PropertiesEditItemList<HistoryRecord> listPanel = new PropertiesEditItemList<HistoryRecord>(new Vector<HistoryRecord>(history));
		//listPanel = 
		HistoryViewList listPanel = new HistoryViewList(history);



		// TODO live update. move code to HistoryStorage? Or to CieHistoryStorage
		eventLogWindowDialog.add(listPanel);
		*/

		List<HistoryRecord> history = ConfigurationFactory.getTransientState().getAlarms();
		// XXX new Vector
		//PropertiesEditItemList<HistoryRecord> listPanel = new PropertiesEditItemList<HistoryRecord>(new Vector<HistoryRecord>(history));
		//listPanel = 
		eventLogListPanel = new HistoryViewList(history);
		// TODO live update. move code to HistoryStorage? Or to CieHistoryStorage
		eventLogWindowDialog.add(eventLogListPanel);
		
		//popup.pack(); // too slow!
		//eventLogWindowDialog.setSize(400, 600);
		
		eventLogWindowDialog.invalidate();
		eventLogWindowDialog.setSize(400, 600);

	}
	
	public static void openEventLogWindow() {
		makeEventLogWindow();
		

		
		//eventLogListPanel.reloadItems();

		
		eventLogWindowDialog.setVisible(true);
		/*
		Frame frame= JOptionPane.getFrameForComponent(null);
		JDialog popup = new JDialog(frame);
		

		List<HistoryRecord> history = ConfigurationFactory.getTransientState().getAlarms();
		// X XX new Vector
		//PropertiesEditItemList<HistoryRecord> listPanel = new PropertiesEditItemList<HistoryRecord>(new Vector<HistoryRecord>(history));
		//listPanel = 
		HistoryViewList listPanel = new HistoryViewList(history);

		// TO DO live update. move code to HistoryStorage? Or to CieHistoryStorage
		popup.add(listPanel);
		//popup.pack(); // too slow!
		popup.setSize(400, 600);
		popup.setVisible(true);
		*/
	}

}
