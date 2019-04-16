package ru.dz.shipMaster.config.items;


import ru.dz.shipMaster.config.ConfigListItem;
import ru.dz.shipMaster.ui.config.item.CieWindowPanelConstraint;

public class CliWindowPanelConstraint extends ConfigListItem {
	GridBagConstraintsWrapper constraints = new GridBagConstraintsWrapper();
	{
		constraints.weightx = 1;
		constraints.weighty = 1;
	}
	private boolean visible = true;
	private String id = "";
	
	@Override
	public void destroy() {
		constraints = null;
		dialog = null;			
	}

	
	private CieWindowPanelConstraint dialog = null;
	
	@Override
	public void displayPropertiesDialog() {
		if(!hasEditRight()) return;
		if(dialog == null) dialog = new CieWindowPanelConstraint(this);
		dialog.displayPropertiesDialog(); 
		}

	@Override
	public String getName() {
		if(!visible)
			return id+": off";
		return id+": at "+constraints.gridx+"-"+constraints.gridy+ " ("+constraints.gridwidth+"x"+constraints.gridheight+")";
	}

	public GridBagConstraintsWrapper getConstraints() {		return constraints;	}
	public void setConstraints(GridBagConstraintsWrapper constraints) {		this.constraints = constraints;	}

	public boolean isVisible() {		return visible;	}
	public void setVisible(boolean visible) {		this.visible = visible;	}

	/**
	 * Id is used in properties edit code.
	 * @param id
	 */
	public void setId( String id) { this.id = id; }
	public String getId() {		return id;	}
	
}

