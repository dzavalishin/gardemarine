package ru.dz.shipMaster.dev.controller.dz;

import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.dz.shipMaster.data.misc.CommunicationsException;
import ru.dz.shipMaster.dev.common.BiPipe;
import ru.dz.shipMaster.dev.common.TcpBiPipe;
import ru.dz.shipMaster.dev.controller.BinaryTools;
import ru.dz.shipMaster.dev.controller.ModBusException;
import ru.dz.shipMaster.misc.CrcExceprion;

public class HomeNetProtocol {
	protected static final Logger log = Logger.getLogger(HomeNetProtocol.class.getName());

	private byte  myNode = (byte) 255;
	private final BiPipe pipe;


	public HomeNetProtocol(BiPipe pipe) {
		this.pipe = pipe;
	}

	public HomeNetProtocol(BiPipe pipe, byte myNode) {
		this.pipe = pipe;
		this.myNode = myNode;		
	}



	public void sendSetGroup(byte group, byte generation, int value) throws CommunicationsException
	{
		HomeNetPacket p = new HomeNetPacket(myNode, (byte)0, HomeNetPacket.PKT_APP_L_SETGROUP, group, generation, value );
		p.setApp(HomeNetPacket.PKT_APP_LIGHTING);
		sendPacket(p);
	}

	public void sendGetGroup(byte group) throws CommunicationsException {
		HomeNetPacket p = new HomeNetPacket(myNode, (byte)0, HomeNetPacket.PKT_APP_L_GETGROUP, group, (byte)0, 0 );
		p.setApp(HomeNetPacket.PKT_APP_LIGHTING);
		sendPacket(p);
	}

	
	public void sendSetTemperature(byte group, byte generation, int value) throws CommunicationsException
	{
		HomeNetPacket p = new HomeNetPacket(myNode, (byte)0, HomeNetPacket.PKT_APP_T_SETTEMP, group, generation, value );
		p.setApp(HomeNetPacket.PKT_APP_TEMPERATURE);
		sendPacket(p);
	}

	public void sendGetTemperature(byte group) throws CommunicationsException {
		HomeNetPacket p = new HomeNetPacket(myNode, (byte)0, HomeNetPacket.PKT_APP_T_GETTEMP, group, (byte)0, 0 );
		p.setApp(HomeNetPacket.PKT_APP_TEMPERATURE);
		sendPacket(p);
	}



	public void sendPacket( HomeNetPacket p ) throws CommunicationsException
	{
		byte[] pkt = p.getPacket();
		pipe.write( pkt );
		//BinaryTools.dump("send DZ:HomeNet ", pkt);
		log.log(Level.SEVERE, "Send "+p );
	}

	public HomeNetPacket recvPacket() throws CommunicationsException 
	{
		byte [] pkt = new byte[HomeNetPacket.PACKET_SIZE];
		int nchars = 0;
		while( true ) 
		{
			int ch = pipe.readTimed(350);
			log.log(Level.FINEST,"get ch "+ch);

			if( ch < 0 )
				throw new CommunicationsException("DZ:HomeNet pkt timeout");

			if( nchars == 0 && ! HomeNetPacket.isStartByte((byte)ch))
				continue;

			if( nchars == HomeNetPacket.PACKET_SIZE ) 
				break;

			pkt[nchars++] = (byte)ch;

			if( nchars == HomeNetPacket.PACKET_SIZE ) 
				break;

		}

		// Eat all the junk. Note that controllers send CRLF after packet
		// to make it come faster through the TCP/RS485 gates.
		while(true)
		{
			int ch = pipe.peek();
			if( (ch < 0) || HomeNetPacket.isStartByte((byte)ch) )
				break;
			pipe.readTimed(1);
		}

		//log.log(Level.FINE, "DZ:HomeNet recv: " + sb );
		BinaryTools.dump("recv DZ:HomeNet ", pkt);

		HomeNetPacket packet;
		try {
			packet = new HomeNetPacket(pkt);
		} catch (CrcExceprion e) {
			throw new CommunicationsException("Wrong CRC",e);
		}

		log.log(Level.SEVERE, "Recv "+packet );
		return packet;

	}

	// -----------------------------------------------------------
	// test main 
	// -----------------------------------------------------------



	public static void main(String args[]) throws IOException, ModBusException, CommunicationsException 
	{

		InetAddress address = InetAddress.getByName("192.168.1.103");
		TcpBiPipe pipe = new TcpBiPipe( address, 13033 );
		HomeNetProtocol w = new HomeNetProtocol(pipe);

		pipe.connect();

		for(int i = 0; i < 22; i++ )
		{
			try {
				w.sendSetGroup((byte)0, (byte)i, 0xFFFF);
				BinaryTools.sleepMsec(200);
				w.sendSetGroup((byte)1, (byte)i, 0xFFFF);
				BinaryTools.sleepMsec(200);

			} catch (Throwable e) {
				e.printStackTrace();
			}
		}

		for(int i = 0; i < 60; i++)
		{
			byte [] data = new byte [12];
			pipe.read(data);
			BinaryTools.dump("recv DZ:HomeNet ", data);
		}

		pipe.disconnect();
	}




}
