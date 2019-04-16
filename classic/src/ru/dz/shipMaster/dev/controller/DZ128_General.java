package ru.dz.shipMaster.dev.controller;

import java.util.Vector;
import java.util.logging.Level;

import ru.dz.shipMaster.DashBoard;
import ru.dz.shipMaster.config.ConfigurationFactory;
import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.data.misc.CommunicationsException;
import ru.dz.shipMaster.dev.DriverPort;
import ru.dz.shipMaster.dev.PortDataOutput;
import ru.dz.shipMaster.dev.ThreadedDriver;
import ru.dz.shipMaster.ui.logger.GeneralLogWindow.Item;
import ru.dz.shipMaster.ui.logger.MessageWindow;


public abstract class DZ128_General extends ThreadedDriver {

	protected DZ128_General(long updateIntervalMsec, int priority,
			String threadName) {
		super(updateIntervalMsec, priority, threadName);
	}


	
	
	
	
	
	
	// -----------------------------------------------------------
	// Ports connection 
	// -----------------------------------------------------------

	// reserve 64 1-bit digital outputs
	protected static final int DIGITAL_START_PORT_INDEX = 0; 
	protected static final int DIGITAL_END_PORT_INDEX = DIGITAL_START_PORT_INDEX+64; 
 
	// reserve 16 PWM outputs
	protected static final int PWM_START_PORT_INDEX = DIGITAL_END_PORT_INDEX; 
	protected static final int PWM_END_PORT_INDEX = PWM_START_PORT_INDEX+16; 

	
	// reserve 32 analog inputs
	protected static final int ANALOG_IN_COUNT = 16;
	protected static final int ANALOG_IN_START_PORT_INDEX = PWM_END_PORT_INDEX; 
	protected static final int ANALOG_IN_END_PORT_INDEX = ANALOG_IN_START_PORT_INDEX+ANALOG_IN_COUNT;
	DriverPort [] analogInPorts = new DriverPort[ANALOG_IN_COUNT];

	// reserve 64 digital inputs
	protected static final int DIGITAL_IN_COUNT = 64;
	protected static final int DIGITAL_IN_START_PORT_INDEX = ANALOG_IN_END_PORT_INDEX;
	protected static final int DIGITAL_IN_END_PORT_INDEX = DIGITAL_IN_START_PORT_INDEX+DIGITAL_IN_COUNT;
	//DriverPort [] digitalInPorts = new DriverPort[DIGITAL_IN_COUNT];
	DriverPort [] digitalInPortsA = new DriverPort[8];
	DriverPort [] digitalInPortsB = new DriverPort[8];
	DriverPort [] digitalInPortsC = new DriverPort[8];
	DriverPort [] digitalInPortsD = new DriverPort[8];
	DriverPort [] digitalInPortsE = new DriverPort[8];
	DriverPort [] digitalInPortsF = new DriverPort[8];
	DriverPort [] digitalInPortsG = new DriverPort[5];
	
	// reserve 8 PWM inputs
	protected static final int PWM_IN_COUNT = 8;
	protected static final int PWM_IN_START_PORT_INDEX = DIGITAL_IN_END_PORT_INDEX;
	protected static final int PWM_IN_END_PORT_INDEX = PWM_IN_START_PORT_INDEX+PWM_IN_COUNT; 
	DriverPort [] pwmInPorts = new DriverPort[PWM_IN_COUNT];
	
	// reserve 8 frequency inputs
	protected static final int FREQUENCY_IN_COUNT = 8;
	protected static final int FREQUENCY_IN_START_PORT_INDEX = PWM_IN_END_PORT_INDEX;
	protected static final int FREQUENCY_IN_END_PORT_INDEX = FREQUENCY_IN_START_PORT_INDEX+FREQUENCY_IN_COUNT; 
	DriverPort [] frequencyInPorts = new DriverPort[FREQUENCY_IN_COUNT];
	
	// reserve 16 temperature inputs
	protected static final int TEMPERATURE_IN_COUNT = 16;
	protected static final int TEMPERATURE_IN_START_PORT_INDEX = FREQUENCY_IN_END_PORT_INDEX;
	protected static final int TEMPERATURE_IN_END_PORT_INDEX = TEMPERATURE_IN_START_PORT_INDEX+TEMPERATURE_IN_COUNT; 
	DriverPort [] temperatureInPorts = new DriverPort[TEMPERATURE_IN_COUNT];
	
	protected static final int GRAND_FINAL_PORT_NO = TEMPERATURE_IN_END_PORT_INDEX;

	protected static final int SAME_VALUE_SKIP_COUNT = 20;
	
	@Override
	protected void updatePorts(Vector<DriverPort> ports) {
		
		for(int dummy = 0; dummy < GRAND_FINAL_PORT_NO; dummy++ )
		{
			//DriverPort port = 
			getPort(ports, dummy, Direction.Input, Type.Boolean, "--" );			
		}
		
		DriverPort.absentize(ports);
		DriverPort.sortPorts(ports);
		
		int portNo = DIGITAL_START_PORT_INDEX;
		portNo = createNDigitalPorts( ports, portNo, "PortA", 0, 0, 8 );
		portNo = createNDigitalPorts( ports, portNo, "PortB", 1, 0, 8 ); // Atmega 8 has it
		//portNo += 8; // Skip B
		portNo = createNDigitalPorts( ports, portNo, "PortC", 2, 0, 8 );
		portNo = createNDigitalPorts( ports, portNo, "PortD", 3, 0, 8 ); // Atmega 8 has it
		//portNo += 8; // Skip D
		portNo = createNDigitalPorts( ports, portNo, "PortE", 4, 4, 4 );
		
		if(portNo > DIGITAL_END_PORT_INDEX)
			log.severe("Ports overlap in "+getControllerName()+" driver");
		
		portNo = PWM_START_PORT_INDEX;
		for( int nPwm = 0; nPwm < 4; nPwm++ )
			createPWMPort( ports, portNo++, "PWM", nPwm	);

		if(portNo > PWM_END_PORT_INDEX)
			log.severe("Ports overlap in "+getControllerName()+" driver");
		
		allocateInputAnalogPorts( ports, ANALOG_IN_START_PORT_INDEX, ANALOG_IN_COUNT, analogInPorts, "ADC" );
		allocateInputAnalogPorts( ports, PWM_IN_START_PORT_INDEX, PWM_IN_COUNT, pwmInPorts, "PWM" );
		allocateInputAnalogPorts( ports, FREQUENCY_IN_START_PORT_INDEX, FREQUENCY_IN_COUNT, frequencyInPorts, "Freq" );
		allocateInputAnalogPorts( ports, TEMPERATURE_IN_START_PORT_INDEX, TEMPERATURE_IN_COUNT, temperatureInPorts, "Temperature" );

		allocateInputAnalogDigitalPorts( ports );
	}

	private void allocateInputAnalogDigitalPorts(Vector<DriverPort> ports) {

		int start = DIGITAL_IN_START_PORT_INDEX;
		
		start = allocateInputDigitalPorts( ports, start, digitalInPortsA, "PortA", 8 );
		start = allocateInputDigitalPorts( ports, start, digitalInPortsB, "PortB", 8 );
		start = allocateInputDigitalPorts( ports, start, digitalInPortsC, "PortC", 8 );
		start = allocateInputDigitalPorts( ports, start, digitalInPortsD, "PortD", 8 );
		start = allocateInputDigitalPorts( ports, start, digitalInPortsE, "PortE", 8 );
		start = allocateInputDigitalPorts( ports, start, digitalInPortsF, "PortF", 8 );
		start = allocateInputDigitalPorts( ports, start, digitalInPortsG, "PortG", 5 );
		
		if(start > DIGITAL_IN_END_PORT_INDEX)
			log.severe("Ports overlap in "+getControllerName()+" driver");
	
	}

	private int allocateInputDigitalPorts(Vector<DriverPort> ports, int startPortIndex,
			DriverPort[] copy, String portBaseName, int count) {
		for( int i = 0; i < count; i++ )
		{
			String portName = portBaseName+"."+i;
			copy[i] = getPort(ports, startPortIndex+i, Direction.Input, Type.Boolean, portName );
		}
		
		return startPortIndex+count;
	}

	private void allocateInputAnalogPorts(Vector<DriverPort> ports,
			int startPortIndex, int count,
			DriverPort[] copy, String portBaseName) {
		
		
		for( int i = 0; i < count; i++ )
		{
			String portName = portBaseName+"."+i;
			copy[i] = getPort(ports, startPortIndex+i, Direction.Input, Type.Numeric, portName );
		}
		
	}

	protected int createNDigitalPorts(
			Vector<DriverPort> ports, int portNo, String portBaseName,
			final int controllerPortNo, int startBit, int nBits )
	{
		for( int i = startBit; i < nBits; i++ )
			createDigitalPort( ports, portNo++, portBaseName, controllerPortNo, i	);
		
		return portNo;
	}
	
	protected void createDigitalPort(
			Vector<DriverPort> ports, int portNo, String portBaseName,
			final int in_controllerPortNo, final int in_controllerBitNo
			)
	{
		String portName = portBaseName+"."+in_controllerBitNo;
		getPort(ports, portNo, Direction.Output, Type.Boolean, portName).setPortDataOutput(
				new PortDataOutput() {
					final int controllerPortNo = in_controllerPortNo;
					final int controllerBitNo = in_controllerBitNo;
			@Override
			public void receiveDoubleData(double value) {
				
				// TODO read back and compare
				
				boolean out = value > 0.001;
				
				//log.log(Level.SEVERE,"Set dig out to "+value+" ("+out+")");

				
				int mask = 1 << controllerBitNo;
				if(out)
					portValues[controllerPortNo] |= mask;
				else
					portValues[controllerPortNo] &= ~mask; 				

				if(portValues[controllerPortNo] == portOldValues[controllerPortNo])
				{
					portSkipSend[controllerPortNo]--;
					if( portSkipSend[controllerPortNo] <= 0 )
					{
						portSkipSend[controllerPortNo] = SAME_VALUE_SKIP_COUNT;
						// Fall through
					}
					else
						return; // Skip for now
				}	
					
				portOldValues[controllerPortNo] = portValues[controllerPortNo];
				
				// put it back if verification of port mask is implemented
				//if( 0 == (portOutputMasks[port] & mask) )
				if(true){
					portOutputMasks[controllerPortNo] |= mask;
					try {
						sendPacket( new DigitalZone128Packet( DigitalZone128PacketType.FROMHOST_DIGITAL_OUT_ENABLE, controllerPortNo, portOutputMasks[controllerPortNo] ) );
					} catch (CommunicationsException e) {
						log.log(Level.SEVERE,"can't send data", e);
					}
				}
				
				try {
					sendPacket( new DigitalZone128Packet( DigitalZone128PacketType.FROMHOST_DIGITAL_OUT, controllerPortNo, portValues[controllerPortNo] ) );
				} catch (CommunicationsException e) {
					log.log(Level.SEVERE,"can't send data", e);
				}
			}});
	}

	protected void createPWMPort(
			Vector<DriverPort> ports, int portNo, String portBaseName,
			final int controllerPortNo
			)
	{
		String portName = portBaseName+""+controllerPortNo;
		getPort(ports, portNo, Direction.Output, Type.Numeric, portName).setPortDataOutput(
				new PortDataOutput() {
			@Override
			public void receiveDoubleData(double value) {
				// TODO read back and compare
				
				try {
					sendPacket( new DigitalZone128Packet( DigitalZone128PacketType.FROMHOST_ANALOG_OUT, controllerPortNo, (int)value ) );
				} catch (CommunicationsException e) {
					log.log(Level.SEVERE,"can't send data", e);
				}
			}});
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// -------------------------------------------------------------------------------
	// Output mirroring 
	// -------------------------------------------------------------------------------
	
	private static final int MAX_OUT_DIGITAL_PORTS = 10;

	protected int[] portValues = new int[MAX_OUT_DIGITAL_PORTS];
	protected int[] portOldValues = new int[MAX_OUT_DIGITAL_PORTS];
	protected int[] portSkipSend = new int[MAX_OUT_DIGITAL_PORTS];
	protected int[] portOutputMasks = new int[MAX_OUT_DIGITAL_PORTS]; // 1 means output
	/*private boolean[] portVerifyRequests = new boolean[MAX_OUT_DIGITAL_PORTS];
	{
		for(int i = 0; i < portValues.length; i++)
		{
			portValues[i] = 0;
			portOutputMasks[i] = 0;
		}
	}*/
	

	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// -------------------------------------------------------------------------------
	// Protocol
	// -------------------------------------------------------------------------------

	/*private void globalUpdate() {
		while(true)
		{
			DigitalZone128Packet packet = cpt.getPacket();

			if(packet == null)
				return;

			receivePacket(packet);
		}
	}*/

	/** 
	 * com thread will push packets here 
	 * @param packet received packet to process 
	 */
	public void receivePacket(DigitalZone128Packet packet) {
		
		if(dashMessage != null)
		{
			//dashMessage.renew();
			dashMessage.setText(getPortName()+" - контроллер активен");
		}
		
		log.fine(
				"got packet type "+packet.getType()+
				" port "+packet.getPort()+
				" value = "+packet.getValue());
		
		lastInputPacketTimeMillis = System.currentTimeMillis();
		
		switch(packet.getType())
		{
		case DigitalZone128PacketType.TOHOST_PONG:
			// just ignore as lastInputPacketTimeMillis is already set.
			break;

		case DigitalZone128PacketType.TOHOST_ACK:
			// just ignore 
			// TO DO - find some use for it? or just kill?
			break;
		
		case DigitalZone128PacketType.TOHOST_NAK:
			log.severe(
					"Получен отказ от контроллера "+getControllerName()+" ("+getPortName()+
					") порт "+packet.getPort()+
					" значение = "+packet.getValue());
			break;
		
		case DigitalZone128PacketType.TOHOST_SERIAL:
		{
			byte [] serial = packet.getPayload();
			//GenericComPortIO.dump(serial);
			StringBuilder sb = new StringBuilder();
			for(int i = serial.length-2; i > 0 ; i-- )
			{
				sb.append(String.format("%02X", 0xFF&(int)serial[i]) );
				if( i > 1 ) sb.append(":");
				setDeviceSerialField(sb.toString());
			}

		}
			break;

		case DigitalZone128PacketType.TOHOST_DEVTYPE:
			setDeviceTypeField(new String(packet.getPayload()));
			break;
			
		case  DigitalZone128PacketType.TOHOST_ANALOG_VALUE: //0x11:
			receiveAnalogData( packet.getPort(), packet.getValue() );
			break;

		case DigitalZone128PacketType.TOHOST_DIGITAL_VALUE: //0x10:
			receiveDigitalData( packet.getPort(), packet.getValue() );
			break;

		case DigitalZone128PacketType.TOHOST_FREQ_VALUE:
			receiveFrequencyData(packet.getPort(), packet.getValue());
			break;
			
		case DigitalZone128PacketType.TOHOST_TEMPERATURE_VALUE:
			receiveTemperatureData(packet.getPort(), packet.getValue());
			break;
			
		default:
			log.severe(
					"got packet of UNKNOWN type "+packet.getType()+
					" port "+packet.getPort()+
					" value = "+packet.getValue());
		break;
		}
		
		
	}


	private void receiveAnalogData(int port, int value) {
		if(port < 0 || port >= analogInPorts.length)
		{
			log.severe("Got message for unknown analog port "+port+" from "+getPortName());
			return;
		}
		// we divide by four to be range compatible with 8 bit ADC controllers
		analogInPorts[port].sendDoubleData(((double)value)/4);
	}

	private void receiveFrequencyData(int port, int value) {
		if(port < 0 || port >= frequencyInPorts.length)
		{
			log.severe("Got message for unknown frequency port "+port+" from "+getPortName());
			return;
		}
	frequencyInPorts[port].sendDoubleData(value);
	}

	@SuppressWarnings("unused")
	private void receivePwmData(int port, int value) {
		if(port < 0 || port >= pwmInPorts.length)
		{
			log.severe("Got message for unknown PWM port "+port+" from "+getPortName());
			return;
		}
	pwmInPorts[port].sendDoubleData(value);
	}

	private void receiveTemperatureData(int port, int value) {
		if(port < 0 || port >= temperatureInPorts.length)
		{
			log.severe("Got message for unknown temperature port "+port+" from "+getPortName());
			return;
		}
	log.finest(String.format("Got temperature, port %d val 0x%04X", port, value));
	
	//value &= 0xFFF; // XXX For some reason temperature has some junk in upper bits, check controller code?
	
	if(value == 0x550) // GOD knows why, but this value is often glitch :( -- it's power-on default for 18B20 :(
		return;
	
	temperatureInPorts[port].sendDoubleData(((double)value)/16); // Convert to degrees
	}
	
	private void receiveDigitalData(int port, int value) {
		if(port < 0)
		{
			log.severe("Got message for unknown digital port "+port+" from "+getPortName());
			return;
		}
		
		DriverPort [] ports;
		
		switch(port)
		{
		case 0: ports = digitalInPortsA; break;
		case 1: ports = digitalInPortsB; break;
		case 2: ports = digitalInPortsC; break;
		case 3: ports = digitalInPortsD; break;
		case 4: ports = digitalInPortsE; break;
		case 5: ports = digitalInPortsF; break;
		case 6: ports = digitalInPortsG; break;
		default:
			log.severe("Got message for unknown digital port "+port+" from "+getPortName());
			return;
		}
		
		for( int index = 0; index < ports.length; index++ )
		{
			if(index >= ports.length)
			{
				log.severe("Got message for unknown digital port "+port+" from "+getPortName());
				return;
			}
			ports[index].sendBooleanData( ((value >> index) & 0x01) != 0 ); 
		}

	}


	// -------------------------------------------------------------------------------
	// Send helpers
	// -------------------------------------------------------------------------------
	

	@SuppressWarnings("unused")
	private void sendValueRequests() throws CommunicationsException {
		for( int i = 0; i < ANALOG_IN_COUNT; i++ )
		{
			sendPacket( new DigitalZone128Packet( DigitalZone128PacketType.FROMHOST_ANALOG_REQUEST, i, 0 ) );
		}

		for( int i = 0; i < FREQUENCY_IN_COUNT; i++ )
		{
			sendPacket( new DigitalZone128Packet( DigitalZone128PacketType.FROMHOST_FREQ_VALUE_REQUEST, i, 0 ) );
		}

		int digPorts = DIGITAL_IN_COUNT/8;
		for( int i = 0; i < digPorts; i++ )
		{
			sendPacket( new DigitalZone128Packet( DigitalZone128PacketType.FROMHOST_DIGITAL_REQUEST, i, 0 ) );
		}
		
		for( int i = 0; i < TEMPERATURE_IN_COUNT; i++ )
		{
			sendPacket( new DigitalZone128Packet( DigitalZone128PacketType.FROMHOST_TEMPERATURE_VALUE_REQUEST, i, 0 ) );
		}
	}

	protected void sendPing() throws CommunicationsException {
		sendPacket( new DigitalZone128Packet( DigitalZone128PacketType.FROMHOST_PING, 0, 0 ) );
	}

	
	// -------------------------------------------------------------------------------
	// Getters/etc
	// -------------------------------------------------------------------------------
	
	protected volatile long lastInputPacketTimeMillis = -1;

	/** 
	 * @return milliseconds for the time when last packet came in 
	 * or -1 if none was received. 
	 */
	public long getLastInputPacketTimeMillis() { return lastInputPacketTimeMillis; }

	/**
	 * @return True if at least one good packet was received since last port reopen.
	 */
	public boolean gotAtLeastOnePacket() { 	return lastInputPacketTimeMillis >= 0; }

	
	// -------------------------------------------------------------------------------
	// Dashboard message window interface
	// -------------------------------------------------------------------------------
	
	protected Item dashMessage = null;

	{
		DashBoard db = ConfigurationFactory.getTransientState().getDashBoard();
		dashMessage = db.getMessageWindow().getItem(getControllerName()+" controller driver is initializing");
	}
	
	
	
	/**
	 * Set MessageWindow to be used for state tracking.
	 * @param messageWindow
	 */
	public void setMessageWindow(MessageWindow messageWindow) {
		dashMessage = messageWindow.getItem("Port "+getPortName()+" is initializing");		
	}

	// -------------------------------------------------------------------------------
	// Write verification
	// -------------------------------------------------------------------------------
	
	/*private boolean haveVerificationRequests = false;
	protected void verifyWrites()
	{
		synchronized(this)
		{
			// Nothing to do, oh baby, stay in bed
			if(haveVerificationRequests == false)
			{
				return;
			}
			// You're in the army now
			//Channel
			// TO DO implement servant write verification
		}
	}*/

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// -------------------------------------------------------------------------------
	// Abstracts
	// -------------------------------------------------------------------------------
	
	
	
	protected abstract void sendPacket(DigitalZone128Packet p) throws CommunicationsException;
	
	
	protected abstract String getControllerName();


	protected abstract void setDeviceTypeField(String string);
	protected abstract void setDeviceSerialField(String string);
	
	protected abstract String getPortName();


}
