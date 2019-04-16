package ru.dz.shipMaster.dev.clock;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SntpRequest {


	private final InetAddress address;
	
	private NtpMessage msg;
	
	private double roundTripDelay;
	private double localClockOffset;
	private double destinationTimestamp;

	public SntpRequest(InetAddress address)
	{
		this.address = address;
		
	}
	
	public void request() throws IOException
	{
		// Send request
		DatagramSocket socket = new DatagramSocket();

		try { netIO(socket); }
		finally
		{
			socket.close();
		}
	}

	public double averagedRequest(int times) throws IOException
	{
		double [] answers = new double[times];
		// Send request
		DatagramSocket socket = new DatagramSocket();

		try 
		{ 
			for( int i = 0; i < times; i++)
			{
				netIO(socket);
				answers[i] = localClockOffset;
			}
			
			double result = 0;
			for( int i = 0; i < times; i++)
			{
				result += answers[i];
			}
			
			result /= times;
			return result;
		}
		finally
		{
			socket.close();
		}
	}
	
	private void netIO(DatagramSocket socket) throws IOException {

		byte[] buf = new NtpMessage().toByteArray();
		DatagramPacket packet =
			new DatagramPacket(buf, buf.length, address, 123);
		// Set the transmit timestamp *just* before sending the packet
		// ToDo: Does this actually improve performance or not?
		NtpMessage.encodeTimestamp(packet.getData(), 40,
				(System.currentTimeMillis()/1000.0) + 2208988800.0);

		socket.send(packet);


		// Get response
		//System.out.println("NTP request sent, waiting for response...\n");
		packet = new DatagramPacket(buf, buf.length);
		socket.receive(packet);

		// Immediately record the incoming timestamp
		destinationTimestamp = (System.currentTimeMillis()/1000.0) + 2208988800.0;


		// Process response
		msg = new NtpMessage(packet.getData());

		// Corrected, according to RFC2030 errata
		roundTripDelay = (destinationTimestamp-msg.originateTimestamp) -
		(msg.transmitTimestamp-msg.receiveTimestamp);

		localClockOffset =
			((msg.receiveTimestamp - msg.originateTimestamp) +
					(msg.transmitTimestamp - destinationTimestamp)) / 2;
	}

	public double getLocalClockOffset() {		return localClockOffset;	}
	public double getRoundTripDelay() {		return roundTripDelay;	}
	public double getDestinationTimestamp() {		return destinationTimestamp;	}

	public NtpMessage getMsg() {		return msg;	}


}
