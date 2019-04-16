package ru.dz.shipMaster.dev.common;

public class NoHalfDuplexException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7912014328972274272L;

	public NoHalfDuplexException()
	{
		super("Half-duplex is not supported by this device");
	}
}
