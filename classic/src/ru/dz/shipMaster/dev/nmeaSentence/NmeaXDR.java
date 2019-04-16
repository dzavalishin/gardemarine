package ru.dz.shipMaster.dev.nmeaSentence;

import java.util.Vector;

import ru.dz.shipMaster.config.items.CliParameter.Direction;
import ru.dz.shipMaster.config.items.CliParameter.Type;
import ru.dz.shipMaster.dev.DriverPort;

public class NmeaXDR extends GeneralNmeaSentenceDriver {

	private DriverPort pitchPort, rollPort;
	private DriverPort relChillTempPort;
	private DriverPort theorChillTempPort;
	private DriverPort heatIndexPort;
	private DriverPort stationPressurePort;

	@Override
	public String getSentenceId() { return "XDR"; }
	
	@Override
	protected void updatePorts(Vector<DriverPort> ports) {
		pitchPort = getPort(ports, 0, Direction.Input, Type.Numeric, "Pitch");
		rollPort = getPort(ports, 1, Direction.Input, Type.Numeric, "Roll");
	
		relChillTempPort 	= getPort(ports, 2, Direction.Input, Type.Numeric, "Relative chill temperature, C");
		theorChillTempPort	= getPort(ports, 3, Direction.Input, Type.Numeric, "Theoretical wind chill temperature, C");
		heatIndexPort		= getPort(ports, 4, Direction.Input, Type.Numeric, "Calculated heat index, C");
		stationPressurePort	= getPort(ports, 5, Direction.Input, Type.Numeric, "Measured pressure, bars");
		stationPressurePort.setDescription("Actual measured barometric pressure, or \"station pressure\", bars, to thenearest 0.001 bar");
	}

	@Override
	public void parse(NMEA0183Sentence s) throws InvalidNMEASentenceException {
		checkValidity(s);
		//checkLastA(s);

		if(s.getField(0).equalsIgnoreCase("A"))
			parseB(s);
		else
			parseA(s);
	}
	
	public void parseB(NMEA0183Sentence s) throws InvalidNMEASentenceException {
		
		checkField(s, 0, "A");
		checkField(s, 2, "D");
		checkField(s, 3, "PTCH");
		checkField(s, 4, "A");
		checkField(s, 6, "D");
		checkField(s, 7, "ROLL");
		
		/*
		try { 
			pitchPort.sendDoubleData( Double.parseDouble(s.getField(1)) );
			rollPort.sendDoubleData( Double.parseDouble(s.getField(5)) );
			}
		catch(NumberFormatException e) { 
			log.log(Level.SEVERE, "NAN", e);
		}
		*/
		
		sendPossibleDoubleField(s,1,pitchPort);
		sendPossibleDoubleField(s,5,rollPort);
		
	}

	public void parseA(NMEA0183Sentence s) throws InvalidNMEASentenceException {
		checkField(s, 0, "C");
		checkField(s, 2, "C");
		checkField(s, 3, "WCHR");
		checkField(s, 4, "C");
		checkField(s, 6, "C");
		checkField(s, 7, "WCHT");
		checkField(s, 8, "C");
		checkField(s, 10, "C");
		checkField(s, 11, "HINX");
		checkField(s, 12, "P");
		checkField(s, 14, "B");
		checkField(s, 15, "STNP");
		
		sendPossibleDoubleField(s,1,relChillTempPort);
		sendPossibleDoubleField(s,5,theorChillTempPort);
		sendPossibleDoubleField(s,9,heatIndexPort);
		sendPossibleDoubleField(s,13,stationPressurePort);
		
	}
	

}
