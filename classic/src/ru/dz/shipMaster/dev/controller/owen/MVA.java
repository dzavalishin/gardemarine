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
import ru.dz.shipMaster.dev.bus.ModBus;
import ru.dz.shipMaster.dev.controller.BinaryTools;
import ru.dz.shipMaster.dev.controller.ModBusException;
import ru.dz.shipMaster.dev.controller.ModBusIOException;

public class MVA extends BusBasedDriver<ModBus> {


	private JTextField unitBaseField = new JTextField(6);
	private JCheckBox unitActiveCheckbox = new JCheckBox();
	//private JCheckBox [] unitDebugCheckbox = { new JCheckBox(), new JCheckBox(), new JCheckBox(), new JCheckBox() };
	private int unitBase = 16;


	public MVA() {
		super(1000, Thread.NORM_PRIORITY, "Owen MVA updater");
	}


	@Override
	protected boolean correctBusType(IBus bus) {
		return bus instanceof ModBus;
	}

	private int nInputPorts = 8;
	DriverPort[] inputPorts = new DriverPort[ nInputPorts ];


	@Override
	protected void updatePorts(Vector<DriverPort> ports) {

		DriverPort.absentize(ports);
		DriverPort.sortPorts(ports);

		int i;

		for( i = 0; i < nInputPorts; i++ )
		{
			String portName = "An. I "+i;
			inputPorts[i] = getPort(ports, i, Direction.Input, Type.Numeric, portName );
		}
	}

	@Override
	protected void setupPanel(JPanel panel) {
		super.setupPanel(panel);

		panel.add(new JLabel("ModBus base address:"), consL);
		panel.add(unitBaseField, consR);

		panel.add(new JLabel("Active:"), consL);
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
		return "Owen MVA analogue inputs module";
	}



	@Override
	public boolean isAutoSeachSupported() { return false; }


	private double [] vals = new double[8];
	@Override
	protected void doDriverTask() throws Throwable {


		for( int i = 0; i < 8; i++ )
		{
			// Give MVA and TCP gate some time to prepare to the next cmd
			if( i > 0 )
				BinaryTools.sleepMsec(30);

			float val;

			try {
				val = bus.readFloat(unitBase, i*6 + 4);
			} catch(ModBusIOException e)
			{
				try {
					val = bus.readFloat(unitBase, i*6 + 4);
				} catch(ModBusIOException e1)
				{
					log.log(Level.SEVERE, "Owen IO error", e1);
					failCounter.noteFailure();
					continue;
				}
			}
			final int pno = i;
			vals[i] = val;
			if( inputPorts[pno] != null ) inputPorts[pno].sendDoubleData(val);
			markUnitActive();
			failCounter.noteSuccess();
		}
		//log.log(Level.SEVERE, String.format("MVA%d is %f %f %f %f %f %f %f %f\n", unit, vals[0], vals[1], vals[2], vals[3], vals[4], vals[5], vals[6], vals[7] ));


	}





	private void markUnitPassive() { unitActiveCheckbox.setSelected(false); }
	private void markUnitActive() {  unitActiveCheckbox.setSelected(true);  }

	/**
	 * 
	 * @param out 3-word buffer for mac-address
	 * @throws ModBusException
	 * /
	protected void getMacAddress(int [] out) throws ModBusException 
	{ 
		protocolDriver.read_input_registers( 0x1031, 3, out );
	}


	protected void readWagoConfiguration() throws ModBusException
	{
			int maxRegs = 65;
			int [] dest = new int[maxRegs];

			protocolDriver.read_input_registers( 0x2030, maxRegs, dest );	
			loadConfigWords(maxRegs, dest);

			/*
			maxRegs = 64;
			protocolDriver.read_input_registers( 1, 0x2031, maxRegs, dest );	
			loadConfigWords(maxRegs, dest);

			protocolDriver.read_input_registers( 1, 0x2032, maxRegs, dest );	
			loadConfigWords(maxRegs, dest);

			maxRegs = 63;
			protocolDriver.read_input_registers( 1, 0x2033, maxRegs, dest );	
			loadConfigWords(maxRegs, dest);
	 * /
	}

	private void loadConfigWords(int maxWords, int[] configWords) {
		for( int wordNo = 0; wordNo < maxWords; wordNo++ )
		{
			int config = configWords[wordNo];
			if(config != 0)
				loadConfigWord(config);
		}
	}


	private List<WagoModule> wagoModules = new LinkedList<WagoModule>();

	private void loadConfigWord(int config) {
		String desc;
		if((config & 0x8000) != 0)
		{
			boolean isInput = (config & 0x1) != 0;
			boolean isOutput = (config & 0x2) != 0;
			int nBits = 0x7F & (config >> 8);

			desc = "Digital IO module: "+
			(isInput ? "in" : (isOutput ? "out" : "unknown")) +
			", " + nBits +  " bits";			
		}
		else
		{
			desc = "Module: "+config;			
		}

		log.severe(desc);
		wagoModules.add(new WagoModule(config,desc));
	}


	class WagoModule
	{
		int		moduleType; // as returned by modbus 0x2030 register
		String	description;

		public WagoModule(int moduleType, String description) {
			this.moduleType = moduleType;
			this.description = description;
		}

	}*/

	// -----------------------------------------------------------
	// test main 
	// -----------------------------------------------------------

	/*public static void main(String args[]) throws IOException, ModBusException, CommunicationsException 
	{
		MVA w = new MVA();

		w.start();
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
	}*/


	// -----------------------------------------------------------
	// getters/setters 
	// -----------------------------------------------------------


	public int getUnitBase() {		return unitBase;	}
	public void setUnitBase(int unitBase) {		this.unitBase = unitBase;	}




}
