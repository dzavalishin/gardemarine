package ru.dz.shipMaster.events;

import java.util.EventObject;

public abstract class GenericEvent extends EventObject {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8841749451565419676L;

	/**
     * Constructs a BiPipe Event.
     *
     * @param    source    The object on which the Event initially occurred.
     * @exception  IllegalArgumentException  if source is null.
     */
	public GenericEvent(Object source) {
		super(source);
	}

}
