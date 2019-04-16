package ru.dz.shipMaster.net.protocol;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class NetIO {
    private static final Logger log = Logger.getLogger(NetIO.class.getName()); 

	private Thread receptionThread;
	//private DatagramSocket socket;
	private MulticastSocket socket;
	private InetAddress inetAddress;

	public NetIO() throws SocketException, IOException
	{
		inetAddress = InetAddress.getByName(ProtocolDefs.multicastGroupName);
		socket = new MulticastSocket(ProtocolDefs.UDP_PORT);
		socket.joinGroup(inetAddress);
		
		receptionThread = new Thread() {
			@Override
			public void run() {
				while(true)
				{
					try { receiveData(); }
					catch(Throwable e)
					{
						log.log(Level.SEVERE,"Net recv error",e);
					}
				}
			}};
			
		receptionThread.setName("UDP listener");
		receptionThread.setDaemon(true);
		receptionThread.start();
	}

	protected void receiveData() throws IOException, NetException {
		byte[] buf = new byte[ProtocolDefs.MAX_PACKET_SIZE];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		socket.receive(packet);
		log.log(Level.FINEST,"Got UDP");			
			
		//NetPacket p = new NetPacket(packet.getData());
		NetPacket p = new NetPacket(packet);
		
		if(p.getSrcHash() == NodeHash.getMyNodeHash().getHash())
			log.log(Level.FINER,"Packet from me ignored");
		else
			receivePacket(p);
	}

	protected abstract void receivePacket(NetPacket packet) throws IOException, NetException;

	
	public void sendPacket(NetPacket p) throws NetException
	{
		byte[] pData;
		try { pData = p.assemble(); } 
		catch (UnsupportedEncodingException e) {
			throw new NetException("Encoding problem",e);
		}
		try {
			DatagramPacket packet = new DatagramPacket(pData, pData.length, inetAddress, ProtocolDefs.UDP_PORT);
			socket.send(packet);
		} catch (IOException e) {
			throw new NetIOException("Net IO problem",e);
		}
	}
	
	
}
