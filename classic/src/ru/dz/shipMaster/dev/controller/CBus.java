package ru.dz.shipMaster.dev.controller;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.RXTXPort;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.data.misc.CommunicationsException;
import ru.dz.shipMaster.dev.DriverPort;
import ru.dz.shipMaster.dev.PortDataOutput;
import ru.dz.shipMaster.dev.ThreadedDriver;


public class CBus extends ThreadedDriver {
	private static final Logger log = Logger.getLogger(CBus.class.getName()); 

	private JTextField portNameField = new JTextField(10);
	private String portName = "COM1";

	private RXTXPort port;

	private int nOutputPorts = 2;
	private DriverPort[] outputPorts = new DriverPort[ nOutputPorts ];
	private double [] lastOutValue = new double [nOutputPorts]; 

	private int nInputPorts = 2;
	private DriverPort[] inputPorts = new DriverPort[ nInputPorts ];
	private OutputStream oStream;
	private InputStream iStream;


	public CBus() {
		super(50, Thread.NORM_PRIORITY, "CBus driver");
	}

	@Override
	protected void doDriverTask() throws Throwable {
		List<CBusCommand> cmd;

		do {
			cmd = readPacket(iStream);

			if( cmd == null)
				return;

			for( CBusCommand c : cmd )
			{    	
				System.out.println("CBus.doDriverTask() got cmd = "+c);
				processCmd(c);
			}
		} while(cmd != null);
	}

	@Override
	protected void doLoadPanelSettings() {
		portNameField.setText(portName);
	}

	@Override
	protected void doSavePanelSettings() {
		portName = portNameField.getText();
	}

	@Override
	protected void doStartDriver() throws CommunicationsException {
		CommPortIdentifier portIdentifier;
		try {
			portIdentifier = CommPortIdentifier.getPortIdentifier(portName);

			port = (RXTXPort)portIdentifier.open("CBus", 100);		

			port.setSerialPortParams( 9600, 
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			oStream = port.getOutputStream();
			iStream = port.getInputStream();

			oStream.write("~~~\r".getBytes("LATIN1"));
			oStream.write("A3210038g\r".getBytes("LATIN1"));
			oStream.write("A3420002g\r".getBytes("LATIN1"));
			oStream.write("A3300079g\r".getBytes("LATIN1"));

		} catch (NoSuchPortException e) {
			throw new CommunicationsException("No such COM port", e);
		} catch (PortInUseException e) {
			throw new CommunicationsException("COM port in use", e);
		} catch (UnsupportedCommOperationException e) {
			throw new CommunicationsException("Unsupported COM port operation", e);
		} catch (UnsupportedEncodingException e) {
			log.log(Level.SEVERE,"can't happen?",e);
		} catch (IOException e) {
			log.log(Level.SEVERE,"io",e);
		}

	}

	@Override
	protected void doStopDriver() throws CommunicationsException {
		try {
			iStream.close();
			oStream.close();
		} catch (IOException e) {
			log.log(Level.SEVERE,"close",e);
		}
		port.close();
	}

	@Override
	public String getDeviceName() { return "C-Bus Serial adapter"; }

	@Override
	public boolean isAutoSeachSupported() { return false; }

	@Override
	protected void setupPanel(JPanel panel) {
		panel.add(new JLabel("COM port:"), consL);
		panel.add(portNameField, consR);
	}

	@Override
	protected void updatePorts(Vector<DriverPort> ports) {
		DriverPort.absentize(ports);
		DriverPort.sortPorts(ports);

		/* None yet
         {
         DriverPort port = DriverPort.getPort(ports,0);
         port.setDirection(Direction.Output);
         port.setHardwareName("TextToShow");
         port.setType(Type.String);
         }*/

		int i;

		for( i = 0; i < nInputPorts; i++ )
		{
			String portName = "In group "+i;
			inputPorts[i] = getPort(ports, i, Direction.Input, Type.Numeric, portName );
		}

		for( i = 0; i < nOutputPorts; i++ )
		{
			String portName = "Out group "+i;
			outputPorts[i] = getPort(ports, i+nInputPorts, Direction.Output, Type.Numeric, portName );

			final int controllerPortNo = i;

			outputPorts[i].setPortDataOutput
			(
					new PortDataOutput() {
						@Override
						public void receiveDoubleData(double value) {
							if(value != lastOutValue[controllerPortNo])
							{
								lastOutValue[controllerPortNo] = value;
								log.info("Set out: "+value);
								setOutput( controllerPortNo, value );
							}
						}});

		}


	}


	/** Translate value to output port */
	private void setOutput( int controllerPortNo, double value )
	{
		try {
			oStream.write(CBusCommand.onOffCommand(controllerPortNo, value > 0.01).getPacket());
		} catch (IOException e) {
			log.log(Level.SEVERE,"io",e);
		}
	}

	public List<CBusCommand> readPacket(InputStream is) throws CommunicationsException 
	{
		List<CBusCommand> out = new LinkedList<CBusCommand>();
		byte [] ipkt = new byte[CBusProtocol.PACKET_MAX_LEN];

		int ppos = 0;

		// TODO timeout reading packet
		try {
			int c;

			/*while( (c = is.read()) != CBusProtocol.PACKET_START_CHAR )
			{
				if(isStopRequested() || c < 0 )
					return null;
				//System.out.println("CBus.readPacket() got char = " + c);
				System.out.println(String.format("'%c'", c));
			}*/

			//ipkt[ppos++] = (byte)c;

			do {
				c = is.read();

				if(isStopRequested() || c < 0 )
					return null;

				//System.out.print(String.format("'%c' ", c));

				ipkt[ppos++] = (byte)c;
			} while( c != CBusProtocol.PACKET_END_CHAR);

			//out.add( new CBusSimpleCommand(ipkt) );

			CBusProtocol.decodeCmds(out,sink,ipkt);

			return out;

		} catch (IOException e) {
			throw new CommunicationsException("CBus IO error", e);
		}

	}

	private CBusStatusSink sink = new CBusStatusSink() {

		@Override
		void receiveStatus(int application, int group, CBusGroupState s) {
			if(s != CBusGroupState.DoesNotExist)
				System.out.println(String.format("CBus status grp %02X %s", group, s.toString() ));
			
			if(s == CBusGroupState.IsON)
				inputPorts[group].sendBooleanData(true);
			if(s == CBusGroupState.IsOFF)
				inputPorts[group].sendBooleanData(false);
			
		}};





	private void processCmd(CBusCommand c) {
		if(c instanceof CBusSimpleCommand )
		{
			CBusSimpleCommand sc = (CBusSimpleCommand)c;
			
			int port = sc.getGroup();
			
			inputPorts[port].sendBooleanData(sc.getCommand() == CBusCommand.CMD_ON);
		}

	}

	@Override
	public String getInstanceName() {
		return portName;
	}


}
