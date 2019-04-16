package ru.dz.shipMaster.events;

import java.util.EventListener;

public interface GenericEventListener extends EventListener {

	public abstract void event(final GenericEvent event);
	
}
