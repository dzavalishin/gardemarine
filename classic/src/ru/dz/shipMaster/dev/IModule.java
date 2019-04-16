package ru.dz.shipMaster.dev;

import javax.swing.JPanel;

public interface IModule {
	/**
	 * Deactivate driver, close and detach all resources.
	 * To be implemented in subclass.
	 */
	public void stop();

	/**
	 * Activate driver, allocate and open all resources.
	 * To be implemented in subclass.
	 */
	public void start();

	/**
	 * @return True if driver is running now.
	 */
	public boolean isRunning();

	/**
	 * Driven device class name, like "WAGO 750" or "ASC105 line display"
	 * @return Name of driven device.
	 */
	public String getDeviceName();

	/**
	 * Driven device instance name or identifier.
	 * @return name/id
	 */
	public String getInstanceName();

	/**
	 * If this driver is to be automatically started on system startup.
	 * @return the autoStart setting for driver.
	 */
	public boolean isAutoStart();

	/**
	 * If this driver is to be automatically started on system startup.
	 * @param autoStart the autoStart value.
	 */
	public void setAutoStart(boolean autoStart);

	
	
	/**
	 * Returns true if this kind of drivers supports automatic search of devices.
	 * TODO add autoSearch method to be called for device search.
	 * @return True for drivers able to automatically search for devices.
	 */
	public boolean isAutoSeachSupported();


	
	
	
	
	public JPanel getSetupPanel();

	/**
	 * Load settings from module instance to panel widgets.
	 *
	 */
	public void loadPanelSettings(); 
	
	/**
	 * Save settings made in panel to driver, activate settings.
	 *
	 */
	public void savePanelSettings(); 

	
	
	
	
	
	
	
	
	
	
	public String getName();

	public void destroy();

}
