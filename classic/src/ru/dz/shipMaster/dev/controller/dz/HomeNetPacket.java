package ru.dz.shipMaster.dev.controller.dz;

import java.util.logging.Logger;

import ru.dz.shipMaster.data.misc.CommunicationsException;
import ru.dz.shipMaster.misc.CrcExceprion;

public class HomeNetPacket extends DigitalZoneHomeNetPacketType {
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(HomeNetPacket.class.getName());

	private byte fromNode;
	private byte toNode;
	private byte cmd;
	private byte group;
	private byte generation;
	private int value;

	private byte app = (byte)PKT_APP_LIGHTING;


	static boolean isStartByte( byte b ) { return b == PKT_START_BYTE; }

	/**
	 * Returns if CRC is correct.
	 * @param packet Packet to check CRC for.
	 * @throws CrcExceprion If CRC is wrong.
	 */
	static private void checkPacketCRC( byte[] packet ) throws CrcExceprion
	{
		int crc = DigitalZoneBinaryTools.crc16_calc( packet, PACKET_SIZE-2 );

		int myCrcHi = (crc >> 8) & 0xFF;
		int myCrcLo = crc & 0xFF;

		int hisCrcHi = ((int)packet[PACKET_SIZE-2]) & 0xFF;
		int hisCrcLo = ((int)packet[PACKET_SIZE-1]) & 0xFF;

		if( !((myCrcHi == hisCrcHi) && (myCrcLo == hisCrcLo)))
			throw new CrcExceprion(String.format("Got %02X:%02X, need %02X:%02X",hisCrcHi,hisCrcLo,myCrcHi,myCrcLo));

	}





	public HomeNetPacket(byte[] pkt) throws CrcExceprion, CommunicationsException {

		checkPacketCRC(pkt);

		fromNode = pkt[1];
		toNode = pkt[2];

		byte protoLevel = pkt[3];
		if( protoLevel != PKT_PROTO_LEVEL )
			throw new CommunicationsException("Protocol level is "+protoLevel);

		byte app = pkt[4];
		if( app != PKT_APP_LIGHTING ) 
			throw new CommunicationsException("Pkt app is not lighting but "+app);

		cmd = pkt[5];
		if( cmd != PKT_APP_L_SETGROUP && cmd != PKT_APP_L_GETGROUP )
			throw new CommunicationsException("Pkt cmd is not known, = "+cmd);

		group = pkt[6];
		generation = pkt[7]; 

		value = (((int)pkt[8])<<8) & 0xFF00;
		value |= ((int)pkt[9]) & 0xFF;

		// Process negatives
		//if( (value & 0x8000) != 0 )			value |= 0xFFFF0000;

	}


	/**
	 * Construct packet for sending.
	 * @param toNode Destination node number.
	 * @param cmd Packet command (type).
	 * @param group Destination lighting group number.
	 * @param value Value to send.
	 */
	public HomeNetPacket(byte fromNode, byte toNode, byte cmd, byte group, byte generation, int value) {
		this.fromNode = fromNode;
		this.toNode = toNode;
		this.cmd = cmd;
		this.group = group;
		this.generation = generation;
		this.value = value;		
	}




	public byte getFromNode() {		return fromNode;	}
	public byte getToNode() {		return toNode;	}
	public byte getGeneration() {		return generation;	}
	public byte getGroup() { return group; }
	public int getValue() {		return value;	}
	public byte getCmd() {			return cmd;		}



	/**
	 * Generate network packet representation.
	 * @return Packet data.
	 */
	public byte[] getPacket()
	{
		byte[] packet = new byte[PACKET_SIZE];

		packet[0] = PKT_START_BYTE;
		packet[1] = (byte)fromNode;
		packet[2] = (byte)toNode;
		packet[3] = (byte)PKT_PROTO_LEVEL;
		packet[4] = app ; 
		packet[5] = (byte)cmd;
		packet[6] = (byte)group;
		packet[7] = (byte)generation;
		packet[8] = (byte)((value >> 8) & 0xFF);
		packet[9] = (byte)(value & 0xFF);

		int crc = DigitalZoneBinaryTools.crc16_calc( packet, PACKET_SIZE-2 );

		packet[PACKET_SIZE-2] = (byte)((crc >> 8) & 0xFF);
		packet[PACKET_SIZE-1] = (byte)(crc & 0xFF);

		return packet;
	}

	// TODO replace cmd defines with enum
	
	@Override
	public String toString() {
		String cmds = "?";
		switch (cmd) {
		case PKT_APP_L_GETGROUP: cmds = "PKT_APP_L_GETGROUP"; break;
		case PKT_APP_L_SETGROUP: cmds = "PKT_APP_L_SETGROUP"; break;
		default: cmds = "??"; break;
		}
		return String.format("cmd %s grp %d gen %d val %d", cmds, group, generation, value);
	}

	public byte getApp() {
		return app;
	}

	public void setApp(byte app) {
		this.app = app;
	}

}

class DigitalZoneHomeNetPacketType {

	final static int PKT_START_BYTE 		 = 0x1B;
	final static int PKT_PROTO_LEVEL 		 = 0x01;
	
	
	
	final static byte PKT_APP_LIGHTING        = 0x38;

	final public static byte PKT_APP_L_SETGROUP      = 0x01;
	final public static byte PKT_APP_L_GETGROUP      = 0x02; // Request others to send setgroup with actual (nonincremented) generation

	
	final static byte PKT_APP_TEMPERATURE     = 0x39;
	
	final public static byte PKT_APP_T_SETTEMP       = 0x01;
	final public static byte PKT_APP_T_GETTEMP       = 0x02;

	
	
	
	final static int PKT_APP_ACK_MASK = 0x80;


	final static int PACKET_SIZE  = 12;


}
