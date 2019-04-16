package ru.dz.shipMaster.dev.nmeaSentence;

import java.util.Vector;
import java.util.logging.Level;

import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.dev.DriverPort;

public class NmeaVLW extends GeneralNmeaSentenceDriver {
	private DriverPort distTotalPort;
	private DriverPort distResetPort;


	@Override
	public String getSentenceId() { return "VLW"; }

	@Override
	protected void updatePorts(Vector<DriverPort> ports) {
		distTotalPort = getPort(ports, 0, Direction.Input, Type.Numeric, "Total distance in nautical miles");
		distResetPort = getPort(ports, 1, Direction.Input, Type.Numeric, "Distance since reset in nautical miles");
	}

	@Override
	public void parse(NMEA0183Sentence s) throws InvalidNMEASentenceException {
		checkValidity(s);
		//checkLastA(s);

		try { 			

			
			if( (!s.getField(1).equals("N")) || (!s.getField(3).equals("N")) )
			{
				log.log(Level.SEVERE,"Invalid record: "+s);
			}
			
			distTotalPort.sendDoubleData(Double.parseDouble(s.getField(0)));
			distResetPort.sendDoubleData(Double.parseDouble(s.getField(2)));
		}		
		catch(NumberFormatException e) { 
			log.log(Level.SEVERE, "NAN in "+s, e);
		}

	}


}
