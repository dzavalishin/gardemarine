package ru.dz.shipMaster.dev;


/**
 * Used to check that some state/event is kept for required time.
 * @author dz
 *
 */
public class EventSteadyTimer {

	private long steadyTime = 0;
	private long eventStartTime = -1;
	private boolean eventStarted = false; // True if we detected event
	private boolean eventReported = false; // True if we reported event
	
	public EventSteadyTimer() {
		// empty
	}
	
	public EventSteadyTimer(long steadyTime) {
		setSteadyTime(steadyTime);
	}

	public long getSteadyTime() {		return steadyTime;	}
	
	/**
	 * Sets time (milliseconds) for state to be true before being reported as true by
	 * checkEvent.
	 * @param steadyTime Time to check state before reporting it.
	 */
	public void setSteadyTime(long steadyTime) {		this.steadyTime = steadyTime;	}
	
	/**
	 * If argument is false, result is false and wait time is reset.
	 * Returns true if argument was true for at least 2 calls and at least some
	 * time is passed. 
	 */
	public boolean checkEvent( boolean input )
	{
		if( input == false )
		{
			eventStarted = false;
			eventReported = false;
			return false;
		}
		
		// Input is true;
		
		// We already decided to report it
		if(eventReported)
			return true;
		
		if(!eventStarted)
		{
			eventStartTime = System.currentTimeMillis();
			eventStarted = true;
			eventReported = false;
			return false; // Not on first time in any way!
		}
		
		long now = System.currentTimeMillis();
		if( now >= eventStartTime+steadyTime)
		{
			eventReported = true;
			return true;
		}
		
		eventReported = false;
		return false;
	}

	/**
	 * Returns true if event is in reported state (passed through the filter).
	 * @return what last checkEvent() returned.
	 */
	public boolean isReported() { return eventReported; }
	
}
