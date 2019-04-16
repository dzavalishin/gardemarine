package ru.dz.shipMaster.net.protocol;

public class NetIOException extends NetException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8675227922717072564L;

	public NetIOException() {
	}

	public NetIOException(String message) {
		super(message);
	}

	public NetIOException(Throwable cause) {
		super(cause);
	}

	public NetIOException(String message, Throwable cause) {
		super(message, cause);
	}

}
