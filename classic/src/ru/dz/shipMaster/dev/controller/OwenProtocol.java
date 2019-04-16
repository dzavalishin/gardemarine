package ru.dz.shipMaster.dev.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.dz.shipMaster.data.misc.CommunicationsException;
import ru.dz.shipMaster.dev.common.BiPipe;
import ru.dz.shipMaster.dev.common.TcpBiPipe;

public class OwenProtocol {
	protected static final Logger log = Logger.getLogger(OwenProtocol.class.getName());

	//** максимальная длина пакета согласно протоколу ОВЕН */
	//final static int  maxFrameSize = 21;

	/** максимальная длина ASCII-пакета включая маркеры и символ '\0'  */
	final static int  MAX_ASCII_FRAME_SIZE = 45;

	private final int unitNo;
	private final BiPipe pipe;


	public OwenProtocol(BiPipe pipe, int unitNo) {
		this.pipe = pipe;
		this.unitNo = unitNo;		
	}

	
	private float cmdRead(int inputNo) throws CommunicationsException {
		OwenProtocolFrame f = new OwenProtocolFrame();
		f.setAddress(unitNo+inputNo);
		f.setHash(0x8784); //rEAd
		f.setRequest(true);
		f.setAddrLen(8);
		
		sendPacket(f);
		
		OwenProtocolFrame re = recvPacket();
		
		log.log(Level.FINE, String.format(" port %d = %f, tm = %d", 
				inputNo,
				re.unpackIEEE32Float(), 
				re.unpackIEEE32Time()
				) );
		
		return re.unpackIEEE32Float();
	}

	
	
	
	
	
	
	
	public void sendPacket( OwenProtocolFrame f ) throws CommunicationsException
	{
			byte[] pkt = OwenProtocolFrame.packFrameToAscii( f.packFrame() );
			pipe.write( pkt );
			//BinaryTools.dump("send owen ", pkt);
	}

	OwenProtocolFrame recvPacket() throws CommunicationsException 
	{
		StringBuilder sb = new StringBuilder();
		int nchars = 0;
		while( true ) 
		{
			int ch = pipe.readTimed(250);
			log.log(Level.FINEST,"get ch "+ch);
			
			if( ch < 0 )
				throw new CommunicationsException("Owen timeout");

			if( nchars == 0 && !( ch == '#' ))
				continue;
			
			if( ch == 0x0D ) //|| ch == 0x0A )
				break;
			
			sb.append((char)ch); 
			nchars++;
			
			if( nchars > MAX_ASCII_FRAME_SIZE )
				throw new CommunicationsException("Reply is too long");
		}
		
		log.log(Level.FINE, "owen recv: " + sb );

		OwenProtocolFrame f = new OwenProtocolFrame();
		
		try {
			f.unpackFrame(OwenProtocolFrame.unpackAsciiFrame(sb.toString().getBytes("latin1")));
		} catch (UnsupportedEncodingException e) {
			// can't happen?
			throw new CommunicationsException("Incoming packet is not in latin1", e);
		}
		
		return f;
		
	}

	// -----------------------------------------------------------
	// test main 
	// -----------------------------------------------------------
	


	public static void main(String args[]) throws IOException, ModBusException, CommunicationsException 
	{
		
		InetAddress address = InetAddress.getByName("192.168.1.103");
		TcpBiPipe tcpBiPipe = new TcpBiPipe( address, 13033 );
		OwenProtocol w = new OwenProtocol(tcpBiPipe,16);
		
		tcpBiPipe.connect();
		
		for(int i = 0; i < 20; i++ )
		{
			try {
				w.cmdRead(0);
				BinaryTools.sleepMsec(200);

			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		
		tcpBiPipe.disconnect();
	}


	
}
