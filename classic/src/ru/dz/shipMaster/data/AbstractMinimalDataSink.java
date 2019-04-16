package ru.dz.shipMaster.data;

import ru.dz.shipMaster.config.items.CliAlarm.NearStatus;

public abstract class AbstractMinimalDataSink implements DataSink {

	//private NearStatus nearStatus;
	@Override
	public void setCurrent(double newCurr, NearStatus nearStatus) {
		//this.nearStatus = nearStatus;
		setCurrent(newCurr);
	}
	
	
	@Override
	public double getCurrentValue() { return -1; }

	//public double getWarningThreshold() { return -1; }
	//public double getCriticalThreshold() { return -1; }

	@Override
	public double getMaximum() { return -1; }
	@Override
	public double getMinimum() { return -1; }

	//public void setWarningThreshold(double newWarnPcnt) { }
	//public void setCriticalThreshold(double newCritPcnt) {}

	@Override
	public void setLegend(String newLegend) { }
	@Override
	public void setUnits(String newUnits) { }

	@Override
	public void setMaximum(double max) { }
	@Override
	public void setMinimum(double min) { }	

}
