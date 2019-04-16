package ru.dz.shipMaster.dev.nmeaSentence;

import java.util.Vector;
import java.util.logging.Level;

import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.dev.DriverPort;

public class NmeaMTW extends GeneralNmeaSentenceDriver {

	private DriverPort temperaturePort;

	@Override
	public String getSentenceId() { return "MTW"; }
	
	@Override
	protected void updatePorts(Vector<DriverPort> ports) {
		temperaturePort = getPort(ports, 0, Direction.Input, Type.Numeric, "Temperature");
		
	}

	@Override
	public void parse(NMEA0183Sentence s) throws InvalidNMEASentenceException {
		checkValidity(s);
		checkLastA(s);

		try { temperaturePort.sendDoubleData( Double.parseDouble(s.getField(0)) ); }
		catch(NumberFormatException e) { 
			log.log(Level.SEVERE, "NAN", e);
		}
		
	}


}
