package ru.dz.shipMaster.dev.nmeaSentence;

public class InvalidNMEASentenceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4913526650255592934L;

	public InvalidNMEASentenceException() {
	}

	public InvalidNMEASentenceException(String message) {
		super(message);
	}

	public InvalidNMEASentenceException(Throwable cause) {
		super(cause);
	}

	public InvalidNMEASentenceException(String message, Throwable cause) {
		super(message, cause);
	}

}
