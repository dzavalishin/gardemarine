package ru.dz.shipMaster.av.audio;

public interface ISoundGenerator {

	void start();
	void start( long timeMsec );
	void stop();
}
