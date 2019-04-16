package ru.dz.shipMaster.dev.common;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.RXTXPort;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import ru.dz.shipMaster.data.misc.CommunicationsException;

public class ComPortGnuBiPipe extends AbstractSerialBiPipe implements SerialPortEventListener {
	private InputStream inputStream;
	private OutputStream outputStream;

	private RXTXPort serialPort;
	private IdWrapper idw;

	/*public ComPortGnuBiPipe(SerialPort serialPort)
	{
		this.serialPort = serialPort;		
	}*/

	public ComPortGnuBiPipe()
	{
	}

	public ComPortGnuBiPipe(CommPortIdentifier id)
	{
		this.idw = new IdWrapper(id);
	}


	@Override
	protected void readStep() {
		reader.stop(); // I don't need me
		reader.sleep(20000); // Do nothing in case stop failed :)
	}

	@Override
	protected void writeStep() { writeStep(outputStream); }

	
	

	
	@Override
	public void connect() throws CommunicationsException {
		try {
			serialPort = (RXTXPort)(idw.getId()).open(this.getName(), 100);
			inputStream = serialPort.getInputStream();
			outputStream = serialPort.getOutputStream();
			serialPort.addEventListener(this);

			//serialPort.setBaudBase(RXTXPort.);
			serialPort.setSerialPortParams( baudRate, 
					SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			
			switch (flowControl) {
			default:
			case None:
				serialPort.setFlowControlMode(RXTXPort.FLOWCONTROL_NONE);
				break;

			case RTSCTS:
				serialPort.setFlowControlMode(RXTXPort.FLOWCONTROL_RTSCTS_IN|RXTXPort.FLOWCONTROL_RTSCTS_OUT);
				break;
				
			case XonXoff:
				serialPort.setFlowControlMode(RXTXPort.FLOWCONTROL_XONXOFF_IN|RXTXPort.FLOWCONTROL_XONXOFF_OUT);
				break;
			}
		} catch (TooManyListenersException e) {
			throw new CommunicationsException("Can't connect to port", e);
			/*} catch (IOException e) {
			throw new CommunicationsException("Can't connect to port", e);
			 */
		} catch (PortInUseException e) {
			throw new CommunicationsException("Can't connect to port", e);
		} catch (UnsupportedCommOperationException e) {
			throw new CommunicationsException("Can't setup port mode", e);
		} 
		serialPort.notifyOnDataAvailable(true);
		startThreads();
		connected = true;
	}

	@Override
	public void disconnect() throws CommunicationsException {
		if( serialPort != null ) serialPort.notifyOnDataAvailable(false);
		stopThreads();
		connected = false;
	}

	@Override
	public String getEndPointName() { 
		//return serialPort.getName();
		return (idw == null) ? "(unknown)" : idw.getId().getName();
	}

	@Override
	public String getTypeName() { return "Com port"; }


	public void serialEvent(SerialPortEvent event) {
		switch (event.getEventType()) {
		case SerialPortEvent.BI:
		case SerialPortEvent.OE:
		case SerialPortEvent.FE:
		case SerialPortEvent.PE:
		case SerialPortEvent.CD:
		case SerialPortEvent.CTS:
		case SerialPortEvent.DSR:
		case SerialPortEvent.RI:
		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
		default:
			break;
		case SerialPortEvent.DATA_AVAILABLE:
			// we get here if data has been received
			try {
				while (inputStream.available() > 0) {
					int c = inputStream.read();
					recvQueue.put( (byte)c );
				}
				kickReceiveEvent();

			} catch (IOException e) {
				kickErrorEvent(e);
			} 
			catch (InterruptedException e) {
				// we are stopping then, return.
			}

			break;
		}
	}


	private JPanel panel;
	private JComboBox comboBoxPorts;

	@Override
	public JPanel getSetupPanel() {
		if( panel != null )
			return panel;

		panel = new JPanel();
		comboBoxPorts = new JComboBox();

		panel.add(comboBoxPorts);

		loadPortIds();

		return panel;
	}


	class IdWrapper
	{
		private CommPortIdentifier id;

		public IdWrapper(CommPortIdentifier id) {
			this.id = id;
		}
		
		@Override
		public String toString() {
			return getName();
			}

		public CommPortIdentifier getId() {
			return id;
		}

		public String getName() {
			return (id == null) ? null : id.getName();
		}
	}


	//@SuppressWarnings("rawtypes")
	//private static Enumeration portIdentifiers = gnu.io.CommPortIdentifier.getPortIdentifiers();

	private void loadPortIds() {

		comboBoxPorts.removeAllItems();
		@SuppressWarnings("rawtypes")
		Enumeration portIdentifiers = gnu.io.CommPortIdentifier.getPortIdentifiers();

		while(portIdentifiers.hasMoreElements())
		{
			comboBoxPorts.addItem( new IdWrapper( (CommPortIdentifier) portIdentifiers.nextElement() ) );
		}
	}

	@Override
	public void loadPanelSettings() {
		comboBoxPorts.setSelectedItem(idw);
	}


	@Override
	public void savePanelSettings() {
		idw = (IdWrapper)comboBoxPorts.getSelectedItem();
	}


	public String getId() {
		return (idw == null) ? null : idw.getName();
	}


	public void setId(String idName) throws NoSuchPortException {
		this.idw = new IdWrapper( CommPortIdentifier.getPortIdentifier(idName) );
	} 

}
