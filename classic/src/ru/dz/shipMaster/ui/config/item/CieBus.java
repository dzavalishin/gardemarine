package ru.dz.shipMaster.ui.config.item;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import ru.dz.shipMaster.config.items.CliBus;
import ru.dz.shipMaster.dev.IBus;

/**
 * Configuration item editor for bus item.
 * @author dz
 */
//public class CieDriver extends SideFrameConfigItemEditor //implements ActionListener
public class CieBus extends SubWindowConfigItemEditor 
{
	public static final String DRV_CLASS_PREFIX = "ru.dz.shipMaster.dev.bus.";
	private static final String NODRV = "None";

	final private String[] driverClasses = { 
			NODRV, 
			"ModBus",
			"NmeaBus",
	};


	private JLabel drvLabel;
	private JButton searchButton;
	private JComboBox drvList;

	private final CliBus bean;
	private int ignoreEvent = 0;

	/**
	 * Create new editor for this bean. 
	 * @param bean Bean to edit.
	 */
	@SuppressWarnings("serial")
	public CieBus(CliBus bean, IBus myBus) {
		super(new Dimension(240,200),"Bus properties");
		this.bean = bean;
		//this.myDriver = myDriver;

		searchButton = new JButton();
		searchButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				searchForDeviceInstance();			
			}});
		searchButton.setText("AutoSearch");
		searchButton.setEnabled(false);

		panel.add(new JLabel("Bus type:"),consL);
		panel.add(drvLabel = new JLabel("No bus"),consR);

		panel.add(new JLabel("Autodetect:"),consL);
		panel.add(searchButton,consR);

		panel.add(new JLabel("Bus class:"),consL);
		drvList = new JComboBox(driverClasses);
		drvList.setSelectedIndex(0);
		drvList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { selectBusClass(e); }});

		panel.add(drvList,consR);


		//if(myDriver != null) setSFPanel(myDriver.getSetupPanel());
		setBus(myBus);

		discardSettings();
	}

	protected void searchForDeviceInstance() {
	}

	/**
	 * drvList support. 
	 */  
	public void selectBusClass(ActionEvent e) {
		// XXX hack!
		// We ignore events caused by setting combo box position from code 
		if(ignoreEvent > 0)
			return;

		JComboBox cb = (JComboBox)e.getSource();
		String rawName = (String)cb.getSelectedItem();
		String drvName = DRV_CLASS_PREFIX+rawName;

		if( rawName.equalsIgnoreCase(NODRV))
		{
			bean.setBus(null);
			return;
		}

		try {
			Class<? extends IBus> drvClass = (Class<? extends IBus>) Class.forName(drvName);

			IBus bus = drvClass.newInstance();

			bean.setBus(bus);
		}
		catch(ClassNotFoundException e1)
		{
			//log.severe("Driver class not found: "+e.toString());
			log.log(Level.SEVERE,"Bus class not found: ",e1);
			return;
		} catch (InstantiationException e1) {
			log.log(Level.SEVERE,"Bus object can't be created: ",e1);
			return;
		} catch (IllegalAccessException e1) {
			log.log(Level.SEVERE,"Bus object can't be created: ",e1);
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
	public String getTypeName() { return "Bus"; }

	/**
	 * Returns name of item instance.
	 * @return Instance name string.
	 */

	@Override
	public String getName() { return bean.getName(); }

	@Override
	public void applySettings() {
		IBus drv = bean.getBus();
		if(drv != null) drv.savePanelSettings();
		setTitle();
	}

	@Override
	public void discardSettings() {
		IBus dvr = bean.getBus();
		if(dvr != null)
		{
			try {


				String cn = dvr.getClass().getName();
				if( cn.startsWith(CieBus.DRV_CLASS_PREFIX))
					cn = cn.substring(CieBus.DRV_CLASS_PREFIX.length());

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
	public void setBus(IBus myBus) {
		//this.myDriver = myDriver;
		if(myBus != null)
		{
			setSFPanel(myBus.getSetupPanel());
			searchButton.setEnabled(myBus.isAutoSeachSupported());
			drvLabel.setText(myBus.getDeviceName());
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
