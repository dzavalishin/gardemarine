package ru.dz.shipMaster.data.com;

import ru.dz.shipMaster.data.misc.CommunicationsException;

public abstract class GenericComPortIO {
	protected boolean halfDuplexMode = true;

	public GenericComPortIO() {
		super();
	}

	//abstract public byte[] read(int nbytes) throws CommunicationsException;
	
	abstract public int read(byte data[]) throws CommunicationsException;
	
	abstract public void write(byte[] data) throws CommunicationsException;

	@Deprecated
	public static void dump(String prefix, byte[] pkt) {
		System.out.print(prefix+": 0x.. ");
		for(int i = 0; i < pkt.length; i++ )
		{
			System.out.print(String.format("%02X", 0xFF&(int)pkt[i]));
			System.out.print(" ");
		}
		System.out.println();
		
	}

	abstract public void reopen();

	abstract public void close();

	public boolean isHalfDuplexMode() {		return halfDuplexMode;	}

	public void setHalfDuplexMode(boolean halfDuplexMode) {		this.halfDuplexMode = halfDuplexMode;	}
	

	
	protected void sleepMsec(int i) {
		try { synchronized(this) { wait(i); } } 
		catch (InterruptedException e) {
			// ignore
		}
	}

	// -----------------------------------------------------------
	// Half duplex support 
	// -----------------------------------------------------------

	protected long lastDataIn;
	protected long dataInWas()
	{
		return System.currentTimeMillis() - lastDataIn;
	}
	

}