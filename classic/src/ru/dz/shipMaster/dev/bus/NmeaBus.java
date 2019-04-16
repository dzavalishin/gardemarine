package ru.dz.shipMaster.dev.bus;

import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;

import ru.dz.shipMaster.data.misc.CommunicationsException;
import ru.dz.shipMaster.dev.common.BiPipeReceiveEventListener;
import ru.dz.shipMaster.dev.nmeaSentence.NMEA0183Sentence;
import ru.dz.shipMaster.events.GenericEvent;

public class NmeaBus extends AbstractPipedBus   {

	private BiPipeReceiveEventListener recv = new BiPipeReceiveEventListener() {
		@Override
		public void event(GenericEvent event) {
			String line = pipe.getPipe().readLineUnblocked();
			if(line == null)
				return;

			String[] split = line.split("\\$");
			
			//processLine(line);
			
			for( String one : split )
			{
				if(one.length() > 0)
					processLine("$"+one);
			}
		}
	};

	private BiPipeReceiveEventListener err = new BiPipeReceiveEventListener() {
		@Override
		public void event(GenericEvent event) {
			String line = pipe.getPipe().readLineUnblocked();
			log.severe(line);
		}
	};

	@Override
	protected void doStartBus() throws CommunicationsException {
		pipe.getPipe().addReceiveListener(recv);
		pipe.getPipe().addErrorListener(err);
		pipe.getPipe().connect();
	}


	@Override
	protected void doStopBus() throws CommunicationsException {
		if(pipe == null || pipe.getPipe() == null)
			return;
		pipe.getPipe().disconnect();
		pipe.getPipe().removeReceiveListener(recv);
		pipe.getPipe().removeErrorListener(err);
	}





	@Override
	public String getDeviceName() {
		return "Nmea0183 input";
	}

	@Override
	public boolean isAutoSeachSupported() {
		return false;
	}


	private Set<INmeaSentenceListener> listeners = new TreeSet<INmeaSentenceListener>();
	/**
	 * Adds receiver for the nmea messages
	 * @param generalNmeaSentenceDriver
	 */
	public void addDataListener(
			INmeaSentenceListener listener) {
		listeners.add(listener);
	}

	public void removeDataListener(
			INmeaSentenceListener listener) {
		listeners.remove(listener);
	}

	protected void sendSentence(NMEA0183Sentence sentence) {
		for(INmeaSentenceListener l : listeners)
		{
			l.receiveNmeaSentence(sentence);
		}

		showMessage(sentence.toString());
	}


	private void processLine(String line) {
		try {
			System.out.println("NmeaBus.data("+line+")");
			NMEA0183Sentence sentence = new NMEA0183Sentence(line);
			sendSentence(sentence);
		}
		catch(IllegalArgumentException e)
		{
			log.log(Level.SEVERE,"Invalid NMEA sentence: "+line,e);
		}
		catch( StringIndexOutOfBoundsException e )
		{
			log.log(Level.SEVERE,"Invalid NMEA sentence: "+line,e);
		}
	}


}
