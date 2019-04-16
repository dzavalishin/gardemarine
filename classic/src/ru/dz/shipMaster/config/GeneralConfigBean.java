package ru.dz.shipMaster.config;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import ru.dz.shipMaster.config.items.CliUser;

public class GeneralConfigBean {
    @SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(GeneralConfigBean.class.getName()); 
	
	
	private boolean demoMode = true;
	private boolean simulationMode = false;
	private boolean networkActive = false;
	private boolean splashScreenShown = true;
	private boolean autostart = false;
	
	private boolean autologin = false;
	private CliUser autologinUser = null;
	
	private static String myHostName = null; 
	static {
		try {
			InetAddress me = InetAddress.getLocalHost();
			//myHostName = me.getCanonicalHostName(); // better this default than nothing
			myHostName = me.getHostAddress(); // Fast!
			//log.severe("Default node name is "+myHostName);
		} catch (UnknownHostException e) {
			// well. failed.
		}
	}

	private String 	netNodeName = myHostName;


	private boolean globalExtendWidth;


	private boolean globalExtendHeight;

	/**
	 * Dumps all driver port io data.
	 */
	public static boolean debug = false;


	private boolean suppressAlarms = false;


	private String logFileName = "Gardemarine%g.log";

	/**
	 * @return the demoMode
	 */
	public boolean isDemoMode() {		return demoMode;	}
	/**
	 * @param demoMode the demoMode to set
	 */
	public void setDemoMode(boolean demoMode) {
		this.demoMode = demoMode;
	}
	/**
	 * @return the networkActive
	 */
	public boolean isNetworkActive() {		return networkActive;	}
	/**
	 * @param networkActive the networkActive to set
	 */
	public void setNetworkActive(boolean networkActive) {
		this.networkActive = networkActive;
	}
	/**
	 * @return the splashScreenShown
	 */
	public boolean isSplashScreenShown() {		return splashScreenShown;	}
	/**
	 * @param splashScreenShown the splashScreenShown to set
	 */
	public void setSplashScreenShown(boolean splashScreenShown) {
		this.splashScreenShown = splashScreenShown;
	}
	/**
	 * @return the autostart
	 */
	public boolean isAutostart() {		return autostart;	}
	/**
	 * @param autostart the autostart to set
	 */
	public void setAutostart(boolean autostart) {
		this.autostart = autostart;
	}
	/**
	 * @return the autologin
	 */
	public boolean isAutologin() {		return autologin;	}
	/**
	 * @param autologin the autologin to set
	 */
	public void setAutologin(boolean autologin) {
		this.autologin = autologin;
	}
	/**
	 * @return the autologinUser
	 */
	public CliUser getAutologinUser() {		return autologinUser;	}
	/**
	 * @param autologinUser the autologinUser to set
	 */
	public void setAutologinUser(CliUser autologinUser) {		this.autologinUser = autologinUser;	}

	public String getNetNodeName() {		return netNodeName;	}
	public void setNetNodeName(String netNodeName) {		this.netNodeName = netNodeName;	}
	
	/**
	 * True if parameters that have no source are to be simulated from random number generator.
	 * @return True if simulation is enabled.
	 */
	public boolean isSimulationMode() {		return simulationMode;	}
	public void setSimulationMode(boolean simulationMode) {		this.simulationMode = simulationMode;	}

	
	public void setGlobalExtendWidth(boolean globalExtendWidth) {		this.globalExtendWidth = globalExtendWidth;	}
	public void setGlobalExtendHeight(boolean globalExtendHeight) {		this.globalExtendHeight = globalExtendHeight;	}
	
	public boolean isGlobalExtendWidth() {		return globalExtendWidth;	}
	public boolean isGlobalExtendHeight() {		return globalExtendHeight;	}
	
	public void setDebug(boolean debug) {		GeneralConfigBean.debug = debug;	}
	//public boolean isDebug() {		return debug;	}
	public void setSuppressAlarms(boolean suppressAlarms) {
		this.suppressAlarms = suppressAlarms;
		
	}
	public boolean isSuppressAlarms() {
		return suppressAlarms;
	}

	public void setLogFileName(String logFileName) {		this.logFileName = logFileName;	}
	public String getLogFileName() {		return logFileName;	}

}
