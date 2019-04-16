/**
 * 
 */
package ru.dz.shipMaster.net.protocol;

/**
 * Network layer exception.
 * @author dz
 *
 */
public class NetException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1995216225884482504L;

	public NetException() {
	}

	public NetException(String message) {
		super(message);
	}

	public NetException(Throwable cause) {
		super(cause);
	}

	public NetException(String message, Throwable cause) {
		super(message, cause);
	}

}
