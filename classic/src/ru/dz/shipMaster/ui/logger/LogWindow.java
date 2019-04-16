package ru.dz.shipMaster.ui.logger;

import java.text.DateFormat;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Log window is a typical running (scrolling) log with some bells
 * and whistles.
 * @author dz
 */

public class LogWindow extends GeneralLogWindow {
    @SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(LogWindow.class.getName()); 

	/* (non-Javadoc)
	 * @see ru.dz.shipMaster.ui.DashComponent#canExtendHeight()
	 */
	@Override
	public boolean canExtendHeight() {
		return true;
	}

    
    //ASC105 display;
    {
    	haveClock = true;
    	/*try {
    		// TODO - configuration and setup needed!
			//display = new ASC105("COM1");
		} catch (CommunicationsException e) {
			e.printStackTrace();
		} */
    }

    /**
	 * Eclipse id. 
	 */
	private static final long serialVersionUID = 6290670948505535637L;
	
	
	@Override
	protected void processResize() {
		// ignore		
	}


	private long lastItemTime = 0;
	private static final long TIME_MARKER_GAP_MSEC = 30*1000; // 30 sec
	
	private static DateFormat formatter = DateFormat.getDateTimeInstance();
	
	private void addItem(Item i)
	{
		long now = System.currentTimeMillis();
		if( (lastItemTime == 0) || (lastItemTime < now-TIME_MARKER_GAP_MSEC))
		{
			Date d = new Date(lastItemTime == 0 ? now : lastItemTime);
			//String timeStr = d.toLocaleString();
			items.add(0, new Item("        "+	formatter.format(d) ));
			lastItemTime = now;
		}
		
		synchronized(items) { items.add(0, i); }
		repaint();
		// TODO uncomment and fix
		/*
		if(display != null)
			try {
				
				display.setRedColor(i.isCritical);
				display.sendText(i.text);
			} catch (CommunicationsException e) {
				// TO DO Auto-generated catch block
				e.printStackTrace();
			}
		*/
	}
	
	public void addMessage( Item item ) { addItem(item); }
	public void addMessage( String message ) { addItem( new Item(message)); }
	public void addMessage( String message, int secondsToLive ) { addItem( new Item(message,secondsToLive));  }
	public void addCriticalMessage( String message ) { addItem( new Item(message, true)); }
	
	
	
}

