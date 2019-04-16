package ru.dz.shipMaster.dev.nmeaSentence;

import java.util.Vector;
import java.util.logging.Level;

import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.dev.DriverPort;

public class NmeaDBT extends GeneralNmeaSentenceDriver {
	private DriverPort depthFeetPort;
	private DriverPort depthMetersPort;


	@Override
	public String getSentenceId() { return "DBT"; }

	@Override
	protected void updatePorts(Vector<DriverPort> ports) {
		depthFeetPort = getPort(ports, 0, Direction.Input, Type.Numeric, "Depth in feet");
		depthMetersPort = getPort(ports, 1, Direction.Input, Type.Numeric, "Depth in meters");
	}

	@Override
	public void parse(NMEA0183Sentence s) throws InvalidNMEASentenceException {
		checkValidity(s);
		//checkLastA(s);

		try { 			

			
			if( (!s.getField(1).equals("f")) || (!s.getField(3).equalsIgnoreCase("M")) )
			{
				log.log(Level.SEVERE,"Invalid record: "+s);
			}
			double dFeet = Double.parseDouble(s.getField(0));
			double dMeters = Double.parseDouble(s.getField(2) );
			
			depthFeetPort.sendDoubleData(dFeet);
			depthMetersPort.sendDoubleData(dMeters);
		}		
		catch(NumberFormatException e) { 
			log.log(Level.SEVERE, "NAN in "+s, e);
		}

	}


}
