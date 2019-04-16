package ru.dz.shipMaster.dev;

import java.util.Vector;

/** 
 * Just identifies class as driver
 * @author dz
 *
 */
public interface IDriver extends IModule {

	/**
	 * Connect driver ports to actual parameters.
	 */
	public void connectPorts();
	
	
	/**
	 * Disconnect driver ports from parameters.
	 */
	public void disconnectPorts();

	
	/**
	 * Returns a list of available ports for the driver.
	 * @return Collection of ports.
	 */
	public Vector<DriverPort> getPorts(); 

	public void setPorts(Vector<DriverPort> ports);
}