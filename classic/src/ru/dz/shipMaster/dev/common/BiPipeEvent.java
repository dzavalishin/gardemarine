package ru.dz.shipMaster.dev.common;

import ru.dz.shipMaster.events.GenericEvent;

public abstract class BiPipeEvent extends GenericEvent {

    /**
	 * UID
	 */
	private static final long serialVersionUID = 676252387602488405L;

	/**
     * Constructs a BiPipe Event.
     *
     * @param    source    The object on which the Event initially occurred.
     * @exception  IllegalArgumentException  if source is null.
     */
	public BiPipeEvent(BiPipe source) {
		super(source);
	}


}
