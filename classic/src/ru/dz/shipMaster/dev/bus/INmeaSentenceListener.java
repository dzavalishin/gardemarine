package ru.dz.shipMaster.dev.bus;

import ru.dz.shipMaster.dev.nmeaSentence.NMEA0183Sentence;

public interface INmeaSentenceListener {

	public void receiveNmeaSentence(NMEA0183Sentence s);
	
}
