package ru.dz.shipMaster.ui.config.item;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import ru.dz.shipMaster.config.items.CliDriver;
import ru.dz.shipMaster.dev.AbstractDriver;

/**
 * Configuration item editor for driver item.
 * @author dz
 */
//public class CieDriver extends SideFrameConfigItemEditor //implements ActionListener
public class CieDriver extends SubWindowConfigItemEditor 
{
	public static final String DRV_CLASS_PREFIX = "ru.dz.shipMaster.dev.";
	private static final String NODRV = "None";

	final private String[] driverClasses = { 
			NODRV, 
			"controller.DigitalZoneHomeNet",
			"controller.owen.MVA",
			"controller.owen.MDVV",
			"controller.OwenMVA",
			"controller.OwenMDVV",
			"controller.MMNet101",
			"controller.deif.MTR2",
			"controller.deif.MTR2Direct",
			"controller.technicomar.Micromar",
			"camera.WebCameraImage",
			"misc.LPT",
			"misc.TestSignalDriver",
			"ups.Megatec",
			"ups.MegatecUpsDriver",
			"loop.GeneratorDieselController",
			"loop.HornSequensor",
			"loop.Subtractor",
			"loop.BooleanOr",
			"loop.Adder",
			"loop.Divider",
			"loop.Constant",
			"loop.TimeCounter",
			"loop.Timer",
			"nmeaSentence.NmeaDBT",
			"nmeaSentence.NmeaDPT",
			"nmeaSentence.NmeaGGA",
			"nmeaSentence.NmeaGLL",
			"nmeaSentence.NmeaHDM",
			"nmeaSentence.NmeaHDT",
			"nmeaSentence.NmeaMDA",
			"nmeaSentence.NmeaMTW",
			"nmeaSentence.NmeaMWD",
			"nmeaSentence.NmeaMWV",
			"nmeaSentence.NmeaRMA",
			"nmeaSentence.NmeaRMC",
			"nmeaSentence.NmeaRPM",
			"nmeaSentence.NmeaVHW",
			"nmeaSentence.NmeaVLW",
			"nmeaSentence.NmeaVTG",
			"nmeaSentence.NmeaXDR",
			"nmeaSentence.NmeaZDA",
			"script.Script",
			"display.sigma.ASC105", 
			"display.SigmaASC105", 
			"clock.SntpClient",
			"controller.DigitalZone128",
			"controller.Wago750",
			"controller.CBus",
	};


	private JLabel drvLabel;
	private JButton searchButton;
	private JComboBox drvList;

	private final CliDriver bean;
	private int ignoreEvent = 0;

	/**
	 * Create new editor for this bean. 
	 * @param bean Bean to edit.
	 */
	@SuppressWarnings("serial")
	public CieDriver(CliDriver bean, AbstractDriver myDriver) {
		super(new Dimension(240,200),"Driver properties");
		this.bean = bean;
		//this.myDriver = myDriver;

		searchButton = new JButton();
		searchButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				searchForDeviceInstance();			
			}});
		searchButton.setText("AutoSearch");
		searchButton.setEnabled(false);

		panel.add(new JLabel("Device type:"),consL);
		panel.add(drvLabel = new JLabel("No driver"),consR);

		panel.add(new JLabel("Autodetect:"),consL);
		panel.add(searchButton,consR);

		panel.add(new JLabel("Driver class:"),consL);
		drvList = new JComboBox(driverClasses);
		drvList.setSelectedIndex(0);
		drvList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { selectDriverClass(e); }});

		panel.add(drvList,consR);


		//if(myDriver != null) setSFPanel(myDriver.getSetupPanel());
		setDriver(myDriver);

		discardSettings();
	}

	protected void searchForDeviceInstance() {
	}

	/**
	 * drvList support. 
	 */  
	public void selectDriverClass(ActionEvent e) {
		// XXX hack!
		// We ignore events caused by setting combo box position from code 
		if(ignoreEvent > 0)
			return;

		JComboBox cb = (JComboBox)e.getSource();
		String rawName = (String)cb.getSelectedItem();
		String drvName = DRV_CLASS_PREFIX+rawName;

		if( rawName.equalsIgnoreCase(NODRV))
		{
			bean.setDriver(null);
			return;
		}

		try {
			Class<? extends AbstractDriver> drvClass = (Class<? extends AbstractDriver>) Class.forName(drvName);

			AbstractDriver driver = drvClass.newInstance();

			bean.setDriver(driver);
		}
		catch(ClassNotFoundException e1)
		{
			//log.severe("Driver class not found: "+e.toString());
			log.log(Level.SEVERE,"Driver class not found: ",e1);
			return;
		} catch (InstantiationException e1) {
			log.log(Level.SEVERE,"Driver object can't be created: ",e1);
			return;
		} catch (IllegalAccessException e1) {
			log.log(Level.SEVERE,"Driver object can't be created: ",e1);
			return;
		}


	}

	@Override
	protected void informContainer() {		bean.informContainers(); }

	/**
	 * Returns name of subclass's item type, like user, alarm station, etc
	 * @return constant name for each subclass
	 */
	@Override
	public String getTypeName() { return "Driver"; }

	/**
	 * Returns name of item instance.
	 * @return Instance name string.
	 */

	@Override
	public String getName() { return bean.getName(); }

	@Override
	public void applySettings() {
		AbstractDriver drv = bean.getDriver();
		if(drv != null) drv.savePanelSettings();
		setTitle();
	}

	@Override
	public void discardSettings() {
		AbstractDriver dvr = bean.getDriver();
		if(dvr != null)
		{
			try {


				String cn = dvr.getClass().getName();
				if( cn.startsWith(CieDriver.DRV_CLASS_PREFIX))
					cn = cn.substring(CieDriver.DRV_CLASS_PREFIX.length());

				int select = 0;
				for(int i = 0; i < driverClasses.length; i++)
				{
					if(driverClasses[i].equals(cn))
					{
						select = i;
						break;
					}
				}
				ignoreEvent++;
				drvList.setSelectedIndex(select);
				ignoreEvent--;
			} 
			finally
			{
				dvr.loadPanelSettings();
			}
		}

	}

	//private AbstractDriver myDriver;
	public void setDriver(AbstractDriver myDriver) {
		//this.myDriver = myDriver;
		if(myDriver != null)
		{
			setSFPanel(myDriver.getSetupPanel());
			searchButton.setEnabled(myDriver.isAutoSeachSupported());
			drvLabel.setText(myDriver.getDeviceName());
		}
		else
		{
			setSFPanel(null);
			drvLabel.setText("");
		}
		panel.validate();
		panel.getParent().repaint(100);
	}

}
