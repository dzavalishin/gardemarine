package ru.dz.shipMaster.net.protocol;

/**
 * Kind is one of two:
 * 
 * <li>Request - "I want you to reply with that type of packet"  
 * <li>Broadcast - "Here is my data for all" (possibly a reply to the request)  
 * 
 * @author dz
 *
 */

public class PacketKind {
	private byte	kind;

	public byte getKindByte() { return kind; }
	public char getKind() { return (char)kind; }
	
	private PacketKind(char b) { kind = (byte)b; }
	
	public static final PacketKind REQUEST = new PacketKind('R'); 
	public static final PacketKind BROADCAST = new PacketKind('B'); 
	
	public static PacketKind kindByByte(byte b) throws NetException
	{
		switch((char)b)
		{
		case 'R': return REQUEST;
		case 'B': return BROADCAST;
			
		default:
			throw new NetException("Unknown packet kind: "+((char)b));
		}
	}
	
	@Override
	public String toString() {
		return "Kind "+((char)kind);
	}
	
	
}
