package ru.dz.shipMaster.dev;

public interface IBus extends IModule {

	/**
	 * Disconnect from everything and kill.
	 */
	void destroy();

	boolean isRunning();

	void stop();

	String getInstanceName();

}
