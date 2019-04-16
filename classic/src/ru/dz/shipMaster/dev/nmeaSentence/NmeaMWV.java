package ru.dz.shipMaster.dev.nmeaSentence;

import java.util.Vector;
import java.util.logging.Level;

import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.dev.DriverPort;

public class NmeaMWV extends GeneralNmeaSentenceDriver {

	private DriverPort windAnglePort, windSpeedPort;

	@Override
	public String getSentenceId() { return "MWV"; }
	
	@Override
	protected void updatePorts(Vector<DriverPort> ports) {
		windAnglePort = getPort(ports, 0, Direction.Input, Type.Numeric, "Wind angle");
		windSpeedPort = getPort(ports, 1, Direction.Input, Type.Numeric, "Wind speed");
		
	}

	@Override
	public void parse(NMEA0183Sentence s) throws InvalidNMEASentenceException {
		checkValidity(s);
		checkLastA(s);

		try { 
			windAnglePort.sendDoubleData( Double.parseDouble(s.getField(0)) );
			windSpeedPort.sendDoubleData( Double.parseDouble(s.getField(2)) );
			}
		catch(NumberFormatException e) { 
			log.log(Level.SEVERE, "NAN", e);
		}
		
	}


}
