package ru.dz.shipMaster.dev.clock;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.data.misc.CommunicationsException;
import ru.dz.shipMaster.dev.DriverPort;
import ru.dz.shipMaster.dev.DrivertStartObjection;
import ru.dz.shipMaster.dev.ThreadedDriver;




/**
 * SNTP (time) client.
 * @author dz
 *
 */
public class SntpClient extends ThreadedDriver
{
	protected static final String threadName = "SNTP time updater";
	//protected static final long UPDATE_INTERVAL_MSEC = 1000*60*60; // 1 hour
	protected static final long UPDATE_INTERVAL_MSEC = 1000*30; // 30 sec

	private InetAddress address;
	private String hostName = "";

	private DrivertStartObjection badAddress = new DrivertStartObjection("Incorrect host address");
	
	public SntpClient() {
		super(UPDATE_INTERVAL_MSEC, Thread.MAX_PRIORITY, threadName);

	}
	
	// ------------------------------------------------------------
	// Driver part
	// ------------------------------------------------------------
	



	@Override
	protected synchronized void doStartDriver() throws CommunicationsException {
		// empty
	}

	@Override
	protected synchronized void doStopDriver() throws CommunicationsException {
		// empty
	}


	/**
	 * Called once in UPDATE_INTERVAL_MSEC by separate thread to 
	 * update clock on this computer.
	 * @throws IOException 
	 */ 
	@Override
	protected void doDriverTask() throws Throwable {
		SntpRequest sr = new SntpRequest(address);
	
		double av = sr.averagedRequest(4);
		displayTimeDiff(av);
		// TO DO actually change clock
		// TO DO if timediff is too big - ask for confirmation by enabling button or checkbox
		//System.out.println("Averaged local clock offset: " + new DecimalFormat("0.00").format(av*1000) + " ms");

		tDiffPort.sendDoubleData(av);
	}

	private DriverPort tDiffPort;
	
	@Override
	protected void updatePorts(Vector<DriverPort> ports) {

		DriverPort.absentize(ports);
		DriverPort.sortPorts(ports);
		
		{
		tDiffPort = DriverPort.getPort(this, ports,0);
		tDiffPort.setDirection(Direction.Input);
		tDiffPort.setHardwareName("TimeDiffMsec");
		tDiffPort.setType(Type.Numeric);
		tDiffPort.setDescription("This port provides difference in milliseconds\nbetween local and server time.");
		}
	}
	
	
// this is for XML serializer
/*
	public String getHostName() {		
		return hostName;	
		}

	public void setHostName(String hostName) {		
		this.hostName = hostName;	
		try {
			address = InetAddress.getByName(hostName);
		} catch (UnknownHostException e) {
			address = null;
		}
	}
*/		

	// ------------------------------------------------------------
	// UI part
	// ------------------------------------------------------------

	@Override
	protected void doLoadPanelSettings() {
		// ntp1.gatech.edu
		hostName = hostName.trim();
		if(hostName.length() == 0)
		{
			//hostName = "ntp1.gatech.edu";
			hostNameField.setText("ntp1.gatech.edu");
		}
		else
			hostNameField.setText(hostName);		
	}



	@Override
	protected void doSavePanelSettings() {
		setHostName( hostNameField.getText() );		
	}
	
	@Override
	public String getDeviceName() {
		return "NTP network time client";
	}


	private JTextField hostNameField = new JTextField(hostName);
	private JTextField timeDiffField = new JTextField("--");

	@Override
	protected void setupPanel(JPanel panel) {
		panel.add(new JLabel("NTP host:"), consL);
		panel.add(hostNameField, consR);
		hostNameField.setColumns(20);

		timeDiffField.setEditable(false);
		timeDiffField.setColumns(20);
		panel.add(new JLabel("Last time diff, msec:"), consL);
		panel.add(timeDiffField, consR);
	}
	
	private static final DecimalFormat df = new DecimalFormat("0.00");
	private void displayTimeDiff(final double diff)
	{
		SwingUtilities.invokeLater(new Runnable() {					
			public void run() {
				timeDiffField.setText(df.format(diff*1000) + " ms");				
			}});
	}



	@Override
	public boolean isAutoSeachSupported() {
		// TO DO we can do some autosearch by checking some predefined hosts
		// and most of local network too
		return false;
	}

	
// -------------------------------------------- TEST --------------------------------------------	

	public static void main(String[] args) throws IOException
	{
		String serverName;
		
		// Process command-line args
		if(args.length==1)
		{
			serverName = args[0];
		}
		else
		{
			//printUsage();
			return;
		}
		
		InetAddress address = InetAddress.getByName(serverName);

		SntpRequest sr = new SntpRequest(address);
		sr.request();
		
		// Display response
		System.out.println("NTP server: " + serverName);
		System.out.println(sr.getMsg().toString());
		
		System.out.println("Dest. timestamp:     " +
			NtpMessage.timestampToString(sr.getDestinationTimestamp()));
		
		System.out.println("Round-trip delay: " +
			new DecimalFormat("0.00").format(sr.getRoundTripDelay()*1000) + " ms");
		
		System.out.println("Local clock offset: " +
			new DecimalFormat("0.00").format(sr.getLocalClockOffset()*1000) + " ms");
	
		double av = sr.averagedRequest(10);
		System.out.println("Averaged local clock offset: " +
				new DecimalFormat("0.00").format(av*1000) + " ms");
		
	}

	
	// -----------------------------------------------------------
	// Getters/setters 
	// -----------------------------------------------------------

	

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
		try {
			address = InetAddress.getByName(hostName);
			badAddress.setStartForbidden(false);
		} catch (UnknownHostException e) {
			showMessage("Address is wrong");
			badAddress.setStartForbidden(true);
		}
	}

	@Override
	public String getInstanceName() { 		return hostName;	}
	
	
	


	
}

/**
 * NtpClient - an NTP client for Java.  This program connects to an NTP server
 * and prints the response to the console.
 * 
 * The local clock offset calculation is implemented according to the SNTP
 * algorithm specified in RFC 2030.  
 * 
 * Note that on windows platforms, the curent time-of-day timestamp is limited
 * to an resolution of 10ms and adversely affects the accuracy of the results.
 * 
 * 
 * This code is copyright (c) Adam Buckley 2004
 *
 * This program is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the Free 
 * Software Foundation; either version 2 of the License, or (at your option) 
 * any later version.  A HTML version of the GNU General Public License can be
 * seen at http://www.gnu.org/licenses/gpl.html
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for 
 * more details.
 *  
 * @author Adam Buckley
 */
