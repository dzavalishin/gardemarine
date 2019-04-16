package ru.dz.shipMaster.dev.system;

import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ru.dz.shipMaster.config.ConfigurationFactory;
import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.data.misc.CommunicationsException;
import ru.dz.shipMaster.dev.AbstractDriver;
import ru.dz.shipMaster.dev.DriverPort;
import ru.dz.shipMaster.dev.PortDataOutput;

/**
 * System tray driver - translates data to system tray tool tip.
 * @author dz
 */
public class Tray extends AbstractDriver {
	private JTextField tooltipField;
	private String firstMessageTitle;
	private DriverPort firstMessagePort;

	/**
	 * Default constructor.
	 */
	public Tray()
	{
		// empty
	}
	
	@Override
	protected void setupPanel(JPanel panel) {
		panel.add(new JLabel("First message line title:"),consL);
		panel.add(tooltipField = new JTextField(),consR);
		tooltipField.setColumns(20);
	}
	
	@Override
	protected void doLoadPanelSettings() {
		tooltipField.setText(firstMessageTitle);
	}

	@Override
	protected void doSavePanelSettings() {
		firstMessageTitle = tooltipField.getText();
	}

	@Override
	protected void doStartDriver() throws CommunicationsException  {
		/*
		try {
			tray.add(tIcon);			
		} catch (AWTException e) {
			log.log(Level.SEVERE,"Error adding tray icon",e);
		}*/
		setFirstMessage("No data");			
	}

	private void setFirstMessage(String text)
	{
		if(firstMessagePort.getGivenName() != null)
			firstMessageTitle = firstMessagePort.getGivenName();
		if(firstMessageTitle != null && text != null)
			ConfigurationFactory.getTransientState().setTrayMessage(0,firstMessageTitle+": "+text);
	}
	
	@Override
	protected void doStopDriver() throws CommunicationsException {
		ConfigurationFactory.getTransientState().setTrayMessage(0,null);
	}

	@Override
	public String getDeviceName() { return "system tray driver"; }

	@Override
	public boolean isAutoSeachSupported() { return false; }


	private final PortDataOutput messageOnePortDataOutput = new PortDataOutput() {
		@Override
		public void receiveDoubleData(double value) { 
			setFirstMessage(String.format("%.2f", value)); 
			}
		};

		private final PortDataOutput message2PortDataOutput = new PortDataOutput() {
			@Override
			public void receiveDoubleData(double value) { 
				//setFirstMessage(String.format("%.2f", value)); 
				}
			};
		
	@Override
	protected void updatePorts(Vector<DriverPort> ports) {
		DriverPort.absentize(ports);
		DriverPort.sortPorts(ports);

		/*{
			firstMessagePort = DriverPort.getPort(ports,0);
			firstMessagePort.setDirection(Direction.Output);
			firstMessagePort.setHardwareName("Tray message 1");
			firstMessagePort.setType(Type.Numeric);
			firstMessagePort.setDescription("Data from this output will be shown in a System Tray tooltip for Gardemarine icon.");
			firstMessagePort.setPortDataOutput(messageOnePortDataOutput);
		}*/

		{
			firstMessagePort = getPort(ports,0,Direction.Output,Type.Numeric,"Tray message 1");
			firstMessagePort.setDescription("Data from this output will be shown in a System Tray tooltip for Gardemarine icon.");
			firstMessagePort.setPortDataOutput(messageOnePortDataOutput);
		}
		
		{
			firstMessagePort = getPort(ports,1,Direction.Output,Type.Numeric,"Tray message 2 (unimplemented)");
			firstMessagePort.setDescription("Data from this output will be shown in a System Tray tooltip for Gardemarine icon.");
			firstMessagePort.setPortDataOutput(message2PortDataOutput);
		}

	}

	/**
	 * @return Title for the first tray tool tip message line.
	 */
	public String getFirstMessageTitle() {		return firstMessageTitle;	}

	/**
	 * @param toolTip Title for the first tray tool tip message line.
	 */
	public void setFirstMessageTitle(String firstMessageTitle) {
		this.firstMessageTitle = firstMessageTitle;
	}

	@Override
	public String getInstanceName() {
		return "internal";
	}


}
