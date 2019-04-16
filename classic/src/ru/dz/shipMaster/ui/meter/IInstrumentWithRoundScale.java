package ru.dz.shipMaster.ui.meter;

public interface IInstrumentWithRoundScale {

	/** where on the circle scale starts, 0-360 degrees */
	public abstract void setBaseRotationAngle(double baseRotationAngle);

	public abstract double getBaseRotationAngle();

	/** How much of the circle scale takes, 0-360 degrees. */
	public abstract void setScaleAngle(double scaleAngle);

	public abstract double getScaleAngle();

}