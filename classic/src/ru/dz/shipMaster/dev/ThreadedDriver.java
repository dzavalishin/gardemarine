package ru.dz.shipMaster.dev;

import java.util.logging.Level;

import ru.dz.shipMaster.config.ConfigurationFactory;
import ru.dz.shipMaster.config.GeneralConfigBean;
import ru.dz.shipMaster.data.misc.CommunicationsException;

public abstract class ThreadedDriver extends AbstractDriver  {


	private static final long MIN_SLEEP = 10; // Sleep at least 10 msec per round
	private Thread updater = null;
	private boolean stopRequest = false;
	private long updateIntervalMsec;
	private String threadName;
	private int priority;

	private final Object threadSleepMonitor = new Object();
	private final Object threadStopMonitor = new Object();
	private double loadLevel;
	
	protected ThreadedDriver(final long updateIntervalMsec, int priority, final String threadName) 
	{
		this.updateIntervalMsec = updateIntervalMsec;
		this.priority = priority;
		this.threadName = threadName;
	}

	private void createThread()
	{
		updater = new Thread() {
			@Override
			public void run() {
				stateLabel.setText("Running");
				runLoop();
				try {
					doStopModule();
				} catch (CommunicationsException e) {
					log.log(Level.SEVERE,"Error stopping driver",e);
				}
				stateLabel.setText("Stopped");
				updater = null;
				stopRequest = false;
				moduleRunning = false; // XXX a bit of races here
				synchronized (threadStopMonitor) {
					threadStopMonitor.notifyAll(); // release stopDriver
				}
			}

		};

		updater.setName(threadName);
		//updater.setDaemon(true);
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

	
	private void runLoop()
	{
		log.fine(threadName+" started");


		while(true)
		{
			long stepStart = System.currentTimeMillis();
			try 
			{ 
				doDriverTask(); 
				//failCounter.noteSuccess();
			} 
			catch (Throwable e) {
				log.log(Level.SEVERE, "Exception in "+threadName, e);
				failCounter.noteFailure();
			}
			long stepEnd = System.currentTimeMillis();

			long msecLeft = updateIntervalMsec - (stepEnd - stepStart);
			
			if(msecLeft < MIN_SLEEP)
			{
				msecLeft = MIN_SLEEP;
				loadLevel = 1.0;
			}
			else
				loadLevel = 1-(msecLeft/updateIntervalMsec);
			
			putAverage(loadLevel);
			
			sleep(msecLeft);
			//sleep(updateIntervalMsec);			
			if(stopRequest)
			{
				return;
			}
		}
		
	}

		/**
		 * Must be implemented in driver subclass. Called in a regular basis
		 * by driver worker thread.
		 * @throws Throwable 
		 */
		protected abstract void doDriverTask() throws Throwable;

		/**
		 * Can be implemented in children. Called early in process of stopping driver. Can be used by
		 * implementation to flag return from doDriverTask, if it blocks. Default implementation does nothing.
		 */
		protected void signalStop() throws Throwable {};

		

		
		/**
		 * Deactivate driver, close and detach all resources.
		 * To be implemented in subclass.
		 */
		@Override
		public void stop()
		//public void internalStop()
		{
			stateLabel.setText("Stopping");
			//driverRunning = false;
			try { 
				if(updater != null)
					updater.setPriority(Thread.NORM_PRIORITY); // for any case
				stopRequest = true;
				synchronized(threadSleepMonitor) {
					threadSleepMonitor.notifyAll(); // Awake thread
				}
				//doStopDriver();
				//stateLabel.setText("Stopped");
				signalStop();
				
				if(moduleRunning)
				{
					synchronized (threadStopMonitor) {
						threadStopMonitor.wait(1000); // Wait one second for driver to really stop
					}
				}
				
				if(!moduleRunning)
					stopRequest = false;				
			}
			catch( Throwable e )
			{
				log.severe("Error stopping driver: "+e.toString());
				stateLabel.setText("Stop failed");
			}
		}

		public boolean isStopRequested() { return stopRequest; } 

		/**
		 * Activate driver, allocate and open all resources.
		 * To be implemented in subclass.
		 */
		@Override
		public void start()
		{
			if(moduleRunning)
			{
				return;
			}
			
			if(haveObjection())
				return;

			RunRequested = true;
			failCounter.reset();

			assert(updater == null);
			createThread();

			ConfigurationFactory.getConfiguration().getGeneral();
			if( 
					GeneralConfigBean.debug && 
					isDebug() )
			{
				try {
					internalStartDebugger();
				}
				catch(Throwable e) {
					log.log(Level.SEVERE,"Starting module debugger", e);
				}
				
			}
			
			stateLabel.setText("Starting");
			try 
			{ 
				//doStartModule();
				restartOnFailure();
				updater.start();
				updater.setPriority(priority);

				moduleRunning = true;
			}
			catch( Throwable e )
			{
				stateLabel.setText("Start failed");
				log.severe("Error starting driver "+getName()+": "+e.toString());
			}
		}

		public void setInterval(long interval) {
			if(interval < 10) interval = 10; // Not too fast
			updateIntervalMsec = interval;			
		}

		public long getInterval() { return updateIntervalMsec; }
		
		/**
		 * Returns the level of CPU load produced by this thread. Returns 0 if this
		 * thread actually does not consume any processor. Returns 1 if thread needs
		 * more than processor can offer. In real life 0 is never returned.
		 * @return The load level, from 0 to 1. Value of 1 means this thread loads
		 * processor nearly completely (gets less processor time that it
		 * wants).
		 */
		public double getLoadLevel() {
			return loadLevel;
		}

		// Load level averager
		
		private final double[] values = new double[10];
		private int putPosition = 0;
		private void putAverage(double val)
		{
			synchronized(values)
			{
				values[putPosition] = val;
				putPosition++;
				putPosition %= values.length;
			}
		}

		/**
		 * Returns averaged load level for this thread.
		 * @see getLoadLevel()
		 * @return The average load level, from 0 to 1. 0 is no load, 1 is full load.
		 */
		public double getLoadLevelAverage()
		{
			synchronized(values)
			{			
				double sum = 0;

				for( int i = 0; i < values.length; i++)
					sum += values[i];

				return sum/values.length;
			}
		}
		

	}
