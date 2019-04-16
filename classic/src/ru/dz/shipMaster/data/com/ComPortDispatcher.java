package ru.dz.shipMaster.data.com;

public interface ComPortDispatcher {

	/*
	 * Causes dispatcher to offer ports to given
	 * ComPortUser one by one. Call returns after
	 * the port is found.
	 */
	
	void giveMePort( ComPortUser me );
	
}
