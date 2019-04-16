package ru.dz.shipMaster.ui.logger;

import java.awt.Container;
import java.awt.Dimension;
import java.util.logging.Logger;

/**
 * Message window has some (fixed?) messages that can update themselves being
 * in place.
 * @author dz
 *
 */

public class MessageWindow extends GeneralLogWindow {
    @SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(MessageWindow.class.getName()); 

    /**
	 * Eclipse id. 
	 */
	private static final long serialVersionUID = 6290670948505535637L;

	public MessageWindow() {
		recalcHeight();
	}
	
	@Override
	protected void processResize() {
		// ignore		
		recalcHeight();
	}

	
	private void addItem(Item i)
	{
		synchronized(items) { items.add(0, i); }
		i.setOwner(this);
		recalcHeight();
		repaint();
	}
	
	public void removeItem(Item item) {
		synchronized(items) { items.remove(item); }
		recalcHeight();
		repaint(100);		
	}

	/*
	public void addMessage( Item item ) { addItem(item); }
	public void addMessage( String message ) { addItem( new Item(message)); }
	public void addMessage( String message, int secondsToLive ) { addItem( new Item(message,secondsToLive));  }
	public void addCriticalMessage( String message ) { addItem( new Item(message, true)); }
	*/
	
	public Item getItem(String initialText) {
		Item i = new Item(initialText);
		addItem(i);
		
		//recalcHeight();
		
		return i;
	}
	
	private void recalcHeight()
	{
		
		Dimension myPreferredSize = new Dimension();

		myPreferredSize.height = 4 + (lineHeight * items.size());
		myPreferredSize.width = 350; //getWidth();
		
		Dimension myMinimalSize = new Dimension();
		
		myMinimalSize.height = myPreferredSize.height;
		myMinimalSize.width = 300;
		
		setMinimumSize(myMinimalSize);
		
		setPreferredSize(myPreferredSize);
		setMaximumSize(myPreferredSize);
		
		Container myParent = getParent();
		if( myParent != null ) { myParent.validate(); }
	}

	public void clear() {
		synchronized (items) {
			items.clear();
		}
	}

	
}

