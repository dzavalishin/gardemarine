package ru.dz.shipMaster.net.protocol;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NodeHash {
	private static final Logger log = Logger.getLogger(NodeHash.class.getName());
	
	private int		hash;
	InetAddress		address;

	public NodeHash(int hash, InetAddress address) 
	{
		this.hash = hash;
		this.address = address;		
	}
	
	public int getHash() {		return hash;	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NodeHash) {
			NodeHash him = (NodeHash) obj;
			return (hash == him.hash) && (address.equals(him.address));
		}
		return super.equals(obj);
	}

	
	
	
	@Override
	public int hashCode() { return hash; }
	

	@Override
	public String toString() { return "Hash "+hash; }
	
	// We are
	
	static private NodeHash weAre = null;
	static {
		int hash =  (0xFFFF & ((int)System.currentTimeMillis()));
		try {
			weAre = new NodeHash(hash,InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			log.log(Level.SEVERE,"Hash generation error",e);
		}
	}

	static NodeHash getMyNodeHash() { return weAre; }
	
}
