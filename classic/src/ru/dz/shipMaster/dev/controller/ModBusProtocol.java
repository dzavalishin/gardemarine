package ru.dz.shipMaster.dev.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import ru.dz.shipMaster.data.misc.CommunicationsException;


/**
 * Simple Modbus protocol implementation.
 * 
 * (C) 2007 Digital Zone, http://www.dz.ru
 * 
 * @author dz
 *
 * This code is partially based on C sources (modbus_tcp.c) by P.Costigan
 *
 */

public abstract class ModBusProtocol {
	protected static final Logger log = Logger.getLogger(ModBusProtocol.class.getName()); 

	/** Used to interlock request/response pair. */
	private final Object ioLock = new Object(); 

	private static final int F_SET_MULTIPLE_REGISTERS = 0x10;
	private static final int F_PRESET_SINGLE_REGISTER = 0x06;
	private static final int F_FORCE_SINGLE_COIL = 0x05;
	private static final int F_SET_MULTIPLE_COILS = 0x0F;
	private static final int F_READ_INPUT_REGISTERS = 0x04;
	private static final int F_READ_HOLDING_REGISTERS = 0x03;
	private static final int F_READ_INPUT_STATUS = 0x02;
	private static final int F_READ_COIL_STATUS = 0x01;


	// -----------------------------------------------------------
	// Sizes 
	// -----------------------------------------------------------

	//private final static int PRESET_QUERY_SIZE = 210;
	//private final static int REQUEST_QUERY_SIZE = 12;
	//private final static int CHECKSUM_SIZE = 2;



	// -----------------------------------------------------------
	// Limits
	// -----------------------------------------------------------

	private final static int MAX_WRITE_COILS = 128; 
	private static final int MAX_WRITE_REGS = 128; 
	protected static final int MAX_RESPONSE_LENGTH = 1024*4; // TO DO check
	private static final int MAX_INPUT_REGS = 128; 
	private static final int MAX_READ_REGS = 128; 


	// -------------------------------------------
	// State
	//-------------------------------------------

	protected int slave = 0;

	// -------------------------------------------
	// Conversions
	//-------------------------------------------

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
	public int read32bits(int startAddr) throws ModBusException
	{
		int[] dest = new int[2];
		read_input_registers( startAddr, 2, dest);
		return ( ((dest[0] << 16) & 0xFFFF0000 | (dest[1] & 0xFFFF) ) );
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
	public float readFloat(int startAddr) throws ModBusException
	{
		return Float.intBitsToFloat( read32bits(startAddr) );
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
	public String readString(int startAddr, int stringByteCount) throws ModBusException
	{
		int regCount = ((stringByteCount-1)/2)+1;
		int[] regs = new int[regCount];
		byte[] bytes = new byte[stringByteCount];
		read_input_registers( startAddr, regCount, regs);

		for( int charNo = 0; charNo < stringByteCount; charNo++ )
		{
			if( (charNo & 1) != 0 )
				bytes[charNo] = (byte)(regs[charNo/2]);
			else
				bytes[charNo] = (byte)(regs[charNo/2] >> 8);
		}

		try {
			return new String(bytes,"LATIN1");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	// -------------------------------------------
	// Interface
	//-------------------------------------------

	/** 
	 * read_coil_status (MODBUS FC01) <br><br>
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
	public synchronized void read_coil_status( int startAddr, int count, boolean []dest ) throws ModBusException {
		readIoStatus( F_READ_COIL_STATUS, startAddr, count, dest );
	}

	/** 
	 * read_input_status  (MODBUS FC02) <br><br>
	 * 
	 * Read digital inputs.
	 *  
	 * @param startAddr Address of starting input to read.
	 * @param count Number of inputs to read.
	 * @param dest Array to put received data to.
	 * 
	 * @throws ModBusException 
	 */
	public synchronized void read_input_status( int startAddr, int count, boolean [] dest ) throws ModBusException {
		readIoStatus( F_READ_INPUT_STATUS, startAddr, count, dest );
	}



	/** 
	 * read_holding_registers (MODBUS FC03) <br><br>
	 * 
	 * Read the holding registers (analog outputs) in a slave and put the data into an array.
	 * 
	 * @param startAddr Number of starting register to read. 
	 * @param count Number of registers to read.
	 * @param dest Buffer to read to.
	 *  
	 * @throws ModBusException 
	 */
	public synchronized void read_holding_registers( int startAddr, int count, int []dest ) throws ModBusException {
		if ( count > MAX_READ_REGS ) {
			count = MAX_READ_REGS; 
			log.severe("Too many registers requested");
		} 
		readRegisters( F_READ_HOLDING_REGISTERS, startAddr, count, dest ); 
	}

	/** 
	 * read_input_registers (MODBUS FC04) <br><br>
	 * 
	 * Read the input registers (analog inputs) in a slave and put the data into an array.
	 * 
	 * @param startAddr Number of starting register to read.
	 * @param count Number of registers to read.
	 * @param dest Array to put read data to.
	 *  
	 * @throws ModBusException 
	 */
	public synchronized  void read_input_registers( int startAddr, int count, int []dest ) throws ModBusException {	
		if ( count > MAX_INPUT_REGS ) {
			count = MAX_INPUT_REGS; 
			log.severe("Too many input registers requested");
		}
		try {
			readRegisters( F_READ_INPUT_REGISTERS, startAddr, count, dest );
		} catch(Exception e)
		{
			readRegisters( F_READ_INPUT_REGISTERS, startAddr, count, dest );
			
		}
	}

	/**
	 * Read one input register. (MODBUS FC04) <br><br>
	 * 
	 * @param regAddr address of register to read.
	 * @return Register value.
	 * @throws ModBusException in case of some problem, such as incorrect register address.
	 */
	public int readInputRegister( int regAddr ) throws ModBusException
	{
		int [] out = new int[1];
		read_input_registers( regAddr, 1, out );
		return out[0];
	}


	/**
	 * force_single_coil (MODBUS FC05) <br><br>
	 * 
	 * Turn on or off a single coil (digital output) on the slave device
	 *  
	 * @param coilAddr Number of digital out to set.
	 * @param state Turn output on or off.
	 * @throws ModBusException
	 */
	public synchronized  void force_single_coil( int coilAddr, boolean state ) throws ModBusException {
		int out = 0;
		if ( state ) out = 0xFF00;
		setSingleRegister( F_FORCE_SINGLE_COIL, coilAddr, out );
	}

	/** 
	 * preset_single_register (MODBUS FC06) <br><br>
	 * 
	 * Sets a value in one holding register (analog output, for example) in the slave device.
	 * 
	 * @param regAddr Address of register to set. 
	 * @param value New value.
	 *  
	 * @throws ModBusException 
	 */
	public synchronized  void preset_single_register( int regAddr, int value ) throws ModBusException {
		setSingleRegister( F_PRESET_SINGLE_REGISTER, regAddr, value );
	}

	/**
	 * set_multiple_coils (MODBUS FC15) <br><br>
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
	public synchronized  void set_multiple_coils( int startAddr, int count, boolean []data ) throws ModBusException {
		int byte_count = (count / 8) + 1; 

		int i, bit;
		int coil_check = 0;
		int data_array_pos = 0;
		byte [] pdata = new byte[ byte_count ];

		if ( count > MAX_WRITE_COILS ) {
			count = MAX_WRITE_COILS; 
			log.severe("Writing to too many coils");
		} 


		int pdpos = 0;
		pdata[ pdpos ] = (byte)(byte_count); 

		for( i = 0; i < byte_count; i++) { 
			pdata[ ++pdpos ] = 0; 
			bit = 0x01; 
			while( ((bit & 0xFF) != 0) && coil_check++ < count ) 
			{ 
				if( data[ data_array_pos++ ] ) 
				{ 
					pdata[ pdpos ] |= bit; 
				} 
				else 
				{ 
					pdata[ pdpos ] &=~ bit; 
				} 
				bit = bit << 1; 
			} 
		} 

		byte [] packet = buildDataPacket( F_SET_MULTIPLE_COILS, startAddr, count, pdata ); 

		synchronized (ioLock) {	
			send_query( packet, packet.length );
			modbusResponse( F_SET_MULTIPLE_COILS );
		}
	}


	/** 
	 * preset_multiple_registers (MODBUS FC16) <br><br>
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

	public synchronized  void preset_multiple_registers( int startAddr, int reg_count, int [] data ) throws ModBusException {
		int byte_count = reg_count * 2; 
		byte [] pdata= new byte[ byte_count+1 ];

		if ( reg_count > MAX_WRITE_REGS ) {
			reg_count = MAX_WRITE_REGS; 
			log.severe("Trying to write to too many registers");
		} 

		int pdpos = 0;
		pdata[ pdpos++ ] = (byte)byte_count; 

		for( int i = 0; i < reg_count; i++ ) 
		{ 
			pdata[ pdpos++ ] = (byte)(data[ i ] >> 8); 
			pdata[ pdpos++ ] = (byte)(data[ i ] & 0x00FF); 
		} 

		byte [] packet = buildDataPacket( F_SET_MULTIPLE_REGISTERS, startAddr, reg_count, pdata ); 

		synchronized (ioLock) {	
			send_query( packet, packet.length );
			modbusResponse( F_SET_MULTIPLE_REGISTERS );
		}
	}



	/** 
	 * Sends a value to a register in a slave. 
	 * 
	 * @throws IOException 
	 * @throws ModBusException 
	 */
	private void setSingleRegister( int function, int addr, int value ) throws ModBusException {
		byte [] packet = buildSetSinglePacket( function, addr, value );

		synchronized (ioLock) {	
			send_query( packet, packet.length );
			modbusResponse( function );
		}
	}


	/** 
	 * read_reg_response reads the response data from a slave and puts the data into an array. 
	 * 
	 * @throws ModBusException 
	 */
	private void read_reg_response( int []dest, int function ) throws ModBusException 
	{
		//int expectedResposnceSizeBytes = expectedItems*2 + 5;
		byte [] data = modbusResponse( function );

		int responceItemsCount = (0xFF & data[2])/2;

		for( int i = 0; i < responceItemsCount; i++ )
		{
			int temp = 0xFF00 & (data[ 3 + i *2 ] << 8); /* OR with lo_byte */
			temp |= 0x00FF & (data[ 4 + i * 2 ]);
			dest[i] = temp;
		}
	}




	/** 
	 * read_registers read the data from a modbus slave and put that data into an array.
	 *  
	 * @throws ModBusException 
	 */ 

	private void readRegisters( int function, int start_addr, int count, int []dest ) throws ModBusException { 
		//byte [] packet = new byte[ REQUEST_QUERY_SIZE + CHECKSUM_SIZE ];
		byte [] packet = buildRequestPacket( function, start_addr, count );

		synchronized (ioLock) {
			send_query( packet, packet.length );
			read_reg_response( dest, function );
		}
	}




	/**
	 * Receive response and check if it is correct.
	 * @param expectedResposnceSizeBytes 
	 *  
	 * @param query query to check response against.
	 * @return response, if no error happened. 
	 * @throws ModBusProtocolException if response is incorrect.
	 */

	private byte [] modbusResponse( int function) throws ModBusException {
		byte [] response = receiveResponse();

		//BinaryTools.dump("Modbus query: ",query);
		//BinaryTools.dump("Modbus reply: ",response);

		// check that func num is the same
		if ( response[ 1 ] != function )
			throw new ModBusProtocolException( response[2] );

		return response;
	}





	/** 
	 * Interrogate a modbus slave to get coil status. 
	 * An array of coils shall be set to TRUE or FALSE according to the 
	 * response from the slave.
	 *  
	 * @throws ModBusException 
	 */ 

	private void readIoStatus( int function, int start_addr, int count, boolean []dest  ) throws ModBusException 
	{ 
		//byte [] packet = new byte[ REQUEST_QUERY_SIZE + CHECKSUM_SIZE ];
		byte [] packet = buildRequestPacket( function, start_addr, count );
		synchronized (ioLock) {	
			send_query( packet, packet.length );
			read_IO_stat_response( dest, count, function  );
		}
	}

	/** 
	 * read_IO_stat_response 
	 * this function does the work of setting array elements to true or false.
	 *  
	 * @throws ModBusException 
	 */
	private void read_IO_stat_response( boolean [] dest, int coil_count, int function  ) throws ModBusException {
		int temp, i, bit, dest_pos = 0;
		int coils_processed = 0;
		byte[] data = modbusResponse( function );

		for ( i = 0; i < ( data[2] ) && i < dest.length; i++ ) { 
			temp = data[ 3 + i ] ;

			for ( bit = 0x01; ((bit & 0xff) != 0) && coils_processed < coil_count; bit = bit << 1 ) {
				dest[ dest_pos ] = ( (temp & bit) != 0 );
				coils_processed++;
				dest_pos++;				
			} 
		} 
	}


	/**
	 * Check if register contains required value.
	 * 
	 * @param addr Address of register to check.
	 * @param value Value this register must contain.
	 * 
	 * @throws ModBusException Thrown if something is wrong with modbus IO.
	 * @throws CommunicationsException Thrown if register can be read, but contains wrong value.
	 */
	void checkRegister(int addr, int value) throws ModBusException, CommunicationsException
	{
		int recv = readInputRegister(addr);
		if( (recv & 0xFFFF) != (value & 0xFFFF) )
			throw new CommunicationsException( 
					String.format("No device found, constant field at 0x%04X is 0x%04X instead of 0x%04X", addr, recv, value ));
	}





	// -------------------------------------------
	// Getters/setters
	//-------------------------------------------

	/**
	 * Get currently set slave number.
	 * 
	 * @see setSlave
	 * @return Slave number.
	 */
	public int getSlave() {		return slave;	}

	/**
	 * Set slave number. Modbus protocol supports addressing on the bus, but, really
	 * it seems to be ignored everywhere. You can set slave number here.
	 * 
	 * @param slave Slave number to be used in Modbus requests.
	 */
	public void setSlave(int slave) {		this.slave = slave;	}


	// -------------------------------------------
	// Abstract ones
	//-------------------------------------------

	/** 
	 * The following functions construct the required query into a modbus query packet. 
	 */

	abstract protected byte [] buildRequestPacket( int function, int startAddr, int count ); 
	abstract protected byte [] buildDataPacket( int function, int startAddr, int count, byte [] data );
	abstract protected byte [] buildSetSinglePacket( int function, int addr, int value );


	abstract protected void send_query( byte [] query, int size ) throws ModBusIOException;
	abstract protected byte [] receiveResponse() throws ModBusException;




}




