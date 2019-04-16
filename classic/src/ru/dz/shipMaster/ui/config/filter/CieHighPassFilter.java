package ru.dz.shipMaster.ui.config.filter;

import ru.dz.shipMaster.data.filter.HighPassFilter;

public class CieHighPassFilter extends CieGeneralFilter {

	//private final HighPassFilter bean;
	
	
	public CieHighPassFilter(HighPassFilter bean) {
		super(bean);
		//this.bean = bean;
	}

	@Override
	protected void informContainer() {		
		// TODO implement informContainer
		//bean.informContainers(); 
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
	public String getTypeName() { return "Hight pass filter"; }

}
