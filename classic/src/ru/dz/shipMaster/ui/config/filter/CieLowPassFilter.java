package ru.dz.shipMaster.ui.config.filter;

import ru.dz.shipMaster.data.filter.LowPassFilter;

public class CieLowPassFilter extends CieGeneralFilter {

	private final LowPassFilter bean;
	
	
	public CieLowPassFilter(LowPassFilter bean) {
		super(bean);
		this.bean = bean;
	}

	@Override
	protected void informContainer() {		
		bean.informContainers(); 
		}

	@Override
	public void applySettings() {
		super.applySettings();
	}

	@Override
	public void discardSettings() {
		super.discardSettings();
	}
	
	@Override
	public String getTypeName() { return "Low pass filter"; }

}
