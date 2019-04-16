package ru.dz.shipMaster.dev.common;

public class BiPipeErrorEvent extends BiPipeEvent {
	/**
	 * UID 
	 */
	private static final long serialVersionUID = -8712294645202755564L;
	
	private Exception exception;
	
	public BiPipeErrorEvent(BiPipe source, Exception e) {
		super(source);
		exception = e;
	}

	public Exception getException() {
		return exception;
	}

}
