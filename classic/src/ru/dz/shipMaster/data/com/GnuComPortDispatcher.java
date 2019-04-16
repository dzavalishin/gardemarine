package ru.dz.shipMaster.data.com;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Used for cycling through available communication ports to find the one needed.
 * @author dz
 */
public class GnuComPortDispatcher extends GeneralComPortDispatcher {
	private static final int NEXT_PORT_TIMEOUT_MSEC = 600; //6000;
	private static final Logger log = Logger.getLogger(GnuComPortDispatcher.class.getName()); 

	private Map<ComPortUser,CommPortIdentifier> usedPorts = new HashMap<ComPortUser,CommPortIdentifier>();
	
	private void addToUsedMap(ComPortUser u,CommPortIdentifier i) { usedPorts.put(u, i); }
	private void removeFromUsedMap(ComPortUser u) { usedPorts.remove(u); }
	private boolean isInUsedMap(CommPortIdentifier i) { return usedPorts.containsValue(i); }
	
	
	@Override
	public void giveMePort(ComPortUser me) {
		
		removeFromUsedMap(me);
		while(true)
		{
			//log.info("Looking for a port to try, user "+me.getName());
			CommPortIdentifier nextFreePort = getNextFreePort();
			//synchronized(usedPorts)
			synchronized(log)
			{
				if(isInUsedMap(nextFreePort))
				{
					//log.info("Skipping port "+nextFreePort.getName());
					continue;
				}
				addToUsedMap(me, nextFreePort);
			}
			//log.info("Got a port to try("+nextFreePort.getName()+"), user "+me.getName());
			if(me.tryThisPort(nextFreePort))
				return;
			
			removeFromUsedMap(me);
			
			//log.info("Sleeping after the try, user "+me.getName());
			sleepMsec(NEXT_PORT_TIMEOUT_MSEC);
		}

	}

	// ---------------------------------------------------------------------
	// workers
	// ---------------------------------------------------------------------

	private List<CommPortIdentifier> freePorts = new LinkedList<CommPortIdentifier>(); 
	
	private void findFreePorts()
	{
		Enumeration<?> pList = CommPortIdentifier.getPortIdentifiers(); 

		while (pList.hasMoreElements()) {
			CommPortIdentifier  pId = (CommPortIdentifier) pList.nextElement();

            if (
            		(pId.getPortType() == CommPortIdentifier.PORT_SERIAL)
            		&& (pId.getCurrentOwner() == null) &&
            		(!pId.isCurrentlyOwned())
            ) {
                    //System.out.println(pId.getName());
                    //freePorts.add(pId);
                    
                    try {
                        CommPort thePort = pId.open("GnuComPortDispatcher", 50);
                        thePort.close();
                        freePorts.add(pId);
                    } catch (PortInUseException e) {
                        //System.out.println("Port, "   com.getName()   ", is in use.");
                    } catch (Exception e) {
                        //System.err.println("Failed to open port "   com.getName());
                        //e.printStackTrace();
                    	log.severe("Failed to open port "+ pId.getName());
                    }
            }
        }	
	}

	private synchronized CommPortIdentifier getNextFreePort()
	{
		if(freePorts.size() == 0)
			findFreePorts();

		while(freePorts.size() == 0)
		{
			findFreePorts();
			sleepMsec(1000);
		}
		
		return freePorts.remove(0);
	}

	private void sleepMsec(int msec) {
		try {
			synchronized (this) { wait(msec); }
		} catch (InterruptedException e) {
			log.log(Level.SEVERE,"Interrupted sleep",e);
		}
		
	}
}
