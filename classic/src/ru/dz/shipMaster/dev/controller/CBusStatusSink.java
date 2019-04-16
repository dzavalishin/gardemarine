package ru.dz.shipMaster.dev.controller;

public abstract class CBusStatusSink {
	/**
	 * 
	 * @param application app id to receive data for
	 * @param group group for which this status is received
	 * @param s status itself
	 */
	abstract void receiveStatus(int application, int group, CBusGroupState s );
	
}
