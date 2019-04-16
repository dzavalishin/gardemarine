package ru.dz.shipMaster.ui.config.item;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import ru.dz.shipMaster.config.items.CliPipe;
import ru.dz.shipMaster.dev.common.BiPipe;

/**
 * Configuration item editor for bus item.
 * @author dz
 */
//public class CieDriver extends SideFrameConfigItemEditor //implements ActionListener
public class CiePipe extends SubWindowConfigItemEditor 
{
	public static final String DRV_CLASS_PREFIX = "ru.dz.shipMaster.dev.common.";
	private static final String NODRV = "None";

	final private String[] driverClasses = { 
			NODRV, 
			"TcpBiPipe",
			"TcpListenBiPipe",
			"UdpBiPipe",
			"ComPortGnuBiPipe",
			"FT232BiPipe",
			"PipeBiPipe"
	};


	private JLabel drvLabel;
	private JButton searchButton;
	private JComboBox drvList;

	private final CliPipe bean;
	private int ignoreEvent = 0;

	/**
	 * Create new editor for this bean. 
	 * @param bean Bean to edit.
	 */
	@SuppressWarnings("serial")
	public CiePipe(CliPipe bean, BiPipe myBus) {
		super(new Dimension(240,200),"Channel properties");
		this.bean = bean;
		//this.myDriver = myDriver;

		searchButton = new JButton();
		searchButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				searchForDeviceInstance();			
			}});
		searchButton.setText("AutoSearch");
		searchButton.setEnabled(false);

		panel.add(new JLabel("Channel:"),consL);
		panel.add(drvLabel = new JLabel("unknown"),consR);

		panel.add(new JLabel("Autodetect:"),consL);
		panel.add(searchButton,consR);

		panel.add(new JLabel("Channel class:"),consL);
		drvList = new JComboBox(driverClasses);
		drvList.setSelectedIndex(0);
		drvList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { selectBusClass(e); }});

		panel.add(drvList,consR);


		//if(myDriver != null) setSFPanel(myDriver.getSetupPanel());
		setPipe(myBus);

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
			bean.setPipe(null);
			return;
		}

		try {
			Class<? extends BiPipe> drvClass = (Class<? extends BiPipe>) Class.forName(drvName);

			BiPipe bus = drvClass.newInstance();

			bean.setPipe(bus);
		}
		catch(ClassNotFoundException e1)
		{
			//log.severe("Driver class not found: "+e.toString());
			log.log(Level.SEVERE,"Pipe class not found: ",e1);
			return;
		} catch (InstantiationException e1) {
			log.log(Level.SEVERE,"Pipe object can't be created: ",e1);
			return;
		} catch (IllegalAccessException e1) {
			log.log(Level.SEVERE,"Pipe object can't be created: ",e1);
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
	public String getTypeName() { return "Channel"; }

	/**
	 * Returns name of item instance.
	 * @return Instance name string.
	 */

	@Override
	public String getName() { return bean.getName(); }

	@Override
	public void applySettings() {
		BiPipe pipe = bean.getPipe();
		if(pipe != null) pipe.savePanelSettings();
		setTitle();
		drvLabel.setText(pipe.getName());
		bean.informContainers();
	}

	@Override
	public void discardSettings() {
		BiPipe dvr = bean.getPipe();
		if(dvr != null)
		{
			try {


				String cn = dvr.getClass().getName();
				if( cn.startsWith(CiePipe.DRV_CLASS_PREFIX))
					cn = cn.substring(CiePipe.DRV_CLASS_PREFIX.length());

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
	public void setPipe(BiPipe pipe) {
		//this.myDriver = myDriver;
		if(pipe != null)
		{
			setSFPanel(pipe.getSetupPanel());
			//searchButton.setEnabled(myBus.isAutoSeachSupported());
			drvLabel.setText(pipe.getName());
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
