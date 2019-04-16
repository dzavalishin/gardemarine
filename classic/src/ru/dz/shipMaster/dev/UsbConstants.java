package ru.dz.shipMaster.dev;

/**
 * USB-related constants.
 * @author dz
 *
 */
public class UsbConstants {

	
	/**
	 * We can use this VID for our products with PIDS in below defined range. 
	 * These VID/PID pairs supposed to be used with USB/Com converter FT232R based devices.
	 */
	public static final int		FTDI_VID = 0x0403;
	
	/**
	 * First PID in 0x0403 VID that we can use.
	 */
	public static final int		FTDI_START_PID = 0xAB67;
	
	/**
	 * Last PID in 0x0403 VID that we can use.
	 */
	public static final int		FTDI_END_PID = 0xAB6F; 
}
