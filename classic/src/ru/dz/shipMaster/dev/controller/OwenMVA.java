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
import ru.dz.shipMaster.dev.ThreadedDriver;

public class OwenMVA extends ThreadedDriver {

	private static final int MAX_UNITS = 4;
	//private static final int OUTPUT_REGISTERS_START_NUMBER = 512;
	//protected static final int UNIT_ID = 1;
	private JTextField ipAddressField = new JTextField(20);
	private String ipAddressString = "192.168.1.103";

	private InetAddress ipAddress;
	private JTextField tcpPortField = new JTextField(6);
	private int tcpPort = 13033;

	private JCheckBox [] unitEnableCheckbox = { new JCheckBox(), new JCheckBox(), new JCheckBox(), new JCheckBox() };
	private JTextField [] unitBaseField = { new JTextField(), new JTextField(), new JTextField(), new JTextField() };
	private JCheckBox [] unitActiveCheckbox = { new JCheckBox(), new JCheckBox(), new JCheckBox(), new JCheckBox() };
	//private JCheckBox [] unitDebugCheckbox = { new JCheckBox(), new JCheckBox(), new JCheckBox(), new JCheckBox() };
	private int [] unitBase = { 16, -1, -1, -1 };
	private boolean [] unitEnabled = { true, false, false, false };

	ModBusSerial protocolDriver = new ModBusSerial();

	public OwenMVA() {
		super(1000, Thread.NORM_PRIORITY, "MVA updater");
		protocolDriver.setAsciiMode(true);
		protocolDriver.setSlave(16);
	}


	private int nOutputPorts = 2;
	DriverPort[] outputPorts = new DriverPort[ nOutputPorts ];  

	private int nInputPorts = 8*MAX_UNITS;
	DriverPort[] inputPorts = new DriverPort[ nInputPorts ];
	//private int analogueInputCount;
	//private int analogueOutputCount;
	//private int digitalInputCount;
	//private int digitalOutputCount;  

	@Override
	protected void updatePorts(Vector<DriverPort> ports) {

		DriverPort.absentize(ports);
		DriverPort.sortPorts(ports);

		int i;

		for( i = 0; i < nInputPorts; i++ )
		{
			String portName = "An. I "+i/8+"."+i%8;
			inputPorts[i] = getPort(ports, i, Direction.Input, Type.Numeric, portName );
		}

		/*
		if(false){
			for( i = 0; i < nOutputPorts; i++ )
			{
				String portName = "An. O "+i;
				outputPorts[i] = getPort(ports, i+nInputPorts, Direction.Output, Type.Numeric, portName );

				final int controllerPortNo = i;

				outputPorts[i].setPortDataOutput(
						new PortDataOutput() {
							@Override
							public void receiveDoubleData(double value) {
								try {
									log.severe("Set analogue out: "+value);
									protocolDriver.preset_single_register( 
									//OUTPUT_REGISTERS_START_NUMBER+
									controllerPortNo, 256*(int)value );

									protocolDriver.force_single_coil( 
									//OUTPUT_REGISTERS_START_NUMBER+
									 controllerPortNo, value > 50);

								} catch (ModBusException e) {
									log.log(Level.SEVERE,"can't send data", e);
								}
							}});
			}
		}
		*/

	}

	@Override
	protected void setupPanel(JPanel panel) {
		panel.add(new JLabel("IP Address:"), consL);
		panel.add(ipAddressField, consR);

		panel.add(new JLabel("TCP port:"), consL);
		panel.add(tcpPortField, consR);

		/*
		for(int i = 0; i < MAX_UNITS; i++)
		{
			panel.add(new JLabel(String.format("Unit %d enabled:",i)), consL);
			panel.add(unitEnableCheckbox[i], consR);

			panel.add(new JLabel(String.format("Unit %d base address:",i)), consL);
			panel.add(unitBaseField[i], consR);

			panel.add(new JLabel(String.format("Unit %d active:",i)), consL);
			panel.add(unitActiveCheckbox[i], consR);
			unitActiveCheckbox[i].setEnabled(false);
		}
		 */

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
		} catch (IOException e) {
			throw new CommunicationsException(e.getMessage(),e);
		}

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
		return "Owen MVA analogue inputs module";
	}

	@Override
	public String getInstanceName() {
		return ipAddressString;
	}


	@Override
	public boolean isAutoSeachSupported() { return false; }

	
	private double [] vals = new double[8];
	@Override
	protected void doDriverTask() throws Throwable {

		for( int unit = 0; unit < MAX_UNITS; unit++ )
		{
			markUnitPassive(unit);
			if(!unitEnabled[unit])
				continue;

			protocolDriver.setSlave(unitBase[unit]);
			for( int i = 0; i < 8; i++ )
			{
				// Give MVA and TCP gate some time to prepare to the next cmd
				if( i > 0 && unit > 0 )
					BinaryTools.sleepMsec(30);

				float val;

				try {
					int[] dest = new int[2];
					protocolDriver.read_input_registers( i*6 + 4, 2, dest);
					val = Float.intBitsToFloat( ((dest[0] << 16) & 0xFFFF0000 | (dest[1] & 0xFFFF) ) );
				} catch(ModBusIOException e)
				{
					try {
						int[] dest = new int[2];
						protocolDriver.read_input_registers( i*6 + 4, 2, dest);
						val = Float.intBitsToFloat( ((dest[0] << 16) & 0xFFFF0000 | (dest[1] & 0xFFFF) ) );
					} catch(ModBusIOException e1)
					{
						log.log(Level.SEVERE, "Owen IO error", e1);
						failCounter.noteFailure();
						continue;
					}
				}
				final int pno = i+unit*8;
				vals[i] = val;
				if( inputPorts[pno] != null ) inputPorts[pno].sendDoubleData(val);
				markUnitActive(unit);
				// Don't note success at all, or else even one active unit will prevent restart
				//failCounter.noteSuccess();
			}
			//log.log(Level.SEVERE, String.format("MVA%d is %f %f %f %f %f %f %f %f\n", unit, vals[0], vals[1], vals[2], vals[3], vals[4], vals[5], vals[6], vals[7] ));

		}
	}





	private void markUnitPassive(int unit) { unitActiveCheckbox[unit].setSelected(false); }
	private void markUnitActive(int unit) {  unitActiveCheckbox[unit].setSelected(true);  }

	/**
	 * 
	 * @param out 3-word buffer for mac-address
	 * @throws ModBusException
	 * /
	protected void getMacAddress(int [] out) throws ModBusException 
	{ 
		protocolDriver.read_input_registers( 0x1031, 3, out );
	}


	protected void readWagoConfiguration() throws ModBusException
	{
			int maxRegs = 65;
			int [] dest = new int[maxRegs];

			protocolDriver.read_input_registers( 0x2030, maxRegs, dest );	
			loadConfigWords(maxRegs, dest);

			/*
			maxRegs = 64;
			protocolDriver.read_input_registers( 1, 0x2031, maxRegs, dest );	
			loadConfigWords(maxRegs, dest);

			protocolDriver.read_input_registers( 1, 0x2032, maxRegs, dest );	
			loadConfigWords(maxRegs, dest);

			maxRegs = 63;
			protocolDriver.read_input_registers( 1, 0x2033, maxRegs, dest );	
			loadConfigWords(maxRegs, dest);
	 * /
	}

	private void loadConfigWords(int maxWords, int[] configWords) {
		for( int wordNo = 0; wordNo < maxWords; wordNo++ )
		{
			int config = configWords[wordNo];
			if(config != 0)
				loadConfigWord(config);
		}
	}


	private List<WagoModule> wagoModules = new LinkedList<WagoModule>();

	private void loadConfigWord(int config) {
		String desc;
		if((config & 0x8000) != 0)
		{
			boolean isInput = (config & 0x1) != 0;
			boolean isOutput = (config & 0x2) != 0;
			int nBits = 0x7F & (config >> 8);

			desc = "Digital IO module: "+
			(isInput ? "in" : (isOutput ? "out" : "unknown")) +
			", " + nBits +  " bits";			
		}
		else
		{
			desc = "Module: "+config;			
		}

		log.severe(desc);
		wagoModules.add(new WagoModule(config,desc));
	}


	class WagoModule
	{
		int		moduleType; // as returned by modbus 0x2030 register
		String	description;

		public WagoModule(int moduleType, String description) {
			this.moduleType = moduleType;
			this.description = description;
		}

	}*/

	// -----------------------------------------------------------
	// test main 
	// -----------------------------------------------------------

	public static void main(String args[]) throws IOException, ModBusException, CommunicationsException 
	{
		OwenMVA w = new OwenMVA();

		w.setIpAddressString("192.168.1.240");
		//w.setIpAddressString("192.168.1.103");
		w.start();
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
