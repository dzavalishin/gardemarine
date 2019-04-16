package ru.dz.shipMaster.dev.controller.owen;


import java.util.Vector;
import java.util.logging.Level;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.data.misc.CommunicationsException;
import ru.dz.shipMaster.dev.BusBasedDriver;
import ru.dz.shipMaster.dev.DriverPort;
import ru.dz.shipMaster.dev.IBus;
import ru.dz.shipMaster.dev.PortDataOutput;
import ru.dz.shipMaster.dev.bus.ModBus;
import ru.dz.shipMaster.dev.controller.BinaryTools;
import ru.dz.shipMaster.dev.controller.ModBusException;
import ru.dz.shipMaster.dev.controller.ModBusIOException;

public class MDVV extends BusBasedDriver<ModBus> {

	private static final int OUTPUTS_PER_UNIT = 8;
	private static final int INPUTS_PER_UNIT = 12;

	private JTextField unitBaseField = new JTextField(6);
	private JCheckBox unitActiveCheckbox = new JCheckBox();
	private int unitBase = 16;


	public MDVV() {
		super(1000, Thread.NORM_PRIORITY, "Owen MDVV updater");
	}


	@Override
	protected boolean correctBusType(IBus bus) {
		return bus instanceof ModBus;
	}


	
	
	private DriverPort[] outputPorts = new DriverPort[ OUTPUTS_PER_UNIT ];  

	private int nInputPorts = INPUTS_PER_UNIT*2;
	private DriverPort[] inputPorts = new DriverPort[ nInputPorts ];
	private int[] countReadBuffer = new int[INPUTS_PER_UNIT];


	static int calcInPortNo(  int input, boolean isCounter )
	{
		return input*2 + (isCounter ? 1 : 0);
	}


	@Override
	protected void updatePorts(Vector<DriverPort> ports) {

		DriverPort.absentize(ports);
		DriverPort.sortPorts(ports);

		for( int p = 0; p < INPUTS_PER_UNIT; p++ )
		{
			String portName = "DIn "+p;
			inputPorts[calcInPortNo( p, false)] = getPort(ports, OUTPUTS_PER_UNIT + calcInPortNo( p, false ), Direction.Input, Type.Boolean, portName );
			portName = "Cnt "+p;
			inputPorts[calcInPortNo( p, true)] = getPort(ports, OUTPUTS_PER_UNIT + calcInPortNo( p, true ), Direction.Input, Type.Numeric, portName );
			inputPorts[calcInPortNo( p, true)].setDescription("Counter value");
		}

		for( int p = 0; p < OUTPUTS_PER_UNIT; p++ )
		{
			String portName = "DOut "+p;
			outputPorts[p] = getPort(ports, p, Direction.Output, Type.Boolean, portName );

			final int controllerPortNo = p;

			outputPorts[controllerPortNo].setPortDataOutput(
					new PortDataOutput() {
						@Override
						public void receiveDoubleData(double value) {
							try {
								log.severe("Set out: "+value);
								//protocolDriver.preset_single_register( controllerPortNo, (int)(1000*value) );

								int[] data = new int[1];
								data[0] = (int)(1000*value);
								bus.presetMultipleRegisters( unitBase, controllerPortNo, 1, data);
							} catch (ModBusException e) {
								log.log(Level.SEVERE,"can't send data", e);
							}
						}});

		}



	}

	@Override
	protected void setupPanel(JPanel panel) {
		super.setupPanel(panel);
		panel.add(new JLabel("Address"), consL );
		panel.add(unitBaseField, consR);

		panel.add(new JLabel( "Active" ), consL );
		panel.add(unitActiveCheckbox, consR);
		unitActiveCheckbox.setEnabled(false);
	}


	@Override
	protected void doLoadPanelSettings() {
		super.doLoadPanelSettings();

		unitBaseField.setText(Integer.toString(unitBase));
		unitActiveCheckbox.setSelected(false);

	}

	@Override
	protected void doSavePanelSettings() {
		super.doSavePanelSettings();


		try { unitBase = Integer.parseInt( unitBaseField.getText() ); }
		catch( NumberFormatException e ) { /* ignore */ }

		unitActiveCheckbox.setSelected(false);		

	}


	@Override
	protected void doStartDriver() throws CommunicationsException {
		markUnitPassive();
	}

	@Override
	protected void doStopDriver() throws CommunicationsException {
	}

	@Override
	public String getDeviceName() {
		return "Owen MDVV digital IO module";
	}


	@Override
	public boolean isAutoSeachSupported() { return false; }

	@Override
	protected void doDriverTask() throws Throwable {


		// Give MDVV and TCP gate some time to prepare to the next cmd
		BinaryTools.sleepMsec(20);

		try {
			int inputs = bus.readInputRegister(unitBase, 0x33) & 0x7F;

			log.log(Level.FINEST, String.format("MDVV inputs are %02X\n", inputs));
			markUnitActive();

			for( int port = 0; port < INPUTS_PER_UNIT; port ++ )
			{
				int val = inputs & 0x01;
				inputs >>= 1;

			DriverPort driverPort = inputPorts[calcInPortNo(port, false)];
			if( driverPort != null ) driverPort.sendDoubleData( (val != 0) ? 1.0 : 0.0 );
			}

			bus.readInputRegisters(unitBase, 0x40, INPUTS_PER_UNIT, countReadBuffer );
			// Don't note success at all, or else even one active unit will prevent restart
			//failCounter.noteSuccess();

			for( int port = 0; port < INPUTS_PER_UNIT; port ++ )
			{
				DriverPort driverPort = inputPorts[calcInPortNo( port, true )];

				//if( driverPort == null )						continue;

				// Don't ask conter value for inactive ports
				//if( driverPort.getTarget() == null )						continue;

				//log.log(Level.SEVERE, String.format("MDVV %d.%d counter is %d\n", unit, port, countReadBuffer[port]));

				if( driverPort == null )						continue;

				// NB - unsigned 16 bit!
				driverPort.sendDoubleData(countReadBuffer[port] & 0xFFFF);
				log.log(Level.FINER, String.format("MDVV %d counter is %d\n", port, countReadBuffer[port]));
			}


		} catch(ModBusIOException e)
		{
			log.log(Level.SEVERE, "Owen IO error", e);
			failCounter.noteFailure();
		}


	}




	private void markUnitPassive() { unitActiveCheckbox.setSelected(false); }
	private void markUnitActive() {  unitActiveCheckbox.setSelected(true);  }


	// -----------------------------------------------------------
	// test main 
	// -----------------------------------------------------------
	/*
	public static void main(String args[]) throws IOException, ModBusException, CommunicationsException 
	{
		MDVV w = new MDVV();

		w.setIpAddressString("192.168.0.240");
		//w.setIpAddressString("192.168.1.103");
		w.start();

		w.protocolDriver.setSlave(32);
		//w.protocolDriver.preset_single_register( 0x32, 0 );

		int[] data = new int[1];
		data[0] = 0x000;
		w.protocolDriver.preset_multiple_registers(0x32, 1, data);

		//w.readWagoConfiguration();
		for(int i = 0; i < 10; i++ )
		{
			try {
				//w.doDriverTask();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		BinaryTools.sleepMsec(20000);
		w.stop();
	}

	 */
	// -----------------------------------------------------------
	// getters/setters 
	// -----------------------------------------------------------


	public int getUnitBase() {		return unitBase;	}
	public void setUnitBase(int unitBase) {		this.unitBase = unitBase;	}




}
