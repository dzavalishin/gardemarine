package ru.dz.shipMaster.dev.common;


/**
 * Happens when pipe gets new data from other side.
 * @author dz
 */
@SuppressWarnings("serial")
public class BiPipeReceiveEvent extends BiPipeEvent {

	public BiPipeReceiveEvent(BiPipe source) {
		super(source);
	}

}
