package ru.dz.shipMaster.dev.nmeaSentence;

import java.util.Vector;
import java.util.logging.Level;

import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.dev.DriverPort;

public class NmeaHDT extends GeneralNmeaSentenceDriver {


	private DriverPort headingTPort;

	@Override
	public String getSentenceId() { return "HDT"; }
	
	@Override
	protected void updatePorts(Vector<DriverPort> ports) {
		headingTPort = getPort(ports, 0, Direction.Input, Type.Numeric, "Heading, true");
	}

	@Override
	public void parse(NMEA0183Sentence s) throws InvalidNMEASentenceException {
		checkValidity(s);

		checkField(s,1,"T");
		
		try { 
			headingTPort.sendDoubleData( Double.parseDouble(s.getField(0)) );
			}
		catch(NumberFormatException e) { 
			log.log(Level.SEVERE, "NAN", e);
		}
		
	}


}
