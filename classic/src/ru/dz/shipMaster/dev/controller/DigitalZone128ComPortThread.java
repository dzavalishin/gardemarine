package ru.dz.shipMaster.dev.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import ru.dz.shipMaster.data.com.GenericComPortIO;
import ru.dz.shipMaster.data.misc.CommunicationsException;
import ru.dz.shipMaster.misc.CrcExceprion;

public class DigitalZone128ComPortThread extends Thread {




	//private static final long INTERVAL_MSEC = 500L; 
	private static final long INTERVAL_MSEC = 100L;
	//private static final long INTERVAL_MSEC = Config.getInt("statistics.interval") * 1000L;
	static final Logger log = Logger.getLogger("ComPortThread");

	static final String threadName = "Serial port reader";

	/**
	 * True if thread is running.
	 */
	static protected boolean haveThread = false;
	private final GenericComPortIO io;
	private final DigitalZone128 protocol;
	private volatile boolean askedToStop = false;

	/**
	 * Creates thread.
	 * @param protocol one to feed packets to
	 */

	public DigitalZone128ComPortThread(GenericComPortIO io, DigitalZone128 protocol)
	{
		this.io = io;
		this.protocol = protocol;
		setDaemon(true);
		start();
		setPriority(Thread.MAX_PRIORITY);
	}


	//@SuppressWarnings({"InfiniteLoopStatement"})
    @Override
	public void run() {

		log.fine(threadName+" started");
		this.setName(threadName);
		//this.setDaemon(true);

		while(!askedToStop)
		{

			try { doTask(); } 
			catch (Exception e) {
				log.log(Level.SEVERE, "Exception in "+threadName, e);
				
				// sleep a bit to prevent a tight loop  
				try { synchronized (this) { wait(INTERVAL_MSEC); } } 
				catch (InterruptedException e1) {
					continue;
				}
			}
		}
		//haveThread = false;
	}

	//List<ServantPacket> receiveQueue = new LinkedList<ServantPacket>();

	private void doTask() throws CommunicationsException 
	{

		byte[] start = new byte[1];
		while(!askedToStop)
		{
			io.read(start); // Seek for packet start byte
			if(DigitalZone128Packet.isStartByte(start[0]))
				break;
		}

		byte[] pkt = new byte[DigitalZone128Packet.PACKET_SIZE];
		pkt[0] = start[0];

		int need = DigitalZone128Packet.PACKET_SIZE-1;
		int pos = 1;		
		
		packet: while(!askedToStop)
		{
			if(askedToStop)
			{
				return;
			}

			while( (need > 0) && (!askedToStop) )
			{
				byte[] data = new byte[need];
				int got = io.read(data);

				System.arraycopy(data, 0, pkt, pos, need);
				pos += got;
				need -= got;
			}

			if(askedToStop)
			{
				return;
			}
			
			//if(DigitalZone128Packet.checkShortPacketCRC(pkt))
			{
				try {
					addPacket(new DigitalZone128Packet(pkt));
					//log.log(Level.SEVERE,"Packet ok");
					return;
				} catch (CrcExceprion e) {
					log.log(Level.SEVERE,"Packet CRC error", e);
					// Fall through to correction code
				}
			}
			//GenericComPortIO.dump(pkt);

			if(askedToStop)
			{
				return;
			}
			
			for( int startPos = 1; startPos < DigitalZone128Packet.PACKET_SIZE; startPos++ )
			{
				if(DigitalZone128Packet.isStartByte(pkt[startPos]))
				{
					System.arraycopy(pkt, startPos, pkt, 0, DigitalZone128Packet.PACKET_SIZE-startPos);
					
					//data = io.read(startPos); // read bytes to complete to DigitalZone128Packet.PACKET_SIZE bytes					
					//System.arraycopy(data, 0, pkt, DigitalZone128Packet.PACKET_SIZE-startPos, startPos);
					need = startPos;
					pos = DigitalZone128Packet.PACKET_SIZE-startPos; 
					
					continue packet;
				}
			}
		break;
		}		
	
	}

	private boolean readPacketExtension(byte[] ext, int len) throws CommunicationsException
	{
//log.severe("readPacketExtension len = "+len);
		int need = len+2;
		int pos = 0;		

		if(askedToStop)
		{
			return false;
		}

		while( (need > 0) && (!askedToStop) )
		{
			byte[] data = new byte[need];
			int got = io.read(data);

			if(got < 0 )
				return false;
				//continue;
			
			System.arraycopy(data, 0, ext, pos, need);
			pos += got;
			need -= got;
		}

		if(askedToStop)
		{
			return false;
		}

		if(DigitalZone128Packet.checkPacketExtensionCRC(ext, len))
		{
			return true;
		}
		log.severe("Packet extension CRC error");
		return false;
		
	}


	private void addPacket(DigitalZone128Packet p)
	{
		/*synchronized (receiveQueue) {
			receiveQueue.add(p);
		}*/
		
		if(p.getType() == DigitalZone128PacketType.TOHOST_LONG_PACKET )
		{
			byte[] ext = new byte[p.getValue()+2]; // 2 for CRC
			try {
				if(!readPacketExtension(ext,p.getValue()))
					return;
			} catch (CommunicationsException e) {
				log.log(Level.SEVERE,"Can't read packet extension",e);
				return;
			}
			
			protocol.receivePacket( new DigitalZone128Packet(p.getPort(),ext) );
		}
		else
		{
			protocol.receivePacket(p);
		}

	}


	public void stopSelf() {
		askedToStop  = true;		
	}

	/*public DigitalZone128Packet getPacket()
	{
		synchronized (receiveQueue) {
			if(receiveQueue.size() > 0)
				return receiveQueue.remove(0);
			else
				return null;
		}		
	}*/


}
