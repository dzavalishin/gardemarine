package ru.dz.shipMaster.data.history;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.ListIterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import ru.dz.shipMaster.ui.meter.LeftGraph;

public class HistoryGraph extends LeftGraph {
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(HistoryGraph.class.getName()); 

	/**
	 * Eclipse id. 
	 */
	private static final long serialVersionUID = -7739444534475905350L;

	private final List<HistoryRecord> history;
	private ListIterator<HistoryRecord> earlierIterator;
	private ListIterator<HistoryRecord> laterIterator;
	private final String filter;

	public HistoryGraph(List<HistoryRecord> history, String filter) 
	{
		this.history = history;
		this.filter = filter;
		earlierIterator = history.listIterator();
		laterIterator = history.listIterator();
	}


	{
		moveWithMouse = true;
		killToTheRightInUpdate = false;
	}



	private void clearExtraTicks()
	{
		synchronized(ticksSema) {
			//killToTheRight(width*4);    	
			//killToTheLeft(-width*3);
			killToTheRight(width-20);    	
			killToTheLeft(20);
		}    				
	}

	private void feedMoreTicks()
	{
		//feedToTheRight(width*2);
		//feedToTheLeft(-width);
		feedToTheRight(width-25);
		feedToTheLeft(25);
	}

// go to earlier (time is less, position is to the left)
	private HistoryDataRecord goEarlierThan(ListIterator<HistoryRecord> iter, long time)
	{
		//logTime("skip earlier to ",time);
		while(iter.hasPrevious())
		{
			HistoryRecord record = iter.previous();
			if (record instanceof HistoryDataRecord) {
				HistoryDataRecord hd = (HistoryDataRecord) record;
				if(!hd.getParameterName().equalsIgnoreCase(filter))
					continue;
				if(record.getTime() < time)
				{
					//logTime("skip earlier stopped at ",record.getTime());
					return hd;
				}
				//logTime("skip earlier ",record.getTime());
			}
		}
		
		//logTime("skip earlier not found",time);

		return null; // Not found
	}

	private HistoryDataRecord goLaterThan(ListIterator<HistoryRecord> iter, long time)
	{
		//logTime("skip later to ",time);
		while(iter.hasNext())
		{
			HistoryRecord record = iter.next(); 
			if (record instanceof HistoryDataRecord) {
				HistoryDataRecord hd = (HistoryDataRecord) record;
				if(!hd.getParameterName().equalsIgnoreCase(filter))
					continue;
				if(record.getTime() > time)
				{
					//logTime("skip later stopped at ",record.getTime());
					return hd;
				}
				//logTime("skip later ",record.getTime());
			}
		}
		
		//logTime("skip later not found",time);
		return null; // Not found
	}


	// go left on scale, to newer times
	private void feedToTheLeft(int leftPos)
	{

		while(true)
		{
			long oldestTickTime = produceLeftMarginTime()-100000;
			if(ticks.size() > 0)
			{
				Tick t = ticks.get(ticks.size()-1);	    		
				oldestTickTime = t.getTimeMills();
				double tickx = calcXPos( oldestTickTime );

				// Have data to the left from left margin? Ok.
				if(tickx < leftPos)
					break;
			}
			//logTime("look for time < ",oldestTickTime);
			
			// Not enough. Feed more.
			synchronized(history)
			{
				try { goEarlierThan(laterIterator, oldestTickTime); }
				catch(ConcurrentModificationException e)
				{
					//log.severe("restart");
					laterIterator = history.listIterator(0);
				}
				
				HistoryDataRecord success = null;
				try { success = goLaterThan(laterIterator, oldestTickTime); }
				catch(ConcurrentModificationException e)
				{
					//log.severe("restart");
					laterIterator = history.listIterator(history.size()-1);
					success = goLaterThan(laterIterator, oldestTickTime);
				}
				/*
				if(success != null)
				{
					logTime("insert time ",success.getTime());
						ticks.add(0, hdr2tick(success));
						laterIterator.previous(); // step over to reset
				}
				else
					break;
				*/
				
				/*while(success != null)
				{
					logTime("insert time ",success.getTime());
					synchronized(ticksSema) {
						ticks.add(0, hdr2tick(success));
					}
					repaint(100);
					oldestTickTime = success.getTime();
					success = goLaterThan(laterIterator, oldestTickTime);
				}*/
				
				if(success != null)
				{
					//logTime("L insert time ",success.getTime());
					synchronized(ticksSema) {
						ticks.add(hdr2tick(success));
					}
					repaint(100);
					continue;
				}
				
				break;
			}
		}


	}

	// go right on scale, to older times
	private void feedToTheRight(int rightPos)
	{

		while(true)
		{
			long oldestTickTime = produceLeftMarginTime()+100000;
			if(ticks.size() > 0)
			{
				Tick t = ticks.get(0);	    		
				oldestTickTime = t.getTimeMills();
				double tickx = calcXPos( oldestTickTime );

				if(tickx > rightPos)
					break;
			}
			//logTime("look for time > ",oldestTickTime);
			
			// Not enough. Feed more.
			synchronized(history)
			{
				try { goLaterThan(earlierIterator, oldestTickTime); }
				catch(ConcurrentModificationException e)
				{
					//log.severe("restart");
					earlierIterator = history.listIterator(history.size()-1);
				}
				HistoryDataRecord success = goEarlierThan(earlierIterator, oldestTickTime);
				/*
				if(success != null)
				{
					logTime("insert time ",success.getTime());
						ticks.add(0, hdr2tick(success));
						laterIterator.previous(); // step over to reset
				}
				else
					break;
				*/
				
				/*while(success != null)
				{
					logTime("insert time ",success.getTime());
					synchronized(ticksSema) {
						ticks.add(hdr2tick(success));
					}
					repaint(100);
					oldestTickTime = success.getTime();
					success = goEarlierThan(laterIterator, oldestTickTime);
				}*/

				if(success != null)
				{
					//logTime("R insert time ",success.getTime());
					synchronized(ticksSema) {
						ticks.add(0,hdr2tick(success));
					}
					repaint(100);
					continue;
				}
				
				
				break;
			}
		}


	}
	

	private Tick hdr2tick(HistoryDataRecord hd) {
		Tick tick = new Tick(hd.getValue(),hd.getTime());
		return tick;
	}

	@Override
	public void setCurrent(double newCurr) {
		// TO DO we are not a meter, really
		/*super.setCurrent(newCurr);
		if(ticks != null)
		{
			synchronized(ticksSema) { ticks.add(new Tick(newCurr)); }
			repaint(100);
		}*/
	}



	// --------------------------------------------------------------------------
	// Abstracts
	// --------------------------------------------------------------------------

	private long creationTime = System.currentTimeMillis();
	private long diff = 0;
	private long dragStartTime = creationTime;
	/**
	 * This method must return time which left (latest) margin
	 * of display corresponds to.
	 */
	@Override
	protected long produceLeftMarginTime()
	{
		if(moveWithMouse)
			return dragStartTime+diff;
		return System.currentTimeMillis();
	}

	@Override
	protected void setMouseDragTime(long timediff)
	{
		diff = timediff;
		try {
			clearExtraTicks();
			feedMoreTicks();
		}
		catch(Throwable e)
		{
			e.printStackTrace();
		}
	}

	{
		TimerTask tt = new TimerTask() {

			@Override
			public void run() {
				clearExtraTicks();
				feedMoreTicks();
			}};
		
		Timer t = new Timer("History Graph update", true);
		t.schedule(tt, 2000, 2000);
	}
	
	@Override
	protected void noteMouseDragStart() {
		//dragStartTime += diff;
		//diff = 0;
	}

	@Override
	protected void noteMouseDragEnd() {
		dragStartTime += diff;
		diff = 0;
	}

}
