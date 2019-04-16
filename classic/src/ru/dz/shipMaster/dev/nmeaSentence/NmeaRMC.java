package ru.dz.shipMaster.dev.nmeaSentence;

import java.util.Vector;
import java.util.logging.Level;

import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.dev.DriverPort;

/*


 RMC - Recommended Minimum Navigation Information
                                                            12
        1         2 3       4 5        6  7   8   9    10 11|  13
        |         | |       | |        |  |   |   |    |  | |   |
 $--RMC,hhmmss.ss,A,llll.ll,a,yyyyy.yy,a,x.x,x.x,xxxx,x.x,a,m,*hh<CR><LF>

 Field Number: 
  1) UTC Time
  2) Status, V=Navigation receiver warning A=Valid
  3) Latitude
  4) N or S
  5) Longitude
  6) E or W
  7) Speed over ground, knots
  8) Track made good, degrees true
  9) Date, ddmmyy
 10) Magnetic Variation, degrees
 11) E or W
 12) FAA mode indicator (NMEA 2.3 and later)
 13) Checksum

A status of V means the GPS has a valid fix that is below an internal
quality threshold, e.g. because the dilution of precision is too high 
or an elevation mask test failed.


 */


public class NmeaRMC extends GeneralNmeaSentenceDriver {
	private DriverPort lattitudePort, longitudePort, magVarPort;
	private DriverPort speedPort;
	private DriverPort trackPort;
	private DriverPort timePort;
	private DriverPort datePort;


	@Override
	public String getSentenceId() { return "RMC"; }

	@Override
	protected void updatePorts(Vector<DriverPort> ports) {
		lattitudePort = getPort(ports, 0, Direction.Input, Type.Numeric, "Lattitude");
		lattitudePort.setDescription("S is negative, N is positive");
		longitudePort = getPort(ports, 1, Direction.Input, Type.Numeric, "Longitude");
		longitudePort.setDescription("W is negative, E is positive");

		magVarPort = getPort(ports, 2, Direction.Input, Type.Numeric, "Magnetic variation");
		magVarPort.setDescription("W is negative, E is positive");
	
		speedPort = getPort(ports, 3, Direction.Input, Type.Numeric, "Speed Over Ground, Knots");
		trackPort = getPort(ports, 4, Direction.Input, Type.Numeric, "Track made good, degrees true");
		
		timePort = getPort(ports, 5, Direction.Input, Type.String, "Time string");
		datePort = getPort(ports, 6, Direction.Input, Type.String, "Date string");
	}

	@Override
	public void parse(NMEA0183Sentence s) throws InvalidNMEASentenceException {
		checkValidity(s);

		if(!s.getField(1).equalsIgnoreCase("A"))
		{
			log.log(Level.SEVERE,"Invalid "+getSentenceId()+" data: "+s);
			return;
		}
		
		lattitudePort.sendDoubleData(parseLattitude(s, s.getField(2), s.getField(3)));
		longitudePort.sendDoubleData(parseLongitude(s, s.getField(4), s.getField(5)));
		magVarPort.sendDoubleData(parseMagneticVariation(s, s.getField(9), s.getField(10)));

		s.sendNumericField(6, speedPort);
		s.sendNumericField(7, trackPort);
		
		timePort.sendStringData(s.getField(0));
		datePort.sendStringData(s.getField(8));
	}


}
