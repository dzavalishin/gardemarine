package ru.dz.shipMaster.dev.ups;

/**
 * Thrown if UPS protocol fails to understand UPS.
 * @author dz
 */
@SuppressWarnings("serial")
public class UpsProtocolException extends Exception {

	public UpsProtocolException() {
	}

	public UpsProtocolException(String message) {
		super(message);
	}

	public UpsProtocolException(Throwable cause) {
		super(cause);
	}

	public UpsProtocolException(String message, Throwable cause) {
		super(message, cause);
	}

}
