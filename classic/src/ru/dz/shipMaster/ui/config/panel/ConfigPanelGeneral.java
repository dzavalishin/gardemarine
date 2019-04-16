package ru.dz.shipMaster.ui.config.panel;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ru.dz.shipMaster.Main;
import ru.dz.shipMaster.Version;
import ru.dz.shipMaster.config.ConfigurationFactory;
import ru.dz.shipMaster.config.GeneralConfigBean;
import ru.dz.shipMaster.config.items.CliParameter;
import ru.dz.shipMaster.config.items.CliUser;
import ru.dz.shipMaster.data.history.HistoryGraph;
import ru.dz.shipMaster.ui.config.ConfigPanel;
import ru.dz.shipMaster.ui.config.Messages;

/**
 * General configuration panel.
 * @author dz
 */
@SuppressWarnings("serial")
public class ConfigPanelGeneral extends ConfigPanel {
	//GeneralConfigBean bean = ConfigurationFactory.getConfiguration().getGeneral();
	/** @return panel name for tab */
	@Override
	public String getName() { return Messages.getString("ConfigPanelGeneral.PanelName"); } //$NON-NLS-1$

	/** @return panel tip for tab */
	@Override
	public String getTip() { return Messages.getString("ConfigPanelGeneral.PanelDescription"); } //$NON-NLS-1$


	private final GeneralConfigBean bean;

	private JTextField logFileNameField = new JTextField(20);

	private JCheckBox splashScreenEnabledField = new JCheckBox(""); //$NON-NLS-1$
	private JCheckBox demoModeField = new JCheckBox(""); //$NON-NLS-1$
	private JCheckBox simulationModeField = new JCheckBox(""); //$NON-NLS-1$
	private JCheckBox networkActiveField = new JCheckBox(""); //$NON-NLS-1$
	private JTextField networkHostNameField = new JTextField(20);
	private JCheckBox autostartField = new JCheckBox(""); //$NON-NLS-1$
	private JButton showEventLog = new JButton();

	private JCheckBox debugPrtintField = new JCheckBox(""); //$NON-NLS-1$
	private JCheckBox suppressAlarmsField = new JCheckBox(""); //$NON-NLS-1$
	
	private JCheckBox globalExtendWidthField = new JCheckBox("");//$NON-NLS-1$
	private JCheckBox globalExtendHeightField = new JCheckBox("");//$NON-NLS-1$

	private JButton showHistoryGraphButton = new JButton();

	private JCheckBox autologinEnabledField = new JCheckBox();
	private JButton autologinUserButton = new JButton();
	private CliUser autologinUser;

	private JButton loginButton = new JButton();

	public ConfigPanelGeneral(GeneralConfigBean bean) {
		this.bean = bean;
		discardChanges();

		cons.gridx = 0;
		cons.gridy = 0;
		JPanel leftPanel = new JPanel(new GridBagLayout() );
		add(leftPanel, cons);

		createLeftPanel(leftPanel);

		cons.gridx = 1;
		cons.gridy = 0;
		JPanel rightPanel = new JPanel(new GridBagLayout() );
		add(rightPanel, cons);

		createRightPanel(rightPanel);

		cons.gridx = 0;
		cons.gridy = 1;
		cons.gridwidth = 2;
		cons.weighty = 0;
		JPanel botPanel = new JPanel(new GridBagLayout() );
		add(botPanel, cons);

		createBottomPanel(botPanel);

		//setButtonTexts(); // Again, due to the error in button impl 	
	}



	private void createRightPanel(JPanel panel) {
		GridBagConstraints consL;
		GridBagConstraints consR;

		{
			{
				GridBagConstraints c = new GridBagConstraints();
				c.weightx = 1;				c.weighty = 1;
				c.gridx = 0;				c.gridy = GridBagConstraints.RELATIVE;
				c.anchor = GridBagConstraints.NORTHEAST;
				c.ipadx = 4;				c.ipady = 2;
				//c.insets = new Insets(4,4,4,4);
				c.insets = new Insets(2,2,2,2);
				consL = c;
			}

			{
				GridBagConstraints c = new GridBagConstraints();
				c.weightx = 1;				c.weighty = 1;
				c.gridx = 1;				c.gridy = GridBagConstraints.RELATIVE;
				c.anchor = GridBagConstraints.NORTHWEST;
				c.ipadx = 4;				c.ipady = 2;
				//c.insets = new Insets(4,4,4,4);
				c.insets = new Insets(2,2,2,2);
				consR = c;
			}

		}

		//Messages.getString("GeneralPanel.Autostart") //$NON-NLS-1$
		panel.add(new JLabel(Messages.getString("GeneralPanel.Autostart")+": "), consL);  //$NON-NLS-1$//$NON-NLS-2$
		panel.add(autostartField, consR);

		panel.add(new JLabel(Messages.getString("GeneralPanel.DemoMode")+": "), consL);  //$NON-NLS-1$//$NON-NLS-2$
		panel.add(demoModeField, consR);

		String simTip = Messages.getString("ConfigPanelGeneral.SimulateDescription"); //$NON-NLS-1$
		panel.add(new JLabel(Messages.getString("ConfigPanelGeneral.Simulate")), consL); //$NON-NLS-1$
		panel.add(simulationModeField, consR);
		simulationModeField.setToolTipText(simTip);

		panel.add(new JLabel(Messages.getString("ConfigPanelGeneral.ShowSplash")), consL); //$NON-NLS-1$
		panel.add(splashScreenEnabledField, consR);				

		panel.add(new JLabel(Messages.getString("ConfigPanelGeneral.Autologin")), consL); //$NON-NLS-1$
		panel.add(autologinEnabledField, consR);				

		panel.add(new JLabel(Messages.getString("ConfigPanelGeneral.AutologinAs")), consL); //$NON-NLS-1$
		panel.add(autologinUserButton, consR);				
		autologinUserButton.addActionListener(new AbstractAction() { public void actionPerformed(ActionEvent e) { selectAutologinUser(); }});
		//autologinUserButton.setText("--");		

		panel.add(new JLabel(Messages.getString("ConfigPanelGeneral.NetworkActive")), consL); //$NON-NLS-1$
		panel.add(networkActiveField, consR);

		panel.add(new JLabel(Messages.getString("ConfigPanelGeneral.HostName")), consL); //$NON-NLS-1$
		panel.add(networkHostNameField, consR);
		
	}




	private void createLeftPanel(JPanel panel) {
		GridBagConstraints consL;
		GridBagConstraints consR;


		{
			{
				GridBagConstraints c = new GridBagConstraints();
				c.weightx = 1;
				c.weighty = 1;
				c.gridx = 0;
				c.gridy = GridBagConstraints.RELATIVE;
				c.anchor = GridBagConstraints.NORTHEAST;
				c.ipadx = 4;
				c.ipady = 2;
				//c.insets = new Insets(4,4,4,4);
				c.insets = new Insets(2,2,2,2);
				consL = c;
			}

			{
				GridBagConstraints c = new GridBagConstraints();
				c.weightx = 1;
				c.weighty = 1;
				c.gridx = 1;
				c.gridy = GridBagConstraints.RELATIVE;
				c.anchor = GridBagConstraints.NORTHWEST;
				c.ipadx = 4;
				c.ipady = 2;
				//c.insets = new Insets(4,4,4,4);
				c.insets = new Insets(2,2,2,2);
				consR = c;
			}

		}

		{
			panel.add(new JLabel(Messages.getString("ConfigPanelGeneral.Version")), consL); //$NON-NLS-1$
			//panel.add(new JLabel(Version.VERSION+" (bld "+Version.BUILD+")"), consR);

			JTextField vf = new JTextField(Version.VERSION+" (bld "+Version.BUILD+")");
			vf.setEditable(false);
			panel.add(vf, consR);
		}


		panel.add(new JLabel(Messages.getString("ExtendWidthOnResize")), consL); //$NON-NLS-1$ 
		panel.add( globalExtendWidthField, consR );

		panel.add(new JLabel(Messages.getString("ExtendHeightOnResize")), consL); //$NON-NLS-1$ 
		panel.add( globalExtendHeightField, consR );

		panel.add(new JLabel(Messages.getString("ConfigPanelGeneral.Login")), consL); //$NON-NLS-1$
		loginButton.addActionListener(new AbstractAction() { public void actionPerformed(ActionEvent e) { login(); }});
		//loginButton.setText("--");		
		panel.add(loginButton, consR);

		panel.add(new JLabel(Messages.getString("ConfigPanelGeneral.AlarmLog")), consL); //$NON-NLS-1$
		showEventLog.addActionListener(new AbstractAction() { public void actionPerformed(ActionEvent e) { openEventLogWindow(); }});
		showEventLog.setText(Messages.getString("ConfigPanelGeneral.ShowLog"));		 //$NON-NLS-1$
		panel.add(showEventLog, consR);

		panel.add(new JLabel(Messages.getString("ConfigPanelGeneral.HistoryGraph")), consL); //$NON-NLS-1$
		showHistoryGraphButton.addActionListener(new AbstractAction() { public void actionPerformed(ActionEvent e) { openHistoryGraphWindow(); }});
		showHistoryGraphButton.setText(Messages.getString("ConfigPanelGeneral.ShowHistory"));		 //$NON-NLS-1$
		panel.add(showHistoryGraphButton, consR);
		
		panel.add(new JLabel("Debug"), consL); 
		panel.add(debugPrtintField, consR);
		
		panel.add(new JLabel("Suppress alarms"), consL); 
		panel.add(suppressAlarmsField, consR);

		panel.add(new JLabel("Log file"), consL); 
		panel.add(logFileNameField, consR);
	}





	private void createBottomPanel(JPanel panel) {
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = GridBagConstraints.RELATIVE;
		c.gridy = 0;
		c.anchor = GridBagConstraints.NORTH;
		c.ipadx = 4;
		c.ipady = 2;
		c.insets = new Insets(4,4,4,4);


		JButton applyButton = new JButton();
		//panel.add(new JLabel("Apply: "), consL);
		applyButton.addActionListener(new AbstractAction() { public void actionPerformed(ActionEvent e) { applyChanges(); }});
		applyButton.setText(Messages.getString("ConfigPanelGeneral.ApplyButton"));		 //$NON-NLS-1$
		panel.add(applyButton, c);

		JButton discardButton = new JButton();
		//panel.add(new JLabel("System stop: "), consL);
		discardButton.addActionListener(new AbstractAction() { public void actionPerformed(ActionEvent e) { discardChanges(); }});
		discardButton.setText(Messages.getString("ConfigPanelGeneral.DiscardButton"));		 //$NON-NLS-1$
		panel.add(discardButton, c);
		
        applyButton.setIcon(new ImageIcon(ConfigurationFactory.getVisualConfiguration().getApplyIcon()));
        discardButton.setIcon(new ImageIcon(ConfigurationFactory.getVisualConfiguration().getDiscardIcon()));

	}


	protected void startSystem() {
		ConfigurationFactory.startSystem();		
	}

	protected void stopSystem() {
		ConfigurationFactory.stopSystem();		
	}


	//public boolean getDemoMode() {		return demoMode.isSelected();	}
	//public void setDemoMode(boolean demoMode) {		this.demoMode.setSelected( demoMode );	}
	protected void openEventLogWindow() {
		ConfigurationFactory.openEventLogWindow();

		/*
		Frame frame= JOptionPane.getFrameForComponent(this);
		JDialog popup = new JDialog(frame);
		popup.setTitle("Alarms");

		List<HistoryRecord> history = ConfigurationFactory.getTransientState().getAlarms();
		// XXX new Vector
		//PropertiesEditItemList<HistoryRecord> listPanel = new PropertiesEditItemList<HistoryRecord>(new Vector<HistoryRecord>(history));
		//listPanel = 
		HistoryViewList listPanel = new HistoryViewList(history);

		// TODO live update. move code to HistoryStorage? Or to CieHistoryStorage
		popup.add(listPanel);
		//popup.pack(); // too slow!
		popup.setSize(400, 600);
		popup.setVisible(true);
		*/
	}


	protected void openHistoryGraphWindow() {
		Frame frame= JOptionPane.getFrameForComponent(this);
		JDialog popup = new JDialog(frame);

		CliParameter parameter = ConfigurationFactory.getConfiguration().selectParameter(this);

		if(parameter == null)
			return;

		String selector = parameter.getName();//"system.memory.used";
		HistoryGraph hg = new HistoryGraph(ConfigurationFactory.getTransientState().getHistory(),selector );

		hg.setMaximum(parameter.getMaxValue());
		hg.setMinimum(parameter.getMinValue());
		String un = ""; //$NON-NLS-1$
		if(parameter.getUnit() != null)
			un = parameter.getUnit().getName();
		hg.setUnits(un);
		hg.setLegend(parameter.getLegend());

		popup.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		popup.setLocationByPlatform(true);
		popup.setAlwaysOnTop(false);
		popup.setTitle(selector+Messages.getString("ConfigPanelGeneral.History")); //$NON-NLS-1$
		popup.add(hg);
		popup.pack();
		popup.setVisible(true);
	}

	//public Boolean getNetworkActive() {		return networkActive.isSelected();	}
	//public void setNetworkActive(Boolean networkActive) {		this.networkActive.setSelected( networkActive );	}


	// Apply/discard

	protected void discardChanges() {
		networkActiveField.setSelected(bean.isNetworkActive());
		networkHostNameField.setText(bean.getNetNodeName());
		demoModeField.setSelected(bean.isDemoMode());
		simulationModeField.setSelected(bean.isSimulationMode());
		splashScreenEnabledField.setSelected(bean.isSplashScreenShown());
		autostartField.setSelected(bean.isAutostart());
		autologinUser = bean.getAutologinUser();
		autologinEnabledField.setSelected(bean.isAutologin());

		globalExtendWidthField.setSelected(bean.isGlobalExtendWidth());
		globalExtendHeightField.setSelected(bean.isGlobalExtendHeight());
		// TODO hack - this translates settings just due to the fact that configuration panel is shown first on program run
		//ConfigurationFactory.getVisualConfiguration().setGlobalExtendWidth(globalExtendWidthField.isSelected());
		//ConfigurationFactory.getVisualConfiguration().setGlobalExtendHeight(globalExtendHeightField.isSelected());

		suppressAlarmsField.setSelected(bean.isSuppressAlarms());
		
		logFileNameField.setText(bean.getLogFileName());
		
		setButtonTexts();
	}

	protected void applyChanges() {
		bean.setNetworkActive(networkActiveField.isSelected());
		bean.setNetNodeName(networkHostNameField.getText());
		bean.setDemoMode(demoModeField.isSelected());
		bean.setSimulationMode(simulationModeField.isSelected());
		bean.setSplashScreenShown(splashScreenEnabledField.isSelected());
		bean.setAutostart(autostartField.isSelected());
		bean.setAutologin(autologinEnabledField.isSelected());
		bean.setAutologinUser(autologinUser);

		bean.setGlobalExtendWidth(globalExtendWidthField.isSelected());
		bean.setGlobalExtendHeight(globalExtendHeightField.isSelected());

		bean.setDebug(debugPrtintField.isSelected());
		bean.setSuppressAlarms(suppressAlarmsField.isSelected());
		
		ConfigurationFactory.getVisualConfiguration().setGlobalExtendWidth(globalExtendWidthField.isSelected());
		ConfigurationFactory.getVisualConfiguration().setGlobalExtendHeight(globalExtendHeightField.isSelected());

		boolean logFnDiffers = !bean.getLogFileName().equals(logFileNameField.getText());
		
		bean.setLogFileName(logFileNameField.getText());
		
		if(logFnDiffers)
			Main.reopenLogFile();
		
		// TO DO we might apply some changes to the running system as well
	}

	protected void selectAutologinUser() {
		//CliUser user = ConfigurationFactory.getConfiguration().selectUser(this);
		CliUser user = ConfigurationFactory.getConfiguration().login(this);

		autologinUser = user;
		if(autologinUser == null)
			autologinEnabledField.setSelected(false);
		setButtonTexts();
	}

	protected void login() {
		ConfigurationFactory.getTransientState().login(this);
		setButtonTexts();
	}


	private void setButtonTexts() {
		autologinUserButton.setText((autologinUser == null) ? Messages.getString("ConfigPanelGeneral.NoUser") : autologinUser.getLogin()); //$NON-NLS-1$
		loginButton.setText(ConfigurationFactory.getTransientState().getLoggedInUserLogin());
	}

}
