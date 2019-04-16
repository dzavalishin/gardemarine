package ru.dz.shipMaster.dev.nmeaSentence;

import java.util.Vector;
import java.util.logging.Level;

import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.dev.DriverPort;

public class NmeaDPT extends GeneralNmeaSentenceDriver {
	private DriverPort offsetPort;
	private DriverPort depthMetersPort;


	@Override
	public String getSentenceId() { return "DPT"; }

	@Override
	protected void updatePorts(Vector<DriverPort> ports) {
		depthMetersPort = getPort(ports, 0, Direction.Input, Type.Numeric, "Depth in meters");
		offsetPort = getPort(ports, 1, Direction.Input, Type.Numeric, "Offset from transducer");
		offsetPort.setDescription("positive means distance from tansducer to water line, negative means distance from transducer to keel");
	}

	@Override
	public void parse(NMEA0183Sentence s) throws InvalidNMEASentenceException {
		checkValidity(s);
		//checkLastA(s);

		try { 			
		
			depthMetersPort.sendDoubleData(Double.parseDouble(s.getField(0)));
			offsetPort.sendDoubleData(Double.parseDouble(s.getField(1)));
		}		
		catch(NumberFormatException e) { 
			log.log(Level.SEVERE, "NAN in "+s, e);
		}

	}


}
