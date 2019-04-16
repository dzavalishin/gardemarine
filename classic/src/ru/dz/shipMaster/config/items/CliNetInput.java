package ru.dz.shipMaster.config.items;

import ru.dz.shipMaster.config.ConfigListItem;
import ru.dz.shipMaster.ui.config.item.CieNetInput;

/**
 * Describes network reception endpoint. Each endpoint will be connected to local parameter
 * and will receive data from network, if any will be translated. 
 * @author dz
 */
public class CliNetInput extends ConfigListItem {
	private String hostName;
	private String itemName;
	private CliParameter target;
	private boolean enabled = false;

	
	@Override
	public void destroy() {
		target = null;
		dialog = null;			
	}


	// Dialog
	
	private CieNetInput dialog = null;
	



	@Override
	public void displayPropertiesDialog() {
		if(!hasEditRight()) return;
		if(dialog == null) dialog = new CieNetInput(this);
		dialog.displayPropertiesDialog(); 
		}


	
	@Override
	public String getName() { return hostName+":"+itemName; }



	public String getHostName() {		return hostName;	}
	/**
	 * From which host to receive this item.
	 * @param hostName Name of the host, case is ignored.
	 */
	public void setHostName(String hostName) {		this.hostName = hostName;	}


	public String getItemName() {		return itemName;	}
	/**
	 * Item name must be equal (ignoring case) to translated parameter name on source computer.
	 * @param itemName Name of item to receive.
	 */
	public void setItemName(String itemName) {		this.itemName = itemName;	}



	public CliParameter getTarget() {		return target;	}
	/**
	 * Which parameter received data will be translated to.
	 * @param target Parameter.
	 */
	public void setTarget(CliParameter target) {		this.target = target;	}


	public boolean isEnabled() {		return enabled;	}
	/**
	 * Is this item active. Passive items will not be connected on network startup.
	 * @param enabled True to set item as active.
	 */
	public void setEnabled(boolean enabled) {		this.enabled = enabled;	}
}
