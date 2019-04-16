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
 * Boolean 'or' element (soft driver).
 * @author dz
 */
public class BooleanOr extends AbstractDriver {

	private static final int N_INPUTS = 6;

	private DriverPort resultPort;

	private DriverPort[] inPort = new DriverPort[N_INPUTS];
	protected double[] inVal = new double[N_INPUTS];

	private boolean haveSome = false;

	private Object sema = new Object();

	public BooleanOr() {
	}

	@Override
	protected void doStartDriver() throws CommunicationsException {
	}

	@Override
	protected void doStopDriver() throws CommunicationsException {
	}

	@Override
	public String getDeviceName() { return "BooleanOr"; }

	@Override
	public boolean isAutoSeachSupported() { return false; }


	@Override
	protected void updatePorts(Vector<DriverPort> ports) {
		DriverPort.absentize(ports);
		DriverPort.sortPorts(ports);

		for(int i = 0; i < N_INPUTS; i++)
			inPort[i] = makeIn(ports, i);
		
		resultPort = getPort(ports, N_INPUTS, Direction.Input, Type.Numeric, "Result");
	}

	// -----------------------------------------------------------
	// Implementation
	// -----------------------------------------------------------

	private DriverPort makeIn(Vector<DriverPort> ports, final int i) {
		DriverPort in = getPort(ports, i, Direction.Output, Type.Numeric, "in"+i );
		in.setPortDataOutput(new PortDataOutput() {

			@Override
			public void receiveDoubleData(double value) {
				synchronized (sema) {
					inVal[i] = value;
					haveSome = true;
					log.log(Level.FINEST,"in"+i+" = "+value);
					trySend();
				}
			}});
		
		return in;
	}

	protected void trySend() {
		// called in sema
		if( haveSome )
		{
			haveSome = false;
			boolean res = false;

			for(int i = 0; i < N_INPUTS; i++)
			{
				if(inVal[i] > 0.1)
				{
					res = true;
					break;
				}
			}
			
			resultPort.sendBooleanData( res );
			log.log(Level.FINEST,"out = "+res);
		}
	}

	// -----------------------------------------------------------
	// UI 
	// -----------------------------------------------------------


	@Override
	protected void setupPanel(JPanel panel) {
	}

	@Override
	protected void doLoadPanelSettings() {
	}

	@Override
	protected void doSavePanelSettings() {
	}

	@Override
	public String getInstanceName() {
		return "N/A";
	}




}
