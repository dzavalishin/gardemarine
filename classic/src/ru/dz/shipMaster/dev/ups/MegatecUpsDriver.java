package ru.dz.shipMaster.dev.ups;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.RXTXPort;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.dz.shipMaster.config.ConfigurationFactory;
import ru.dz.shipMaster.data.com.ComPortDispatcher;
import ru.dz.shipMaster.data.com.ComPortUser;
import ru.dz.shipMaster.data.misc.CommunicationsException;

/**
 * Driver for Megatec UPS and compatibles.
 * @author dz
 *
 */
public class MegatecUpsDriver extends GeneralUpsDriver implements ComPortUser {
	private static final Logger log = Logger.getLogger(MegatecUpsDriver.class.getName());

	// ----------------------------------------------------------------------------
	// Constructors
	// ----------------------------------------------------------------------------

	public MegatecUpsDriver() {
		super("Megatec UPS Driver");
	}

	@Override
	protected void finalize() throws Throwable {
		closePort();
		super.finalize();
	}

	// ----------------------------------------------------------------------------
	// Info
	// ----------------------------------------------------------------------------
	
	//@Override	
	public String getName() { return upsName; }
	
	@Override
	public String getDeviceName() { return "Megatec protocol based UPS driver"; }


	@Override
	public String getInstanceName() {
		return portName;
	}
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private static final int SERIAL_BUF_SIZE = 256;
	private static final int MEGATEC_BAUD_RATE = 2400;

	private static final Object ioSync = new Object();
	

	private String upsName = "Unknown UPS";

	private String infoUpsManufacturer = "Unknown manufacturer";
	private String infoUpsName = "Unknown name";
	private String infoUpsVersion = "Unknown version";

	private boolean portSearchMode = true;
	
	private boolean ineltQuirks = false;
	private ComPortDispatcher comPortDispatcher = ConfigurationFactory.getTransientState().getComPortDispatcher();
	
	
	// ----------------------------------------------------------------------------
	// Setup
	// ----------------------------------------------------------------------------


	/**
	 * Try to open given port and check for a known UPS on the other side.
	 * @param serialPortName port to try.
	 * @return true if port is ok and valid UPS is there.
	 */
	public synchronized boolean openPort(String serialPortName)
	{
		portSearchMode = false;
		closePort();
		portName = serialPortName;
		return doOpenPort();
	}

	public synchronized void openPort(ComPortDispatcher comPortDispatcher) {
		this.comPortDispatcher = comPortDispatcher;
		portSearchMode = true;
		closePort();	
		//dataReader.start(); // Datareader will call dispacher for port
	}

	/*private synchronized boolean reOpenPort()
	{
		closePort();
		return doOpenPort();
	}*/
	
	protected synchronized boolean doOpenPort()
	{
		showMessage("Ищу ИБП на "+portName);
		
		try { portIdentifier = CommPortIdentifier.getPortIdentifier(portName); } 
		catch (NoSuchPortException e) {
			log.log(Level.SEVERE, "Port is not available: "+portName, e);
			showMessage("Порт занят: "+portName);
			return false;
		}
		
		try { init(); } 
		catch (Exception e) {
			log.log(Level.SEVERE, "Can't open port "+portName, e);
			showMessage("ИБП - не могу открыть порт "+portName);
			return false;
		}

		showMessage("ИБП найден на порту "+portName);
		upsFound = true;
		
		//dataReader.start();

		return upsFound;
	}
	
	

	
	private void searchOpenPort() {
		closePort();
		// Dispatcher will call us back to tryThisPort
		// with different ports in loop. Call will
		// return on success
		
		comPortDispatcher.giveMePort(this);
	}

	//public String getName() { return "Megatec/Inel UPS driver"; }

	/**
	 * Implementation of ComPortUser 
	 * @param portId port to try.
	 * @return True if we claim this port.
	 */
	public boolean tryThisPort(CommPortIdentifier portId) {
		closePort();
		portIdentifier = portId;
		portName = portId.getName();
		try { init(); } 
		catch (Throwable e) {
			log.log(Level.SEVERE, "Can't open port "+portName, e);
			showMessage("ИБП - не могу открыть порт "+portName);
			closePort();
			return false;
		}

		showMessage("ИБП найден на порту "+portName);
		upsFound = true;
		
		return true;
	}
	
	
	private synchronized void closePort()
	{
		upsFound = false;
		try {
			if(inputStream != null) inputStream.close();
			if(outputStream!= null) outputStream.close();
		} catch (IOException e) {
			log.log(Level.SEVERE,"Can't close com stream",e);
		}
		if(port != null) port.close();
	}
	
	// ----------------------------------------------------------------------------
	// Serial port IO
	// ----------------------------------------------------------------------------
	private String portName = "undefined";
	private CommPortIdentifier portIdentifier = null;
	private RXTXPort port = null;
	private InputStream inputStream;
	private OutputStream outputStream;


	private synchronized void init() throws 
			CommunicationsException, PortInUseException, 
			UnsupportedCommOperationException, IOException, UpsProtocolException {

		port = (RXTXPort)portIdentifier.open("ShipMaster/UPS", 100);		
		
		port.setSerialPortParams( MEGATEC_BAUD_RATE, 
				SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		
		port.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
		
		port.setInputBufferSize(SERIAL_BUF_SIZE);
		port.setOutputBufferSize(256);
		
		port.disableReceiveThreshold();
		port.enableReceiveTimeout(10);
		
		inputStream = port.getInputStream();
		outputStream = port.getOutputStream();		
	
		try {
			getUpsInfo(); // Will throw if unable to get
		} catch (UpsProtocolException e) {
			// Some devices have no I command :(
			//e.printStackTrace();
		} 
		
		getUpsRatings(); // Will throw if unable to get
		getUpsState(); // Will throw if unable to get
		
		log.fine("Megatec protocol based UPS found: "+upsName);
		ConfigurationFactory.getTransientState().getDashBoard().getLogWindow().addMessage(
				"UPS found: "+
					infoUpsManufacturer+" "+
					infoUpsName+" "+
					infoUpsVersion
				);

		
		if(infoUpsManufacturer.equalsIgnoreCase("inelt"))
			ineltQuirks  = true;
		
		log.fine("UPS "+upsName+" is monitored on port "+portName);
		
	}	

	// try twice in 100 msec
	private int read(byte data[]) throws IOException {
		if(inputStream == null)
			throw new IOException("No input stream");
		int got = 0;
		got = inputStream.read(data);
		if( got == 0 )
		{
			sleep(200);
			return inputStream.read(data);
			//continue;
		}
		return got;
	}

	private void drainInput() throws IOException {
		if(inputStream == null)
			throw new IOException("No input stream");
		byte[] data = new byte[1];
		while( inputStream.read(data) > 0 )
		{
			/* repeat */;
		}
	}
	
	
	/*private void write(byte[] data) throws IOException
	{
		if(outputStream == null)
			throw new IOException("No output stream");
		outputStream.write(data);
	}*/

	private String getUpsLine() throws IOException
	{
		StringBuilder out = new StringBuilder();
		byte[] c1 = new byte[1];
		
		while(true)
		{
			int nc = read(c1);
			if(nc <= 0) throw new IOException("Ошибка чтения");
			//new Character(c1[1])
			if(c1[0] == '\r')
				break;
			out.append(new String(c1));
		}
		
		return out.toString();
	}
	
	private void putUpsLine( String out ) throws IOException
	{
		outputStream.write(	out.getBytes() );
		outputStream.write( '\r' );
		outputStream.flush();
		sleep(100); // now sleep for 100 msec for device to process
	}
	

	// ----------------------------------------------------------------------------
	// Control
	// ----------------------------------------------------------------------------

	@Override
	protected void setBeeperOnOutput(boolean setBeeperOn) {
		try {
			synchronized(ioSync)
			{
				getUpsState();
				if(isBeeperOn() != setBeeperOn)
				{
					putUpsLine("Q");		
					// On INELT we receive sometimes non-standard reply here. 
					// Meaning is unknown so we just skip everything here
					// need to sleep here a bit so that answer will be given and we'll eat it
					sleep(300);
					drainInput();
					getUpsState();
					if(isBeeperOn() != setBeeperOn)
						log.severe("Can't change beeper status");
				}
			}
		}
		catch (IOException e) { log.log(Level.SEVERE, "Unable to set UPS beeper state", e);	} 
		catch (UpsProtocolException e) { log.log(Level.SEVERE, "Unable to set UPS beeper state", e);	}
	}

	@Override
	protected void setLoadEnabledOutput(boolean setLoadEnabled) {
		try {
			synchronized(ioSync)
			{
				if(setLoadEnabled)
				{
					putUpsLine("C"); // come on
				}
				else
				{
					putUpsLine("C"); // cancel anything in action
					putUpsLine("S00R0000"); // Go off
				}
				getUpsState();
				if(setLoadEnabled != isLoadEnabled())
					log.severe("Can't change load status");
			}
		}
		catch (IOException e) { log.log(Level.SEVERE, "Unable to set UPS load state", e);	}
		catch (UpsProtocolException e) { log.log(Level.SEVERE, "Unable to set UPS load state", e);	}
	}

	// ----------------------------------------------------------------------------
	// UPS state read
	// ----------------------------------------------------------------------------

	private void getUpsInfo() throws IOException, UpsProtocolException
	{
		String answer;
		synchronized(ioSync)
		{
			drainInput();
			putUpsLine("I");
			//getUpsLine();
			answer = getUpsLine();
		}
		
		if(answer.charAt(0) != '#')
			throw new UpsProtocolException("I command UPS answer has no '#' at the beginning: "+answer); 
		
		//Scanner s = new Scanner(answer.substring(1));

		try {
			infoUpsManufacturer = answer.substring(1,17).trim(); // s.next();
			infoUpsName = answer.substring(17,27).trim();//s.next();
			//infoUpsVersion = s.next();
			infoUpsVersion = answer.substring(27).trim();
		} catch (Throwable e)
		{
			throw new UpsProtocolException("Непонятный ответ");
		}
		
		upsName = infoUpsManufacturer + " " + infoUpsName + " " + infoUpsVersion;
	}


	private void getUpsRatings() throws IOException, UpsProtocolException
	{
		String answer;
		synchronized(ioSync)
		{
			drainInput();
			putUpsLine("F");
			answer = getUpsLine();
		}

		if(answer.charAt(0) != '#')
			throw new UpsProtocolException("F command UPS answer has no '#' at the beginning: "+answer);

		try {
			Scanner s = new Scanner(answer.substring(1));
			s.useLocale(Locale.PRC);

			setUpsVoltageRating(s.nextFloat());
			setUpsCurrentRating(s.nextFloat());
			setUpsBatteryVoltageRating(s.nextFloat());
			setUpsFrequencyRating(s.nextFloat());
		} catch (Throwable e)
		{
			throw new IOException("Непонятный ответ");
		}
		
		// sanity check
		
		if( getUpsVoltageRating() < 100 || getUpsVoltageRating() > 400 )
		{
			log.severe("ИБП - странное напряжение: "+getUpsVoltageRating() );
		}

		if( getUpsFrequencyRating() < 40 || getUpsFrequencyRating() > 80 )
		{
			log.severe("ИБП - странная частота: "+getUpsFrequencyRating() );
		}
	}

	/* The UPS status flags positions*/
	private final int FL_ON_BATT    = 0; // INELT ok
	private final int FL_LOW_BATT   = 1; // INELT ok
	private final int FL_BOOST_TRIM = 2; // INELT ok
	private final int FL_FAILED     = 3; // INELT ?
	private final int FL_UPS_TYPE   = 4; // INELT doesnt?
	private final int FL_BATT_TEST  = 5; // INELT ok
	private final int FL_LOAD_OFF   = 6; // INELT doesn't respond
	private final int FL_BEEPER_ON  = 7; // INELT doesn't respond

	
	
	private void getUpsState() throws IOException, UpsProtocolException
	{
		String answer;
		synchronized(ioSync)
		{
			drainInput();
			putUpsLine("Q1");
			answer = getUpsLine();
		}
		
		if(answer.charAt(0) != '(')
			throw new UpsProtocolException("Q1 command UPS answer has no '(' at the beginning: "+answer);

		//boolean nowBatteryLow = false;
		try {
			Scanner s = new Scanner(answer.substring(1));
			s.useLocale(Locale.PRC);

			setInputVoltage(s.nextFloat());
			setInputFaultVoltage(s.nextFloat());
			setOutputVoltage(s.nextFloat());
			setOutputLoadPercent(s.nextFloat());
			setInputFrequency(s.nextFloat());
			setBatteryVoltage(s.nextFloat());
			setTemperature(s.nextFloat());

			setBatteryCharge(get_battery_charge(getBatteryVoltage()));

			String bits = s.next();

			//showMessage( "ИБП флаги "+bits );

			char [] bit = bits.toCharArray();

			setBeeperOn( ineltQuirks ? true : bit[FL_BEEPER_ON] != '1');
			setTestInProgress(bit[FL_BATT_TEST] == '1');
			setBoostTrim(bit[FL_UPS_TYPE] == '1');
			setOnBattery(bit[FL_ON_BATT] == '1');
			setBatteryLow(bit[FL_LOW_BATT] == '1');
			setLoadEnabled(bit[FL_LOAD_OFF] != '1');
			setBypassActive(bit[FL_BOOST_TRIM] != '1');
			setHardwareFailure(bit[FL_FAILED] == '1');
		} catch (Throwable e)
		{
			throw new UpsProtocolException("Непонятный ответ: "+answer);
		}

		
		/*if(nowBatteryLow != batteryLow )
		{
			batteryLow = nowBatteryLow;
			dashMessage.setCritical(batteryLow);
			// TO DO show ups battery charge level in dash
			showMessage(batteryLow ? "Заряд ИБП недостаточен" : "ИБП в порядке");
		}*/
	}

	static private float CLAMP(float x, float min, float max) 
	{
		return ((x < min) ? min : ((x > max) ? max : x));
	}
	
	static float battvolt_empty = 1.69f;  /* experimental 4 INELT. */
	static float battvolt_full = 2.26f;   /* experimental 4 INELT. */
	static private float get_battery_charge(float battvolt)
	{
		float value;

		if (battvolt_empty < 0 || battvolt_full < 0) {
			return -1;
		}

		float clBattvolt = CLAMP(battvolt, battvolt_empty, battvolt_full);
		value = (clBattvolt - battvolt_empty) / (battvolt_full - battvolt_empty);

		return value * 100;  /* percentage */
	}
	
	private float oldBatteryVoltage = -1;
	private float oldBatteryCharge = -1;
	private float oldOutputLoadPercent = -1;
	
	private void updateUpsData() { 
		if(!upsFound) 
			{
			if(!portSearchMode)
				return;
			searchOpenPort();
			}
		
		try { 
			getUpsState();

			int rndCharge = Math.round( getBatteryCharge()*10 );

			float loadDiff = oldOutputLoadPercent - getOutputLoadPercent();
			if( loadDiff < 0 ) loadDiff = -loadDiff;
			
			if(
					oldBatteryVoltage != getBatteryVoltage() ||
					oldBatteryCharge  != rndCharge ||
					loadDiff > 5 || rndCharge < 20
			)
			{
				oldBatteryVoltage = getBatteryVoltage();
				oldBatteryCharge  = rndCharge;
				oldOutputLoadPercent = getOutputLoadPercent();

				//dashMessage.setCritical(onBattery);
				showMessage(
						"ИБП батарея "+getBatteryVoltage()+"в, ("
						+rndCharge / 10 +"%), нагрузка "
						+getOutputLoadPercent()+"%" );
			}		
			} 
		catch (IOException e) {
			//db.getLogWindow().addMessage("UPS IO error: "+e.toString());
			//dashMessage.setCritical(true);
			showMessage("ИБП ошибка: "+e.toString());
			oldBatteryVoltage = -1;
			oldBatteryCharge = -1;
		} catch (UpsProtocolException e) {
			showMessage("ИБП ошибка: "+e.toString());
			oldBatteryVoltage = -1;
			oldBatteryCharge = -1;
		} 
	}


	// ----------------------------------------------------------------------------
	// Called by driver thread periodically
	// ----------------------------------------------------------------------------

	@Override
	protected void doDriverTask() throws Throwable {
		updateUpsData();
	}

	
}
