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
 * Values subtract element (soft driver).
 * @author dz
 */
//public class Subtractor extends ThreadedDriver {
public class Adder extends AbstractDriver {

	private DriverPort resultPort;

	private DriverPort in1, in2, in3, in4;
	protected double in1v, in2v, in3v, in4v;

	private boolean have1 = false, have2 = false, have3 = false, have4 = false;

	private Object sema = new Object();

	public Adder() {
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
	public String getDeviceName() { return "Adder"; }

	@Override
	public boolean isAutoSeachSupported() { return false; }


	@Override
	protected void updatePorts(Vector<DriverPort> ports) {
		DriverPort.absentize(ports);
		DriverPort.sortPorts(ports);

		in1 = getPort(ports, 0, Direction.Output, Type.Numeric, "In1");
		//in1.setDescription("Value to subtract from.");
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
		//in2.setDescription("Value to subtract.");
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

		in3 = getPort(ports, 2, Direction.Output, Type.Numeric, "In3");
		//in2.setDescription("Value to subtract.");
		in3.setPortDataOutput(new PortDataOutput() {

			@Override
			public void receiveDoubleData(double value) {
				synchronized (sema) {
					in3v = value;
					have3 = true;
					log.log(Level.FINEST,"in3 = "+value);
					trySend();
				}
			}});

		in4 = getPort(ports, 3, Direction.Output, Type.Numeric, "In4");
		//in2.setDescription("Value to subtract.");
		in4.setPortDataOutput(new PortDataOutput() {

			@Override
			public void receiveDoubleData(double value) {
				synchronized (sema) {
					in4v = value;
					have4 = true;
					log.log(Level.FINEST,"in4 = "+value);
					trySend();
				}
			}});


		resultPort = getPort(ports, 4, Direction.Input, Type.Numeric, "Sum");
	}

	// -----------------------------------------------------------
	// Implementation
	// -----------------------------------------------------------

	protected void trySend() {
		// called in sema
		if( have1 && have2 && have3 && have4 )
		{
			have1 = have2 = have3 = have4 = false;
			resultPort.sendDoubleData( in1v + in2v + in3v + in4v );
			log.log(Level.FINEST,"out = "+(in1v + in2v + in3v + in4v));
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
