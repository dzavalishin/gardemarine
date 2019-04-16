package ru.dz.shipMaster.data.history;

import java.util.LinkedList;
import java.util.List;

/**
 * Short time (hours?) history data storage.
 * @author dz
 */
public class HistoryStorage {
	// TODO must be vector to be compatible with other code (UI)
	private final List<HistoryRecord> list = new LinkedList<HistoryRecord>();
	private final List<HistoryRecord> shortList = new LinkedList<HistoryRecord>();
	private int timetoHoldSec; 

	/**
	 * Create storage.
	 * @param timetoHoldSec Time in seconds to hold history events.
	 */
	public HistoryStorage(int timetoHoldSec )
	{
		this.timetoHoldSec = timetoHoldSec;
		
	}
	
	private long lastMove = 0;
	public void addRecord( HistoryRecord hr )
	{
		long now = System.currentTimeMillis();
		
		synchronized(shortList)
		{
		shortList.add(hr);
		
		if(shortList.size() > 100 || now > lastMove+1000)
			synchronized(list)
			{
				lastMove = now;
				list.addAll(shortList);
				shortList.clear();
				//list.add(hr);
				cleanupByTime();
			}
		}
	}

	private void cleanupByTime() {
		while(list.size() > 0)
		{
			HistoryRecord lastEl = list.get(0);
			if( lastEl.isOlderThan(timetoHoldSec) )				
				list.remove(0);
			else
				break;
		}		
	}

	public List<HistoryRecord> getList() {		return list;	}
	//public void setList(List<HistoryRecord> list) {		this.list = list;	}

	public int getTimetoHoldSec() {		return timetoHoldSec;	}
	public void setTimetoHoldSec(int timetoHoldSec) {		this.timetoHoldSec = timetoHoldSec;	}

	
}
