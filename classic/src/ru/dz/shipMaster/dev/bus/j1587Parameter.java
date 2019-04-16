package ru.dz.shipMaster.dev.bus;

public class j1587Parameter {
	private int pid;
	private int value;
	private byte[] data;
	
	/** multibyte data */
	public byte[] getData() {		return data;	}
	public void setData(byte[] data) {		this.data = data;	}
	
	public int getPid() {		return pid;	}
	public void setPid(int pid) {		this.pid = pid;	}
	
	public int getValue() {		return value;	}
	public void setValue(int value) {		this.value = value;	}	

	/**
	 * @param startPos start position in buffer
	 * @param packet buffer to parse to.
	 * @return packet part size in bytes
	 */
	int parse( byte [] packet, final int startPos )
	{
		int pos = startPos;
		
		/*
		byte pid0 = packet[startPos++];
		byte pid1 = 0;
		
		if(pid0 == 255)
			pid1 = packet[startPos++];
		*/
		
		pid = packet[pos++];
		if(pid == 255)
			pid = 256+packet[pos++];
		
		//int len = 1;

		if(isLenVar(pid))
		{
			int len = packet[pos++];
			data = new byte[len];
			System.arraycopy(packet, pos, data, 0, len);
			pos += len;
		}
		else
		{
			value = packet[pos++];
			if(isLen2(pid))
			{
				//len = 2;
				value |= (packet[pos++]) << 8;
			}
		}
		
		return pos - startPos;
	}

	/**
	 * @param startPos start position in buffer
	 * @param packet buffer to assemble to.
	 * @return packet part size in bytes
	 */
	int assemble( byte [] packet, final int startPos )
	{
		int pos = startPos;
		
		if( pid >= 256 )
		{
			packet[pos++] = (byte)255;
			packet[pos++] = (byte)(pid - 256);
		}
		
		if(isLenVar(pid))
		{
			int len = data.length;
			packet[pos++] = (byte)len;

			System.arraycopy(data, 0, packet, pos, len);
			pos += len;
		}
		else
		{
			value = packet[pos++];
			if(isLen2(pid))
			{
				//len = 2;
				value |= (packet[pos++]) << 8;
			}
		}
		
		
		return pos - startPos;
	}
	
	
	private boolean isLen2( int pid )
	{
		if( pid >= 128 && pid <= 191 )
			return true;
		if( pid >= 384 && pid <= 447 )
			return true;
		return false;
	}

	private boolean isLenVar( int pid )
	{
		if( pid >= 192 && pid <= 253 )
			return true;
		if( pid >= 448 && pid <= 509 )
			return true;
		return false;
	}
	
}
