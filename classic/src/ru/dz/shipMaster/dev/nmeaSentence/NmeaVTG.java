package ru.dz.shipMaster.dev.nmeaSentence;

/* 
   VTG - Track made good and Ground speed

         1  2  3  4  5	6  7  8 9   10
         |  |  |  |  |	|  |  | |   |
 $--VTG,x.x,T,x.x,M,x.x,N,x.x,K,m,*hh<CR><LF>

 Field Number: 
  1) Track Degrees
  2) T = True
  3) Track Degrees
  4) M = Magnetic
  5) Speed Knots
  6) N = Knots
  7) Speed Kilometers Per Hour
  8) K = Kilometers Per Hour
  9) FAA mode indicator (NMEA 2.3 and later)
  10) Checksum

Note: in some older versions of NMEA 0183, the sentence looks like this:

         1  2  3   4  5
         |  |  |   |  |
 $--VTG,x.x,x,x.x,x.x,*hh<CR><LF>

 Field Number: 
  1) True course over ground (degrees) 000 to 359
  2) Magnetic course over ground 000 to 359
  3) Speed over ground (knots) 00.0 to 99.9
  4) Speed over ground (kilometers) 00.0 to 99.9
  5) Checksum

The two forms can be distinguished by field 2, which will be
the fixed text 'T' in the newer form.  The new form appears
to have been introduced with NMEA 3.01 in 2002.

Some devices, such as those described in [GLOBALSAT], leave the
magnetic-bearing fields 3 and 4 empty.

 */

import java.util.Vector;
import java.util.logging.Level;

import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.dev.DriverPort;

public class NmeaVTG extends GeneralNmeaSentenceDriver {
	private DriverPort trueDegreesPort, magneticDegreesPort, speedKnotsPort, speedKmHPort;


	@Override
	public String getSentenceId() { return "VTG"; }

	@Override
	protected void updatePorts(Vector<DriverPort> ports) {
		trueDegreesPort = getPort(ports, 0, Direction.Input, Type.Numeric, "True course, degrees");
		magneticDegreesPort = getPort(ports, 1, Direction.Input, Type.Numeric, "Magnetic course, degrees");
		speedKnotsPort = getPort(ports, 2, Direction.Input, Type.Numeric, "Speed, knots");
		speedKmHPort = getPort(ports, 3, Direction.Input, Type.Numeric, "Speed, km/h");
	}

	@Override
	public void parse(NMEA0183Sentence s) throws InvalidNMEASentenceException {
		checkValidity(s);
		//checkLastA(s);

		try { 			


			if( (s.getField(1).equals("T")) )
			{
				if( !(s.getField(3).equals("M")) )
				{
					log.log(Level.SEVERE,"Invalid record: "+s);
				}
				// New style
				/*{
					String f0 = s.getField(0);
					double trueDegrees = f0.length() > 0 ? Double.parseDouble(f0) : 0;
					trueDegreesPort.sendDoubleData(trueDegrees);
				}
				{
					String f2 = s.getField(2);
					double magneticDegrees = f2.length() > 0 ? Double.parseDouble(f2) : 0;
					magneticDegreesPort.sendDoubleData(magneticDegrees);
				}*/

				s.sendNumericField(0, trueDegreesPort);
				s.sendNumericField(2, magneticDegreesPort);

				//speedKnotsPort.sendDoubleData(Double.parseDouble(s.getField(4)));
				//speedKmHPort.sendDoubleData(Double.parseDouble(s.getField(6)));
				s.sendNumericField(4, speedKnotsPort);
				s.sendNumericField(6, speedKmHPort);
			}
			else
			{
				// old style

				/*{
					String f0 = s.getField(0);
					double trueDegrees = f0.length() > 0 ? Double.parseDouble(f0) : 0;
					trueDegreesPort.sendDoubleData(trueDegrees);
				}
				{
					String f2 = s.getField(1);
					double magneticDegrees = f2.length() > 0 ? Double.parseDouble(f2) : 0;				
					magneticDegreesPort.sendDoubleData(magneticDegrees);
				}*/

				s.sendNumericField(0, trueDegreesPort);
				s.sendNumericField(1, magneticDegreesPort);
				s.sendNumericField(2, speedKnotsPort);
				s.sendNumericField(3, speedKmHPort);
			}

		}		
		catch(NumberFormatException e) { 
			log.log(Level.SEVERE, "NAN in "+s, e);
		}

	}


}
