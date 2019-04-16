package ru.dz.shipMaster.dev.controller;

/**
 * General Modbus exception.
 * @author dz
 *
 */
@SuppressWarnings("serial")
public class ModBusException extends Exception {

	public ModBusException() {
		super();
	}

	public ModBusException(String message, Throwable cause) {
		super(message, cause);
	}

	public ModBusException(String message) {
		super(message);
	}

	public ModBusException(Throwable cause) {
		super(cause);
	}

	public ModBusException(String message, int b) {
		super(message+" ("+b+")");
	}

}
