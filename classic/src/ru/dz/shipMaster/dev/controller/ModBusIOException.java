package ru.dz.shipMaster.dev.controller;

public class ModBusIOException extends ModBusException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -563597987398118138L;

	public ModBusIOException() {
	}

	public ModBusIOException(String message, Throwable cause) {
		super(message, cause);
	}

	public ModBusIOException(String message) {
		super(message);
	}

	public ModBusIOException(Throwable cause) {
		super(cause);
	}

	public ModBusIOException(String message, int b) {
		super(message, b);
	}

}
