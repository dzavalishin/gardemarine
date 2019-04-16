package ru.dz.shipMaster.dev.controller;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.RXTXPort;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.List;

import ru.dz.shipMaster.data.misc.CommunicationsException;


public class CBusProtocol {


	public static final char PACKET_START_CHAR = '\\';
	public static final char PACKET_END_CHAR = '\r';
	public static final int PACKET_MAX_LEN = 128;

	static public byte calculateChecksum(byte [] pkt, int length )
	{
		int sum = 0;

		for( int i = 0; i < length; i ++ )
		{
			//sum += ( (pkt[i]) & 0x00FF); // &FF to make sure we're unsigned
			sum += pkt[i];

			//System.out.print(String.format("CS add %02X sum %02X  ", pkt[i], sum));
		}

		//return (byte)( (((~sum) & 0xFF) + 1 ) & 0xFF );
		return (byte)( (-sum) & 0xFF );
	}

	static void putHex(ByteBuffer out, byte b)
	{
		String s = String.format("%02X", b );
		out.put((byte)s.charAt(0));
		out.put((byte)s.charAt(1));
	}

	static public byte[] createPacket( byte [] command )
	{
		ByteBuffer out = java.nio.ByteBuffer.allocate(256);

		byte cs = calculateChecksum(command, command.length);

		out.put((byte)'\\');

		for( int i = 0; i < command.length; i ++ )
		{
			putHex(out, command[i]);
		}

		putHex(out, cs);
		out.put((byte)'g');
		out.put((byte)'\r');

		byte[] ret = new byte[out.position()];
		System.arraycopy(out.array(), 0, ret, 0, out.position());

		return ret;
	}

	/* * decodes hex char or returns -1 * /
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

	public static int getHexPair(byte cu, byte cl)
	{
		int iu = getHexChar(cu);
		int il = getHexChar(cl);
		if(iu < 0 || il < 0) return -1;

		//iu <<= 4;		return (iu & 0x0F0) | (il & 0x0F);
		return ((iu & 0xF) << 4) | (il & 0xF);
	}
    */
	public static byte[] decodePacket(byte[] idata) throws CommunicationsException {
		byte [] outPacket = new byte[idata.length/2]; // roughly

		int pos = 0;
		int opos = 0;

		while(idata[pos] == (byte)'\r' || idata[pos] == (byte)'\n')
		{
			pos++;
			if(pos > idata.length)
				throw new CommunicationsException("CBus packet format error");
		}

		while(true)
		{
			if(idata[pos] == (byte)'\r' || idata[pos] == (byte)'\n')
				break;
			int v = BinaryTools.getHexPair(idata[pos], idata[pos+1]);
			if(v < 0) 
				throw new CommunicationsException(String.format("Not a hex in CBus packet: '%c' (0x%02X)",idata[pos],idata[pos]));
			pos += 2;
			outPacket[opos++] = (byte)v;
			//System.out.print(String.format("pkt add %02X  ", v));
		}

		byte cs = calculateChecksum(outPacket, opos);
		if( cs != 0)
			throw new CommunicationsException(String.format("Wrong CBus packet checksum: want %02X, got %02X ", 0, cs ));

		byte [] reta = new byte[opos-1];
		System.arraycopy(outPacket, 0, reta, 0, opos-1);

		return reta;
	}



	public static void dumpPacket(byte [] pk)
	{
		for(int i = 0; i < pk.length; i++)
		{
			//System.out.print(String.format("%02X", pk[i]));
			System.out.print(String.format("%c", pk[i]));
		}
		System.out.println();
	}



	public static void decodeCmds(List<CBusCommand> out, CBusStatusSink ss, byte[] idata) throws CommunicationsException {

		int pos = 0;

		byte[] data = decodePacket(idata);

		int messageType = 0xFF & data[pos++];
		int application;

		if(messageType > 0xC0)
		{
			int number = 2 * (messageType-0xC0);
			//int number = messageType-0xC0;
			application = data[pos++];
			int grp = 0xFF & data[pos++];

			int inByte = 0;
			byte val = 0;
			while(number-- > 0)
			{
				if(inByte == 0)
				{
					inByte = 4;
					val = data[pos++];
				}
				inByte--;
				switch( val & 0x3 )
				{
				case 0: ss.receiveStatus(application, grp++, CBusGroupState.DoesNotExist); break;
				case 1: ss.receiveStatus(application, grp++, CBusGroupState.IsON); break;
				case 2: ss.receiveStatus(application, grp++, CBusGroupState.IsOFF); break;
				default:
				case 3: ss.receiveStatus(application, grp++, CBusGroupState.IsError); break;
				}
				val >>= 2;
			}
			return;
		}

		int sourceAddress = data[pos++];
		application = data[pos++];

		if(data[pos] == 0)
			pos++;
		else
		{
			if(data[pos] != 1 && data[pos+1] != 0)
				throw new CommunicationsException("Unknown packet format");
			pos += 2;
		}

		while(pos < data.length)
		{
			int group;
			int command = data[pos++];

			if(command == 0x01 || command == 0x79 )
			{
				group = data[pos++];
				out.add(new CBusSimpleCommand(messageType,application,command,group));
				continue;
			}

			// ramp
			// 02, 0A, 12, 1A....72, 7A
			// cmd bits are 10000111, val bits are 01111000 
			if( (command & 0x87) == 0x02 )
			{
				group = data[pos++];
				double level = (data[pos++])/255.0;
				
				int timeSec = 0;
				switch( (command >> 3) & 0xF )
				{
				default:
				case 0: timeSec = 0; break;
				case 1: timeSec = 4; break;
				case 2: timeSec = 8; break;
				case 3: timeSec = 12; break;
				case 4: timeSec = 20; break;
				case 5: timeSec = 30; break;
				case 6: timeSec = 40; break;
				case 7: timeSec = 60; break;
				case 8: timeSec = 90; break;
				case 9: timeSec = 120; break;
				case 10: timeSec = 180; break;
				case 11: timeSec = 300; break;
				case 12: timeSec = 420; break;
				case 13: timeSec = 600; break;
				case 14: timeSec = 900; break;
				case 15: timeSec = 1020; break;				
				}
				
				out.add(new CBusRampCommand(sourceAddress, application, timeSec,group,level));
			}
			
		}

	}





	public static void main(String args[]) throws IOException, CommunicationsException, NoSuchPortException, PortInUseException, UnsupportedCommOperationException, InterruptedException 
	{
		//CBus cb = new CBus();

		byte [] test = {0x05, 0x38, 0x00, 0x79, (byte)0x88 };

		System.out.println( String.format( "CBus.main() csum = 0x%X",calculateChecksum(test,test.length)) );

		dumpPacket(createPacket(test) );

		dumpPacket(CBusCommand.onCommand(0).getPacket() );
		dumpPacket(CBusCommand.offCommand(0).getPacket() );


		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier("COM14");

		RXTXPort port = (RXTXPort)portIdentifier.open("CBus", 100);		

		port.setSerialPortParams( 9600, 
				SerialPort.DATABITS_8,
				SerialPort.STOPBITS_1,
				SerialPort.PARITY_NONE);


		/*if(false)
		{
			/*
			 * ComPortGnuBiPipe pipe = new ComPortGnuBiPipe("");

			pipe.write( CBusCommand.onCommand(0).getPacket() );
			synchronized(pipe) { pipe.wait(1000); }
			pipe.write( CBusCommand.offCommand(0).getPacket() );
			 * /
		}
		else
		*/
		{
			OutputStream stream = port.getOutputStream();

			System.out.println("CBus.main() on");
			stream.write(CBusCommand.onCommand(0).getPacket());
			synchronized(port) { port.wait(1200); }
			System.out.println("CBus.main() off");
			stream.write(CBusCommand.offCommand(0).getPacket());
		}

		port.close();

		/*
		cb.setIpAddressStr("192.168.10.200");
		cb.startDriver();
		cb.readWagoConfiguration();
		cb.stopDriver();
		 */
	}


}
