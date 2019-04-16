package ru.dz.shipMaster.dev.loop;

import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.data.misc.CommunicationsException;
import ru.dz.shipMaster.dev.AbstractDriver;
import ru.dz.shipMaster.dev.DriverPort;

/**
 * Constant generator (soft driver).
 * @author dz
 */
public class Constant extends AbstractDriver {

	private DriverPort resultPort;
	private double myConstant;

	@Override
	protected void doStartDriver() throws CommunicationsException {
		resultPort.sendDoubleData(myConstant);
	}

	@Override
	protected void doStopDriver() throws CommunicationsException {
	}

	@Override
	public String getDeviceName() { return "Constant"; }

	@Override
	public String getInstanceName() {
		return Double.toString(myConstant);
	}

	@Override
	public boolean isAutoSeachSupported() { return false; }


	@Override
	protected void updatePorts(Vector<DriverPort> ports) {
		DriverPort.absentize(ports);
		DriverPort.sortPorts(ports);

		resultPort = getPort(ports, 0, Direction.Input, Type.Numeric, "Constant");
	}


	// -----------------------------------------------------------
	// UI 
	// -----------------------------------------------------------

	private JTextField fileNameField = new JTextField();


	@Override
	protected void setupPanel(JPanel panel) {
		panel.add(new JLabel("Constant:"), consL);
		panel.add(fileNameField, consR);
	}

	@Override
	protected void doLoadPanelSettings() {
		fileNameField.setText(Double.toString(myConstant));
	}

	@Override
	protected void doSavePanelSettings() {
	
		try { myConstant = Double.parseDouble( fileNameField.getText() ); }
		catch( NumberFormatException e )
		{
			// Ignore?
		}

	}

	public double getConstant() {		return myConstant; 	}

	public void setConstant(double myConstant) {		this.myConstant = myConstant; 	}



	// -----------------------------------------------------------
	// Implementation
	// -----------------------------------------------------------



}
