package ru.dz.shipMaster.ui.misc;

public class AlarmRegion implements Comparable<AlarmRegion> {
	private final boolean critical;
	private final boolean user;
	private final double from;
	private final double to;
	
	
	/**
	 * Create region.
	 * @param from Lower margin of region. Starting from this value (inclusive) region begins.
	 * @param to Upper value for the region, first one to not to be included.
	 * @param critical True for critical regions, false for warning.
	 * @param user True for user regions, false for system.
	 */
	public AlarmRegion(double from, double to, boolean critical, boolean user)
	{
		this.from = from;
		this.to = to;
		this.critical = critical;
		this.user = user;		
	}

	/**
	 * @return True for critical region, false for warning
	 */
	public boolean isCritical() {		return critical;	}

	/**
	 * @return Starting value for the region, inclusive
	 */
	public double getFrom() {		return from;	}

	/**
	 * @return Ending value for the region, the value itself is excluded. But who cares.
	 */
	public double getTo() {		return to;	}

	/**
	 * @return True for user region.
	 */
	public boolean isUser() {		return user;	}

	
	
	public int compareTo(AlarmRegion o) {
		if(Math.abs(o.from - from) < 0.001)
			return 0;
		return (o.from < from) ? 1 : -1;
	}
	
	
}
