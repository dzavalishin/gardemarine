package ru.dz.shipMaster.net.protocol;

public class NetNoItemException extends NetException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4100453278532431870L;
	public NetNoItemException() { }
	public NetNoItemException(String message) {		super(message); }
	public NetNoItemException(Throwable cause) { super(cause); }
	public NetNoItemException(String message, Throwable cause) { super(message, cause); }

}
