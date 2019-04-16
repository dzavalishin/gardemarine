package ru.dz.shipMaster.dev.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.dz.shipMaster.data.misc.CommunicationsException;
import ru.dz.shipMaster.dev.common.BiPipe;
import ru.dz.shipMaster.dev.common.TcpBiPipe;

public class DconProtocol {
	protected static final Logger log = Logger.getLogger(DconProtocol.class.getName());
	private static final int MAX_REPLY = 10240;
	private final BiPipe pipe;
	private final int unitNo;
	 
	
	public DconProtocol(BiPipe pipe, int unitNo) {
		this.pipe = pipe;
		this.unitNo = unitNo;
	}
	
	public double[] cmdGetAllInputs(int max) throws CommunicationsException
	{
		sendPacket('#', unitNo , "");
		String reply = getPacket();
		String orig = reply; // for exception
		
		double [] out = new double[max];
		int pos = 0;
		while( reply.length() > 0 )
		{
			if( pos >= max )
				break;
			
			int endPos = reply.indexOf('+', 1);
			if( endPos < 0)
				endPos = reply.indexOf('-', 1);
			
			String part;
			if( endPos < 0)
			{
				part = reply;
				reply = "";
			}
			else
			{
				part = reply.substring(0, endPos);
				reply = reply.substring(endPos);
			}
			
			try {  out[pos++] = Double.parseDouble(part);  } 
			catch(NumberFormatException e)
			{
				throw new CommunicationsException("Invalid reply: '"+orig+"'", e);
			}
			
			log.log(Level.SEVERE, String.format("port %d = %f", pos-1, out[pos-1]) );
		}
		
		return out;
	}
	
	// -------------------------------------------
	// IO
	//-------------------------------------------
	
	
	void sendPacket(char lead, int unitNo, String cmd ) throws CommunicationsException 
	{
		try {
			pipe.write( buildPacket(lead, unitNo, cmd) );
		} catch (UnsupportedEncodingException e) {
			throw new CommunicationsException( "Unable to convert packet "+cmd, e);			
		} 
	}
	
	static byte[] buildPacket(char lead, int unitNo, String cmd ) throws UnsupportedEncodingException
	{
		byte[] hex = new byte[2];
		BinaryTools.toHex(hex, (byte)unitNo);
		
		byte[] bytes = cmd.getBytes("latin1");
		
		int len = 1 + 2 + bytes.length + 2 + 1 + 1;
		
		byte[] out = new byte[len];
		
		out[0] = (byte)lead;
		out[1] = hex[0];
		out[2] = hex[1];
		
		System.arraycopy(bytes, 0, out, 3, bytes.length);
		
		byte cs = 0;
		for( int i = 0; i < bytes.length+3; i++)			cs += out[i];
		//cs = DconCRC.Calculate_DOW_CRC(out,bytes.length+3);

		BinaryTools.toHex(hex, cs);
		
		out[bytes.length + 3] = hex[0];
		out[bytes.length + 4] = hex[1];

		out[bytes.length + 5] = 0x0d;
		out[bytes.length + 6] = 0;
		
		
		log.log( Level.SEVERE, "dcon pkt out "+new String(out,"latin1"));
		BinaryTools.dump("dcon send", out);
		
		return out;
	}

	
	private String getPacket() throws CommunicationsException {
		StringBuilder sb = new StringBuilder();
		int nchars = 0;
		while( true ) 
		{
			int ch = pipe.readTimed(250);
			log.log(Level.FINEST,"get ch "+ch);
			
			if( ch < 0 )
				throw new CommunicationsException("DCON timeout");

			if( nchars == 0 && !( ch == '!' || ch == '?'|| ch == '>' ))
				continue;
			
			if( ch == 0x0D ) //|| ch == 0x0A )
				break;
			
			sb.append((char)ch); 
			nchars++;
			
			if( nchars > MAX_REPLY )
				throw new CommunicationsException("Reply is too long");
		}
		
		log.log(Level.SEVERE, "dcon recv: " + sb );
		
		String css = sb.substring(0, sb.length()-2 );
		byte cs = 0;
		for( int cspos = css.length()-1; cspos >= 0; cspos-- )			
			cs += css.charAt(cspos);
		/*try {
			cs = DconCRC.Calculate_DOW_CRC(css.getBytes("latin1"), css.length());
		} catch (UnsupportedEncodingException e) {
			throw new CommunicationsException("Checksum calc error in '"+sb+"'", e );			
		}*/

		byte[] out = new byte[2];
		BinaryTools.toHex(out, cs);
		
		//if( sb.charAt(sb.length()-2) != out[0] || sb.charAt(sb.length()-1) != out[1] )
			//throw new CommunicationsException("Checksum error in '"+sb+"', calculated cs is "+Integer.toHexString(cs) );
		
		if( sb.charAt(0) == '?' )
			throw new CommunicationsException("Controller refused: "+sb);
		
		// Return packet without leading char and chesksum
		return sb.substring(1, sb.length()-2 );
	}


	
	// -----------------------------------------------------------
	// test main 
	// -----------------------------------------------------------
	
	public static void main(String args[]) throws IOException, ModBusException, CommunicationsException 
	{
		
		InetAddress address = InetAddress.getByName("192.168.1.103");
		TcpBiPipe tcpBiPipe = new TcpBiPipe( address, 13033 );
		DconProtocol w = new DconProtocol(tcpBiPipe,16);
		
		tcpBiPipe.connect();
		
		for(int i = 0; i < 10; i++ )
		{
			try {
				w.cmdGetAllInputs(8);
				BinaryTools.sleepMsec(200);

			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		
		tcpBiPipe.disconnect();
	}
	
}
