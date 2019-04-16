package ru.dz.shipMaster.ui.meter;

import java.awt.Dimension;

@SuppressWarnings("serial")
public class GeoNumber extends JustNumber implements IInstrumentWithDigits {

	protected int nDigits;

	protected Dimension findSize() {
		int w = myMetrics.charWidth('0')*9+myMetrics.charWidth(' ')*0+myMetrics.charWidth('\'')+myMetrics.charWidth('"');
		Dimension myPreferredSize = new Dimension(w,myMetrics.getHeight());
		//System.out.println("GeoNumber.findSize() "+myPreferredSize);
		return myPreferredSize;
	}

	@Override
	public void setNDigits(int nDigits) {
		this.nDigits = nDigits;
	}

	@Override
	public int getNDigits() { return nDigits; }
	
}
