package ru.dz.shipMaster.dev.nmeaSentence;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ru.dz.shipMaster.config.Configuration;
import ru.dz.shipMaster.config.items.CliBus;
import ru.dz.shipMaster.data.misc.CommunicationsException;
import ru.dz.shipMaster.dev.AbstractDriver;
import ru.dz.shipMaster.dev.DriverPort;
import ru.dz.shipMaster.dev.bus.INmeaSentenceListener;
import ru.dz.shipMaster.dev.bus.NmeaBus;

public abstract class GeneralNmeaSentenceDriver extends AbstractDriver implements INmeaSentenceListener {


	protected CliBus bus;
	protected CliBus newBus;
	private JButton selectBusButton = new JButton("None");
	private NmeaBus nb;


	@Override
	protected void doStartDriver() throws CommunicationsException {
		nb.addDataListener(this);
	}

	@Override
	protected void doStopDriver() throws CommunicationsException {
		nb.removeDataListener(this);
	}


	@Override
	protected void doLoadPanelSettings() {
		newBus = bus;
		setDispName();
	}

	@Override
	protected void doSavePanelSettings() {
		setBus( newBus );
		setDispName();
	}

	private void setDispName() {
		selectBusButton.setText(bus == null ? "None" : newBus.getName());
		selectBusButton.repaint(100);
	}

	@Override
	protected void setupPanel(final JPanel panel) {
		panel.add(new JLabel("Bus"),consL);
		panel.add(selectBusButton  ,consR);
		selectBusButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				newBus = Configuration.selectBus(panel);
				if(newBus == null) return;

				if(! (newBus.getBus() instanceof NmeaBus)) {
					newBus = null;
					showMessage("Wrong bus type, need Nmea0183");
				}
				setDispName();
			}} );
	}

	@Override
	public String getDeviceName() {		return "Nmea0183 parser";	}


	@Override
	public boolean isAutoSeachSupported() {		return false;	}


	public CliBus getBus() {		return bus;	}
	public void setBus(CliBus newBus) {		

		if( (newBus != null) && (! (newBus.getBus() instanceof NmeaBus)) ) {
			newBus = null;
			showMessage("Wrong bus type, need Nmea0183");
		}

		this.bus = newBus;	
		if(bus == null)
			nb = null;
		else
			nb = (NmeaBus)bus.getBus();

	}


	@Override
	public void receiveNmeaSentence(NMEA0183Sentence s) {
		if( !s.getSentenceId().equalsIgnoreCase(getSentenceId())) return;
		try {
			parse(s);
		} catch (InvalidNMEASentenceException e) {
			log.log(Level.SEVERE,"Invalid sentence format: "+s, e);
		}		
	}

	/**
	 * @return Id of sentence this driver looks for.
	 */
	public abstract String getSentenceId();

	/**
	 * Parse my sentence. 
	 * @throws InvalidNMEASentenceException 
	 */
	protected abstract void parse(NMEA0183Sentence s) throws InvalidNMEASentenceException;

	public void checkValidity(NMEA0183Sentence s) throws InvalidNMEASentenceException {
		if( !s.isValid() )
			throw new InvalidNMEASentenceException();		
	}

	/**
	 * 
	 * Check that last field of sentence is "A". 
	 * 
	 * @throws InvalidNMEASentenceException if last field is not letter 'A'.
	 * 
	 **/
	protected void checkLastA(NMEA0183Sentence s) throws InvalidNMEASentenceException {

		String last = s.getField( s.getDataFields().size()-1 );
		if(!last.equals("A"))
			throw new InvalidNMEASentenceException("Last field is not 'A'");		
	}


	
	/**
	 * 
	 * Check that field contains given value. 
	 * 
	 * @throws InvalidNMEASentenceException if not.
	 * 
	 **/
	protected void checkField(NMEA0183Sentence s, int no, String value) throws InvalidNMEASentenceException {

		String f = s.getField( no );
		if(!f.equals(value))
			throw new InvalidNMEASentenceException("Field "+no+" is not '"+value+"'");		
	}
	
	
	
	
	@Override
	public String getInstanceName() {
		return "Nmea "+getSentenceId()+" @ "+( bus == null ? "?" : bus.getName());
	}





	/**
	 * Parses NMEA DDMM.MM notation.	
	 * @param NMEA field data in DDMM.MM (Degrees/minutes) format.
	 * @return Result in degrees.
	 */


	protected static double parseNmeaDegrees(String field) {
		String[] split = field.split("\\.");
		
		int mainlen = split[0].length();
/*		
		double degrees = Double.parseDouble( field.substring(0, mainlen-2) ); // first 2 digits
		//double minutes = Double.parseDouble( field.substring(mainlen-2) ); // last digits
		double minutes = Double.parseDouble( split[0].substring(mainlen-2) ); // last digits
		double seconds = Double.parseDouble( "."+split[1] ); // .xxx

		seconds *= 100;
		
		//minutes += sec*1.6;
		
		//return degrees+(minutes/60.0);
		//return degrees+(minutes/60.0)+(sec/60.0)/60.0;
	
		double minVal = minutes;
		double degVal = degrees;
		double secVal = seconds;
		if (seconds == 0) secVal = 0.1;
		minVal += (secVal/60.);
		degVal += (minVal/60.);
		//degVal = degVal * sign;
		return degVal;
*/
		double degrees = Double.parseDouble( field.substring(0, mainlen-2) ); // first 2 digits
		double minutes = Double.parseDouble( field.substring(mainlen-2) ); // last digits
		return degrees+(minutes/60.0);
		
		
	}

	protected static double parseLattitude(NMEA0183Sentence s, String val, String dir) {
		double lattitude;
		try { 			

			double lattitudeAbs = parseNmeaDegrees( val );

			boolean neg = dir.equalsIgnoreCase("S");

			lattitude = lattitudeAbs * (neg ? -1 : 1);
		}		
		catch(NumberFormatException e) { 
			log.log(Level.SEVERE, "NAN in lattitude "+s, e);
			lattitude = 0;
		}
		return lattitude;
	}

	protected static double parseLongitude(NMEA0183Sentence s, String val, String dir) {
		double lattitude;
		try { 			

			double lattitudeAbs = parseNmeaDegrees( val );

			boolean neg = dir.equalsIgnoreCase("W");

			lattitude = lattitudeAbs * (neg ? -1 : 1);
		}		
		catch(NumberFormatException e) { 
			log.log(Level.SEVERE, "NAN in longitude "+s, e);
			lattitude = 0;
		}
		return lattitude;
	}
	
	protected static double parseMagneticVariation(NMEA0183Sentence s, String val, String dir) {
		double lattitude;
		try { 			

			double lattitudeAbs = Double.parseDouble( val );

			boolean neg = dir.equalsIgnoreCase("W");

			lattitude = lattitudeAbs * (neg ? -1 : 1);
		}		
		catch(NumberFormatException e) { 
			log.log(Level.SEVERE, "NAN in magnetic variation "+s, e);
			lattitude = 0;
		}
		return lattitude;
	}
	

	protected static void sendPossibleDoubleField(NMEA0183Sentence s, int no, DriverPort p)
	{
		String field = s.getField(no);
		
		// Empty?
		if(field.length() == 0)
			return;
		
		try { 
			p.sendDoubleData( Double.parseDouble(field) );

			}
		catch(NumberFormatException e) { 
			log.log(Level.SEVERE, "NAN", e);
		}
		
	}
	
}
