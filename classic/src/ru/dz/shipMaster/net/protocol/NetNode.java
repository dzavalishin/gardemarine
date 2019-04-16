package ru.dz.shipMaster.net.protocol;

import java.net.InetAddress;

public class NetNode {

	public NetNode(NodeHash hash, InetAddress address, String name) {
		this.hash = hash;
		this.address = address;
		this.name = name;		
	}
	
	private InetAddress		address;
	private NodeHash		hash;
	private String 			name;
	private long			lastUpdateTime = -1; 
	
	public InetAddress getAddress() {		return address;	}
	public NodeHash getHash() {		return hash;	}
	
	public String getName() { return name; }	
	public void setName(String name) {		this.name = name;	}
	
	/**
	 * Remember that this node just send some packet.
	 */
	public void markPacketReceptionTime() { lastUpdateTime = System.currentTimeMillis(); }
	
	/**
	 * Find out if we had packets from this node last N seconds.
	 * @param nSeconds N
	 * @return True if we had some input within these seconds.
	 */
	public boolean HadPacketWithinLast(int nSeconds )
	{
		if( lastUpdateTime <0 )
			return false;
		
		return lastUpdateTime >= (System.currentTimeMillis() - (nSeconds*1000));
	}
}
