package ru.dz.shipMaster.data.history;

import java.util.Iterator;

public class ParameterHistory {

	public class HistoryIterator implements Iterator<HistoryPoint> {
		int pos = putPosition;

		public HistoryIterator() {
		}

		@Override
		public boolean hasNext() {
			return pos != putPosition;
		}

		@Override
		public HistoryPoint next() {
			synchronized (ParameterHistory.this) {

				HistoryPoint point = new HistoryPoint(time[pos], value[pos]);
				pos++;
				if( pos >= size ) pos = 0;
				return point;
			}
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	public class HistoryPoint {
		private long time;
		private double value;

		public HistoryPoint(long time, double value) {
			super();
			this.time = time;
			this.value = value;
		}

		public long getTime() {			return time;		}
		public double getValue() {		return value;		}


	}

	/** msec, as in System.currentTimeMillis() */
	protected final long []			time;
	protected final double []			value;
	protected int putPosition = 0;
	protected int used = 0;
	private final int size;

	public ParameterHistory(int size ) {
		this.size = size;
		time = new long[size];
		value = new double[size];
	}

	public ParameterHistory(int secondsToHold, int pointsPerMinute ) {
		this( Math.min(1, secondsToHold /(pointsPerMinute*60) ) );
	}

	public ParameterHistory() {
		this(60*20*10); // some 10 min
	}


	/** How many slots are used. */
	public int used() { 
		return used;
	}

	public synchronized void put( long time, double value )
	{
		this.time[putPosition] = time;
		this.value[putPosition] = value;
		putPosition++;
		if(putPosition >= size)
			putPosition = 0;

		if( used < size )
			used++;
	}

	public Iterator<HistoryPoint> iterator()
	{
		return new HistoryIterator();
	}

}
