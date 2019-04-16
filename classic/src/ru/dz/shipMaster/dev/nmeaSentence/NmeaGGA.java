package ru.dz.shipMaster.dev.nmeaSentence;

import java.util.Vector;
import java.util.logging.Level;

import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.dev.DriverPort;


/*
GGA - Global Positioning System Fix Data
Time, Position and fix related data for a GPS receiver.

        1         2       3 4        5 6 7  8   9  10 |  12 13  14   15
        |         |       | |        | | |  |   |   | |   | |   |    |
 $--GGA,hhmmss.ss,llll.ll,a,yyyyy.yy,a,x,xx,x.x,x.x,M,x.x,M,x.x,xxxx*hh<CR><LF>

 Field Number: 
  1) Universal Time Coordinated (UTC)
  2) Latitude
  3) N or S (North or South)
  4) Longitude
  5) E or W (East or West)
  6) GPS Quality Indicator,
     0 - fix not available,
     1 - GPS fix,
     2 - Differential GPS fix
     (values above 2 are 2.3 features)
     3 = PPS fix
     4 = Real Time Kinematic
     5 = Float RTK
     6 = estimated (dead reckoning)
     7 = Manual input mode
     8 = Simulation mode
  7) Number of satellites in view, 00 - 12
  8) Horizontal Dilution of precision (meters)
  9) Antenna Altitude above/below mean-sea-level (geoid) (in meters)
 10) Units of antenna altitude, meters
 11) Geoidal separation, the difference between the WGS-84 earth
     ellipsoid and mean-sea-level (geoid), "-" means mean-sea-level
     below ellipsoid
 12) Units of geoidal separation, meters
 13) Age of differential GPS data, time in seconds since last SC104
     type 1 or 9 update, null field when DGPS is not used
 14) Differential reference station ID, 0000-1023
 15) Checksum

 */


public class NmeaGGA extends GeneralNmeaSentenceDriver {
	private DriverPort timePort, lattitudePort, longitudePort, numSatPort, horDilutionPort,
	altitudePort, separationPort;


	@Override
	public String getSentenceId() { return "GGA"; }

	@Override
	protected void updatePorts(Vector<DriverPort> ports) {
		timePort = getPort(ports, 0, Direction.Input, Type.String, "Time string");

		lattitudePort = getPort(ports, 1, Direction.Input, Type.Numeric, "Lattitude");
		lattitudePort.setDescription("S is negative, N is positive");
		longitudePort = getPort(ports, 2, Direction.Input, Type.Numeric, "Longitude");
		longitudePort.setDescription("W is negative, E is positive");

		numSatPort = getPort(ports, 3, Direction.Input, Type.Numeric, "Number of sattelites");

		horDilutionPort = getPort(ports, 4, Direction.Input, Type.Numeric, "Horizontal Dilution of precision, meters");

		altitudePort = getPort(ports, 5, Direction.Input, Type.Numeric, "Altitude, meters");
		altitudePort.setDescription("Antenna altitude above/below mean-sea-level (geoid)");

		separationPort = getPort(ports, 6, Direction.Input, Type.Numeric, "Geoidal separation, meters");
		separationPort.setDescription("the difference between the WGS-84 earth ellipsoid and mean-sea-level");
	}

	@Override
	public void parse(NMEA0183Sentence s) throws InvalidNMEASentenceException {
		checkValidity(s);

		// 1) Universal Time Coordinated (UTC)
		timePort.sendStringData( s.getField(0) );

		{
			// 2) Latitude
			// 3) N or S (North or South)
			String val = s.getField(1);
			String dir = s.getField(2);
			lattitudePort.sendDoubleData(parseLattitude(s, val, dir));
		}

		{ 			
			// 4) Longitude
			// 5) E or W (East or West)
			String val = s.getField(3);
			String dir = s.getField(4);
			longitudePort.sendDoubleData(parseLongitude(s, val, dir));
		}

		// 6) GPS Quality Indicator,
		// 7) Number of satellites in view, 00 - 12

		s.sendNumericField(6, numSatPort);

		// 8) Horizontal Dilution of precision (meters)

		s.sendNumericField(7, horDilutionPort);

		// 9) Antenna Altitude above/below mean-sea-level (geoid) (in meters)
		// 10) Units of antenna altitude, meters

		if( s.getField(8).length() == 0 )
			altitudePort.sendDoubleData(0);
		{
			if( (s.getField(9).equals("M")) )
				s.sendNumericField(8, altitudePort);
			else
				log.log(Level.SEVERE,"Invalid altitude: "+s);
		}
		// 11) Geoidal separation, the difference between the WGS-84 earth ellipsoid and mean-sea-level (geoid), "-" means mean-sea-level below ellipsoid
		// 12) Units of geoidal separation, meters

		if( s.getField(10).length() == 0 )
			separationPort.sendDoubleData(0);
		else
		{
			if( (s.getField(11).equals("M")) )
				s.sendNumericField(10, separationPort);
			else
				log.log(Level.SEVERE,"Invalid separation: "+s);
		}

		// 13) Age of differential GPS data, time in seconds since last SC104 type 1 or 9 update, null field when DGPS is not used
		// 14) Differential reference station ID, 0000-1023


	}




}
