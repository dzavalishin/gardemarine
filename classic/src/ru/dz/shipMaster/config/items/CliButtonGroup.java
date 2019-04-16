package ru.dz.shipMaster.config.items;

import java.util.Vector;
import java.util.WeakHashMap;
import java.util.logging.Logger;

import ru.dz.shipMaster.config.ConfigListItem;
import ru.dz.shipMaster.ui.config.item.CieButtonGroup;
import ru.dz.shipMaster.ui.control.DashButton;

/**
 * Represents group of buttons which can't be turned on at once.   
 * @author dz
 *
 */
public class CliButtonGroup extends ConfigListItem {
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(CliButtonGroup.class.getName()); 
	
	private String name;

	
	@Override
	public void destroy() {
	}

	private WeakHashMap<DashButton, Object> buttons = new WeakHashMap<DashButton, Object>(8);
	
	
	public void addButton( DashButton b ) { buttons.put(b, null); }
	public void removeButton( DashButton b ) { buttons.remove(b); }

	public void propagate( DashButton b ) 
	{ 
		for( DashButton i : buttons.keySet() )
		{
			if( i == b )
				continue;
			i.setPressedExternal(false);
		}
	}


	/**
	 * Create new group. It is supposed to be done only from here, see below.
	 * @param name Name of the group.
	 */
	protected CliButtonGroup(String name)
	{
		this.name = name;
	}

	
	/**
	 * Default constructor. For deserialization mostly.
	 */
	public CliButtonGroup()
	{
		name = "";
	}


	private static Vector<CliButtonGroup> out = null;


	/**
	 * Get list of all existing right.
	 * @return Rights.
	 */
	public static Vector<CliButtonGroup> getGroups()
	{
		if(out == null)
		{
			out = new Vector<CliButtonGroup>();
			
			out.add(new CliButtonGroup("Group 1"));
			out.add(new CliButtonGroup("Group 2"));
			out.add(new CliButtonGroup("Group 3"));
			out.add(new CliButtonGroup("Group 4"));
			out.add(new CliButtonGroup("Group 5"));
			out.add(new CliButtonGroup("Group 6"));
			out.add(new CliButtonGroup("Group 7"));
			out.add(new CliButtonGroup("Group 8"));
			out.add(new CliButtonGroup("Group 9"));
			out.add(new CliButtonGroup("Group 10"));
		}

		// TODO NLS? How?		
		return out;
	}

	private CieButtonGroup	dialog;
	
	/** This class has no properties dialog. Instances shouldn't be modified. */
	@Override
	public void displayPropertiesDialog() {
		if(dialog == null) dialog = new CieButtonGroup(this);
		dialog.displayPropertiesDialog(); 
	}

	/**
	 * Get name of right - user readable.
	 */
	@Override
	public String getName() { return name;	}

	/**
	 * Set name of right - user readable.
	 */
	public void setName(String name) {		this.name = name;	}

	

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CliButtonGroup) {
			CliButtonGroup g = (CliButtonGroup) obj;
			return g.name.equalsIgnoreCase(name);
		}
		return false;
	}


}
