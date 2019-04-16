package ru.dz.shipMaster.dev.controller.dz;

import java.util.logging.Level;
import java.util.logging.Logger;

import ru.dz.shipMaster.data.misc.CommunicationsException;

public class HomeNetGroup {
	protected static final Logger log = Logger.getLogger(HomeNetGroup.class.getName());

	private static final int MAX_REQUEST_STATE_TRIES = 20;
	private int requestStateTries = 0;
	

	private int value = 0;
	private byte number = -1;
	private byte generation = 0;
	private boolean inSynch = false;
	private boolean enabled = false;

	public void setState(int value, HomeNetProtocol p) throws CommunicationsException
	{
		if(!enabled) return;
		this.value = value;
		inSynch = true; // have to treat self as in sync
		generation++;
		p.sendSetGroup(number, generation, value);
	}

	public void sendState(HomeNetProtocol p) throws CommunicationsException
	{
		if(!enabled) return;
		p.sendSetGroup(number, generation, value);
	}

	/**
	 * Ask other nodes to send me their state so that i'll be in sync.
	 * Supposed to be called once in 10 sec.
	 * @param protocolDriver to communicate through.
	 * @throws CommunicationsException
	 */
	public void requestState(HomeNetProtocol protocolDriver) throws CommunicationsException {
		if((!enabled) || inSynch)
			return;
		
		if(requestStateTries > MAX_REQUEST_STATE_TRIES)
			return;
		
		requestStateTries++;
		
		protocolDriver.sendGetGroup(number);
	}
	
	/**
	 * Receive group state.
	 * @param p packet to process
	 * @return true if new state was received, false otherwise.
	 */
	public boolean recvState(HomeNetPacket packet)
	{
		if(!enabled)
			return false;

		int val = packet.getValue();
		byte gen = packet.getGeneration();
		byte grp = packet.getGroup();

		if(grp != number)
			return false;
		
		if(inSynch && !gen_gt(generation, gen))
		{
			log.log(Level.SEVERE, String.format("Group %d to %d, ignored due to generation(my %d, his %d)", 
					number, val,
					generation, gen)
					);
			return false;
		}
		
		this.generation = gen;
		this.value = val;
		this.inSynch = true;
		log.log(Level.SEVERE, String.format("Group %d is %d", number, value));

		return true;
	}
	
	
	
	/** 
	 * 
	 * Is his generation is greater than mine.
	 * 
	 * @param ghis His generation.
	 * @param gmy Mine generation. 
	 *  
	 */
	public static boolean gen_gt( int gmy, int ghis )
	{
		gmy &= 0xFF;
		ghis &= 0xFF;
		
	    int diff = ghis - gmy;
	    if( diff < 0 ) diff = -diff;
	    if( diff > 4 ) return true; // diff is too big, sync

	    if( gmy > 127 )
	    {
	        gmy -= 127;
	        ghis -= 127;
	    }

	    return ghis > gmy;
	}

	public int getValue() {				return value;	}
	public byte getGeneration() {		return generation;	}

	public boolean isInSynch() {		return inSynch;	}

	
	/** Get group number */
	public byte getNumber() {			return number;	}
	
	/** Set group number */
	public void setNumber(byte number) {		
		this.number = number;
		inSynch = false;
		requestStateTries = 0;
		}

	
	
	/** If this group is active. Passive group won't send packets. */
	public boolean isEnabled() {		return enabled;	}
	public void setEnabled(boolean enabled) {		
		this.enabled = enabled;	
		inSynch = false;
		requestStateTries = 0;
		}


}
