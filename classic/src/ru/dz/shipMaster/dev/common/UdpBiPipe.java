package ru.dz.shipMaster.dev.common;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import ru.dz.shipMaster.data.misc.CommunicationsException;

public class UdpBiPipe extends AbstractTcpBiPipe {

	private DatagramSocket socket;



	@Override
	protected void readStep() {
		byte[] buf = new byte[1024];
		DatagramPacket p = new DatagramPacket(buf, buf.length);
		try {


			socket.receive(p);
			int length = p.getLength();
			
			int pos = 0;
			while(length-- > 0)
				recvQueue.put(buf[pos++]);

		} catch (SocketTimeoutException e) {
			return;
		} catch (IOException e) {
			kickErrorEvent(e);
		} catch (InterruptedException e) {
			kickErrorEvent(e);
		} 
		finally {
			kickReceiveEvent();
		}

	}

	@Override
	protected void writeStep() {
		byte[] bs;
		bs = getChunkToSend();

		try {
			halfDuplexStartSend();
			DatagramPacket p = new DatagramPacket(bs, bs.length);
			socket.send(p);
			halfDuplexEndSend();
		} catch (IOException e) {
			kickErrorEvent(e);
		}
	}


	@Override
	public void connect() throws CommunicationsException {
		try {
			socket = new DatagramSocket();
			socket.setSoTimeout(2000); 
			
		} catch (SocketException e1) {
			//e1.printStackTrace();
			throw new CommunicationsException("Can't create socket", e1);
		}	
		try {
			socket.connect(new InetSocketAddress(inetAddress,port));
			//outputStream = socket.getOutputStream();
		} catch (IOException e) {
			throw new CommunicationsException("Can't connect to host", e);
		}
		startThreads();
		connected = true;
	}

	@Override
	public void disconnect() throws CommunicationsException {
		connected = false;

		stopThreads();

		if(socket != null)
		{
			socket.disconnect();
			socket.close();
		}
	}


	@Override
	public String getTypeName() { return "UDP"; }	
}
