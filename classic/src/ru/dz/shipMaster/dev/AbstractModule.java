package ru.dz.shipMaster.dev;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import ru.dz.shipMaster.config.ConfigurationFactory;
import ru.dz.shipMaster.config.GeneralConfigBean;
import ru.dz.shipMaster.data.misc.CommunicationsException;

public abstract class AbstractModule implements IModule, Comparable<AbstractModule> {
	protected static final Logger log = Logger.getLogger(IModule.class.getName()); 

	public AbstractModule() {
		super();
	}

	@Override
	public String getName() {
		return getDeviceName() + ":" + getInstanceName();
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public int compareTo(AbstractModule o) {
		if(hashCode() < o.hashCode()) return -1;
		if(hashCode() > o.hashCode()) return -1;
		return 0;
	}

	//private boolean debug;
	
	private boolean autoStart = true;

	/* (non-Javadoc)
	 * @see ru.dz.shipMaster.dev.IModule#isAutoStart()
	 */
	public boolean isAutoStart() {		return autoStart;	}

	/* (non-Javadoc)
	 * @see ru.dz.shipMaster.dev.IModule#setAutoStart(boolean)
	 */
	public void setAutoStart(boolean autoStart) {		this.autoStart = autoStart;	}



	public static boolean doubleToBoolean(double in)
	{
		return in > 0.0001;
	}









	protected JPanel moduleSetupPanel = new JPanel( new GridBagLayout() ); 
	protected final GridBagConstraints consL, consR;

	{

		moduleSetupPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

		{
			GridBagConstraints c = new GridBagConstraints();
			c.weightx = 1;
			c.weighty = 1;
			c.gridx = 0;
			c.gridy = GridBagConstraints.RELATIVE;
			c.anchor = GridBagConstraints.NORTHWEST;
			//c.ipadx = 4;
			//c.ipady = 4;
			c.insets = new Insets(4,4,4,4);
			consL = c;
		}

		{
			GridBagConstraints c = new GridBagConstraints();
			c.weightx = 1;
			c.weighty = 1;
			c.gridx = 1;
			c.gridy = GridBagConstraints.RELATIVE;
			c.anchor = GridBagConstraints.NORTHWEST;
			//c.ipadx = 4;
			//c.ipady = 4;
			c.insets = new Insets(4,4,4,4);
			consR = c;
		}
	}

	private boolean panelIsSetUp = false;


	protected JLabel stateLabel = new JLabel("Born");
	private JLabel messageLabel  = new JLabel("");
	private JButton startButton;
	private JButton stopButton;
	private JCheckBox autoStartField;
	private JCheckBox debugField = new JCheckBox();



	/**
	 * Load settings from driver instance to panel widgets.
	 * To be implemented in subclass.
	 */
	protected abstract void doLoadPanelSettings();

	/**
	 * Save settings made in panel to driver, activate settings.
	 * To be implemented in subclass.
	 */
	protected abstract void doSavePanelSettings();

	/**
	 * Load settings from module instance to panel widgets.
	 *
	 */
	public final void loadPanelSettings() 
	{
		autoStartField.setSelected(autoStart);
		//debugField.setSelected(debug);
		doLoadPanelSettings(); 
	}


	/**
	 * Save settings made in panel to driver, activate settings.
	 *
	 */
	public final void savePanelSettings() 
	{
		boolean wasRunning = isRunning();
		stop(); 
		autoStart = false; // If something will go wrong - auto start will be off
		doSavePanelSettings(); 
		autoStart = autoStartField.isSelected();
		
		//debug = debugField.isSelected();
		
		if(wasRunning)
			start();
	}

	/**
	 * Used by subclass to show message in control panel window.
	 * @param msg Message to show.
	 */
	protected void showMessage(String msg)
	{
		messageLabel.setText(msg);
	}


	/**
	 * Must be implemented in child to add controls to panel.
	 * @param panel Panel to add controls to.
	 */
	protected abstract void setupPanel(JPanel panel);

	public final JPanel getSetupPanel()
	{
		if(!panelIsSetUp)
		{
			mainSetupPanel();
			setupPanel(moduleSetupPanel);
			panelIsSetUp = true;
		}

		return moduleSetupPanel;		
	}

	@SuppressWarnings("serial")
	protected void mainSetupPanel()
	{
		JPanel modulePanel = new JPanel( new GridBagLayout() );



		consL.gridwidth = 2;
		consL.fill = GridBagConstraints.HORIZONTAL;
		moduleSetupPanel.add(modulePanel, consL );
		consL.gridwidth = 1;
		consL.fill = GridBagConstraints.NONE;

		modulePanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

		{

			final GridBagConstraints mCconsL, mConsR;

			{
				{
					GridBagConstraints c = new GridBagConstraints();
					c.weightx = 1;
					c.weighty = 1;
					c.gridx = 0;
					c.gridy = GridBagConstraints.RELATIVE;
					c.anchor = GridBagConstraints.NORTHWEST;
					//c.ipadx = 4;
					//c.ipady = 4;
					c.insets = new Insets(4,4,4,4);
					mCconsL = c;
				}

				{
					GridBagConstraints c = new GridBagConstraints();
					c.weightx = 1;
					c.weighty = 1;
					c.gridx = 1;
					c.gridy = GridBagConstraints.RELATIVE;
					c.anchor = GridBagConstraints.NORTHWEST;
					//c.ipadx = 4;
					//c.ipady = 4;
					c.insets = new Insets(4,4,4,4);
					mConsR = c;
				}
			}


			modulePanel.add(startButton = new JButton(), mCconsL);
			modulePanel.add(stopButton = new JButton(), mConsR);

			modulePanel.add(new JLabel("Debug:"), mCconsL);
			modulePanel.add(debugField, mConsR);

			modulePanel.add(new JLabel("Autostart:"), mCconsL);
			modulePanel.add(autoStartField = new JCheckBox(), mConsR);

			modulePanel.add(new JLabel("State:"), mCconsL);
			modulePanel.add(stateLabel, mConsR);

			modulePanel.add(new JLabel("Message:"), mCconsL);
			modulePanel.add(messageLabel, mConsR);


		}


		startButton.setAction(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				start();				
			}});

		stopButton.setAction(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				stop();				
			}});

		startButton.setText("Start");
		stopButton.setText("Stop");

	}















	/**
	 * Activate driver, allocate and open all resources.
	 * To be implemented in subclass.
	 * @throws CommunicationsException if can not start due to device communications error
	 */
	protected abstract void doStartModule() throws CommunicationsException;

	/**
	 * Deactivate module, close and detach all resources.
	 * To be implemented in subclass.
	 * @throws CommunicationsException if can not stop for some reason
	 */
	protected abstract void doStopModule() throws CommunicationsException;


	protected boolean moduleRunning = false;

	/* (non-Javadoc)
	 * @see ru.dz.shipMaster.dev.IModule#isRunning()
	 */
	public final boolean isRunning() {
		return moduleRunning;
	}


	protected FailCounter failCounter = new FailCounter() {

		@Override
		public void actOnFailures() { restartOnFailure(); }

	};

	protected boolean RunRequested = false;
	protected void restartOnFailure() {
		failCounter.reset();

		if(!RunRequested)			return;

		new Thread() {@Override
			public void run() {

			if(moduleRunning)
			{
				log.log(Level.SEVERE,"Restarting driver: PREPARE "+AbstractModule.this.getName());
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					log.log(Level.SEVERE,"Error in sleep restarting driver", e);
				}
				log.log(Level.SEVERE,"Restarting driver: STOP "+AbstractModule.this.getName());
				AbstractModule.this.internalStop();
			}

			try {
				sleep(1000);
			} catch (InterruptedException e) {
				log.log(Level.SEVERE,"Error in sleep restarting driver", e);
			}
			//if(!moduleRunning) 		return;
			log.log(Level.SEVERE,"Restarting driver: START "+AbstractModule.this.getName());
			try { AbstractModule.this.internalStart(); }
			catch(Exception e)
			{
				log.log(Level.SEVERE,"Problem restarting driver "+AbstractModule.this.getName(), e);
				restartOnFailure();
			}
			log.log(Level.SEVERE,"Restarting driver: OK "+AbstractModule.this.getName());
		}}.start();
	}



	/* (non-Javadoc)
	 * @see ru.dz.shipMaster.dev.IModule#stop()
	 */
	@Override
	public void stop()
	{
		RunRequested = false;
		internalStop();
		
		try {
			internalStopDebugger();
		}
		catch(Throwable e) {
			log.log(Level.SEVERE,"Stopping module debugger", e);
		}
		
	}

	protected abstract void internalStopDebugger();

	private void internalStop()
	{
		stateLabel.setText("Stopping");
		moduleRunning = false;
		try { 
			doStopModule();
			stateLabel.setText("Stopped");
		}
		catch( Throwable e )
		{
			log.severe("Error stopping module: "+e.toString());
			stateLabel.setText("Stop failed");
		}
	}


	/* (non-Javadoc)
	 * @see ru.dz.shipMaster.dev.IModule#start()
	 */
	@Override
	public void start()
	{
		RunRequested = true;
		restartOnFailure();
		ConfigurationFactory.getConfiguration().getGeneral();
		if( 
				GeneralConfigBean.debug && 
				isDebug() )
		{
			try {
				internalStartDebugger();
			}
			catch(Throwable e) {
				log.log(Level.SEVERE,"Starting module debugger", e);
			}
			
		}
	}

	protected abstract void internalStartDebugger();
	
	private void internalStart()
	{
		if(haveObjection())
			return;

		failCounter.reset();

		stateLabel.setText("Starting");
		try 
		{ 
			doStartModule();
			stateLabel.setText("Running");
			moduleRunning = true;
		}
		catch( Throwable e )
		{
			stateLabel.setText("Start failed");
			log.severe("Error starting module ("+getName()+"): "+e.toString());
		}
	}
















	// Objections

	private List<DrivertStartObjection> objections = new LinkedList<DrivertStartObjection>();
	protected void addStartObjection( DrivertStartObjection o ) { objections.add(o); }
	protected boolean haveObjection()
	{
		for( DrivertStartObjection o : objections )
		{
			if(o.isStartForbidden())
			{
				stateLabel.setText("Can't start: "+o.getReason());
				return true;
			}
		}
		return false;
	}




















	@Override
	protected void finalize() throws Throwable {
		stop();
		super.finalize();
	}

	public void destroy() {
		stop();
	}

	public boolean isDebug() {		
		//return debug;	
		return debugField.isSelected();
		}

	public void setDebug(boolean debug) {		
		//this.debug = debug;
		debugField.setSelected(debug);
		}

	

}