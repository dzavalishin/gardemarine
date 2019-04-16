package ru.dz.shipMaster.dev.bus;

import ru.dz.shipMaster.data.misc.CommunicationsException;
import ru.dz.shipMaster.dev.common.BiPipe;
import ru.dz.shipMaster.dev.controller.BinaryTools;
import ru.dz.shipMaster.dev.controller.ModBusException;
import ru.dz.shipMaster.dev.controller.ModBusIOException;
import ru.dz.shipMaster.dev.controller.ModBusProtocol;
import ru.dz.shipMaster.dev.controller.ModbusBinaryTools;

public class ModBusProtocolAdapter extends ModBusProtocol {

	private static final int RTU_READ_TIMEOUT_MSEC = 40;
	private static final int ASCII_READ_TIMEOUT_MSEC = 150;
	private static final int TCP_READ_TIMEOUT_MSEC = 10;;
	
	public enum Mode { Ascii, RTU, TCP }
	private Mode mode = Mode.Ascii;

	private BiPipe pipe;

	public void setPipe(BiPipe pipe) {		this.pipe = pipe;	}


	public Mode getMode() {		return mode;	}
	public void setMode(Mode mode) {		this.mode = mode;	}


	@Override
	protected byte[] buildDataPacket(int function, int startAddr, int count, byte[] data) {
		if( mode != Mode.TCP )
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
		else
		{
			byte [] packet = buildRequestPacket( function, startAddr, count, data.length ); 

			System.arraycopy(data, 0, packet, 12, data.length);

			int data_legth = packet.length - 6; 
			packet[ 4 ] = (byte)(data_legth >> 8); 
			packet[ 5 ] = (byte)(data_legth & 0x00FF); 

			return packet;
		}
	}


	@Override
	protected byte[] buildRequestPacket( int function, int startAddr, int count ) 
	{
		assert( mode != Mode.TCP );
		return buildRequestPacket( function, startAddr, count, 0 );
	}

	protected byte[] buildRequestPacket(int function, int startAddr, int count, int addsize) {
		if( mode != Mode.TCP )
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
		else
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
	}

	@Override
	protected byte[] buildSetSinglePacket(int function, int addr, int value) {
		if( mode != Mode.TCP )
		{			
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
		else
		{
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
	}

	@Override
	protected byte[] receiveResponse() throws ModBusException {
		switch(mode)
		{
		case Ascii:
			return receiveResponseAscii();

		case RTU:
			return receiveResponseRTU();

		case TCP:
			return receiveResponseTCP();

		default:
			throw new ModBusException("Unknown ModBus mode");
		}
	}

	private byte[] receiveResponseRTU() throws ModBusException {

		byte [] received_string = new byte[MAX_RESPONSE_LENGTH];
		int bytes_received = 0;

		while( true ) { 
			/* if no character at the buffer wait char_interval_timeout */ 
			/* before accepting end of response */ 

			int c = 0;

			//c = socket.getInputStream().read();
			c = pipe.readTimed(RTU_READ_TIMEOUT_MSEC);
			if( c < 0 ) 
				break;

			received_string[ bytes_received ++ ] = (byte)(c & 0xFF); 
			log.finest(String.format("<%02X>", c));

			if( bytes_received >= MAX_RESPONSE_LENGTH ) 
				throw new ModBusIOException("Socket reception buffer overflow");
		} 

		int[] crc = ModbusBinaryTools.calculateCRC(received_string, 0, bytes_received - 2);
		int recv0crc = 0xFF&(int)(received_string[bytes_received-1]);
		int recv1crc = 0xFF&(int)(received_string[bytes_received-2]);
		
		if( recv0crc != (0xFF&(int)(crc[1])) || recv1crc != (0xFF&(int)(crc[0])))
			throw new ModBusException("crc error");

		byte [] out = new byte[bytes_received - 2];
		System.arraycopy(received_string, 0, out, 0, bytes_received - 2);

		return out; 
	}


	private byte[] receiveResponseAscii() throws ModBusException {
		byte [] received_string = new byte[MAX_RESPONSE_LENGTH*2];
		int bytes_received = 0;


		while( true ) { 
			int c = 0;

			c = pipe.readTimed(ASCII_READ_TIMEOUT_MSEC);
			if( c < 0 ) 
				break;

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
	 * Receive response from the modbus slave. 
	 * This function blocks for timeout seconds if there is no reply.
	 *  
	 * @returns packet received 
	 * @throws ModBusIOException On communication problems 
	 */
	protected byte[] receiveResponseTCP() throws ModBusException {

		byte [] received_string = new byte[MAX_RESPONSE_LENGTH];
		int bytes_received = 0;
		int toRead = 0; // Will be filled later after reading TCP header

		while( true ) { 

			// If we already know packet size - use it to finish read fast
			if( toRead > 0 && bytes_received >= toRead )
				break;

			int c = 0;

			c = pipe.readTimed(TCP_READ_TIMEOUT_MSEC);
			if( c < 0 ) 
				throw new ModBusIOException("Socket reception error");

			if( bytes_received >= MAX_RESPONSE_LENGTH ) 
				throw new ModBusIOException("Socket reception buffer overflow");

			received_string[ bytes_received ++ ] = (byte)(c & 0xFF); 
			log.finest(String.format("<%02X>", c));			 

			if( bytes_received == 6 )
			{
				// We got TCP specific header, get packet size from it
				toRead = received_string[5] & 0xFF;
				toRead |= 0xFF00 & (received_string[4] << 8);
			}

		} 
		// skipping the leading 6 bytes
		bytes_received -= 6;

		byte [] out = new byte[bytes_received];
		System.arraycopy(received_string, 5, out, 0, bytes_received);

		return out; 
	}









	@Override
	protected void send_query(byte[] query, int size) throws ModBusIOException {
		switch(mode)
		{
		case Ascii:
			try {
				sendQueryAscii(query, size);
			} catch (CommunicationsException e) {
				throw new ModBusIOException("Communications error", e);
			}
			break;

		case RTU:
			sendQueryRTU(query, size);
			break;

		case TCP:
			sendQueryTCP(query, size);
			break;

		default:
			throw new ModBusIOException("Unknown ModBus mode");
		}
	}


	private void sendQueryTCP(byte[] query, int size) throws ModBusIOException {
		try {
			pipe.write(query,size);
		} catch (CommunicationsException e) {
			throw new ModBusIOException("Modbus IO Error", e);
		}
	}


	private void sendQueryRTU(byte[] query, int size) throws ModBusIOException {
		int[] crc = ModbusBinaryTools.calculateCRC(query, 0, size);
		try {
			
			/*
			pipe.write(query,size);
			pipe.write((byte)crc[0]);
			pipe.write((byte)crc[1]);
			*/
			byte [] pkt = new byte[size+2];
			System.arraycopy(query, 0, pkt, 0, size);
			pkt[size] = (byte)crc[0];
			pkt[size+1] = (byte)crc[1];
			pipe.write(pkt,size+2);
			
			
		} catch (CommunicationsException e) {
			throw new ModBusIOException("Modbus IO Error", e);
		}
		BinaryTools.sleepMsec(20); // RTU end of packet is some silence! :(
	}


	private void sendQueryAscii(byte[] query, int size) throws CommunicationsException {
		byte lrc = ModbusBinaryTools.calculateLRC(query, size);
		pipe.write((byte)':');
		int pos = 0;
		while(size-- > 0)
		{
			BinaryTools.putHex(pipe,query[pos++]);
		}
		BinaryTools.putHex(pipe,lrc);
		pipe.write((byte)'\r');
		pipe.write((byte)'\n');
	}









}
