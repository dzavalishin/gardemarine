package ru.dz.shipMaster.ui.config.item;

import ru.dz.shipMaster.config.items.CliSystemDriver;
import ru.dz.shipMaster.dev.AbstractDriver;
import ru.dz.shipMaster.ui.config.ConfigItemEditor;

public class CieSystemDriver extends ConfigItemEditor {

	private final CliSystemDriver bean;
	private final AbstractDriver myDriver;

	/**
	 * Create new editor for this bean. 
	 * @param bean Bean to edit.
	 */
	public CieSystemDriver(CliSystemDriver bean, AbstractDriver myDriver) {
		this.bean = bean;
		this.myDriver = myDriver;
	
		//GridBagConstraints cons = new GridBagConstraints(consL);
		
		consL.gridwidth = 2;
		panel.add(myDriver.getSetupPanel(),consL);
		consL.gridwidth = 1;
	}
	
	@Override
	protected void informContainer() {		bean.informContainers(); }

	@Override
	public void applySettings() {
		myDriver.savePanelSettings();	
	}

	@Override
	public void discardSettings() {
		myDriver.loadPanelSettings();
	}

	@Override
	public String getName() { return myDriver.getDeviceName(); }

	@Override
	public String getTypeName() { return "System Driver"; }

}
