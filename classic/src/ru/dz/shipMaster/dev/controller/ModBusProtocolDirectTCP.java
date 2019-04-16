package ru.dz.shipMaster.dev.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public abstract class ModBusProtocolDirectTCP extends ModBusProtocol {

	// -------------------------------------------
	// TCP
	//-------------------------------------------

	protected static final int MODBUS_TCP_PORT = 502;


	protected Socket socket = null;

	/**
	 * Connect to modbus device by TCP.
	 * 
	 * @param ipAddress Address of slave to connect to.
	 * @throws IOException
	 */
	public synchronized void startTcp(InetAddress ipAddress) throws IOException {
		InetSocketAddress endpoint = new InetSocketAddress(ipAddress,MODBUS_TCP_PORT);
		socket = new Socket();	
		socket.connect(endpoint);
	}

	/**
	 * Connect to modbus device by TCP.
	 * 
	 * @param ipAddress Address of slave to connect to.
	 * @param port port to connect to
	 * @throws IOException
	 */
	public synchronized void startTcp(InetAddress ipAddress, int port) throws IOException {
		InetSocketAddress endpoint = new InetSocketAddress(ipAddress,port);
		socket = new Socket();	
		socket.connect(endpoint);
	}

	/**
	 * Disconnect from device.
	 * 
	 * @throws IOException
	 */
	public synchronized void stopTcp() throws IOException {
		if(socket != null)
		{
			socket.close();
		}
		socket = null;
	}

	
}
