package ru.dz.shipMaster.data.com;

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
import java.util.TooManyListenersException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.dz.shipMaster.data.misc.CommunicationsException;

public class ComPortGnuIO extends GenericComPortIO {
	static final Logger log = Logger.getLogger(ComPortGnuIO.class.getName());

	private static final int HALF_DUPLEX_TIMEOUT_MSEC = 100; 

	protected final Object ioSync = new Object();

	private final String portname;
	private RXTXPort port;

	protected BlockingQueue<Byte> recvQueue = new ArrayBlockingQueue<Byte>(2000);

	private CommPortIdentifier portIdentifier;


	protected volatile InputStream inputStream; // used to break read loop
	private OutputStream outputStream;


	private final int baudRate;

	// We use 9600?
	public ComPortGnuIO(String _portname, int baudRate) throws CommunicationsException
	{
		this.portname = _portname;
		this.baudRate = baudRate;

		try { portIdentifier = CommPortIdentifier.getPortIdentifier(portname); } 
		catch (NoSuchPortException e) {
			throw new CommunicationsException("Порт отсутствует: "+portname, e);
		}

		try { init(baudRate); } 
		catch (Exception e) {
			throw new CommunicationsException("Не могу открыть порт "+portname, e);
		}
	}

	public void init(int baudRate) throws CommunicationsException, PortInUseException, UnsupportedCommOperationException, IOException {

		synchronized(ioSync)
		{
			port = (RXTXPort)portIdentifier.open("ShipMaster", 100);		

			port.setSerialPortParams( baudRate, 
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			port.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);

			//port.setInputBufferSize(8192);
			//port.setOutputBufferSize(8192);

			//port.disableReceiveThreshold();
			//port.disableReceiveTimeout();

			//port.setParityErrorChar((byte)0xFF);
			//port.setUARTType(arg0, arg1)

			inputStream = port.getInputStream();
			outputStream = port.getOutputStream();
			
			try {
				port.addEventListener(new SerialPortEventListener() {
					@Override
					public void serialEvent(SerialPortEvent event) {
						switch(event.getEventType()) {
						case SerialPortEvent.DATA_AVAILABLE:
							lastDataIn = System.currentTimeMillis();
							
							try {
								synchronized (ioSync) {
									//System.out.print(inputStream.read());
									int available = inputStream.available();
									byte [] buf = new byte[available];
									inputStream.read(buf);
									for(int i = 0; i < buf.length; i++)
										recvQueue.offer(buf[i]); // We ignore if queue is empty
								}
								} catch (IOException e) {
									log.log(Level.SEVERE,"io",e);
								}
							break;
							
						default:
							break;
						}
					}});
				port.notifyOnDataAvailable(true);
			} catch (TooManyListenersException e) {
				log.log(Level.SEVERE,"listener",e);
			}
		}
	}	

	// -------------------------------------------------------------------------------
	// I/O
	// -------------------------------------------------------------------------------

	@Override
	public int read(byte data[]) {		
		try {		
			int got = 0;
			//while( true ) {
				if(inputStream == null) //return 0;
				{
					throw new CommunicationsException("Port is closed");
				}
				/*
				synchronized(ioSync)
				{
					got = inputStream.read(data);
				}*/
				
				while(got < data.length && inputStream == null)
					data[got++] = recvQueue.take();
				
				/*if( got == 0 )
				{
					synchronized (this) { wait(100); }
					continue;
				}*/
				//break;			}

//			System.out.println("got from serial: "+got);
//			System.out.println("got from serial: "+data.length);
//			GenericComPortIO.dump(data);
			return got;
		}
		catch( Exception e ) {
			log.severe("Ошибка чтения: "+e.toString());
			//reopen();		
			return 0;
		}		
	}

	
	@Override
	public void write(byte[] data) throws CommunicationsException
	{
		if(outputStream == null)
		{
			throw new CommunicationsException("Port is closed");
		}
		try { 
			synchronized(ioSync)
			{
				while(halfDuplexMode && dataInWas() < HALF_DUPLEX_TIMEOUT_MSEC)
					sleepMsec(HALF_DUPLEX_TIMEOUT_MSEC*2);
				
				//outputStream.write(data);
				for( int i = 0; i < data.length; i++)
				{
					outputStream.write(data[i]);
				}
			}
		}
		catch( Exception e ) {
			log.severe("Ошибка записи: "+e.toString());
			//reopen();		
		}		
	}


	// -------------------------------------------------------------------------------
	// Reinit stuff
	// -------------------------------------------------------------------------------


	@Deprecated
	@Override
	public void reopen() {
		synchronized(ioSync)
		{
			close();
		}

		sleepMsec(500);

		try { init(baudRate); }  
		catch (Exception e) {
			log.log(Level.SEVERE, "Не могу повторно открыть "+portname, e );
		}
	}

	@Override
	public void close() {
		synchronized(ioSync)
		{
			try {
				if(inputStream != null ) { inputStream.close(); }
				if(outputStream != null) { outputStream.close(); }
			} catch (IOException e) {
				log.log(Level.SEVERE,"Close error",e);
			}
			inputStream = null;
			outputStream = null;
			if(port != null) { port.close(); }
			port = null;
			
			recvQueue.offer((byte)0); // to unlock read method if it waits for character
		}
	}	


}
