package ru.dz.shipMaster.dev.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import ru.dz.shipMaster.dev.controller.dz.DigitalZoneBinaryTools;
import ru.dz.shipMaster.misc.CrcExceprion;

public class DigitalZone128Packet extends DigitalZone128PacketType {
	    private static final Logger log = Logger.getLogger(DigitalZone128Packet.class.getName());

	    public static final int PACKET_SIZE = 8;
	    
	    //public static final int PACKET_START_BYTE = 0x11;
	    public static final int FROMHOST_PACKET_START_BYTE = 0x11;
	    public static final int TOHOST_PACKET_START_BYTE = 0x12;
	    
	    private byte[] payload = null;

	    static boolean isStartByte( byte b ) { return b == TOHOST_PACKET_START_BYTE; }
	    
	    /**
	     * Returns if CRC is correct.
	     * @param packet Packet to check CRC for.
	     * @throws CrcExceprion If CRC is wrong.
	     */
		static private void checkShortPacketCRC( byte[] packet ) throws CrcExceprion
		{
			int crc = DigitalZoneBinaryTools.crc16_calc( packet, PACKET_SIZE-2 );
			
			int myCrcHi = (crc >> 8) & 0xFF;
			int myCrcLo = crc & 0xFF;
			
			int hisCrcHi = ((int)packet[PACKET_SIZE-2]) & 0xFF;
			int hisCrcLo = ((int)packet[PACKET_SIZE-1]) & 0xFF;
			
			if( !((myCrcHi == hisCrcHi) && (myCrcLo == hisCrcLo)))
				throw new CrcExceprion(String.format("Got %02X:%02X, need %02X:%02X",hisCrcHi,hisCrcLo,myCrcHi,myCrcLo));

		}

		static boolean checkPacketExtensionCRC( byte[] packet, int dataLen )
		{
			int crc = DigitalZoneBinaryTools.crc16_calc( packet, dataLen );
			
			int myCrcHi = (crc >> 8) & 0xFF;
			int myCrcLo = crc & 0xFF;
			
			int hisCrcHi = ((int)packet[dataLen]) & 0xFF;
			int hisCrcLo = ((int)packet[dataLen+1]) & 0xFF;
			
			return (myCrcHi == hisCrcHi) && (myCrcLo == hisCrcLo);
		}
		



		private int node;
		private int type;
		private int port;
		private int value;

		
		public DigitalZone128Packet(byte[] pkt) throws CrcExceprion {
			
			checkShortPacketCRC(pkt);
			
			node = ((int)pkt[1]) & 0xFF;
			type = ((int)pkt[2]) & 0xFF;
			port = ((int)pkt[3]) & 0xFF;
			
			value = ((int)pkt[4]) & 0xFF;
			value |= (((int)pkt[5])<<8) & 0xFF00;

			// Process negatives
			if( (value & 0x8000) != 0 )
				value |= 0xFFFF0000;
			
		}
		
		/**
		 * Compatibility. Sends for node 0.
		 * @param type
		 * @param port
		 * @param value
		 */
		//@Deprecated
		public DigitalZone128Packet(int type, int port, int value) {
			this.node = 0;
			this.type = type;
			this.port = port;
			this.value = value;		
		}

		/**
		 * Construct packet for sending.
		 * @param node Destination node number.
		 * @param type Packet type.
		 * @param port Destination port number.
		 * @param value Value to send.
		 */
		public DigitalZone128Packet(int node, int type, int port, int value) {
			this.node = node;
			this.type = type;
			this.port = port;
			this.value = value;		
		}
		
		/**
		 * Create incoming extended packet. This one is for COM port IO.
		 * @param portFromShort Port number from 'short' part of packet.
		 * @param ext Bytes of the long packet extension.
		 */
		public DigitalZone128Packet(int portFromShort, byte[] ext) {
			/*
			this.value = portFromShort;		
			this.type = ext[0];
			this.port = ext[1];
			*/
			this.value = 0; // i'm not such a simple packet!
			this.port = ext[0]; // if some...
			this.type = portFromShort;
			byte [] pl = new byte[ext.length-2];
			System.arraycopy(ext, 0, pl, 0, ext.length-2);
			this.payload = pl; 
		}

		/**
		 * Create incoming extended packet. This one is for UDP IO.
		 * @param buf Bytes of the long packet.
		 * @param len size of packet received 
		 * @return Constructed DZ128 packet
		 */
		public static DigitalZone128Packet decodeLongUDP(byte[] buf, int len) {

			//GenericComPortIO.dump("Long UDP",buf);
			log.log(Level.FINE, "long type ="+buf[0]);
			
			DigitalZone128Packet out = new DigitalZone128Packet(0, buf[0], buf[1], 0);
			
			byte [] pl = new byte[len-1];
			System.arraycopy(buf, 1, pl, 0, len-1);
			out.payload = pl; 

			return out;
		}

		
		public int getNode() {		return node;	}
		public int getPort() {		return port;	}
		public int getType() {		return type;	}
		public int getValue() {		return value;	}
		public byte[] getPayload() { return payload; }

		/**
		 * Generate network packet representation.
		 * @return Packet data.
		 */
		public byte[] getPacket()
		{
			byte[] packet = new byte[PACKET_SIZE];
			
			packet[0] = FROMHOST_PACKET_START_BYTE;
			packet[1] = (byte)node;
			packet[2] = (byte)type;
			packet[3] = (byte)port;
			packet[4] = (byte)value; 
			packet[5] = (byte)(value>>8); 
			
			int crc = DigitalZoneBinaryTools.crc16_calc( packet, PACKET_SIZE-2 );
			
			packet[PACKET_SIZE-2] = (byte)((crc >> 8) & 0xFF);
			packet[PACKET_SIZE-1] = (byte)(crc & 0xFF);
			
			return packet;
		}



	}

	class DigitalZone128PacketType {
		
//		to me

		public static final int    TOHOST_PONG  			=  0x00;
		public static final int    TOHOST_ACK  				=  0x01;
		public static final int    TOHOST_NAK  				=  0x02;
		public static final int    TOHOST_SERIAL 			=  0x03;
		public static final int    TOHOST_DEVTYPE 			=  0x04;


		public static final int    TOHOST_LONG_PACKET		=	0x0F;
		
		public static final int    TOHOST_DIGITAL_VALUE     =  0x10;
		public static final int    TOHOST_ANALOG_VALUE      =  0x11;

		public static final int    TOHOST_DIGITAL_OUT_VALUE     =  0x12;
		public static final int    TOHOST_ANALOG_OUT_VALUE      =  0x13;
		public static final int    TOHOST_FREQ_VALUE       		=	0x14;
		public static final int    TOHOST_TEMPERATURE_VALUE 	= 0x15;

//		from me

		public static final int    FROMHOST_PING  			=  0x00;

		public static final int    FROMHOST_DIGITAL_REQUEST =  0x10;
		public static final int    FROMHOST_ANALOG_REQUEST  =  0x11;
		public static final int    FROMHOST_DIGITAL_OUT_REQUEST	=  0x12;
		public static final int    FROMHOST_ANALOG_OUT_REQUEST  =  0x13;
		public static final int    FROMHOST_FREQ_VALUE_REQUEST  = 0x14;
		public static final int    FROMHOST_TEMPERATURE_VALUE_REQUEST = 0x15;
		
		public static final int    FROMHOST_DIGITAL_OUT  	=  0x20;
		public static final int    FROMHOST_ANALOG_OUT  	=  0x21;

		public static final int    FROMHOST_DIGITAL_OUT_ENABLE  =  0x30;
		public static final int    FROMHOST_ANALOG_OUT_ENABLE  	=  0x31;

		public static final int    FROMHOST_DIGITAL_WARN_MASK  	=  0x40;
		public static final int    FROMHOST_ANALOG_WARN_LOW  	=  0x41;
		public static final int    FROMHOST_DIGITAL_WARN_VALUE  =  0x42;
		public static final int    FROMHOST_ANALOG_WARN_HIGH  	=  0x43;

		public static final int    FROMHOST_DIGITAL_CRIT_MASK  	=  0x40;
		public static final int    FROMHOST_ANALOG_CRIT_LOW  	=  0x41;
		public static final int    FROMHOST_DIGITAL_CRIT_VALUE  =  0x42;
		public static final int    FROMHOST_ANALOG_CRIT_HIGH  	=  0x43;

		
// From me, brodcasts
		
		// These 3 packets are used to tell node which node number it must use
		// Each packet contains partial serial number (corresponding 2 bytes in
		// value field) and desired node number in port field.

		public static final int    FROMHOST_BROADCAST_SETNODE_A = 0x81;
		public static final int    FROMHOST_BROADCAST_SETNODE_B = 0x82;
		public static final int    FROMHOST_BROADCAST_SETNODE_C = 0x83;

		// This broadcast asks all nodes that have no node number yet
		// to reply with their serial numbers (TOHOST_SERIAL packets).
		// Host shall repeat this request from time to time to discover
		// new devices.

		public static final int    FROMHOST_BROADCAST_REQUEST_NONSET_NODES = 0x83;
		
	}
