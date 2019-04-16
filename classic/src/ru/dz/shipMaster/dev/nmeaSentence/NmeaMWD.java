package ru.dz.shipMaster.dev.nmeaSentence;

import java.util.Vector;
import java.util.logging.Level;

import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.dev.DriverPort;

public class NmeaMWD extends GeneralNmeaSentenceDriver {


	private DriverPort windDirTPort, windDirMPort, windSpeedKnotsPort, windSpeedMSPort;

	@Override
	public String getSentenceId() { return "MWD"; }
	
	@Override
	protected void updatePorts(Vector<DriverPort> ports) {
		windDirTPort = getPort(ports, 0, Direction.Input, Type.Numeric, "Wind direction, true");
		windDirMPort = getPort(ports, 1, Direction.Input, Type.Numeric, "Wind direction, magnetic");
		windSpeedKnotsPort = getPort(ports, 2, Direction.Input, Type.Numeric, "Wind speed, Knots");
		windSpeedMSPort = getPort(ports, 3, Direction.Input, Type.Numeric, "Wind speed, M/Sec");
		
	}

	@Override
	public void parse(NMEA0183Sentence s) throws InvalidNMEASentenceException {
		checkValidity(s);
		//checkLastA(s);

		checkField(s,1,"T");
		checkField(s,3,"M");
		checkField(s,5,"N");
		checkField(s,7,"M");
		
		try { 
			windDirTPort.sendDoubleData( Double.parseDouble(s.getField(0)) );
			windDirMPort.sendDoubleData( Double.parseDouble(s.getField(2)) );
			windSpeedKnotsPort.sendDoubleData( Double.parseDouble(s.getField(4)) );
			windSpeedMSPort.sendDoubleData( Double.parseDouble(s.getField(6)) );
			}
		catch(NumberFormatException e) { 
			log.log(Level.SEVERE, "NAN", e);
		}
		
	}


}
