package ru.dz.shipMaster.dev.nmeaSentence;

import java.util.Vector;
import java.util.logging.Level;

import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.dev.DriverPort;

public class NmeaHDM extends GeneralNmeaSentenceDriver {


	private DriverPort headingMPort;

	@Override
	public String getSentenceId() { return "HDM"; }
	
	@Override
	protected void updatePorts(Vector<DriverPort> ports) {
		headingMPort = getPort(ports, 0, Direction.Input, Type.Numeric, "Heading, magnetic");
	}

	@Override
	public void parse(NMEA0183Sentence s) throws InvalidNMEASentenceException {
		checkValidity(s);

		checkField(s,1,"M");
		
		try { 
			headingMPort.sendDoubleData( Double.parseDouble(s.getField(0)) );
			}
		catch(NumberFormatException e) { 
			log.log(Level.SEVERE, "NAN", e);
		}
		
	}


}
