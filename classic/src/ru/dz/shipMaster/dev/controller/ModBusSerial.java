package ru.dz.shipMaster.dev.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class ModBusSerial extends ModBusProtocolDirectTCP {

	private boolean asciiMode = false;

	// -------------------------------------------
	// Getters/setters
	//-------------------------------------------


	public boolean isAsciiMode() {		return asciiMode;	}
	public void setAsciiMode(boolean asciiMode) {		this.asciiMode = asciiMode;	}


	// -------------------------------------------
	// Packets
	//-------------------------------------------

	/** 
	 * The following functions construct the required query into a modbus query packet. 
	 * @return 
	 */

	protected byte[] buildRequestPacket( int function, int startAddr, int count ) 
	{
		byte [] packet = new byte [6];

		int i = 0;

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
		byte [] packet = new byte[data.length + 6];

		int i = 0;

		packet[ i++ ] = (byte) slave;
		packet[ i++ ] = (byte) function;
		packet[ i++ ] = (byte) (startAddr >> 8);
		packet[ i++ ] = (byte) (startAddr & 0x00ff);
		packet[ i++ ] = (byte) (count >> 8);
		packet[ i ] =   (byte) (count & 0x00ff);

		System.arraycopy(data, 0, packet, 6, data.length);

		return packet;
	} 

	protected byte[] buildSetSinglePacket( int function, int addr, int value ) {
		int i = 0;
		byte [] packet = new byte[ 6 ];

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
		if(asciiMode) return receiveResponseAscii();
		else return receiveResponseRTU();
	}

	private byte[] receiveResponseRTU() throws ModBusException {

		byte [] received_string = new byte[MAX_RESPONSE_LENGTH];
		int bytes_received = 0;

		try {
			socket.setSoTimeout(400);
		} catch (SocketException e1) {
			throw new ModBusIOException("Unable to setup socket timeout", e1);
		} // 10 ms char interval timeout

		while( true ) { 
			/* if no character at the buffer wait char_interval_timeout */ 
			/* before accepting end of response */ 

			int c = 0;

			try {
				c = socket.getInputStream().read();
			}
			catch(SocketTimeoutException e) {
				break;
			} catch (IOException e) {
				throw new ModBusIOException("Socket read problem", e);
			}

			try {
				socket.setSoTimeout(20);
			} catch (SocketException e1) {
				throw new ModBusIOException("Unable to setup socket timeout", e1);
			} // 10 ms char interval timeout
			
			if( c < 0 ) 
				throw new ModBusIOException("Socket reception error");

			received_string[ bytes_received ++ ] = (byte)(c & 0xFF); 
			log.finest(String.format("<%02X>", c));

			//if( (expectedResponceSizeBytes != 0) && (bytes_received >= expectedResponceSizeBytes-1) )
			//break;

			if( bytes_received >= MAX_RESPONSE_LENGTH ) 
				throw new ModBusIOException("Socket reception buffer overflow");
		} 

		int[] crc = ModbusBinaryTools.calculateCRC(received_string, 0, bytes_received - 2);
		int rcrc0 = 0xFF & (int)received_string[bytes_received-2];
		int rcrc1 = 0xFF & (int)received_string[bytes_received-1];
		if( 
				(rcrc1 != crc[1]) || 
				(rcrc0 != crc[0])
		)
		{
			System.out.println("ModBusSerial.receiveResponseRTU() wanted "+crc[0]+"-"+crc[1]+", got "+rcrc0+"-"+rcrc1);
			throw new ModBusException("crc error in "+BinaryTools.toDump("Pkt", received_string, bytes_received )+", expected "+BinaryTools.toDump("CRC", crc, 2 ));
		}
		byte [] out = new byte[bytes_received - 2];
		System.arraycopy(received_string, 0, out, 0, bytes_received - 2);

		return out; 
	}


	private byte[] receiveResponseAscii() throws ModBusException {
		byte [] received_string = new byte[MAX_RESPONSE_LENGTH*2];
		int bytes_received = 0;

		try {
			socket.setSoTimeout(150); 
		} catch (SocketException e1) {
			throw new ModBusIOException("Unable to setup socket timeout", e1);
		} // 100 ms char interval timeout

		while( true ) { 
			int c = 0;

			try {
				c = socket.getInputStream().read();
			}
			catch (IOException e) {
				throw new ModBusIOException("Socket read problem", e);
			}

			if( bytes_received == 0 && c != ':' )
				continue;

			// Next read will skip over the following '\n' when looking for ':'
			if( bytes_received > 0 && c == '\r' )
				break;

			if( c < 0 ) 
				throw new ModBusIOException("Socket reception error");

			if( bytes_received >= MAX_RESPONSE_LENGTH*2 ) 
				throw new ModBusIOException("Socket reception buffer overflow");

			received_string[ bytes_received++ ] = (byte)(c & 0xFF); 
			log.finest(String.format("<%02X>", c));		 

		} 

		// - 1 for leading :
		// - 1 for LCR 
		int sz = (( bytes_received - 1 ) / 2 ) - 1;
		byte [] out = new byte[sz];

		for( int pos = 0; pos < sz; pos++ )
		{
			out[pos] = BinaryTools.getHexPair(received_string[1+pos*2], received_string[2+pos*2]);
		}

		byte recv_lrc = BinaryTools.getHexPair(received_string[bytes_received-2], received_string[bytes_received-1]);		
		byte calc_lrc = ModbusBinaryTools.calculateLRC(out, sz );

		if(calc_lrc != recv_lrc )
			throw new ModBusException("lrc error");

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

			if(asciiMode)
			{
				OutputStream s = socket.getOutputStream();
				byte lrc = ModbusBinaryTools.calculateLRC(query, size);
				s.write(':');
				int pos = 0;
				while(size-- > 0)
				{
					BinaryTools.putHex(s,query[pos++]);
				}
				BinaryTools.putHex(s,lrc);
				s.write('\r');
				s.write('\n');
			}
			else
			{
				int[] crc = ModbusBinaryTools.calculateCRC(query, 0, size);
				//socket.getOutputStream().write(query,0,size);
				//socket.getOutputStream().write(crc[0]);
				//socket.getOutputStream().write(crc[1]);
				byte [] pkt = new byte[size+2];
				System.arraycopy(query, 0, pkt, 0, size);
				pkt[size] = (byte) crc[0];
				pkt[size+1] = (byte) crc[1];
				socket.getOutputStream().write(pkt,0,size+2);
				BinaryTools.sleepMsec(20); // RTU end of packet is some silence! :(
			}

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
}
