package ru.dz.shipMaster.ui.meter;

public interface IInstrumentWithTicks {

	/** How many labels there will be. */
	public abstract void setNumNumbers(int numNumbers);

	public abstract int getNumNumbers();

	/** Number of minor (unnumbered) ticks. */
	public abstract int getNumMinorTicks();

	public abstract void setNumMinorTicks(int numMinorTicks);

}