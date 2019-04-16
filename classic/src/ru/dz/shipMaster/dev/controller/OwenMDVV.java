package ru.dz.shipMaster.dev.controller;

import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.data.misc.CommunicationsException;
import ru.dz.shipMaster.dev.DriverPort;
import ru.dz.shipMaster.dev.PortDataOutput;
import ru.dz.shipMaster.dev.ThreadedDriver;

public class OwenMDVV extends ThreadedDriver {

	private static final int OUTPUTS_PER_UNIT = 8;
	private static final int INPUTS_PER_UNIT = 12;
	private static final int MAX_UNITS = 4;
	private JTextField ipAddressField = new JTextField(20);
	private String ipAddressString = "192.168.1.103";

	private InetAddress ipAddress;
	private JTextField tcpPortField = new JTextField(6);
	private int tcpPort = 13033;

	private JCheckBox [] unitEnableCheckbox = { new JCheckBox(), new JCheckBox(), new JCheckBox(), new JCheckBox() };
	private JTextField [] unitBaseField = { new JTextField(), new JTextField(), new JTextField(), new JTextField() };
	private JCheckBox [] unitActiveCheckbox = { new JCheckBox(), new JCheckBox(), new JCheckBox(), new JCheckBox() };
	private int [] unitBase = { 16, -1, -1, -1 };
	private boolean [] unitEnabled = { true, false, false, false };

	ModBusSerial protocolDriver = new ModBusSerial();

	public OwenMDVV() {
		super(1000, Thread.NORM_PRIORITY, "Owen MDVV updater");
		protocolDriver.setAsciiMode(true);
		protocolDriver.setSlave(16);
	}


	private int nOutputPorts = OUTPUTS_PER_UNIT*MAX_UNITS;
	DriverPort[] outputPorts = new DriverPort[ nOutputPorts ];  

	private int nInputPorts = INPUTS_PER_UNIT*2*MAX_UNITS;
	DriverPort[] inputPorts = new DriverPort[ nInputPorts ];
	private int[] countReadBuffer = new int[INPUTS_PER_UNIT];


	static int calcInPortNo(int unit, int input, boolean isCounter )
	{
		return unit*INPUTS_PER_UNIT*2 + input*2 + (isCounter ? 1 : 0);
	}


	@Override
	protected void updatePorts(Vector<DriverPort> ports) {

		DriverPort.absentize(ports);
		DriverPort.sortPorts(ports);

		for( int unit = 0; unit < MAX_UNITS; unit++ )
		{
			for( int p = 0; p < INPUTS_PER_UNIT; p++ )
			{
				String portName = "DIn "+unit+"."+p;
				inputPorts[calcInPortNo(unit, p, false)] = getPort(ports, nOutputPorts + calcInPortNo(unit, p, false), Direction.Input, Type.Boolean, portName );
				portName = "Cnt "+unit+"."+p;
				inputPorts[calcInPortNo(unit, p, true)] = getPort(ports, nOutputPorts + calcInPortNo(unit, p, true), Direction.Input, Type.Numeric, portName );
			}
		}

		for( int unit = 0; unit < MAX_UNITS; unit++ )
		{
			for( int p = 0; p < OUTPUTS_PER_UNIT; p++ )
			{
				String portName = "DOut "+unit+"."+p;
				outputPorts[unit] = getPort(ports, unit*OUTPUTS_PER_UNIT+p, Direction.Output, Type.Boolean, portName );

				final int controllerUnitNo = unit;
				final int controllerPortNo = p;

				outputPorts[unit].setPortDataOutput(
						new PortDataOutput() {
							@Override
							public void receiveDoubleData(double value) {
								try {
									log.severe("Set out: "+value);
									protocolDriver.setSlave(unitBase[controllerUnitNo]);
									//protocolDriver.preset_single_register( controllerPortNo, (int)(1000*value) );

									int[] data = new int[1];
									data[0] = (int)(1000*value);
									protocolDriver.preset_multiple_registers(controllerPortNo, 1, data);

								} catch (ModBusException e) {
									log.log(Level.SEVERE,"can't send data", e);
								}
							}});
			}
		}



	}

	@Override
	protected void setupPanel(JPanel panel) {
		panel.add(new JLabel("IP Address:"), consL);
		panel.add(ipAddressField, consR);

		panel.add(new JLabel("TCP port:"), consL);
		panel.add(tcpPortField, consR);

		{
			JPanel uPanel = new JPanel(new GridLayout(MAX_UNITS+1,4));

			consL.gridwidth = 2;
			consL.fill = GridBagConstraints.HORIZONTAL;
			panel.add(uPanel,consL);
			consL.gridwidth = 1;

			uPanel.add(new JLabel(String.format("Unit")) );
			uPanel.add(new JLabel(String.format("Enabled")) );
			uPanel.add(new JLabel(String.format("Address")) );
			uPanel.add(new JLabel(String.format("Active")) );

			for(int i = 0; i < MAX_UNITS; i++)
			{
				uPanel.add(new JLabel(String.format("%d:",i)) );
				uPanel.add(unitEnableCheckbox[i], consR);
				uPanel.add(unitBaseField[i], consR);
				uPanel.add(unitActiveCheckbox[i], consR);
				unitActiveCheckbox[i].setEnabled(false);
			}
		}
	}


	@Override
	protected void doLoadPanelSettings() {
		if(ipAddress != null)
		{
			ipAddressString = ipAddress.getHostName();
		}
		if(ipAddressString != null)
			ipAddressField.setText(ipAddressString);

		tcpPortField.setText(Integer.toString(tcpPort));

		for(int i = 0; i < MAX_UNITS; i++)
		{
			unitBaseField[i].setText(unitBase[i] < 0 ? "" : Integer.toString(unitBase[i]));
			unitBaseField[i].setEnabled(unitEnabled[i]);
			unitEnableCheckbox[i].setSelected(unitEnabled[i]);
			unitActiveCheckbox[i].setSelected(false);
		}
	}

	@Override
	protected void doSavePanelSettings() {
		String tmpIpAddressString = ipAddressField.getText();
		try { tcpPort = Integer.parseInt(tcpPortField.getText()); }
		catch( NumberFormatException e ) { /* ignore */ }

		activateIp(tmpIpAddressString);

		for(int i = 0; i < MAX_UNITS; i++)
		{
			unitEnabled[i] = unitEnableCheckbox[i].isSelected();
			unitBaseField[i].setEnabled(unitEnabled[i]);

			if(unitEnabled[i])
			{
				try { unitBase[i] = Integer.parseInt( unitBaseField[i].getText() ); }
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
		try {
			protocolDriver.startTcp(ipAddress,tcpPort);

			//protocolDriver.checkRegister(0x2000, 0x0000);
			//protocolDriver.checkRegister(0x2001, 0xFFFF); 
		} catch (IOException e) {
			throw new CommunicationsException(e.getMessage(),e);
		} /*catch (ModBusException e) {
			throw new CommunicationsException(e.getMessage(),e);
		} */

	}

	@Override
	protected void doStopDriver() throws CommunicationsException {
		try {
			protocolDriver.stopTcp();
		} catch (IOException e) {
			throw new CommunicationsException(e.getMessage(),e);
		}
	}

	@Override
	public String getDeviceName() {
		return "Owen MDVV digital IO module";
	}

	@Override
	public String getInstanceName() {
		return ipAddressString;
	}


	@Override
	public boolean isAutoSeachSupported() { return false; }

	@Override
	protected void doDriverTask() throws Throwable {

		for( int unit = 0; unit < MAX_UNITS; unit++ )
		{
			markUnitPassive(unit);
			if(!unitEnabled[unit])
				continue;

			protocolDriver.setSlave(unitBase[unit]);

			// Give MDVV and TCP gate some time to prepare to the next cmd
			if( unit > 0 )
				BinaryTools.sleepMsec(20);

			try {
				int inputs = protocolDriver.readInputRegister(0x33) & 0x7F;

				log.log(Level.FINEST, String.format("MDVV %d inputs are %02X\n", unit, inputs));
				markUnitActive(unit);

				for( int port = 0; port < INPUTS_PER_UNIT; port ++ )
				{
					int val = inputs & 0x01;
					inputs >>= 1;
				
					DriverPort driverPort = inputPorts[calcInPortNo(unit, port, false)];
					if( driverPort != null ) driverPort.sendDoubleData( (val != 0) ? 1.0 : 0.0 );
				}

				protocolDriver.read_input_registers(0x40, INPUTS_PER_UNIT, countReadBuffer );
				// Don't note success at all, or else even one active unit will prevent restart
				//failCounter.noteSuccess();

				for( int port = 0; port < INPUTS_PER_UNIT; port ++ )
				{
					DriverPort driverPort = inputPorts[calcInPortNo(unit, port, true)];

					//if( driverPort == null )						continue;

					// Don't ask conter value for inactive ports
					//if( driverPort.getTarget() == null )						continue;

					//log.log(Level.SEVERE, String.format("MDVV %d.%d counter is %d\n", unit, port, countReadBuffer[port]));

					if( driverPort == null )						continue;

					// NB - unsigned 16 bit!
					driverPort.sendDoubleData(countReadBuffer[port] & 0xFFFF);
					log.log(Level.FINER, String.format("MDVV %d.%d counter is %d\n", unit, port, countReadBuffer[port]));
				}


			} catch(ModBusIOException e)
			{
				log.log(Level.SEVERE, "Owen IO error", e);
				failCounter.noteFailure();
				continue;
			}

		}
	}




	private void markUnitPassive(int unit) { unitActiveCheckbox[unit].setSelected(false); }
	private void markUnitActive(int unit) {  unitActiveCheckbox[unit].setSelected(true);  }


	// -----------------------------------------------------------
	// test main 
	// -----------------------------------------------------------
/*
	public static void main(String args[]) throws IOException, ModBusException, CommunicationsException 
	{
		MDVV w = new MDVV();

		w.setIpAddressString("192.168.0.240");
		//w.setIpAddressString("192.168.1.103");
		w.start();

		w.protocolDriver.setSlave(32);
		//w.protocolDriver.preset_single_register( 0x32, 0 );

		int[] data = new int[1];
		data[0] = 0x000;
		w.protocolDriver.preset_multiple_registers(0x32, 1, data);

		//w.readWagoConfiguration();
		for(int i = 0; i < 10; i++ )
		{
			try {
				//w.doDriverTask();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		BinaryTools.sleepMsec(20000);
		w.stop();
	}

*/
	// -----------------------------------------------------------
	// getters/setters 
	// -----------------------------------------------------------


	public int getTcpPort() {		return tcpPort;	}
	public void setTcpPort(int tcpPort) {		this.tcpPort = tcpPort;	}

	public int[] getUnitBase() {		return unitBase;	}
	public void setUnitBase(int[] unitBase) {		this.unitBase = unitBase;	}

	public boolean[] getUnitEnabled() {		return unitEnabled;	}
	public void setUnitEnabled(boolean[] unitEnabled) {		this.unitEnabled = unitEnabled;	}

	public String getIpAddressString() {	return ipAddressString;	}
	public void setIpAddressString(String ipAddressString) {		activateIp(  ipAddressString );	}






}
