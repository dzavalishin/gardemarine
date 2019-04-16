package ru.dz.shipMaster.dev.bus;

import java.io.IOException;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ru.dz.shipMaster.data.misc.CommunicationsException;
import ru.dz.shipMaster.dev.bus.ModBusProtocolAdapter.Mode;
import ru.dz.shipMaster.dev.controller.ModBusException;
import ru.dz.shipMaster.dev.controller.ModBusIOException;

public class ModBus extends AbstractPipedBus {

	@Override
	public String getDeviceName() {		return "ModBus";	}

	@Override
	public boolean isAutoSeachSupported() {		return false;	}

	private ModBusProtocolAdapter protocol = new ModBusProtocolAdapter();
	private static final ModBusProtocolAdapter.Mode[] modes = {
		ModBusProtocolAdapter.Mode.Ascii,
		ModBusProtocolAdapter.Mode.RTU,
		ModBusProtocolAdapter.Mode.TCP,
	};
	
	private JComboBox modeField = new JComboBox(modes);
	private ModBusProtocolAdapter.Mode mode = ModBusProtocolAdapter.Mode.Ascii;


	public ModBusProtocolAdapter.Mode getMode() {		return mode;	}
	public void setMode(ModBusProtocolAdapter.Mode mode) {		this.mode = mode;	}

	
	
	
	@Override
	protected void doStartBus() throws CommunicationsException {
		//pipe.getPipe().addReceiveListener(recv);
		//pipe.getPipe().addErrorListener(err);
		pipe.getPipe().connect();
		protocol.setPipe(pipe.getPipe());
		protocol.setMode(mode);
	}


	@Override
	protected void doStopBus() throws CommunicationsException {
		pipe.getPipe().disconnect();
		//pipe.getPipe().removeReceiveListener(recv);
		//pipe.getPipe().removeErrorListener(err);
		protocol.setPipe(null);
	}







	@Override
	protected void setupPanel(JPanel panel) {
		super.setupPanel(panel);
		
		panel.add(new JLabel("Mode"),consL);
		panel.add( modeField ,consR);
		
	}


	@Override
	public void doSavePanelSettings() {
		super.doSavePanelSettings();
		mode = (Mode) modeField.getSelectedItem();
		//if( (pipe != null) && (pipe.getPipe() instanceof TcpBiPipe) )			mode = Mode.TCP;
	}	


	@Override
	public void doLoadPanelSettings() {
		super.doLoadPanelSettings();
		modeField.setSelectedItem(mode);
	}









	/**
	 * Force single coil (MODBUS FC05) <br><br>
	 * 
	 * Turn on or off a single coil (digital output) on the slave device
	 *  
	 * @param coilAddr Number of digital out to set.
	 * @param state Turn output on or off.
	 * @throws ModBusException
	 */
	public synchronized void forceSingleCoil(int slave, int coilAddr, boolean state)
	throws ModBusException {
		protocol.setSlave(slave);
		protocol.force_single_coil(coilAddr, state);
	}


	/**
	 * Set multiple coils (MODBUS FC15) <br><br>
	 * 
	 * Takes an array of flags and sets or resets the coils on a slave appropriately.
	 * Coil is an one bit digital output. 
	 * 
	 * @param startAddr Address of first coil to set
	 * @param count Number of coils to set
	 * @param data Values to set coils to
	 * 
	 * @throws ModBusException
	 */

	public synchronized void setMultipleCoils(int slave, int startAddr, int count, boolean[] data)
	throws ModBusException {
		protocol.setSlave(slave);
		protocol.set_multiple_coils(startAddr, count, data);
	}




	/** 
	 * Preset multiple registers (MODBUS FC16) <br><br>
	 * 
	 * Copy the values in an array to an array of registers on the slave.
	 * Note that writing to two registers starting at, for example, 0x10 <b>MAY BE NOT THE SAME</b> as
	 * writing to registers 0x10 and 0x11 in separate calls.
	 *  
	 * @param start_addr Register address
	 * @param reg_count Number of registers to set
	 * @param data Values to set registers to
	 * @throws IOException 
	 * @throws ModBusException Modbus replied with an error.
	 */

	public synchronized void presetMultipleRegisters(int slave, int startAddr, int reg_count,
			int[] data) throws ModBusException {
		protocol.setSlave(slave);
		protocol.preset_multiple_registers(startAddr, reg_count, data);
	}


	/** 
	 * Preset single register (MODBUS FC06) <br><br>
	 * 
	 * Sets a value in one holding register (analog output, for example) in the slave device.
	 * 
	 * @param regAddr Address of register to set. 
	 * @param value New value.
	 *  
	 * @throws ModBusException 
	 */

	public synchronized void presetSingleRegister(int slave, int regAddr, int value)
	throws ModBusException {
		protocol.setSlave(slave);
		protocol.preset_single_register(regAddr, value);
	}


	/** 
	 * Read coil status (MODBUS FC01) <br><br>
	 *
	 * Read digital outputs.
	 * 
	 * reads the boolean status of coils and sets the array elements in the 
	 * destination to TRUE or FALSE
	 * 
	 * @param startAddr Address of starting coil to read
	 * @param count Number of coils to read
	 * @param dest Array to put received data to
	 *  
	 * @throws IOException 
	 * @throws ModBusException 
	 */	

	public synchronized void readCoilStatus(int slave, int startAddr, int count, boolean[] dest)
	throws ModBusException {
		protocol.setSlave(slave);
		protocol.read_coil_status(startAddr, count, dest);
	}


	/** 
	 * Read holding registers (MODBUS FC03) <br><br>
	 * 
	 * Read the holding registers (analog outputs) in a slave and put the data into an array.
	 * 
	 * @param startAddr Number of starting register to read. 
	 * @param count Number of registers to read.
	 * @param dest Buffer to read to.
	 *  
	 * @throws ModBusException 
	 */

	public synchronized void readHoldingRegisters(int slave, int startAddr, int count, int[] dest)
	throws ModBusException {
		protocol.setSlave(slave);
		protocol.read_holding_registers(startAddr, count, dest);
	}

	/** 
	 * Read input registers (MODBUS FC04) <br><br>
	 * 
	 * Read the input registers (analog inputs) in a slave and put the data into an array.
	 * 
	 * @param startAddr Number of starting register to read.
	 * @param count Number of registers to read.
	 * @param dest Array to put read data to.
	 *  
	 * @throws ModBusException 
	 */

	public synchronized void readInputRegisters(int slave, int startAddr, int count, int[] dest)
	throws ModBusException {
		protocol.setSlave(slave);
		protocol.read_input_registers(startAddr, count, dest);
	}


	/**
	 * Read one input register. (MODBUS FC04) <br><br>
	 * 
	 * @param regAddr address of register to read.
	 * @return Register value.
	 * @throws ModBusException in case of some problem, such as incorrect register address.
	 */

	public synchronized int readInputRegister(int slave, int regAddr) throws ModBusException {
		protocol.setSlave(slave);
		return protocol.readInputRegister(regAddr);
	}



	/** 
	 * Read input status  (MODBUS FC02) <br><br>
	 * 
	 * Read digital inputs.
	 *  
	 * @param startAddr Address of starting input to read.
	 * @param count Number of inputs to read.
	 * @param dest Array to put received data to.
	 * 
	 * @throws ModBusException 
	 */

	public synchronized void readInputStatus(int slave, int startAddr, int count, boolean[] dest)
	throws ModBusException {
		protocol.setSlave(slave);
		protocol.read_input_status(startAddr, count, dest);
	}



	/** 
	 * read32bits (uses MODBUS FC04) <br><br>
	 * 
	 * Read 2 input registers, return as 32 bit integer.
	 * 
	 * @param startAddr Number of starting register to read.
	 *  
	 * @throws ModBusIOException in case of IO failure
	 * @throws ModBusException on other errors
	 */

	public synchronized int read32bits(int slave, int startAddr) throws ModBusException {
		protocol.setSlave(slave);
		return protocol.read32bits(startAddr);
	}


	/** 
	 * readFloat (uses MODBUS FC04) <br><br>
	 * 
	 * Read 2 input registers, return as 32 bit float.
	 * 
	 * @param startAddr Number of starting register to read.
	 *  
	 * @throws ModBusIOException in case of IO failure
	 * @throws ModBusException on other errors
	 */

	public synchronized float readFloat(int slave, int startAddr) throws ModBusException {
		protocol.setSlave(slave);
		return protocol.readFloat(startAddr);
	}


	/** 
	 * readString (uses MODBUS FC04) <br><br>
	 * 
	 * Read some input registers, return as String. LATIN1 code is assumed.
	 * 
	 * @param startAddr Number of starting register to read.
	 * @param stringByteCount number of bytes (characters) to read.
	 *  
	 * @throws ModBusIOException in case of IO failure
	 * @throws ModBusException on other errors
	 */

	public synchronized String readString(int slave, int startAddr, int stringByteCount)
	throws ModBusException {
		protocol.setSlave(slave);
		return protocol.readString(startAddr, stringByteCount);
	}







}
