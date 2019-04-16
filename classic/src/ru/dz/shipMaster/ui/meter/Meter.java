package ru.dz.shipMaster.ui.meter;

import ru.dz.shipMaster.data.DataSink;


public interface Meter extends DataSink {

	
	
	public boolean isHistogramVisible();
	public void setHistogramVisible(boolean histogramVisible);
	
}
