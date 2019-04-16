package ru.dz.shipMaster.dev.loop;

import java.util.Vector;
import java.util.logging.Level;

import javax.swing.JPanel;

import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.data.misc.CommunicationsException;
import ru.dz.shipMaster.dev.AbstractDriver;
import ru.dz.shipMaster.dev.DriverPort;
import ru.dz.shipMaster.dev.PortDataOutput;

/**
 * Values divide element (soft driver).
 * @author dz
 */
//public class Subtractor extends ThreadedDriver {
public class Divider extends AbstractDriver {

	private DriverPort resultPort;

	private DriverPort in1, in2;
	protected double in1v;
	protected double in2v;

	private boolean have1 = false, have2 = false;

	private Object sema = new Object();

	public Divider() {
		//super(60000, Thread.NORM_PRIORITY, "Subtractor");
	}

	/*
	@Override
	protected void doDriverTask() throws Throwable {
	}*/



	@Override
	protected void doStartDriver() throws CommunicationsException {
	}

	@Override
	protected void doStopDriver() throws CommunicationsException {
	}

	@Override
	public String getDeviceName() { return "Divider"; }

	@Override
	public boolean isAutoSeachSupported() { return false; }


	@Override
	protected void updatePorts(Vector<DriverPort> ports) {
		DriverPort.absentize(ports);
		DriverPort.sortPorts(ports);

		in1 = getPort(ports, 0, Direction.Output, Type.Numeric, "In1");
		in1.setDescription("Value to divide.");
		in1.setPortDataOutput(new PortDataOutput() {

			@Override
			public void receiveDoubleData(double value) {
				synchronized (sema) {
					in1v = value;
					have1 = true;
					log.log(Level.FINEST,"in1 = "+value);
					trySend();
				}
			}});

		in2 = getPort(ports, 1, Direction.Output, Type.Numeric, "In2");
		in2.setDescription("Value to divide by.");
		in2.setPortDataOutput(new PortDataOutput() {

			@Override
			public void receiveDoubleData(double value) {
				synchronized (sema) {
					in2v = value;
					have2 = true;
					log.log(Level.FINEST,"in2 = "+value);
					trySend();
				}
			}});

		resultPort = getPort(ports, 2, Direction.Input, Type.Numeric, "Result");
	}

	// -----------------------------------------------------------
	// Implementation
	// -----------------------------------------------------------

	protected void trySend() {
		// called in sema
		if( have1 && have2 )
		{
			have1 = have2 = false;
			resultPort.sendDoubleData(in1v / in2v);
			log.log(Level.FINEST,"out = "+(in1v / in2v));
		}
	}

	// -----------------------------------------------------------
	// UI 
	// -----------------------------------------------------------

	//private JTextField fileNameField = new JTextField();
	//private String fileName = "";


	@Override
	protected void setupPanel(JPanel panel) {
		//panel.add(new JLabel("Store file name:"), consL);
		//panel.add(fileNameField, consR);
	}

	@Override
	protected void doLoadPanelSettings() {
		//fileNameField.setText(fileName);
	}

	@Override
	protected void doSavePanelSettings() {
		//fileName = fileNameField.getText();
	}

	@Override
	public String getInstanceName() {
		return "N/A";
	}




}
