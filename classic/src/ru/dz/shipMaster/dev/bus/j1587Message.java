package ru.dz.shipMaster.dev.bus;

import java.util.List;

public class j1587Message {
	
	private int mid;
	private List<j1587Parameter> parameters;
	
	public void add( j1587Parameter p ) { parameters.add(p); }
	
	public int getMid() {		return mid;	}
	public void setMid(int mid) {		this.mid = mid;	}
	public List<j1587Parameter> getParameters() {		return parameters;	}
	public void setParameters(List<j1587Parameter> parameters) {		this.parameters = parameters;	} 
	
	void parse( byte [] packet )
	{
		parameters.clear();
		
		@SuppressWarnings("unused")
		byte cs = checkSum(packet, packet.length-1);
		
		int pos = 0;
		
		mid = packet[pos++];
		
		while(pos < packet.length)
		{
			j1587Parameter p = new j1587Parameter();
			
			pos += p.parse(packet, pos);
			
			add(p);
		}
	}

	/**
	 * 
	 * @param packet buffer to assemble to.
	 * @return packet size in bytes
	 */
	int assemble( byte [] packet )
	{
		int pos = 0;
		packet[pos++] = (byte)mid;
		
		for( j1587Parameter p : parameters )
		{
			pos += p.assemble(packet, pos);
		}

		int len = pos;
		
		packet[pos++] = checkSum(packet, len);
		
		return pos;
	}
	

	private byte checkSum(byte[] packet, int len) {
		
		int sum = 0;
		
		for(int i = 0; i < len; i++)
			sum += packet[i];
		
		sum &= 0xFF;
		
		return (byte)(0xFF & (-sum));
	}


}
