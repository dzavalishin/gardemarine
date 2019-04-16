package ru.dz.shipMaster.config.items;

import java.util.Vector;

import javax.swing.JComponent;

import ru.dz.shipMaster.config.ConfigListItem;
import ru.dz.shipMaster.ui.config.item.CieWindowStructure;

public class CliWindowStructure extends ConfigListItem {
	private static final int MAXPANELS = 10;
	private String name = "New";
	Vector<CliWindowPanelConstraint> constraints = new Vector<CliWindowPanelConstraint>(); 

	@Override
	public void destroy() {
		constraints.removeAllElements();
		dialog = null;			
	}

	
	public CliWindowStructure()
	{
		for(int i = 0; i < MAXPANELS; i++) {
			CliWindowPanelConstraint c = new CliWindowPanelConstraint();
			c.setId(Character.toString((char)('A'+i)));
			constraints.add(c);			
		}
	}
	
	@Override
	public String getName() { return name; }

	private CieWindowStructure dialog = null;
	
	@Override
	public void displayPropertiesDialog() {
		if(!hasEditRight()) return;
		if(dialog == null) dialog = new CieWindowStructure(this);
		dialog.displayPropertiesDialog(); 
		}

	public void applyConstraints(JComponent container, Vector<JComponent> components)
	{
		int i = 0;
		container.removeAll();
		for( JComponent jc : components )
		{
			//container.setC
			//jc.set
			container.add(jc, constraints.elementAt(i).getConstraints() );
			jc.setVisible(constraints.elementAt(i).isVisible());
			i++;
		}
	}
	
	// getters/setters
	
	public void setName(String name) { this.name = name; }

	public Vector<CliWindowPanelConstraint> getConstraints() {		return constraints;		}
	public void setConstraints(Vector<CliWindowPanelConstraint> constraints) {		this.constraints = constraints;	}


	public static CliWindowStructure createDefaultStructure() {
		CliWindowStructure ret = new CliWindowStructure();
		
		for( CliWindowPanelConstraint c : ret.constraints )
		{
			c.setVisible(false);
		}
		
		ret.constraints.get(0).setVisible(true);
		//ret.constraints.get(0).getConstraints().fill = 
		
		return ret;
	}

}
