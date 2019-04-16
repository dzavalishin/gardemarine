package ru.dz.shipMaster.config.items;

import java.util.Vector;

import ru.dz.shipMaster.config.ConfigListItem;
import ru.dz.shipMaster.misc.RightsControl;
import ru.dz.shipMaster.ui.config.item.CieUser;

public class CliUser extends ConfigListItem {
	private String login = "New";
	private String password = "";
	private Vector<CliGroup> groups = new Vector<CliGroup>(); 
	
	@Override
	public void destroy() {
		groups.removeAllElements();
		dialog = null;			
	}

	
	private CieUser dialog = null;
	
	@Override
	public void displayPropertiesDialog() {
		if(!hasEditRight()) return;
		if(dialog == null) dialog = new CieUser(this);
		dialog.displayPropertiesDialog(); 
		}

	@Override
	public String getName() { return login; }

	/*public boolean hasRight(CliRight r) 
	{
		// TO DO cache rights?
		for(CliGroup g : groups )
			if( g.hasRight(r) )
				return true;
		return false;
	}*/

	public boolean canEdit(Class<? extends RightsControl> target) {
		for(CliGroup g : groups )
			if( g.canEdit(target) )
				return true;
		return false;
	}
	
	// Getters/Setters
	
	public String getLogin() { return login; }
	public void setLogin(String login) {		this.login = login;	}

	public String getPassword() { 
		return password; 
		}
	
	public void setPassword(String password) { 
		this.password = password; 	
		}

	public Vector<CliGroup> getGroups() {		return groups;	}
	public void setGroups(Vector<CliGroup> groups) {		this.groups = groups;	}

	
}
