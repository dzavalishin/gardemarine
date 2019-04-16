package ru.dz.shipMaster.net.protocol;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Defines packet format. Type 0 packet is:<br>
 * <br>
 * <li>2 bytes		protocol version id: 00 (integer value)
 * <li>4 bytes		source node hash - CRC16 in hex
 * <li>1 byte		packet kind - request or reply/broadcast
 * <li>4 bytes		packet type, ascii string
 * <li>4 bytes		number of payload items, integer, right-justified
 * <li>rest			items
 * <br>
 * 
 * Item is:<br>
 * <li>4 bytes		item length, total 
 * <li>4 bytes		item type (name!), ascii string
 * <li>4 bytes		item data type (name!), ascii string
 * <li>rest			item payload
 * 
 * @author dz
 *
 */

public class NetPacket {
    @SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(NetPacket.class.getName()); 

	private int protocolVersion = -1;
	private PacketKind kind = null;
	private PacketType type = null;
	private int itemCount;
	private Map<String,PacketItem> items = new HashMap<String,PacketItem>();
	private int srcHash = -1;
	private InetAddress srcAddress;

	private NodeHash nodeHash = null;

	public NetPacket(PacketKind kind, PacketType type )
	{
		this.kind = kind;
		this.type = type;
	}

	public NetPacket(DatagramPacket dgPacket) throws NetException
	{
		byte[] rawData = dgPacket.getData();
		srcAddress = dgPacket.getAddress();
		try {
			protocolVersion = extractInt(rawData, 0, 2, 10);
			if( protocolVersion > ProtocolDefs.VERSION )
				throw new NetException("Unknown protocol version: "+protocolVersion);
			
			//$ANALYSIS-IGNORE,codereview.java.rules.switches.RuleSwitchBranch
			switch (protocolVersion) {
			case 0:
			  parseVersion0Packet(rawData);
			break;
			default :
			throw new NetException("Unknown protocol version: " + protocolVersion);
			}
			
			
		} catch (UnsupportedEncodingException e) {
			throw new NetException("Can't decode packet",e);
		}
	}
	

	private void parseVersion0Packet(byte[] rawData) throws UnsupportedEncodingException, NetException {
		srcHash  = extractInt(rawData, 2, 4, 16);
		kind = PacketKind.kindByByte(rawData[6]);
		String typeName = extractString(rawData,7,4);
		type = PacketType.valueOf(typeName);
		itemCount = extractInt(rawData, 11, 4, 10);
		
		int rawPos = 15;
		for( int i = 0; i < itemCount; i++ )
		{
			int itemLen = extractInt(rawData, rawPos, 4, 10);
			String itemName = extractString(rawData,rawPos+4,4).toLowerCase();
			String itemDataTypeName = extractString(rawData,rawPos+4,4);
			PacketItem item = new PacketItem( itemName, itemDataTypeName, rawData, rawPos+12, itemLen-12 );
			rawPos += itemLen;
			
			items.put(itemName, item);
		}
		// TODO NetSigner - extract and check signature

	}

	// Packet assembly
	
	/*void send(NetIO io) throws NetException
	{
		byte[] me;
		try {
			me = assemble();
		} catch (UnsupportedEncodingException e) {
			throw new NetException("Encoding problem",e);
		}
		try {
			io.sendPacket(me);
		} catch (IOException e) {
			throw new NetIOException("Net IO problem",e);
		}
	}*/

	byte[] assemble() throws UnsupportedEncodingException, NetException {
		byte[] header = new byte[15]; 
		 // 2 bytes		protocol version id: 00 (integer value)
		storeInt( ProtocolDefs.VERSION, header, 0, 2, 10);
		int nodeHash = NodeHash.getMyNodeHash().getHash();
		// 4 bytes		source node hash - CRC16 in hex
		storeInt( nodeHash, header, 2, 4, 16);
		 // 1 byte		packet kind - request or reply/broadcast
		header[6] = kind.getKindByte();
		 // 4 bytes		packet type, ascii string
		storeString(type.toString(), header, 7, 4);
		 // 4 bytes		number of payload items, integer, right-justified
		storeInt( items.size(), header, 11, 4, 10);
		
		int totalSize = 15; 
		
		Set<byte[]> itemArrays = new HashSet<byte[]>();
		
		for(PacketItem item : items.values() )
		{
			int itemSize = 12;
//			 TO DO impl -- what??
			byte[] iBytes = item.assemble();
			itemSize += iBytes.length;
			totalSize += itemSize;
			
			byte[] iAll = new byte[itemSize];
			System.arraycopy(iBytes, 0, iAll, 12, iBytes.length);
			
			// <li>4 bytes		item length, total 
			storeInt( itemSize, iAll, 0, 4, 10);
			
			// <li>4 bytes		item type (name!), ascii string
			storeString(item.getName(), iAll, 4, 4);
			
			// <li>4 bytes		item data type (name!), ascii string
			storeString(item.getTypeName(), iAll, 8, 4);

			itemArrays.add(iAll);
		}
		
		// TODO NetSigner - add signature

		byte[] packetBytes = new byte[totalSize];
		System.arraycopy(header, 0, packetBytes, 0, header.length);
		
		int opos = 15;
		for( byte[] iBytes : itemArrays )
		{
			System.arraycopy(iBytes,0,packetBytes, opos, iBytes.length);
			opos += iBytes.length;
		}
		
		return packetBytes;
	}

	// Items

	public double getNumericItem(String name)
	{
		PacketItem item = items.get(name.toLowerCase());
		return item.getNumericValue();
	}
	
	public String getStringItem(String name)
	{
		PacketItem item = items.get(name.toLowerCase());
		if(item == null)
			return null;
		return item.getStringValue();
	}

	public void setItem(String name, String value)
	{
		items.put(name.toLowerCase(),new PacketItem(name,value));
	}

	public void setItem(String name, double value)
	{
		items.put(name.toLowerCase(),new PacketItem(name,value));
	}
	
	// Helpers
	
	public static int extractInt(byte[] rawData, int offset, int length, int radix) throws UnsupportedEncodingException {
		String in = new String(rawData,offset,length,"ascii");
		return Integer.parseInt(in,radix);
	}

	public static String extractString(byte[] rawData, int offset, int length) throws UnsupportedEncodingException {
		return new String(rawData,offset,length,"ascii");
	}

	public static void storeString(String val, byte[] rawData, int offset, int length) throws UnsupportedEncodingException, NetException  {
		while( val.length() < length )
			val = " "+val;
		
		byte[] bytes = val.getBytes("ascii");
		if(bytes.length != length)
			throw new NetException("Unable to store string, length mismatch");
		System.arraycopy(bytes, 0, rawData, offset, length);
	}

	public static void storeInt(int value, byte[] rawData, int offset, int length, int radix) throws UnsupportedEncodingException, NetException  {
		String val = Integer.toString(value, radix);
		while( val.length() < length )
			val = "0"+val;
		
		byte[] bytes = val.getBytes("ascii");
		if(bytes.length != length)
			throw new NetException("Unable to store int, length mismatch");
		System.arraycopy(bytes, 0, rawData, offset, length);
	}
	
	// Getters
	
	public PacketKind getKind() {		return kind;	}
	public int getProtocolVersion() {		return protocolVersion;	}
	public PacketType getType() {		return type;	}

	public int getSrcHash() {		return srcHash;	}
	public InetAddress getSrcAddress() {		return srcAddress;	}

	// Dump
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("Packet from ");
		sb.append(srcAddress);
		sb.append(" of kind ");
		sb.append(getKind().getKind());
		sb.append(" type=");
		sb.append(getType());
		sb.append(" ");
		for( PacketItem item : items.values() )
		{
			sb.append(item.toString());
			sb.append(" ");
		}
		
		return sb.toString();		
	}

	/*public void getSrcNodeName() {
		
		
	}*/

	
	public NodeHash getSrcNodeHash() {
		if(nodeHash == null)
			nodeHash = new NodeHash(getSrcHash(),getSrcAddress());
		return nodeHash;
	}


}
