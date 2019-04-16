package ru.dz.shipMaster.ui.meter;

@SuppressWarnings("serial")
public abstract class GeneralInstrumentWithRoundScale extends GeneralMeter implements
		IInstrumentWithRoundScale {
	protected double baseRotationAngle; // base rotation of the meter
	protected double scaleAngle;

	protected double calcValRotation(double val) {
		//System.out.println("base "+baseRotationAngle+" scale "+scaleAngle+" minmax "+minimum+"-"+maximum+" val "+val);
		double ret = baseRotationAngle + (((val-minimum)*scaleAngle)/(maximum-minimum));
		//System.out.println("ret "+ret);
		return ret;
	}

	
	/* (non-Javadoc)
	 * @see ru.dz.shipMaster.ui.meter.IInstrumentWithRoundScale#setBaseRotationAngle(double)
	 */
	@Override
	public void setBaseRotationAngle(double baseRotationAngle) { this.baseRotationAngle = baseRotationAngle; }

	/* (non-Javadoc)
	 * @see ru.dz.shipMaster.ui.meter.IInstrumentWithRoundScale#getBaseRotationAngle()
	 */
	@Override
	public double getBaseRotationAngle() { return baseRotationAngle; }

	/* (non-Javadoc)
	 * @see ru.dz.shipMaster.ui.meter.IInstrumentWithRoundScale#setScaleAngle(double)
	 */
	@Override
	public void setScaleAngle(double scaleAngle) { this.scaleAngle = scaleAngle; }

	/* (non-Javadoc)
	 * @see ru.dz.shipMaster.ui.meter.IInstrumentWithRoundScale#getScaleAngle()
	 */
	@Override
	public double getScaleAngle() { return scaleAngle; }


}
