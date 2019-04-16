package ru.dz.shipMaster.dev.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

import ru.dz.shipMaster.data.misc.CommunicationsException;
import ru.dz.shipMaster.dev.common.BiPipe;

public class BinaryTools {
	protected static final Logger log = Logger.getLogger(BinaryTools.class.getName()); 

	/**
	 * 
	 * @param out byte[2], result. out[0] is high nibble.
	 * @param b byte to convert to hex.
	 */
	static void toHex( byte[] out, byte b)
	{
		String s = String.format("%02X", b );
		out[0] = (byte)s.charAt(0);
		out[1] = (byte)s.charAt(1);
	}


	public static void putHex(OutputStream stream, byte b) throws IOException {
		String s = String.format("%02X", b );
		stream.write((byte)s.charAt(0));
		stream.write((byte)s.charAt(1));
		//log.log(Level.FINEST," "+s);
	}

	public static void putHex(BiPipe pipe, byte b) throws CommunicationsException {
		String s = String.format("%02X", b );
		pipe.write((byte)s.charAt(0));
		pipe.write((byte)s.charAt(1));
	}


	/** decodes hex char or returns -1 */
	public static int getHexChar(byte c)
	{
		if( c >= (byte)'0' && c <= (byte)'9')
			return (byte)(c - (byte)'0');
		if( c >= (byte)'a' && c <= (byte)'f')
			return (byte)(10 + c - (byte)'a');
		if( c >= (byte)'A' && c <= (byte)'F')
			return (byte)(10 + c - (byte)'A');

		return -1;
	}

	public static byte getHexPair(byte cu, byte cl)
	{
		int iu = getHexChar(cu);
		int il = getHexChar(cl);
		if(iu < 0 || il < 0) return -1;

		//iu <<= 4;		return (iu & 0x0F0) | (il & 0x0F);
		return (byte) ( ((iu & 0xF) << 4) | (il & 0xF));
	}


	private static Object sleepo = new Object();
	public static void sleepMsec(int time) {
		synchronized (sleepo) {
			try {
				sleepo.wait(time);
			} catch (InterruptedException e) {
				// Ignore...
			}
		}
	}

	
	public static void dump(String prefix, byte[] pkt) {
		System.out.print(prefix+": 0x.. ");
		for(int i = 0; i < pkt.length; i++ )
		{
			System.out.print(String.format("%02X", 0xFF&(int)pkt[i]));
			System.out.print(" ");
		}
		System.out.println();
		
	}

	public static String toDump(String prefix, byte[] pkt, int len) {
		StringBuilder out = new StringBuilder();
		out.append(prefix+": 0x.. ");
		for(int i = 0; i < pkt.length; i++ )
		{
			if(i >= len)				break;
			out.append(String.format("%02X", 0xFF&(int)pkt[i]));
			out.append(" ");
		}
	
		return out.toString();
	}

	public static String toDump(String prefix, int[] pkt, int len) {
		StringBuilder out = new StringBuilder();
		out.append(prefix+": 0x.. ");
		for(int i = 0; i < pkt.length; i++ )
		{
			if(i >= len)				break;
			out.append(String.format("%02X", 0xFF&(int)pkt[i]));
			out.append(" ");
		}
	
		return out.toString();
	}
	
}









