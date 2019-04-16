package ru.dz.shipMaster.dev.nmeaSentence;

import java.util.Vector;
import java.util.logging.Level;

import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.dev.DriverPort;

/*
 GLL - Geographic Position - Latitude/Longitude

	1       2 3        4 5         6 7   8
	|       | |        | |         | |   |
 $--GLL,llll.ll,a,yyyyy.yy,a,hhmmss.ss,a,m,*hh<CR><LF>

 Field Number: 
  1) Latitude
  2) N or S (North or South)
  3) Longitude
  4) E or W (East or West)
  5) Universal Time Coordinated (UTC)
  6) Status A - Data Valid, V - Data Invalid
  7) FAA mode indicator (NMEA 2.3 and later)
  8) Checksum
 
 */


public class NmeaGLL extends GeneralNmeaSentenceDriver {
	private DriverPort lattPort, longPort, timePort;


	@Override
	public String getSentenceId() { return "GLL"; }

	@Override
	protected void updatePorts(Vector<DriverPort> ports) {
		lattPort = getPort(ports, 0, Direction.Input, Type.String, "Lattitude string");
		longPort = getPort(ports, 1, Direction.Input, Type.String, "Longitude string");
		timePort = getPort(ports, 2, Direction.Input, Type.String, "Time string");
	}

	@Override
	public void parse(NMEA0183Sentence s) throws InvalidNMEASentenceException {
		checkValidity(s);
		//checkLastA(s);

		try { 			
		
			if( (!s.getField(5).equals("A")))
			{
				log.log(Level.SEVERE,"Invalid record: "+s);
			}

			double lattit = Double.parseDouble(s.getField(0));
			double longit = Double.parseDouble(s.getField(2));
			

			timePort.sendStringData( s.getField(4) );
			lattPort.sendStringData(""+lattit+s.getField(1));
			longPort.sendStringData(""+longit+s.getField(3));

		}		
		catch(NumberFormatException e) { 
			log.log(Level.SEVERE, "NAN in "+s, e);
		}

	}


}
