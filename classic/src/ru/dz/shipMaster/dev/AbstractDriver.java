package ru.dz.shipMaster.dev;

import java.awt.event.ActionEvent;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

import ru.dz.shipMaster.config.items.CliParameter;
import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.data.misc.CommunicationsException;
import ru.dz.shipMaster.dev.debug.DriverDebugger;
import ru.dz.shipMaster.misc.TextViewFrame;
import ru.dz.shipMaster.ui.config.PropertiesEditItemList;


/**
 * Abstract Gardemarine driver. Base class for all the drivers.
 * @author dz
 */
public abstract class AbstractDriver extends AbstractModule implements IDriver {
	protected static final Logger log = Logger.getLogger(AbstractDriver.class.getName()); 

	private PropertiesEditItemList<DriverPort> portsField;
	private JCheckBox showAbsentField  = new JCheckBox();

	private JButton reloadPortsButton  = new JButton();
	private JButton showPortsList = new JButton();


	private Vector<DriverPort> ports = new Vector<DriverPort>();





	@Override
	@SuppressWarnings("serial")
	protected void mainSetupPanel()
	{
		super.mainSetupPanel();

		//moduleSetupPanel.add(new JLabel("Device:"), consL);
		//moduleSetupPanel.add(new JLabel(getDeviceName()), consR);

		//moduleSetupPanel.add(new JLabel("State:"), consL);
		//moduleSetupPanel.add(stateLabel, consR);

		moduleSetupPanel.add(reloadPortsButton, consL);
		moduleSetupPanel.add(showPortsList, consR);

		moduleSetupPanel.add(new JLabel("Show absent ports:"), consL);
		moduleSetupPanel.add(showAbsentField, consR);
		showAbsentField.setSelected(false);
		//showAbsentField.setEnabled(false);
		showAbsentField.setAction(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				portsField.setItems(choosePorts());
			}} );



		moduleSetupPanel.add(new JLabel("Ports:"), consL);	
		moduleSetupPanel.add(portsField = new PropertiesEditItemList<DriverPort>(choosePorts()), consR);

		reloadPortsButton.setAction(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				reloadPorts();				
			}});

		showPortsList.setAction(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				makePortsPatchList();				
			}});

		reloadPortsButton.setText("Resetup ports");
		showPortsList.setText("Show ports patch list");
	}


	protected void cleanupPorts() {
		updatePorts(ports);
		cleanAbsentPorts(ports);
		updatePorts(ports);
		portsField.setItems(choosePorts());
	}

	protected void reloadPorts() {
		updatePorts(ports);
		portsField.setItems(choosePorts());
	}

	private Vector<DriverPort> choosePorts() {
		Vector<DriverPort> portsCopy = (Vector<DriverPort>) getPorts().clone();

		if(!showAbsentField.isSelected())
			cleanAbsentPorts(portsCopy);

		return portsCopy;
	}

	private void cleanAbsentPorts(Vector<DriverPort> portsCopy) {
		restart: while(true)
		{
			for(DriverPort p : portsCopy)
			{
				if(p == null)
				{
					portsCopy.remove(p);
					continue restart;
				}
				if(p.isAbsent() && p.getTarget() == null)
				{
					portsCopy.remove(p);
					continue restart;
				}
			}
			break;
		}
	}





	/**
	 * Deactivate driver, close and detach all resources.
	 * To be implemented in subclass.
	 * @throws CommunicationsException if can not stop for some reason
	 */
	protected abstract void doStopDriver() throws CommunicationsException;

	/**
	 * Activate driver, allocate and open all resources.
	 * To be implemented in subclass.
	 * @throws CommunicationsException if can not start due to device communications error
	 */
	protected abstract void doStartDriver() throws CommunicationsException;


	private DriverDebugger dd = null;

	@Override
	protected void internalStartDebugger() {
		if( dd == null )
			dd = new DriverDebugger(this);
		
		dd.start();
	}
	
	@Override
	protected void internalStopDebugger() {
		if(dd != null)
			dd.stop();
	}
	
	protected final void doStartModule() throws CommunicationsException 
	{ 
		doStartDriver();
	}
	
	protected final void doStopModule() throws CommunicationsException 
	{
		doStopDriver(); 
	}


	/**
	 * Returns a list of available ports for the driver.
	 * @return Collection of ports.
	 */
	public final Vector<DriverPort> getPorts() 
	{ 
		updatePorts(ports); 
		return ports; 
	}

	public final void setPorts(Vector<DriverPort> ports) {
		this.ports = ports;
		updatePorts(ports);
	}

	/**
	 * Must be implemented by driver. On call every port in given
	 * collection must be checked against driver assumptions on
	 * data type, direction and other properties. Missing ports
	 * must be added. NO PORTS must be removed, but if some
	 * DriverPort has no counterpart in hardware, it must be marked
	 * as absent.
	 * @param ports Ports to check.
	 */
	protected abstract void updatePorts(Vector<DriverPort> ports);


	/**
	 * Connect driver ports to actual parameters.
	 */
	@Override
	public void connectPorts() {
		updatePorts(ports);
		for(DriverPort port : ports)
		{
			if(port != null)
				port.connectToParameter();
		}
	}

	/**
	 * Disconnect driver ports from parameters.
	 */
	@Override
	public void disconnectPorts() {
		for(DriverPort port : ports)
		{
			if(port != null)
				port.disconnectFromParameter();
		}
	}


	protected void makePortsPatchList() {
		StringBuilder sb = new StringBuilder();

		sb.append("<table><th>No.</th><th>Dir</th><th>Name</th><th>Parameter</th><th>Legend</th>");

		for(DriverPort port : ports)
		{
			if(port == null)
				continue;

			CliParameter target = port.getTarget();

			if(target == null)
				continue;
			
			sb.append("<tr>");

			sb.append("<td>");	sb.append(port.getInternalPortIndex());	sb.append("</td>");
			sb.append("<td>");	sb.append(port.getDirection());	sb.append("</td>");
			sb.append("<td>");	sb.append(port.getName());	sb.append("</td>");


			if(target != null)
			{
				sb.append("<td>");	sb.append(target.getName());	sb.append("</td>");
				sb.append("<td>");	sb.append(target.getLegend());	sb.append("</td>");
			}

			sb.append("</tr>\n");


		}

		sb.append("</table>");
		
		new TextViewFrame(sb.toString());
	}

	
	/**
	 * Extracts port from sorted array by index or creates and puts it inside. 
	 * NB! Port absent state is cleared!
	 * @param ports Ports to find or add to.
	 * @param index Number of port to get or create.
	 * @param direction Direction to set on port.
	 * @param type Data type to set on port.
	 * @param hardwareName Hardware name to assign to port.
	 * @return Found or created port.
	 */
	public DriverPort getPort( Vector<DriverPort> ports, int index, 
			Direction direction, Type type, String hardwareName )
	{
		return DriverPort.getPort(this, ports, index, direction, type, hardwareName);
	}

	/**
	 * Extracts port from sorted array by index or creates and puts it inside. 
	 * NB! Port absent state is cleared!
	 * @param ports Ports to find or add to.
	 * @param index Number of port to get or create.
	 * @return Found or created port.
	 */
	public DriverPort getPort(Vector<DriverPort> ports, int index )
	{
		return DriverPort.getPort(this, ports, index);
	}
	
	

	/* (non-Javadoc)
	 * @see ru.dz.shipMaster.dev.IModule#destroy()
	 */
	public void destroy() {
		stop();
		disconnectPorts();
		ports.removeAllElements();
	}

}
