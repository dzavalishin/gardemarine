package ru.dz.shipMaster.dev.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.data.misc.CommunicationsException;
import ru.dz.shipMaster.dev.DriverPort;
import ru.dz.shipMaster.dev.PortDataOutput;
import ru.dz.shipMaster.dev.ThreadedDriver;

public class Wago750 extends ThreadedDriver {

	//private static final int OUTPUT_REGISTERS_START_NUMBER = 512;
	//protected static final int UNIT_ID = 1;
	private JTextField ipAddressField;
	private String ipAddressStr = "192.168.10.200";
	private InetAddress ipAddress;

	private ModBusProtocolDirectTCP protocolDriver = new ModBusTCP();

	public Wago750() {
		super(1000, Thread.NORM_PRIORITY, "Wago updater");

	}


	private int nAnOutputPorts = 64;
	private DriverPort[] anOutputPorts = new DriverPort[ nAnOutputPorts ];  

	private int nAnInputPorts = 64;
	private DriverPort[] anInputPorts = new DriverPort[ nAnInputPorts ];

	private int nDigOutputPorts = 128;
	private DriverPort[] digOutputPorts = new DriverPort[ nDigOutputPorts ];  

	private int nDigInputPorts = 128;
	private DriverPort[] digInputPorts = new DriverPort[ nDigInputPorts ];

	private int nPFCInputPorts = 32;
	private DriverPort[] pfcInputPorts = new DriverPort[ nPFCInputPorts ];


	private int analogueInputCount;
	private int digitalInputCount;
	private int analogueOutputCount;
	private int digitalOutputCount;  

	@Override
	protected void updatePorts(Vector<DriverPort> ports) {

		DriverPort.absentize(ports);
		DriverPort.sortPorts(ports);

		/* None yet
		{
		DriverPort port = getPort(ports,0);
		port.setDirection(Direction.Output);
		port.setHardwareName("TextToShow");
		port.setType(Type.String);
		}*/

		int i;

		boolean prevIsUsed = true;
		for( i = 0; i < nAnInputPorts; i++ )
		{
			String portName = "An. I "+i;
			anInputPorts[i] = getPort(ports, i, Direction.Input, Type.Numeric, portName );
			if( i >= analogueInputCount )
				anInputPorts[i].setAbsent(true);
		}

		for( i = 0; i < nAnOutputPorts; i++ )
		{
			//final int portNo = i;
			String portName = "An. O "+i;
			anOutputPorts[i] = getPort(ports, i+nAnInputPorts, Direction.Output, Type.Numeric, portName );
			if( i >= analogueOutputCount )
				anOutputPorts[i].setAbsent(true);

			final int controllerPortNo = i;

			anOutputPorts[i].setPortDataOutput(
					new PortDataOutput() {
						@Override
						public void receiveDoubleData(double value) {
							try {
								if( !anOutputPorts[controllerPortNo].checkIsChanged(value) )
									return;

								log.fine("Set analogue out: "+value);
								protocolDriver.preset_single_register( /*OUTPUT_REGISTERS_START_NUMBER+*/controllerPortNo, 256*(int)value );

								//protocolDriver.force_single_coil( /*OUTPUT_REGISTERS_START_NUMBER+*/controllerPortNo, value > 50);

							} catch (ModBusException e) {
								log.log(Level.SEVERE,"can't send data", e);
							}
						}});

		}


		for( i = 0; i < nDigInputPorts; i++ )
		{
			String portName = "Dig. I "+i;
			final int shift = +nAnInputPorts+nAnOutputPorts;
			digInputPorts[i] = getPort(ports, i+shift, Direction.Input, Type.Boolean, portName );
			if( i >= digitalInputCount )
				digInputPorts[i].setAbsent(true);

		}

		for( i = 0; i < nDigOutputPorts; i++ )
		{
			String portName = "Dig. O "+i;
			final int shift = +nAnInputPorts+nAnOutputPorts+nDigInputPorts;
			digOutputPorts[i] = getPort(ports, i+shift, Direction.Output, Type.Boolean, portName );
			if( i >= digitalOutputCount )
				digOutputPorts[i].setAbsent(true);

			final int controllerPortNo = i;

			digOutputPorts[i].setPortDataOutput(
					new PortDataOutput() {
						@Override
						public void receiveDoubleData(double value) {
							try {
								if( !anOutputPorts[controllerPortNo].checkIsChanged(value) )
									return;

								log.fine("Set digital out: "+value);
								protocolDriver.force_single_coil( controllerPortNo, value > 0.1 );

							} catch (ModBusException e) {
								log.log(Level.SEVERE,"can't send data", e);
							}
						}});

		}

		prevIsUsed = true;
		for( i = 0; i < nPFCInputPorts; i++ )
		{
			String portName = "PFC I "+i;
			final int shift = +nAnInputPorts+nAnOutputPorts+nDigInputPorts+nDigOutputPorts;
			pfcInputPorts[i] = getPort(ports, i+shift, Direction.Input, Type.Numeric, portName );
			pfcInputPorts[i].setAbsent( (!prevIsUsed) && (!pfcInputPorts[i].isUsed()) );
			prevIsUsed = pfcInputPorts[i].isUsed(); 
		}


	}

	@Override
	protected void doLoadPanelSettings() {
		if(ipAddress != null)
		{
			ipAddressStr = ipAddress.getHostName();
		}
		if(ipAddressStr != null)
			ipAddressField.setText(ipAddressStr);

	}

	@Override
	protected void doSavePanelSettings() {
		String ias = ipAddressField.getText();
		activateIp(ias);

	}

	private void activateIp(String addressString) {
		try {
			InetAddress ia = InetAddress.getByName(addressString);
			ipAddressStr = addressString;
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
			protocolDriver.startTcp(ipAddress);

			// Check group of registers to contain correct constant values.
			// Register 0x2002 can contain 0x1234 or 0x4660 for different models and is not checked  

			protocolDriver.checkRegister(0x2000, 0x0000);
			protocolDriver.checkRegister(0x2001, 0xFFFF); 

			protocolDriver.checkRegister(0x2003, 0xAAAA);
			protocolDriver.checkRegister(0x2004, 0x5555);
			protocolDriver.checkRegister(0x2005, 0x7FFF);
			protocolDriver.checkRegister(0x2006, 0x8000);
			protocolDriver.checkRegister(0x2007, 0x3FFF);
			protocolDriver.checkRegister(0x2008, 0x4000);

			analogueInputCount = getAnalogueInputCount();
			analogueOutputCount = getAnalogueOutputCount();
			digitalInputCount = getDigitalInputCount();
			digitalOutputCount = getDigitalOutputCount();

			readWagoConfiguration();

			// We found number of ports, show it to user
			reloadPorts();

		} catch (IOException e) {
			throw new CommunicationsException(e.getMessage(),e);
		} catch (ModBusException e) {
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
		return "Wago 750 controller";
	}

	@Override
	public String getInstanceName() {
		return ipAddressStr;
	}



	@Override
	protected void setupPanel(JPanel panel) {
		panel.add(new JLabel("IP Address:"), consL);
		panel.add(ipAddressField = new JTextField(20), consR);
	}

	@Override
	public boolean isAutoSeachSupported() { return false; }




	/*
	 * java version "1.6.0_02"
	 * Java(TM) SE Runtime Environment (build 1.6.0_02-b06)
	 * Java HotSpot(TM) Client VM (build 1.6.0_02-b06, mixed mode, sharing)
	 */


	private boolean [] boolIn = new boolean[nDigInputPorts]; 
	private int [] anIn = new int[nAnInputPorts*2];

	@Override
	protected void doDriverTask() throws Throwable {
		int dCount = Math.min( digitalInputCount, nDigInputPorts );
		if(dCount > 0) synchronized(boolIn) {
			protocolDriver.read_input_status( 0, boolIn.length, boolIn );
			//System.out.println("Wago750.doDriverTask() dig = 0 "+boolIn[0]);
			//System.out.print("bool ");
			for( int i = 0; i < dCount; i++ )
			{
				DriverPort port = digInputPorts[i];
				if(port != null)
				{
					port.sendBooleanData(boolIn[i]);
					//System.out.print(" "+i+": "+boolIn[i]);
				}
			}
			//System.out.println();
		}
		
		sleep(100);
		
		int aCount = 2 * Math.min(analogueInputCount, nAnInputPorts );
		if(aCount > 0) synchronized(anIn)
		{
			protocolDriver.read_input_registers(0, aCount, anIn);
			for( int i = 0; i < nAnInputPorts; i++ )
			{
				DriverPort port = anInputPorts[i];
				if(port != null)
					port.sendDoubleData(anIn[i]);			
				/*
			protocolDriver.read_input_status( UNIT_ID, i, boolIn.length, boolIn );
			System.out.print("bools: "+boolIn[0]);
			anInputPorts[i].sendBooleanData(boolIn[0]);
				 */			
			}
		}

		sleep(100);

		if(nPFCInputPorts > 0) synchronized(anIn)
		{
			//int aCount = 2 * Math.min(analogueInputCount, nAnInputPorts );
			protocolDriver.read_input_registers(0x100, nPFCInputPorts, anIn);
			for( int i = 0; i < nPFCInputPorts; i++ )
			{
				DriverPort port = pfcInputPorts[i];
				if(port != null && port.isUsed())
					port.sendDoubleData(anIn[i]);			
				/*
			protocolDriver.read_input_status( UNIT_ID, i, boolIn.length, boolIn );
			System.out.print("bools: "+boolIn[0]);
			anInputPorts[i].sendBooleanData(boolIn[0]);
				 */			
			}
		}

	}


	// -----------------------------------------------------------
	// Wago specific IO 
	// -----------------------------------------------------------

	protected int getAnalogueOutputCount() throws ModBusException { return protocolDriver.readInputRegister(0x1022)/16; }

	protected int getAnalogueInputCount() throws ModBusException { return protocolDriver.readInputRegister(0x1023)/16; }

	protected int getDigitalOutputCount() throws ModBusException { return protocolDriver.readInputRegister(0x1024); }

	protected int getDigitalInputCount() throws ModBusException { return protocolDriver.readInputRegister(0x1025); }



	/**
	 * 
	 * @param out 3-word buffer for mac-address
	 * @throws ModBusException
	 */
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
		 */
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

			desc = "digital IO module: "+
			(isInput ? "in" : (isOutput ? "out" : "unknown")) +
			", " + nBits +  " bits";			
		}
		else
		{
			desc = "module: "+config;			
		}

		log.severe("Wago "+desc);
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

	}

	// -----------------------------------------------------------
	// test main 
	// -----------------------------------------------------------

	public static void main(String args[]) throws IOException, ModBusException, CommunicationsException 
	{
		Wago750 w = new Wago750();

		w.setIpAddressStr("192.168.1.106");
		w.start();
		//w.readWagoConfiguration();

		{
			int port = 8;
			boolean [] boolIn = new boolean[1]; 
			w.protocolDriver.read_input_status( port, 1, boolIn );

			System.out.println("Wago750.main() bool "+port+" = "+boolIn[0]);
		}

		w.protocolDriver.force_single_coil( 3, true );
		w.protocolDriver.force_single_coil( 3+4, true );
		w.protocolDriver.force_single_coil( 3+8, true );
		w.protocolDriver.force_single_coil( 3+16, true );


		System.out.println("Wago750.main() d in cnt "+w.digitalInputCount);
		System.out.println("Wago750.main() a in cnt "+w.analogueInputCount);
		{
			int aport = 0;

			int [] anIn = new int[2];
			w.protocolDriver.read_input_registers(aport, 1, anIn);

			int dw = anIn[0] & 0x0000FFFF;
			//dw |= (anIn[1]<<16) & 0xFFFF0000;

			System.out.println("Wago750.main() aport "+aport+" = "+dw);
		}

		for( int r = 10000; r > 0; r-- ) {
			w.protocolDriver.force_single_coil( 3, true );

			
			boolean [] boolIn = new boolean[4]; 
			w.protocolDriver.read_input_status( 0, boolIn.length, boolIn );

			System.out.print("Wago750.main() dports :" );
			for(int i = 0; i < boolIn.length; i++)
			{
				System.out.print(" "+boolIn[i]);
			}
			System.out.println();
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// ign
			}
		}

		w.stop();
	}


	// -----------------------------------------------------------
	// getters/setters 
	// -----------------------------------------------------------


	public String getIpAddressStr() {		return ipAddressStr;	}

	public void setIpAddressStr(String ipAddressStr) {		activateIp( ipAddressStr );	}




}
