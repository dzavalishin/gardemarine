package ru.dz.shipMaster.dev.common;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class StoppableLoopThread {
	protected static final Logger log = Logger.getLogger(StoppableLoopThread.class.getName()); 

	private String name = "Unnamed StoppableLoopThread";
	private int priority = Thread.NORM_PRIORITY;

	protected Thread thread;
	protected boolean stopRequest = false;
	protected boolean running = false;
	protected final Object threadStateSema = new Object();
	protected final Object stopMonitor = new Object();
	protected final Object threadSleepMonitor = new Object();
	
	private void createThread()
	{
		thread = new Thread() {
			@Override
			public void run() {
				loop();
				/*try {
					doStopDriver();
				} catch (CommunicationsException e) {
					log.log(Level.SEVERE,"Error stopping driver",e);
				}*/

				synchronized(threadStateSema)
				{
					thread = null;
					stopRequest = false;
					running  = false; // XXX a bit of races here
					synchronized (stopMonitor ) {
						stopMonitor.notifyAll(); // release stopDriver
					}
				}
			}


		};

		thread.setName(getName());
		thread.setDaemon(true);
	}

	/**
	 * Thread loop.
	 */
	private void loop()
	{
		log.fine(getName()+" started");


		while(true)
		{
			try 
			{
				sleep(1); // Sleep just a bit so that if step will return too fast we will not hog the CPU
				step(); 
			} 
			catch (Throwable e) {
				log.log(Level.SEVERE, "Exception in "+getName()+" thread", e);
			}
			if(stopRequest)
			{
				return;
			}
		}

	}


	/**
	 * Stop thread.
	 */
	public void stop()
	{

		try { 
			synchronized(threadStateSema)
			{
				if(thread == null)
					return;
				
				thread.setPriority(Thread.NORM_PRIORITY); // for any case
				stopRequest = true;

				threadSleepMonitor.notifyAll(); // if sleeping - wakeup
				
				unblock();
				thread.interrupt();

				if(running)
				{
					synchronized (stopMonitor) {
						stopMonitor.wait(1000); // Wait one second for driver to really stop
					}
				}

				if(!running)
					stopRequest = false;
			}
		}
		catch( Throwable e )
		{
			log.severe("Error stopping read thread: "+e.toString());
		}
	}


	/**
	 * Start thread.
	 */
	public void start()
	{
		synchronized(threadStateSema)
		{
			if(running)
			{
				return;
			}
			assert(thread == null);
			createThread();

			try 
			{ 
				//doStartDriver();
				thread.start();
				thread.setPriority(priority);

				running = true;
			}
			catch( Throwable e )
			{
				log.severe("Error starting reader: "+e.toString());
			}
		}
	}

	protected void sleep(long timeMsec) {
		try {
			synchronized (threadSleepMonitor) 
			{ 
				threadSleepMonitor.wait(timeMsec);
			}				
		} catch (InterruptedException e) {
			//continue; // repeat sleep
		}
	}
	
	// -----------------------------------------------------------
	// For children to implement 
	// -----------------------------------------------------------
	

	/**
	 * Called repeatedly in thread.
	 */
	protected abstract void step();
	
	/**
	 * Must unblock everything in doTask.
	 */
	protected abstract void unblock();


	// -----------------------------------------------------------
	// Getters/setters 
	// -----------------------------------------------------------


	public String getName() {		return name;	}

	public void setName(String name) {		this.name = name;	}

	public int getPriority() {		return priority;	}

	public void setPriority(int priority) {		this.priority = priority;	}



}
