package ru.dz.shipMaster.dev.controller.deif;

import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.io.IOException;
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

public class MTR2 extends BusBasedDriver<ModBus> {

	private JTextField unitBaseField = new JTextField(6);
	private JCheckBox  unitActiveCheckbox = new JCheckBox();
	//private JCheckBox [] unitDebugCheckbox = { new JCheckBox(), new JCheckBox(), new JCheckBox(), new JCheckBox() };
	private int unitBase = 0;

	private MTR2Defines defines = new MTR2Defines();


	public MTR2() {
		super(1000, Thread.NORM_PRIORITY, "MTR2 updater");
	}


	@Override
	protected boolean correctBusType(IBus bus) {
		return bus instanceof ModBus;
	}



	@Override
	protected void updatePorts(Vector<DriverPort> ports) {

		DriverPort.absentize(ports);
		DriverPort.sortPorts(ports);

		int pos = 0;

		for( DeifMtr2Input i : inputs )
		{
			i.port = getPort(ports, pos++, Direction.Input, Type.Numeric, i.name );
			if(i.description != null)
				i.port.setDescription(i.description);
		}

	}

	@Override
	protected void setupPanel(JPanel panel) {
		super.setupPanel(panel);
		
		panel.add(new JLabel(String.format("Address")), consL );
		panel.add(unitBaseField, consR);

		panel.add(new JLabel(String.format("Active")), consL );
		panel.add(unitActiveCheckbox, consR);
		unitActiveCheckbox.setEnabled(false);

		{
			JPanel uPanel = new JPanel(new GridLayout(4,4));

			consL.gridwidth = 2;
			consL.fill = GridBagConstraints.HORIZONTAL;
			panel.add(new JLabel("Hardware options"), consL );
			panel.add(uPanel,consL);
			consL.gridwidth = 1;

			for(int i = 0; i < 16; i++)
			{
				defines.hConfBoxes[i].setEnabled(false);
				uPanel.add( defines.hConfBoxes[i] );
			}
		}

	}






	@Override
	protected void doLoadPanelSettings() {
		super.doLoadPanelSettings();
		unitBaseField.setText(unitBase < 0 ? "" : Integer.toString(unitBase));
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

		try {
			showMessage("Device model is " + bus.readString(unitBase, 1, 16) );
			int hConf = bus.readInputRegister(unitBase, 29);

			for(int i = 0; i < 16; i++)
				defines.hConfBoxes[i].setSelected( ((hConf>>i) & 0x1) != 0 );

		} catch (ModBusException e) {
			log.log(Level.SEVERE,"Can't read device configuration",e);
		}

	}

	@Override
	protected void doStopDriver() throws CommunicationsException {
		markUnitPassive();
	}

	@Override
	public String getDeviceName() {
		return "Deif MTR-2 power parameters module";
	}



	@Override
	public boolean isAutoSeachSupported() { return false; }


	@Override
	protected void doDriverTask() throws Throwable {
		for( DeifMtr2Input i : inputs )
		{
			try {
				i.process();
			} catch (ModBusException e) {
				log.log(Level.SEVERE, "Modbus IO error", e);
				failCounter.noteFailure();
			}
		}
	}





	private void markUnitPassive() { unitActiveCheckbox.setSelected(false); }
	private void markUnitActive() {  unitActiveCheckbox.setSelected(true);  }


	// -----------------------------------------------------------
	// test main 
	// -----------------------------------------------------------

	public static void main(String args[]) throws IOException, ModBusException, CommunicationsException 
	{
		MTR2 w = new MTR2();

		//w.setIpAddressString("192.168.1.240");
		//w.setIpAddressString("192.168.1.103");
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
	}


	// -----------------------------------------------------------
	// getters/setters 
	// -----------------------------------------------------------


	public int getUnitBase() {		return unitBase;	}
	public void setUnitBase(int unitBase) {		this.unitBase = unitBase;	}



	// -----------------------------------------------------------
	// Inputs map 
	// -----------------------------------------------------------

	// See MTR2 reference
	enum ConversionType {
		T5,	T6, T7, T17

	}

	class DeifMtr2Input
	{
		private final String description;
		private static final int ADDR_SHIFT = 30000;
	
		
		public DeifMtr2Input(String name, int address, ConversionType conv,
				DriverPort port) {
			this.name = name;
			this.address = address-ADDR_SHIFT;
			this.conv = conv;
			this.port = port;
			this.description = null;
		}

		public DeifMtr2Input(String name, int address, ConversionType conv ) {
			this.name = name;
			this.address = address-ADDR_SHIFT;
			this.conv = conv;
			this.port = null;
			this.description = null;
		}

		public DeifMtr2Input(String name, int address, ConversionType conv,
				String description) {
			this.name = name;
			this.address = address-ADDR_SHIFT;
			this.conv = conv;
			this.description = description;
			this.port = null;
		}


		String 			name;
		int				address;
		ConversionType	conv;
		DriverPort		port;

		int read32bits(int addr) throws ModBusException
		{
			try {
				return bus.read32bits( unitBase, addr );
			} catch(ModBusIOException e)
			{
				return bus.read32bits( unitBase, addr );
			}
		}

		int read16bits(int addr) throws ModBusException
		{
			try {
				return bus.readInputRegister( unitBase, addr );
			} catch(ModBusIOException e)
			{
				return bus.readInputRegister( unitBase, addr );
			}
		}

		void process() throws ModBusException
		{
			switch(conv)
			{
			case T5: // TODO this has to be unsigned conversion!
			case T6:
			{
				float val = Float.intBitsToFloat( read32bits(address) );
				if( port != null ) port.sendDoubleData(val);
				markUnitActive();
				show(val);
			}
			break;

			case T7:
			{
				/*
				 * Power factor (32 bit)
				 * Sign: Import/export (00/FF)
				 * Sign: Inductive/capacitive (00/FF)
				 * Unsigned value (16 bit), 4 decimal places
				 * Example: 0.9876 CAP stored as 00FF 2694
				 */
				int v = read32bits(address);
				// use only import/export byte
				boolean sign = ((v >> 24) & 0xFF) != 0;

				v &= 0xFFFF;

				if(sign) v |= 0xFFFF0000;
				if( port != null ) port.sendDoubleData(v);
				markUnitActive();
				show(v);
			}
			break;

			case T17:
			{
				double v = read16bits(address);
				if( port != null ) port.sendDoubleData(v/100);
				markUnitActive();
				show(v);
			}
			break;
			}
		}

		private void show(double val) {
			//System.out.println("MTR2 "+name+"="+val);			
		}

	};

	DeifMtr2Input[] inputs = {
			new DeifMtr2Input( "Frequency", 30049, ConversionType.T5 ),
			new DeifMtr2Input( "Frequency1", 30051, ConversionType.T5 ),
			new DeifMtr2Input( "Frequency2", 30053, ConversionType.T5 ),
			new DeifMtr2Input( "Frequency3", 30055, ConversionType.T5 ),
			new DeifMtr2Input( "U1", 30057, ConversionType.T5 ),
			new DeifMtr2Input( "U2", 30059, ConversionType.T5 ),
			new DeifMtr2Input( "U3", 30061, ConversionType.T5 ),
			new DeifMtr2Input( "Uavg P0", 30063, ConversionType.T5, "Phase to neutral" ),
			new DeifMtr2Input( "j12", 30065, ConversionType.T17, "angle between U1 and U2" ),
			new DeifMtr2Input( "j23", 30066, ConversionType.T17, "angle between U2 and U3" ),
			new DeifMtr2Input( "j31", 30067, ConversionType.T17, "angle between U3 and U1" ),
			new DeifMtr2Input( "U12", 30068, ConversionType.T5 ),
			new DeifMtr2Input( "U23", 30070, ConversionType.T5 ),
			new DeifMtr2Input( "U31", 30072, ConversionType.T5 ),
			new DeifMtr2Input( "Uavg PP", 30074, ConversionType.T5, "Phase to phase" ),
			new DeifMtr2Input( "I1", 30076, ConversionType.T5 ),
			new DeifMtr2Input( "I2", 30078, ConversionType.T5 ),
			new DeifMtr2Input( "I3", 30080, ConversionType.T5 ),
			new DeifMtr2Input( "IN", 30082, ConversionType.T5, "Neutral current?" ),
			new DeifMtr2Input( "IE", 30084, ConversionType.T5, "Current error?" ),
			new DeifMtr2Input( "Iavg", 30086, ConversionType.T5 ),
			new DeifMtr2Input( "S I", 30088, ConversionType.T5 ),

			new DeifMtr2Input( "Active power total", 30090, ConversionType.T6 ),
			new DeifMtr2Input( "Active Power Phase L1", 30092, ConversionType.T6 ),
			new DeifMtr2Input( "Active Power Phase L2", 30094, ConversionType.T6 ),
			new DeifMtr2Input( "Active Power Phase L3", 30096, ConversionType.T6 ),
			new DeifMtr2Input( "Reactive Power Total", 30098, ConversionType.T6, "VAr L (if > 0) VAr C (if < 0)" ),
			new DeifMtr2Input( "Reactive Power Phase L1", 30100, ConversionType.T6, "VAr L (if > 0) VAr C (if < 0)" ),
			new DeifMtr2Input( "Reactive Power Phase L2", 30102, ConversionType.T6, "VAr L (if > 0) VAr C (if < 0)" ),
			new DeifMtr2Input( "Reactive Power Phase L3", 30104, ConversionType.T6, "VAr L (if > 0) VAr C (if < 0)" ),

			new DeifMtr2Input( "Apparent Power Total", 30106, ConversionType.T5 ),
			new DeifMtr2Input( "Apparent Power Phase L1", 30108, ConversionType.T5 ),
			new DeifMtr2Input( "Apparent Power Phase L2", 30110, ConversionType.T5 ),
			new DeifMtr2Input( "Apparent Power Phase L3", 30112, ConversionType.T5 ),

			new DeifMtr2Input( "Power Factor Total", 30114, ConversionType.T7 ),
			new DeifMtr2Input( "Power Factor Phase 1", 30116, ConversionType.T7 ),
			new DeifMtr2Input( "Power Factor Phase 2", 30118, ConversionType.T7 ),
			new DeifMtr2Input( "Power Factor Phase 3", 30120, ConversionType.T7 ),

			new DeifMtr2Input( "Power Angle Total", 30122, ConversionType.T17, "atan2(Pt,Qt)" ),
			new DeifMtr2Input( "j1 (angle between U1 and I1)", 30123, ConversionType.T17 ),
			new DeifMtr2Input( "j2 (angle between U2 and I2)", 30124, ConversionType.T17 ),
			new DeifMtr2Input( "j3 (angle between U3 and I3)", 30125, ConversionType.T17 ),

			new DeifMtr2Input( "Internal temperature", 30126, ConversionType.T5 ),


	};





}
