package ru.dz.shipMaster.dev;

public abstract class FailCounter {
	private int totalFailCount = 0;
	private int totalSuccessCount = 0;
	
	private int failCount = 0;
	private int successCount = 0;

	private static final int failRepeatTimeMsec = 30*1000; // Default is 30 sec
	
	private EventSteadyTimer timer = new EventSteadyTimer(failRepeatTimeMsec);
	private boolean triggered = false;
	
	
	/**
	 * This method asks counter to take in account one more failure.
	 */
	public void noteFailure()
	{
		totalFailCount++;
		failCount++;
		if(timer.checkEvent(true))
		{
			triggered  = true;
			// Note that this method might not return - 
			// for example, if it kills our thread.
			actOnFailures();
		}
	}
	
	

	/**
	 * This method asks counter to take in account one more success.
	 */
	public void noteSuccess()
	{
		totalSuccessCount++;
		successCount++;
		timer.checkEvent(false);
	}

	
	public double getFailRate()
	{
		return (double)failCount/(double)successCount;
	}

	public double getTotalFailRate()
	{
		return (double)totalFailCount/(double)totalSuccessCount;
	}

	public boolean isTriggered() {
		return triggered;
	}
	
	public void reset()
	{
		triggered = false;
		totalFailCount = totalSuccessCount = 0;
		timer.checkEvent(false);
	}
	
	/**
	 * Do what is to be done if too many failures are happened.
	 */
	public abstract void actOnFailures();


	
}
