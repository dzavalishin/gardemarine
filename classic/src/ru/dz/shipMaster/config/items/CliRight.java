package ru.dz.shipMaster.config.items;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.dz.shipMaster.config.ConfigListItem;
import ru.dz.shipMaster.misc.RightsControl;

/**
 * Represents user's (group's, in fact) right to do something. Generally contains
 * name of the class that can be modified or somehow else used by someone who has this
 * right.   
 * @author dz
 *
 */
public class CliRight extends ConfigListItem {
	private static final Logger log = Logger.getLogger(CliRight.class.getName()); 

	
	private String name;

	private String targetClassName;
	private boolean canEdit;
	/** Shortcut for speed */
	private Class<? extends RightsControl> targetClass;

	@Override
	public void destroy() {
		targetClass = null;
	}

	
	/**
	 * @param toTest Class to check edit right for.
	 * @return True if editing of object of class 'toTest' is granted by this right.
	 */
	public boolean canEdit(Class<? extends RightsControl> toTest)
	{
		return canEdit && toTest.equals(getTargetClass());
	}
	
	private Class<? extends RightsControl> getTargetClass() {
		if(targetClass == null)
		{
			if(targetClassName == null) return null;
			try {
				targetClass = (Class<? extends RightsControl>) Class.forName(targetClassName);
			} catch (ClassNotFoundException e) {
				log.log(Level.SEVERE, "Can't cload class",e);
			} 
		}
		return targetClass;
	}


	/**
	 * Create new right. It is supposed to be done only from here, see below.
	 * @param name Name of right.
	 * @param targetClass Class to object of which this right is applied.
	 * @param canEdit True if objects of above class can be edited by right holder.
	 */
	protected CliRight(String name, @SuppressWarnings("rawtypes") Class targetClass, boolean canEdit )
	{
		this.name = name;
		this.targetClassName = targetClass.getName();
		this.canEdit = canEdit;
	}

	// -----------------------------------------------------------
	// Old junk, mostly to kill 
	// -----------------------------------------------------------

	

	
	/*private boolean canControlShip = false;
	private boolean canSetupParameters = false;
	private boolean canSetupUsers = false;
	private boolean canSetupGrous = false;*/


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof CliRight))
			return false;

		CliRight him = (CliRight) obj;

		return 
		//canControlShip == him.canControlShip &&
		//canSetupParameters == him.canSetupParameters &&
		//canSetupUsers == him.canSetupUsers &&
		//canSetupGrous == him.canSetupGrous &&
		getTargetClass() == him.getTargetClass() &&
		canEdit == him.canEdit &&
		true;
	}

	
	@Override
	public int hashCode() {
		return getTargetClass().hashCode() + (canEdit ? 1:0);
	}
	
	
	
	/**
	 * Default constructor. For deserialization mostly.
	 */
	public CliRight()
	{
		name = "";
		targetClassName = "";
	}

	/*private CliRight( String name )
	{
		this.name = name;		
	}*/

	private static Vector<CliRight> out = null;

	//public static final CliRight shipControlRight = new CliRight("Ship Control");	

	//public static final CliRight parametersSetupRight = new CliRight("Parameters setup");	

	//public static final CliRight usersControl = new CliRight("Users control");	

	//public static final CliRight groupsControl = new CliRight("Groups control");

	/**
	 * Get list of all existing right.
	 * @return Rights.
	 */
	public static Vector<CliRight> getRights()
	{
		if(out == null)
		{
			out = new Vector<CliRight>();
			/*{ shipControlRight.canControlShip = true; out.add(shipControlRight); }
			{ parametersSetupRight.canSetupParameters = true; out.add(parametersSetupRight); }
			{ usersControl.canSetupUsers = true; out.add(usersControl); }
			{ groupsControl.canSetupGrous = true; out.add(groupsControl); }*/
			
			out.add(new CliRight("Edit users", CliUser.class, true));
			out.add(new CliRight("Edit groups", CliGroup.class, true));
			out.add(new CliRight("Edit conversions", CliConversion.class, true));
			out.add(new CliRight("Edit buses", CliBus.class, true));
			out.add(new CliRight("Edit channels", CliPipe.class, true));
			out.add(new CliRight("Edit drivers", CliDriver.class, true));
			out.add(new CliRight("Edit system drivers", CliSystemDriver.class, true));
			out.add(new CliRight("Edit window structures", CliWindowStructure.class, true));
			out.add(new CliRight("Edit parameters", CliParameter.class, true));
			out.add(new CliRight("Edit windows", CliWindow.class, true));
			out.add(new CliRight("Edit panels", CliInstrumentPanel.class, true));
			out.add(new CliRight("Edit instruments", CliInstrument.class, true));
			out.add(new CliRight("Edit loggers", CliLogger.class, true));
			out.add(new CliRight("Edit network inputs", CliNetInput.class, true));
			out.add(new CliRight("Edit network hosts", CliNetHost.class, true));
			out.add(new CliRight("Edit panel constraints", CliWindowPanelConstraint.class, true));
			out.add(new CliRight("Edit alarms", CliAlarm.class, true));
			out.add(new CliRight("Edit alarm stations", CliAlarmStation.class, true));

		}

		// TODO NLS? How?		
		return out;
	}

	/** This class has no properties dialog. Instances shouldn't be modified. */
	@Override
	public void displayPropertiesDialog() {
		// No.
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

	
	public String getTargetClassName() {		return targetClassName;	}
	/**
	 * Set name of class governed by this right.
	 * @param targetClassName Full class name.
	 */
	public void setTargetClassName(String targetClassName) {		this.targetClassName = targetClassName;	}

	/**
	 * @return True if this right grants ability to edit object of target class.
	 */
	public boolean isCanEdit() {		return canEdit;	}

	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}


}
