package ru.dz.shipMaster.dev.nmeaSentence;

import java.util.Vector;
import java.util.logging.Level;

import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.dev.DriverPort;

public class NmeaRPM extends GeneralNmeaSentenceDriver {
	private static final int NCHANNELS = 4;

	private DriverPort[] rpmPorts = new DriverPort[NCHANNELS];
	private DriverPort[] pitchPorts = new DriverPort[NCHANNELS];


	@Override
	public String getSentenceId() { return "RPM"; }

	@Override
	protected void updatePorts(Vector<DriverPort> ports) {

		for(int i = 0; i < NCHANNELS; i++)
		{
			rpmPorts[i] = getPort(ports, i, Direction.Input, Type.Numeric, "RPM."+i);
			pitchPorts[i] = getPort(ports, i+NCHANNELS, Direction.Input, Type.Numeric, "Pitch."+i);
		}
	}

	@Override
	public void parse(NMEA0183Sentence s) throws InvalidNMEASentenceException {
		checkValidity(s);
		checkLastA(s);

		try { 			

			int channel  = (int) Double.parseDouble(s.getField(1) );

			if(channel >= NCHANNELS)
			{
				log.log(Level.SEVERE,"Channel "+channel+" received, max supported is "+(NCHANNELS-1));
				return;
			}	
			double rpm   = Double.parseDouble(s.getField(2) );
			log.log(Level.FINER,"ch "+channel+" rpm "+rpm);
			rpmPorts[channel].sendDoubleData(rpm);

			if(!s.getField(3).equalsIgnoreCase("A"))
			{
				double pitch = Double.parseDouble(s.getField(3) );
				log.log(Level.FINER,"ch "+channel+" pitch "+pitch);
				pitchPorts[channel].sendDoubleData(pitch);
			}
		}		
		catch(NumberFormatException e) { 
			log.log(Level.SEVERE, "NAN", e);
		}

	}


}
