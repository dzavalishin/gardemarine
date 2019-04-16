package ru.dz.shipMaster.data.com;

import java.io.IOException;
import java.util.TooManyListenersException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import jd2xx.JD2XX;
import jd2xx.JD2XXEvent;
import jd2xx.JD2XXEventListener;
import ru.dz.shipMaster.data.misc.CommunicationsException;

/**
 * Simple FT232 USB interface.
 * @author dz
 */
public class FT232PortIO extends GenericComPortIO {
		private static final Logger log = Logger.getLogger(FT232PortIO.class.getName()); 

	private static final int HALF_DUPLEX_TIMEOUT_MSEC = 30; 

	JD2XX jd = new JD2XX();
	private int devNum;

	protected BlockingQueue<Byte> recvQueue = new ArrayBlockingQueue<Byte>(2000);

	@SuppressWarnings("unused")
	private Object halfDuplexSema = new Object();

	@SuppressWarnings("unused")
	private boolean inSend;

	public FT232PortIO(int devNum) throws IOException
	{
		this.devNum = devNum;
		jd.open(devNum); 

		jd.setFlowControl(JD2XX.FLOW_NONE, 0, 0);
		jd.setChars(0, false, 0xFF, false);
		//jd.setBaudRate(9600);
		jd.setBaudRate(38400);

		//jd.setLatencyTimer(2); // lowest!

		jd.setDataCharacteristics(JD2XX.BITS_8, JD2XX.STOP_BITS_1, JD2XX.PARITY_NONE);
		// use 2 stop bits to ensure half-duplex switchback will not cut stop bit?

		//jd.setUSBParameters(64, 64);

		try {
			jd.addEventListener(new JD2XXEventListener() {

				@Override
				public void jd2xxEvent(JD2XXEvent event) {
					if(event.getEventType() == JD2XXEvent.EVENT_RXCHAR)
					{
						try {
							/*if(inSend)
								jd.read(); // just eat it
							else*/
							while(jd.getQueueStatus() > 0)
								recvQueue.offer((byte)jd.read());
						} catch (IOException e) {
							log.log(Level.SEVERE,"rx",e);
						}
					}

				}});
			jd.notifyOnRxchar(true);
		} catch (TooManyListenersException e) {
			log.log(Level.SEVERE,"listener",e);
		}
	}


	@Override
	public void close() {
		try {
			jd.close();
		} catch (IOException e) {
			log.log(Level.SEVERE,"close",e);
		}

	}

	@Override
	public void reopen() {
		try {
			jd.close();
			jd.open(devNum);
		} catch (IOException e) {
			log.log(Level.SEVERE,"reopen",e);
		}

	}


	// -----------------------------------------------------------
	// IO 
	// -----------------------------------------------------------


	/*@Override
	public int read(byte[] data) throws CommunicationsException {
		try {
		int ret = jd.read(data);

			//dump("Recv ", data);

			lastDataIn = System.currentTimeMillis();
			return ret;
			} catch (IOException e) {
			throw new CommunicationsException("IO error",e);
		}
	}*/


	
	@Override
	public int read(byte[] data) throws CommunicationsException {
		int ret = 0;

		while(ret < data.length)
			try {
				data[ret++] = recvQueue.take();
			} catch (InterruptedException e) {
				log.log(Level.SEVERE,"read",e);
			}

		//dump("Recv ", data);

		lastDataIn = System.currentTimeMillis();
		return ret;
	}


	//private static byte [] pad = new byte[5]; 

	@Override
	public void write(byte[] data) throws CommunicationsException {
		try { 	
			while(halfDuplexMode && dataInWas() < HALF_DUPLEX_TIMEOUT_MSEC)
				sleepMsec(HALF_DUPLEX_TIMEOUT_MSEC*2);

			inSend = true;
			jd.write(data);

			while(true)
			{
				int[] status = jd.getStatus();
				if(status[1] > 0)
					sleepMsec(10); // output q is not empty
				else
					break;
			}
			inSend = false;
		} 
		catch (IOException e) {
			throw new CommunicationsException("IO error",e);
		}

		/*
		if(halfDuplexMode)
		{
			// Drain input
			int count = data.length;// + 10;
			try {
				while((jd.getQueueStatus() > 0) && (count-- > 0))
					jd.read();
			} catch (IOException e) {
				log.log(Level.SEVERE,"exception",e);
			}
		}
		 */

	}


}
