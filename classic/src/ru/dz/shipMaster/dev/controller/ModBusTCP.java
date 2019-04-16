package ru.dz.shipMaster.dev.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import ru.dz.shipMaster.data.misc.CommunicationsException;

public class ModBusTCP extends ModBusProtocolDirectTCP {
	
	// -------------------------------------------
	// Packets
	//-------------------------------------------
	
	/** 
	 * The following functions construct the required query into a modbus query packet. 
	 * @return packet
	 */

	protected byte[] buildRequestPacket( int function, int startAddr, int count ) 
	{
		return buildRequestPacket( function, startAddr, count, 0 );
	}

	protected byte[] buildRequestPacket( int function, int startAddr, int count, int addsize ) 
	{
		byte [] packet = new byte [12+addsize];
		int i;

		for ( i = 0; i < 5 ; i++ ) 
			packet[ i ] = 0;

		packet[ i++ ] = 6;
		packet[ i++ ] = (byte) slave;
		packet[ i++ ] = (byte) function;
		packet[ i++ ] = (byte) (startAddr >> 8);
		packet[ i++ ] = (byte) (startAddr & 0x00ff);
		packet[ i++ ] = (byte) (count >> 8);
		packet[ i ] =   (byte) (count & 0x00ff);

		return packet;
	} 


	protected byte [] buildDataPacket( int function, int startAddr, int count, byte [] data ) 
	{
		//byte [] packet = new byte[data.length + 12];
		byte [] packet = buildRequestPacket( function, startAddr, count, data.length ); 
		
		System.arraycopy(data, 0, packet, 12, data.length);

		int data_legth = packet.length - 6; 
		packet[ 4 ] = (byte)(data_legth >> 8); 
		packet[ 5 ] = (byte)(data_legth & 0x00FF); 
		
		return packet;
	} 

	protected byte[] buildSetSinglePacket( int function, int addr, int value ) {
		int i;
		byte [] packet = new byte[ 12 ];

		for ( i = 0; i < 5; i++ ) 
			packet[ i ] = 0;

		packet[ i++ ] = 6;
		packet[ i++ ] = (byte)slave;
		packet[ i++ ] = (byte)function;
		packet[ i++ ] = (byte)(addr >> 8);
		packet[ i++ ] = (byte)(addr & 0x00FF);
		packet[ i++ ] = (byte)(value >> 8);
		packet[ i++ ] = (byte)(value & 0x00FF);
		return packet;
	}

	
	
	
	// -------------------------------------------
	// IO
	//-------------------------------------------
	
	/** 
	 * Receive response from the modbus slave. 
	 * This function blocks for timeout seconds if there is no reply.
	 *  
	 * @returns packet received 
	 * @throws ModBusIOException On communication problems 
	 */
	@Override
	protected byte[] receiveResponse() throws ModBusException {
		
		byte [] received_string = new byte[MAX_RESPONSE_LENGTH];
		int bytes_received = 0;
		int toRead = 0; // Will be filled later after reading TCP header
		
		try {
			// Too low timeout value leads to receive error
			socket.setSoTimeout(1000); 
		} catch (SocketException e1) {
			throw new ModBusIOException("Unable to setup socket timeout", e1);
		} // 1000 ms char interval timeout
		
		while( true ) { 

			// If we already know packet size - use it to finish read fast
			if( toRead > 0 && bytes_received >= toRead+6 )
				break;
			
			int c = 0;

			try {
				c = socket.getInputStream().read();
			} catch(SocketTimeoutException e)	{
				//break;
				throw new ModBusIOException("Socket read timeout", e);
				
			} catch (IOException e) {
				throw new ModBusIOException("Socket read problem", e);
			}

			if( c < 0 ) 
				throw new ModBusIOException("Socket reception error");
			
			if( bytes_received >= MAX_RESPONSE_LENGTH ) 
				throw new ModBusIOException("Socket reception buffer overflow");

			received_string[ bytes_received ++ ] = (byte)(c & 0xFF); 
			log.finest(String.format("<%02X>", c));			 

			if( bytes_received == 6 )
			{
				// We got TCP specific header, get packet size from it
				toRead = received_string[5] & 0x00FF;
				toRead |= 0xFF00 & (received_string[4] << 8);
				//log.log(Level.SEVERE,"MODBUS/TCPpkt len = "+toRead);
			}
		
		} 
		
		// skipping TCP-specific header
		bytes_received -= 6;
		
		byte [] out = new byte[bytes_received];
		System.arraycopy(received_string, 6, out, 0, bytes_received);

		return out; 
	}

	

	
	/** 
	 * send_query( file_descriptor, query_string, query_length ) 
	 * Function to send a query out to a modbus slave.
	 *  
	 * @throws ModBusIOException If communication fails. 
	 */ 

	protected void send_query( byte [] query, int size ) throws ModBusIOException
	{ 
		flushInput();
		
		try {
				socket.getOutputStream().write(query,0,size);	
		} catch (IOException e) {
			throw new ModBusIOException("Modbus IO Error", e);
		}
	}

	public void flushInput() {
		try {
			InputStream is = socket.getInputStream();
			while( is.available() > 0 )
				is.read();
		} catch (IOException e1) {
			// Ignore
		}
	}

	
	
	
	
	
	
	
	
	
	
	public static void main(String args[]) throws IOException, ModBusException, CommunicationsException 
	{
		String ipAddressStr = "192.168.10.230";
		InetAddress ipAddress = InetAddress.getByName(ipAddressStr);;

		ModBusTCP protocolDriver = new ModBusTCP();
		protocolDriver.startTcp(ipAddress);
		
		// For WAGO
		
		protocolDriver.checkRegister(0x2000, 0x0000);
		protocolDriver.checkRegister(0x2001, 0xFFFF); 
		
		protocolDriver.checkRegister(0x2003, 0xAAAA);
		protocolDriver.checkRegister(0x2004, 0x5555);
		protocolDriver.checkRegister(0x2005, 0x7FFF);
		protocolDriver.checkRegister(0x2006, 0x8000);
		protocolDriver.checkRegister(0x2007, 0x3FFF);
		protocolDriver.checkRegister(0x2008, 0x4000);
		
		
		for(int i = 0; i < 2; i++)
		{
			boolean [] boolIn = new boolean[1]; 
			try {
				protocolDriver.read_input_status( i, boolIn.length, boolIn );
			} catch (ModBusException e) {
				e.printStackTrace();
			}
			System.out.println("bools: "+boolIn[0]);
		}
	}


}
