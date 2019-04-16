package ru.dz.shipMaster.dev.controller;

import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.data.misc.CommunicationsException;
import ru.dz.shipMaster.dev.DriverPort;
import ru.dz.shipMaster.dev.PortDataOutput;
import ru.dz.shipMaster.dev.ThreadedDriver;
import ru.dz.shipMaster.dev.common.TcpBiPipe;
import ru.dz.shipMaster.dev.controller.dz.HomeNetGroup;
import ru.dz.shipMaster.dev.controller.dz.HomeNetPacket;
import ru.dz.shipMaster.dev.controller.dz.HomeNetProtocol;

public class DigitalZoneHomeNet extends ThreadedDriver {

	private static final int DRV_TASK_MSEC = 200;
	private static final int POLL_MSEC = 10000;
	private static final int POLL_COUNTER = POLL_MSEC/DRV_TASK_MSEC;

	private static final int MAX_GROUPS = 4;
	private JTextField ipAddressField = new JTextField(20);
	private String ipAddressString = "";
	private InetAddress ipAddress;

	private JTextField tcpPortField = new JTextField(6);
	private int tcpPort = 13033;

	private JCheckBox [] unitEnableCheckbox = new JCheckBox[MAX_GROUPS];//{ new JCheckBox(), new JCheckBox(), new JCheckBox(), new JCheckBox() };
	private JTextField [] groupNumberField = new JTextField[MAX_GROUPS];//{ new JTextField(), new JTextField(), new JTextField(), new JTextField() };
	private JCheckBox [] unitActiveCheckbox = new JCheckBox[MAX_GROUPS];//{ new JCheckBox(), new JCheckBox(), new JCheckBox(), new JCheckBox() };

	private HomeNetGroup[] group = new HomeNetGroup[MAX_GROUPS];

	private final static int nOutputPorts = MAX_GROUPS*2;
	private DriverPort[] outputPorts = new DriverPort[ nOutputPorts ];  
	protected int[] prevOutput = new int[nOutputPorts];

	private final static int nInputPorts = MAX_GROUPS;
	private DriverPort[] inputPorts = new DriverPort[ nInputPorts ];

	private final static int nTempInputPorts = MAX_GROUPS;
	private DriverPort[] tempInputPorts = new DriverPort[ nTempInputPorts ];
	
	
	private TcpBiPipe pipe;
	private HomeNetProtocol protocolDriver;

	public DigitalZoneHomeNet() {
		super(DRV_TASK_MSEC, Thread.NORM_PRIORITY, "DZ:HomeNet updater");

		for(int i = 0; i < MAX_GROUPS; i++)
		{
			unitEnableCheckbox[i] = new JCheckBox();
			unitActiveCheckbox[i] = new JCheckBox();
			groupNumberField[i] = new JTextField(6);
			group[i] = new HomeNetGroup();
			prevOutput[i] = Integer.MAX_VALUE; 
			prevOutput[i+MAX_GROUPS] = Integer.MAX_VALUE;
		}

	}



	@Override
	protected void updatePorts(Vector<DriverPort> ports) {

		DriverPort.absentize(ports);
		DriverPort.sortPorts(ports);

		int i;

		for( i = 0; i < nInputPorts; i++ )
		{
			String portName = "Grp "+i+" In";
			inputPorts[i] = getPort(ports, i, Direction.Input, Type.Numeric, portName );
		}

		// Value outputs
		for( i = 0; i < MAX_GROUPS; i++ )
		{
			String portName = "Grp "+i+" Out";
			outputPorts[i] = getPort(ports, i+nInputPorts, Direction.Output, Type.Numeric, portName );

			final HomeNetGroup g = group[i];
			//final byte groupNo = group[i].getNumber();
			final int groupPos = i;

			outputPorts[i].setPortDataOutput(
					new PortDataOutput() {

						@Override
						public void receiveDoubleData(double value) {
							try {
								if((int)value == prevOutput[groupPos])
									return;
								
								prevOutput [groupPos] = (int)value; 
								
								log.severe("Set group "+groupPos+" to "+value);
								//protocolDriver.sendSetGroup(groupNo, ++generation[groupPos], (int)value);
								g.setState((int)value, protocolDriver);
								
								if(g.isEnabled())
								{
									// Reflect back to system
									if((inputPorts[groupPos] != null) )
										inputPorts[groupPos].sendDoubleData(g.getValue());
								}
							} catch (CommunicationsException e) {
								log.log(Level.SEVERE,"can't send data", e);
							}
						}});
		}

		// Trigger outputs
		for( i = 0; i < MAX_GROUPS; i++ )
		{
			String portName = "Grp "+i+" Trigger";
			outputPorts[i+MAX_GROUPS] = getPort(ports, i+MAX_GROUPS+nInputPorts, Direction.Output, Type.Numeric, portName );

			final HomeNetGroup g = group[i];
			//final byte groupNo = group[i].getNumber();
			final int groupPos = i;

			outputPorts[i+MAX_GROUPS].setPortDataOutput(
					new PortDataOutput() {

						@Override
						public void receiveDoubleData(double value) {
							try {
								if((int)value == prevOutput[groupPos+MAX_GROUPS])
									return;
								
								prevOutput[groupPos+MAX_GROUPS] = (int)value; 
								
								// Only 0 -> 1 transactions are taken in account
								if((int)value == 0)
									return;
								
								log.severe("Set group "+groupPos+" to "+value);
								g.setState( (g.getValue() == 0) ? 0xFFFF : 0, protocolDriver);
								
								if(g.isEnabled())
								{
									// Reflect back to system
									if((inputPorts[groupPos] != null) )
										inputPorts[groupPos].sendDoubleData(g.getValue());
								}
							} catch (CommunicationsException e) {
								log.log(Level.SEVERE,"can't send data", e);
							}
						}});
		}
		


	}

	@Override
	protected void setupPanel(JPanel panel) {
		panel.add(new JLabel("IP Address:"), consL);
		panel.add(ipAddressField, consR);

		panel.add(new JLabel("TCP port:"), consL);
		panel.add(tcpPortField, consR);

		{
			JPanel uPanel = new JPanel(new GridLayout(MAX_GROUPS+1,4));

			consL.gridwidth = 2;
			consL.fill = GridBagConstraints.HORIZONTAL;
			panel.add(uPanel,consL);
			consL.gridwidth = 1;
		
		uPanel.add(new JLabel("Unit"));
		uPanel.add(new JLabel("Enabled"));
		uPanel.add(new JLabel("Group"));
		uPanel.add(new JLabel("Active"));
		
		for(int i = 0; i < MAX_GROUPS; i++)
		{
			uPanel.add(new JLabel(String.format("%d:",i)));
			uPanel.add(unitEnableCheckbox[i]);
			uPanel.add(groupNumberField[i]);
			uPanel.add(unitActiveCheckbox[i]);
			unitActiveCheckbox[i].setEnabled(false);
		
			final int pos = i;
			
			unitEnableCheckbox[i].addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					groupNumberField[pos].setEnabled( unitEnableCheckbox[pos].isSelected() );
				}});
		}
		}
	}


	@Override
	protected void doLoadPanelSettings() {
		if(ipAddress != null)
		{
			ipAddressString = ipAddress.getHostAddress();
			//ipAddressString = ipAddress.getHostName();
		}
		if(ipAddressString != null)
			ipAddressField.setText(ipAddressString);

		tcpPortField.setText(Integer.toString(tcpPort));

		for(int i = 0; i < MAX_GROUPS; i++)
		{
			groupNumberField[i].setText( Integer.toString(group[i].getNumber()));
			groupNumberField[i].setEnabled(group[i].isEnabled());
			unitEnableCheckbox[i].setSelected(group[i].isEnabled());
			unitActiveCheckbox[i].setSelected(false);
		}
	}

	@Override
	protected void doSavePanelSettings() {
		String tmpIpAddressString = ipAddressField.getText();
		try { tcpPort = Integer.parseInt(tcpPortField.getText()); }
		catch( NumberFormatException e ) { /* ignore */ }

		activateIp(tmpIpAddressString);

		for(int i = 0; i < MAX_GROUPS; i++)
		{
			group[i].setEnabled( unitEnableCheckbox[i].isSelected() );
			groupNumberField[i].setEnabled(group[i].isEnabled());

			//if(groupEnabled[i])
			{
				try { group[i].setNumber( (byte)Integer.parseInt( groupNumberField[i].getText() ) ); }
				catch( NumberFormatException e ) { /* ignore */ }
			}

			unitActiveCheckbox[i].setSelected(false);		
		}

	}

	private void activateIp(String addressString) {
		try {
			InetAddress ia = InetAddress.getByName(addressString);
			ipAddressString = addressString;
			ipAddress = ia;
		} catch (UnknownHostException e) {
			log.warning("Can't resolve "+addressString);
			showMessage("Can't resolve address "+addressString);
			return;
		}
	}

	@Override
	protected void doStartDriver() throws CommunicationsException {
		pipe = new TcpBiPipe(ipAddress,tcpPort);
		pipe.connect();
		protocolDriver = new HomeNetProtocol(pipe);
		requestState();
	}

	@Override
	protected void doStopDriver() throws CommunicationsException {
		pipe.disconnect();
		pipe = null;
	}

	@Override
	public String getDeviceName() {
		return "DZ:HomeNet TCP";
	}

	@Override
	public String getInstanceName() {
		return ipAddressString;
	}


	@Override
	public boolean isAutoSeachSupported() { return false; }


	
	
	private int pollForStateTimer = 0;
	/**
	 * Request other nodes to send us their state.
	 */
	private void requestState() {
		if(pollForStateTimer > POLL_COUNTER)
			pollForStateTimer = 0;

		if(pollForStateTimer++ == 0)
		{
			for( int i = 0; i < MAX_GROUPS; i++)
			{				
				try { group[i].requestState(protocolDriver); }
				catch(CommunicationsException e) { /* ignore */ }				
			}
		}
		
	}

	@Override
	protected void doDriverTask() throws Throwable {

		requestState();
		
		// TODO replace polling with event-driven implementation
		while( pipe.available() > 0 )
		{
			HomeNetPacket packet = protocolDriver.recvPacket();

			byte grp = packet.getGroup();
			byte cmd = packet.getCmd();

			for( int i = 0; i < MAX_GROUPS; i++ )
			{
				if( group[i].getNumber() != grp )
					continue;

				markUnitActive(i);

				final HomeNetGroup g = group[i];

				if(cmd == HomeNetPacket.PKT_APP_L_GETGROUP)
				{
					g.sendState(protocolDriver);
				}

				if(cmd == HomeNetPacket.PKT_APP_L_SETGROUP)
				{
					if(g.recvState(packet) && (inputPorts[i] != null) )
						inputPorts[i].sendDoubleData(g.getValue());
				}
			}
		}


	}



	// -----------------------------------------------------------
	// Activity tracking 
	// -----------------------------------------------------------


	//private void markUnitPassive(int unit) { unitActiveCheckbox[unit].setSelected(false); }
	private void markUnitActive(int unit) {  unitActiveCheckbox[unit].setSelected(true);  }


	// -----------------------------------------------------------
	// test main 
	// -----------------------------------------------------------

	/*
	public static void main(String args[]) throws IOException, ModBusException, CommunicationsException 
	{
		DigitalZoneHomeNet w = new DigitalZoneHomeNet();

		w.setIpAddressString("192.168.1.103");

		w.group[0].setNumber((byte)0);		
		w.group[0].setEnabled(true);

		w.group[1].setNumber((byte)1);		
		w.group[1].setEnabled(true);

		w.startDriver();

		BinaryTools.sleepMsec(2000);

		for(int i = 0; i < 10; i++ )
		{
			try {
				w.group[0].setState(((i & 1) == 1) ? 0xFFFF : 0, w.protocolDriver);
				w.group[1].setState(((i & 1) == 0) ? 0xFFFF : 0, w.protocolDriver);
				BinaryTools.sleepMsec(500);

			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		BinaryTools.sleepMsec(20000);
		w.stopDriver();
	}
	*/

	// -----------------------------------------------------------
	// getters/setters 
	// -----------------------------------------------------------


	public String getIpAddressString() {	return ipAddressString;	}
	public void setIpAddressString(String addressString) {		activateIp(  addressString );	}

	public int getTcpPort() {		return tcpPort;	}
	public void setTcpPort(int tcpPort) {		this.tcpPort = tcpPort;	}

	public HomeNetGroup[] getGroups() {		return group;	}
	public void setGroups(HomeNetGroup[] groups) {		this.group = groups; pollForStateTimer = 0;	}



	//public boolean[] getGroupEnabled() {		return groupEnabled;	}
	//public void setGroupEnabled(boolean[] unitEnabled) {		this.groupEnabled = unitEnabled;	}





}
