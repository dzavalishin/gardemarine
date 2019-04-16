package ru.dz.shipMaster.dev.nmeaSentence;

import java.util.Vector;
import java.util.logging.Level;

import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.dev.DriverPort;

public class NmeaMDA extends GeneralNmeaSentenceDriver {

	private DriverPort pressureMPort, pressureBPort, airTemperaturePort;

	@Override
	public String getSentenceId() { return "MDA"; }
	
	@Override
	protected void updatePorts(Vector<DriverPort> ports) {
		pressureMPort = getPort(ports, 0, Direction.Input, Type.Numeric, "Pressure, inches of mercury");
		pressureMPort.setDescription("Barometric pressure, inches of mercury, to the nearest 0.01 inch");
		pressureBPort = getPort(ports, 1, Direction.Input, Type.Numeric, "Pressure, bars");
		pressureBPort.setDescription("Barometric pressure, bars, to the nearest .001 bar");
		airTemperaturePort = getPort(ports, 2, Direction.Input, Type.Numeric, "Air temperature, C");
		
	}

	@Override
	public void parse(NMEA0183Sentence s) throws InvalidNMEASentenceException {
		checkValidity(s);
		//checkLastA(s);

		checkField(s,1,"I");
		checkField(s,3,"B");
		checkField(s,5,"C");
		
		try { 
			pressureMPort.sendDoubleData( Double.parseDouble(s.getField(0)) );
			pressureBPort.sendDoubleData( Double.parseDouble(s.getField(2)) );
			airTemperaturePort.sendDoubleData( Double.parseDouble(s.getField(4)) );
			}
		catch(NumberFormatException e) { 
			log.log(Level.SEVERE, "NAN", e);
		}
		
	}


}
