package ru.dz.shipMaster.misc;

/**
 * Thrown by network code if incoming packet CRC is wrong.
 * @author dz
 *
 */
public class CrcExceprion extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2822929346702352514L;

	public CrcExceprion() {
	}

	public CrcExceprion(String message) {
		super(message);
	}

	public CrcExceprion(Throwable cause) {
		super(cause);
	}

	public CrcExceprion(String message, Throwable cause) {
		super(message, cause);
	}

}
