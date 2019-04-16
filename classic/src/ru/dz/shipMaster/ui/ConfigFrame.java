package ru.dz.shipMaster.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.LineBorder;

import ru.dz.shipMaster.config.Configuration;
import ru.dz.shipMaster.config.ConfigurationFactory;
import ru.dz.shipMaster.config.items.CliAlarm;
import ru.dz.shipMaster.config.items.CliAlarmStation;
import ru.dz.shipMaster.config.items.CliBus;
import ru.dz.shipMaster.config.items.CliConversion;
import ru.dz.shipMaster.config.items.CliDriver;
import ru.dz.shipMaster.config.items.CliGroup;
import ru.dz.shipMaster.config.items.CliInstrument;
import ru.dz.shipMaster.config.items.CliInstrumentPanel;
import ru.dz.shipMaster.config.items.CliLogger;
import ru.dz.shipMaster.config.items.CliNetHost;
import ru.dz.shipMaster.config.items.CliNetInput;
import ru.dz.shipMaster.config.items.CliParameter;
import ru.dz.shipMaster.config.items.CliPipe;
import ru.dz.shipMaster.config.items.CliSystemDriver;
import ru.dz.shipMaster.config.items.CliUser;
import ru.dz.shipMaster.config.items.CliWindow;
import ru.dz.shipMaster.config.items.CliWindowStructure;
import ru.dz.shipMaster.data.units.Unit;
import ru.dz.shipMaster.data.units.UnitGroup;
import ru.dz.shipMaster.ui.config.ConfigPanel;
import ru.dz.shipMaster.ui.config.panel.ConfigPanelColors;
import ru.dz.shipMaster.ui.config.panel.ConfigPanelGeneral;
import ru.dz.shipMaster.ui.config.panel.ConfigPanelList;
import ru.dz.shipMaster.ui.config.panel.ConfigPanelPropertiesList;
import ru.dz.shipMaster.ui.config.panel.ConfigPanelTree;
import ru.dz.shipMaster.ui.misc.VisualHelpers;

/**
 * NB! This class must be instantiated AFTER the configuration was loaded!
 * @author dz
 *
 */

public class ConfigFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5362992357427104628L;

	private static final Logger log = Logger.getLogger(ConfigFrame.class.getName()); 

	private Configuration c = ConfigurationFactory.getConfiguration();
	
	private ConfigPanelGeneral configPanelGeneral = new ConfigPanelGeneral(c.getGeneral());
	private ConfigPanelColors configPanelColors = new ConfigPanelColors();

	private ConfigPanelList<CliUser> configPanelUsers = new ConfigPanelList<CliUser>(Messages.getString("ConfigFrame.UsersTab"),Messages.getString("ConfigFrame.UsersTabTip"),CliUser.class, c.getUserItems(), c.getUserLibItems()); //$NON-NLS-1$ //$NON-NLS-2$
	private ConfigPanelList<CliGroup> configPanelGroups = new ConfigPanelList<CliGroup>(Messages.getString("ConfigFrame.GroupsTab"),Messages.getString("ConfigFrame.GroupsTabTip"),CliGroup.class, c.getGroupItems(), c.getGroupLibItems() ); //$NON-NLS-1$ //$NON-NLS-2$
	private ConfigPanelList<CliAlarmStation> configPanelAlarmStations = new ConfigPanelList<CliAlarmStation>(Messages.getString("ConfigFrame.AStationsTab"),Messages.getString("ConfigFrame.AStationsTabTip"),CliAlarmStation.class, c.getAlarmStationItems(), c.getAlarmStationLibItems()); //$NON-NLS-1$ //$NON-NLS-2$
	private ConfigPanelList<CliDriver> configPanelDrivers = new ConfigPanelList<CliDriver>(Messages.getString("ConfigFrame.DriversTab"),Messages.getString("ConfigFrame.DriversTabTip"),CliDriver.class, c.getDriverItems(), c.getDriverLibItems()); //$NON-NLS-1$ //$NON-NLS-2$
	private ConfigPanelList<CliBus> configPanelBuses = new ConfigPanelList<CliBus>(Messages.getString("ConfigFrame.BusesTab"),Messages.getString("ConfigFrame.BusesTabTip"),CliBus.class, c.getBusItems(), c.getBusLibItems()); //$NON-NLS-1$ //$NON-NLS-2$
	private ConfigPanelList<CliPipe> configPanelPipes = new ConfigPanelList<CliPipe>(Messages.getString("ConfigFrame.PipesTab"),Messages.getString("ConfigFrame.PipesTabTip"),CliPipe.class, c.getPipeItems(), c.getPipeLibItems()); //$NON-NLS-1$ //$NON-NLS-2$
	private ConfigPanelTree<CliParameter> configPanelParameters  = new ConfigPanelTree<CliParameter>(Messages.getString("ConfigFrame.ParametersTab"),Messages.getString("ConfigFrame.ParametersTabTip"),CliParameter.class, c.getParameterItems(), c.getParameterLibItems()); //$NON-NLS-1$ //$NON-NLS-2$
	private ConfigPanelList<CliConversion> configPanelConversions  = new ConfigPanelList<CliConversion>(Messages.getString("ConfigFrame.ConversionsTab"),Messages.getString("ConfigFrame.ConversionsTabTip"),CliConversion.class, c.getConversionItems(), c.getConversionLibItems()); //$NON-NLS-1$ //$NON-NLS-2$
	private ConfigPanelList<CliWindow> configPanelWindows = new ConfigPanelList<CliWindow>(Messages.getString("ConfigFrame.WindowsTab"),Messages.getString("ConfigFrame.WindowsTabTip"),CliWindow.class, c.getWindowItems(), c.getWindowLibItems()); //$NON-NLS-1$ //$NON-NLS-2$
	private ConfigPanelList<CliLogger> configPanelLoggers = new ConfigPanelList<CliLogger>(Messages.getString("ConfigFrame.LoggersTab"),Messages.getString("ConfigFrame.LoggersTabTip"),CliLogger.class, c.getLoggerItems(), c.getLoggerLibItems()); //$NON-NLS-1$ //$NON-NLS-2$
	private ConfigPanelList<CliWindowStructure> configPanelWindowStructures = new ConfigPanelList<CliWindowStructure>(Messages.getString("ConfigFrame.WinStructsTab"),Messages.getString("ConfigFrame.WinStructsTabTip"),CliWindowStructure.class, c.getWindowStructureItems(), c.getWindowStructureLibItems()); 	 //$NON-NLS-1$ //$NON-NLS-2$
	private ConfigPanelList<CliInstrumentPanel> configPanelInstrumentPanels = new ConfigPanelList<CliInstrumentPanel>(Messages.getString("ConfigFrame.PanelsTab"),Messages.getString("ConfigFrame.PanelsTabTip"),CliInstrumentPanel.class, c.getInstrumentPanelItems(), c.getInstrumentPanelLibItems());  //$NON-NLS-1$ //$NON-NLS-2$
	private ConfigPanelList<CliInstrument> configPanelInstruments = new ConfigPanelList<CliInstrument>(Messages.getString("ConfigFrame.InstrumentsTab"),Messages.getString("ConfigFrame.InstrumentsTabTip"),CliInstrument.class, c.getInstrumentItems(), c.getInstrumentLibItems());  //$NON-NLS-1$ //$NON-NLS-2$
	private ConfigPanelList<CliAlarm> configPanelAlarms = new ConfigPanelList<CliAlarm>(Messages.getString("ConfigFrame.AlarmsTab"),Messages.getString("ConfigFrame.AlarmsTabTip"),CliAlarm.class, c.getAlarmItems(), c.getAlarmLibItems());  //$NON-NLS-1$ //$NON-NLS-2$
	private ConfigPanelList<Unit> configPanelUnits = new ConfigPanelList<Unit>(Messages.getString("ConfigFrame.UnitsTab"),Messages.getString("ConfigFrame.UnitsTabTip"),Unit.class, c.getUnitItems(), c.getUnitLibItems());  //$NON-NLS-1$ //$NON-NLS-2$
	private ConfigPanelList<UnitGroup> configPanelUnitGroups = new ConfigPanelList<UnitGroup>(Messages.getString("ConfigFrame.UnitGroupsTab"),Messages.getString("ConfigFrame.UnitGroupsTabTip"),UnitGroup.class, c.getUnitGroupItems(), c.getUnitGroupLibItems());  //$NON-NLS-1$ //$NON-NLS-2$
	private ConfigPanelList<CliNetHost> configPanelNetHosts = new ConfigPanelList<CliNetHost>(Messages.getString("ConfigFrame.NetHostsTabName"),Messages.getString("ConfigFrame.NetHostsTabDescription"),CliNetHost.class, c.getNetHostItems(), c.getNetHostLibItems());   //$NON-NLS-1$ //$NON-NLS-2$
	private ConfigPanelList<CliNetInput> configPanelNetInputs = new ConfigPanelList<CliNetInput>(Messages.getString("ConfigFrame.NetInputsTabName"),Messages.getString("ConfigFrame.NetInputsTabDescription"),CliNetInput.class, c.getNetInputItems(), c.getNetInputLibItems());   //$NON-NLS-1$ //$NON-NLS-2$

	private ConfigPanelPropertiesList<CliSystemDriver> configPanelSystemDrivers = new ConfigPanelPropertiesList<CliSystemDriver>(Messages.getString("ConfigFrame.SysDriversTab"),Messages.getString("ConfigFrame.SysDriversTabTip"),c.getSystemDriverItems());  //$NON-NLS-1$ //$NON-NLS-2$
	
	private JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP,JTabbedPane.WRAP_TAB_LAYOUT);
	//private JPanel saveLoad = new JPanel();

	private JPanel globalPanel = new JPanel();

	private JButton saveButton, saveVisButton;


	public ConfigFrame() {
		super();
		initialize();
	}

	private void initialize() {
        //this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.setJMenuBar(getMenuMain());
        // TO DO move to main?
        //this.setPreferredSize(new Dimension(1140, 800));
        this.setMinimumSize(new Dimension(700, 300));
        this.setTitle(Messages.getString("ConfigFrame.SettingsTitle")); //$NON-NLS-1$
        this.setIconImage(VisualHelpers.getApplicationIconImage());
        this.setLocationByPlatform(true);
        
        //tabbedPane.setLayout(new GridBagLayout());
        
        
        //getContentPane().add(tabbedPane, BorderLayout.CENTER);

        GridBagConstraints c = new GridBagConstraints();
        c.weightx = c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = c.gridy = 0;
        c.insets = new Insets(1,4,4,4);
        
        getContentPane().setLayout(new GridBagLayout());
        getContentPane().add(tabbedPane, c);
        c.gridy = 1;
        getContentPane().add(globalPanel, c);
        fillGlobalPanel(globalPanel);

        addTab(configPanelGeneral);
        addTab(configPanelColors);
        
        addTab(configPanelNetHosts);
        addTab(configPanelNetInputs);
        
		addTab(configPanelInstruments);
        addTab(configPanelInstrumentPanels);
		addTab(configPanelWindows);

		addTab(configPanelParameters);
		addTab(configPanelConversions);
		addTab(configPanelLoggers);
		
        addTab(configPanelUnits);
        addTab(configPanelUnitGroups);

        addTab(configPanelAlarms);
		addTab(configPanelAlarmStations);

        addTab(configPanelWindowStructures);

		addTab(configPanelUsers);
		addTab(configPanelGroups);

		addTab(configPanelDrivers );
		addTab(configPanelSystemDrivers);
		addTab(configPanelBuses);
		addTab(configPanelPipes);


		
		//addTab(new ConfigPanelParameters());
		//addTab(new ConfigPanelConversions());
		//addTab(new ConfigPanelWindows());
		//addTab(new ConfigPanelLoggers());
		
        //getContentPane().add(new ConfigPanelDrivers());
        

		/*
		tabbedPane.addTab(Messages.getString("ConfigFrame.SaveLoadTab"), null, saveLoad, Messages.getString("ConfigFrame.SaveLoadTabTip")); //$NON-NLS-1$ //$NON-NLS-2$

		JButton save = new JButton();
		saveLoad.add(save);
		save.setAction(new AbstractAction() {
			private static final long serialVersionUID = -1167558137161956957L;

			public void actionPerformed(ActionEvent e) {
				try {
					ConfigurationFactory.save();
				} catch (FileNotFoundException e1) {
					log.log(Level.SEVERE,Messages.getString("ConfigFrame.CantSaveConfError"),e); //$NON-NLS-1$
				}
				
			}});
		save.setText(Messages.getString("ConfigFrame.SaveButton")); //$NON-NLS-1$
		*/
		
        this.pack();        
        this.setVisible(true);
        
        //enableConfigTabs(false);
	}

	@SuppressWarnings("serial")
	private void fillGlobalPanel(JPanel gp) {

		gp.setBorder(new LineBorder(Color.GRAY, 1));
		gp.setBackground(Color.decode("#999999"));
		
		
		JButton startButton = new JButton();
		startButton.addActionListener(new AbstractAction() { public void actionPerformed(ActionEvent e) { startSystem(); }});
		startButton.setText(Messages.getString("ConfigFrame.StartButton")); //$NON-NLS-1$		
		startButton.setToolTipText(Messages.getString("ConfigFrame.StartSystemButtonDescription")); //$NON-NLS-1$
		gp.add(startButton);

		JButton stopButton = new JButton();
		stopButton.addActionListener(new AbstractAction() { public void actionPerformed(ActionEvent e) { stopSystem(); }});
		stopButton.setText(Messages.getString("ConfigFrame.StopButton")); //$NON-NLS-1$		
		stopButton.setToolTipText(Messages.getString("ConfigFrame.StopSystemButtonDescription"));		 //$NON-NLS-1$
		gp.add(stopButton);
		
		
		saveButton = new JButton();
		gp.add(saveButton);
		saveButton.setAction(new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				try {
					ConfigurationFactory.save();
				} catch (FileNotFoundException e1) {
					log.log(Level.SEVERE,Messages.getString("ConfigFrame.CantSaveConfError"),e); //$NON-NLS-1$
				}
				
			}});
		saveButton.setText(Messages.getString("ConfigFrame.SaveButton")); //$NON-NLS-1$
		saveButton.setToolTipText(Messages.getString("ConfigFrame.SaveSetupButtonDescription")); //$NON-NLS-1$

		
		saveVisButton = new JButton();
		gp.add(saveVisButton);
		saveVisButton.setAction(new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				try {
					ConfigurationFactory.save();
					// TODO Temp! Remove!
					ConfigurationFactory.saveVisualConfiguration();
				} catch (FileNotFoundException e1) {
					log.log(Level.SEVERE,Messages.getString("ConfigFrame.CantSaveConfError"),e); //$NON-NLS-1$
				}
				
			}});
		saveVisButton.setText("Save Visual Settings"); //$NON-NLS-1$
		//saveVisButton.setToolTipText(Messages.getString("ConfigFrame.SaveSetupButtonDescription")); //$NON-NLS-1$
		
		
	}

	
	protected void startSystem() {
		ConfigurationFactory.startSystem();		
	}
	
	protected void stopSystem() {
		ConfigurationFactory.stopSystem();		
	}
	
	
	
	private void addTab(ConfigPanel p) {
		//p.setMinimumSize(new Dimension(300,200));
		//p.setBorder(new LineBorder(Color.RED));
		tabbedPane.addTab(p.getName(), p.getIcon(), p, p.getTip());
	}



	public void enableConfigTabs(boolean enabled)
	{
		saveButton.setEnabled(enabled);
		
		if(!enabled)
			tabbedPane.setSelectedComponent(configPanelGeneral);

		for( int i = 0; i < tabbedPane.getComponentCount(); i++ )
		{
			if( tabbedPane.getComponentAt(i) == configPanelGeneral )
			{
				tabbedPane.setEnabledAt(i, true);
			}
			else
				tabbedPane.setEnabledAt(i, enabled);
		}
	}


    
}
