package ru.dz.shipMaster.dev.nmeaSentence;

import java.util.Vector;
import java.util.logging.Level;

import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.dev.DriverPort;

public class NmeaVHW extends GeneralNmeaSentenceDriver {
	private DriverPort speedKnotsPort;
	private DriverPort speedKmHPort;


	@Override
	public String getSentenceId() { return "VHW"; }

	@Override
	protected void updatePorts(Vector<DriverPort> ports) {
		speedKnotsPort = getPort(ports, 0, Direction.Input, Type.Numeric, "Speed in knots");
		speedKmHPort = getPort(ports, 1, Direction.Input, Type.Numeric, "Speed in km/h");
	}

	private int nlogging = 10;

	@Override
	public void parse(NMEA0183Sentence s) throws InvalidNMEASentenceException {
		checkValidity(s);
		//checkLastA(s);


		if( (!s.getField(5).equals("N")) || (!s.getField(7).equals("K")) )
		{
			log.log(Level.SEVERE,"Invalid record: "+s);
		}

		try { 			
			speedKmHPort.sendDoubleData(Double.parseDouble(s.getField(6)));
		}		
		catch(NumberFormatException e) { 
			if(nlogging-- > 0)
				log.log(Level.SEVERE, "NAN in "+s, e);
		}

		try { 			
			speedKnotsPort.sendDoubleData(Double.parseDouble(s.getField(4)));
		}		
		catch(NumberFormatException e) { 
			if(nlogging-- > 0)
				log.log(Level.SEVERE, "NAN in "+s, e);
		}

		if(nlogging < 0)
			nlogging = 0;
		
	}


}
