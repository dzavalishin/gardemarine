package ru.dz.shipMaster.dev.ups;

import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.dz.shipMaster.config.ConfigurationFactory;
import ru.dz.shipMaster.data.misc.CommunicationsException;
import ru.dz.shipMaster.dev.common.BiPipe;
import ru.dz.shipMaster.dev.common.SerialBiPipe;
import ru.dz.shipMaster.dev.common.SerialBiPipe.FlowControl;

/**
 * Driver for Megatec UPS and compatibles.
 * @author dz
 *
 */
public class Megatec extends PipedUpsDriver {
	private static final Logger log = Logger.getLogger(Megatec.class.getName());


	// ----------------------------------------------------------------------------
	// Constructors
	// ----------------------------------------------------------------------------

	public Megatec() {
		super("Megatec UPS Driver");
	}

	@Override
	protected void finalize() throws Throwable {
		stop();
		super.finalize();
	}

	// ----------------------------------------------------------------------------
	// Info
	// ----------------------------------------------------------------------------

	@Override
	public String getDeviceName() { return "Megatec protocol based UPS driver"; }
































	private static final int MEGATEC_BAUD_RATE = 2400;

	private static final Object ioSync = new Object();


	private String upsName = "Unknown UPS";

	private String infoUpsManufacturer = "Unknown manufacturer";
	private String infoUpsName = "Unknown name";
	private String infoUpsVersion = "Unknown version";


	private boolean ineltQuirks = false;


	// ----------------------------------------------------------------------------
	// Setup
	// ----------------------------------------------------------------------------


	@Override
	protected void doStartDriver() throws CommunicationsException {
		super.doStartDriver();
		try {
			init();
		} catch (IOException e) {
			throw new CommunicationsException("Unable to init",e);
		} catch (UpsProtocolException e) {
			throw new CommunicationsException("Unable to init",e);
		}
	}	






	// ----------------------------------------------------------------------------
	// Serial port IO
	// ----------------------------------------------------------------------------



	private synchronized void init() throws 
	CommunicationsException, 
	IOException, UpsProtocolException {

		BiPipe p = pipe.getPipe();

		if (p instanceof SerialBiPipe) {
			SerialBiPipe spipe = (SerialBiPipe) p;
			spipe.setBaudRate(MEGATEC_BAUD_RATE);
			spipe.setFlowcontrol(FlowControl.None);
		}

		/*
		port.setSerialPortParams( MEGATEC_BAUD_RATE, 
				SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

		 */

		//port.enableReceiveTimeout(10);


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

		log.fine("UPS "+upsName+" is monitored on port "+pipe.getName());

	}	





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
		write(	out.getBytes() );
		write( (byte)'\r' );
		flush();
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
					
					// Now ask for current UPS state
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

	// ----------------------------------------------------------------------------
	// Called by driver thread periodically
	// ----------------------------------------------------------------------------

	@Override
	protected void doDriverTask() throws Throwable {

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




}
