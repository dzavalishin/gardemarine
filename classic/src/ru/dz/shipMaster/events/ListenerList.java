package ru.dz.shipMaster.events;

import java.util.LinkedList;
import java.util.List;

public class ListenerList {
	private List<GenericEventListener> listeners = new LinkedList<GenericEventListener>();
	
	public void addListener(GenericEventListener l)
	{
		listeners.add(l);
	}
	
	public void removeListener(GenericEventListener l)
	{
		listeners.remove(l);
	}
	
	public void sendEvent(GenericEvent e)
	{
		for(GenericEventListener l : listeners)
			l.event(e);
	}
}
