package ru.dz.shipMaster.config.items;

import ru.dz.shipMaster.config.ConfigListItem;
import ru.dz.shipMaster.dev.AbstractDriver;
import ru.dz.shipMaster.ui.config.item.CieSystemDriver;

public class CliSystemDriver extends ConfigListItem {
	private AbstractDriver driver = null;

	
	@Override
	public void destroy() {
		driver = null;
		dialog = null;			
	}

	/**
	 * Default. It is supposed that setDriver follows.
	 */
	public CliSystemDriver() {/* empty */}
	
	/**
	 * This one is for easier list building.
	 * @param driver
	 */
	public CliSystemDriver(AbstractDriver driver) { this.driver = driver; }
	
	private CieSystemDriver dialog = null;
	
	@Override
	public void displayPropertiesDialog() {
		if(!hasEditRight()) return;
		if(dialog == null) dialog = new CieSystemDriver(this,driver);
		dialog.displayPropertiesDialog(); 
		}

	@Override
	public String getName() { return driver.getDeviceName(); }

	public AbstractDriver getDriver() {		return driver;	}
	public void setDriver(AbstractDriver driver) {		this.driver = driver;	}

}
