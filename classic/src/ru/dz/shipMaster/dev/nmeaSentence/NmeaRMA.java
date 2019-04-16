package ru.dz.shipMaster.dev.nmeaSentence;

import java.util.Vector;

import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.dev.DriverPort;

/**
 RMA - Recommended Minimum Navigation Information.
 <pre>
                                                    12
        1 2       3 4        5 6   7   8   9   10  11|
        | |       | |        | |   |   |   |   |   | |
 $--RMA,A,llll.ll,a,yyyyy.yy,a,x.x,x.x,x.x,x.x,x.x,a*hh<CR><LF>

 Field Number: 
  1) Blink Warning
  2) Latitude
  3) N or S
  4) Longitude
  5) E or W
  6) Time Difference A, uS
  7) Time Difference B, uS
  8) Speed Over Ground, Knots
  9) Track Made Good, degrees true
 10) Magnetic Variation, degrees
 11) E or W
 12) Checksum
</pre>

 */


public class NmeaRMA extends GeneralNmeaSentenceDriver {
	private DriverPort lattitudePort, longitudePort, magVarPort, speedPort;
	private DriverPort trackPort;


	@Override
	public String getSentenceId() { return "RMA"; }

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
	}

	@Override
	public void parse(NMEA0183Sentence s) throws InvalidNMEASentenceException {
		checkValidity(s);

		lattitudePort.sendDoubleData(parseLattitude(s, s.getField(1), s.getField(2)));
		longitudePort.sendDoubleData(parseLongitude(s, s.getField(3), s.getField(4)));
		magVarPort.sendDoubleData(parseMagneticVariation(s, s.getField(9), s.getField(10)));

		s.sendNumericField(7, speedPort);
		s.sendNumericField(8, trackPort);
	}


}
