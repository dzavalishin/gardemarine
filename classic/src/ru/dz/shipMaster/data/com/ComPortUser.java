package ru.dz.shipMaster.data.com;

import gnu.io.CommPortIdentifier;

public interface ComPortUser {

	/** 
	 * Callee must open port and check if device 
	 * on the other side is the one it expects.
	 * 
	 * @return true if device is found and port is taken,
	 * false instead. On false return another port will
	 * be offered if available, or this one will be 
	 * re-offered later.  
	 */
	boolean tryThisPort(CommPortIdentifier portId);
	
	
	/** @return human-readable name of this com port user. */
	String getName();
}
