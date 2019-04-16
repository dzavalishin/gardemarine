package ru.dz.shipMaster.config.items;

import java.util.Vector;

import ru.dz.shipMaster.config.ConfigListItem;
import ru.dz.shipMaster.misc.RightsControl;
import ru.dz.shipMaster.ui.config.item.CieGroup;

/**
 * Users group. Really it is a role.
 * @author dz
 */
public class CliGroup extends ConfigListItem {

	private String name = "New", password = "";
	private Vector<CliRight> rights = new Vector<CliRight>(); 


	private CieGroup dialog = null;
	
	
	@Override
	public void destroy() {
		rights = null;
		dialog = null;			
	}

	
	@Override
	public void displayPropertiesDialog() {
		//if(!hasEditRight()) return;
		if(dialog == null) dialog = new CieGroup(this);
		dialog.displayPropertiesDialog(); 
		}

	//public boolean hasRight(CliRight r) { return rights.contains(r); }
	/**
	 * Returns true if someone who belongs to this group can edit objects
	 * of this class.
	 * @param objectOfClass Class of objects to check for right to edit.
	 * @return True if right is granted.
	 */
	public boolean canEdit(Class<? extends RightsControl> objectOfClass)
	{
		for( CliRight r : rights )
			if( r.canEdit(objectOfClass))
				return true;
		return false;
	}
	
	// Getters/Setters
	
	public void setName(String groupName) {		this.name = groupName;	}
	public String getName() {		return name;	}

	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; 	}

	public Vector<CliRight> getRights() {		return rights;	}
	public void setRights(Vector<CliRight> rights) {		this.rights = rights;	}

}
