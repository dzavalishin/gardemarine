package ru.dz.shipMaster.config;

import java.util.Vector;
import java.util.logging.Logger;

import ru.dz.shipMaster.config.items.CliButtonGroup;
import ru.dz.shipMaster.config.items.CliRight;
import ru.dz.shipMaster.config.items.CliSystemDriver;
import ru.dz.shipMaster.data.units.Unit;

public class ConstantState {
    protected static final Logger log = Logger.getLogger(ConstantState.class.getName()); 
	
	
	/**
	 * Returns list of drivers that must be there for system to work.
	 * This driver list can not be altered, but driver settings can be.
	 * @return Vector of drivers.
	 */
	public Vector<CliSystemDriver> getSystemDriverItems() {		return systemDriverItems;	}
	private Vector<CliSystemDriver> systemDriverItems = new Vector<CliSystemDriver>();


	
	{
		systemDriverItems.add(new CliSystemDriver(new ru.dz.shipMaster.dev.system.DataPump()));
		systemDriverItems.add(new CliSystemDriver(new ru.dz.shipMaster.dev.system.Tray()));
		systemDriverItems.add(new CliSystemDriver(new ru.dz.shipMaster.dev.system.ScreenDimmer()));
		
	}

	// -----------------------------------------------------------
	// Rights 
	// -----------------------------------------------------------

	
	
	/**
	 * Returns system-wide set of access rights used to determine user's
	 * ability to do different kinds of things. 
	 * @return Vector of all existing rights objects.
	 */
	Vector<CliRight> getRightsItems() {		return CliRight.getRights();	}
	//private Vector<CliRight> rightsItems = CliRight.getRights();

	Vector<CliButtonGroup> getButtonGroupsItems() {		return CliButtonGroup.getGroups();	}
	
	// -----------------------------------------------------------
	// Units 
	// -----------------------------------------------------------

	Vector<Unit> getUnitItems() {
		return Unit.getPredefinedUnits();
	}

	public static Unit getBooleanUnit()
	{
		for(Unit u : ConfigurationFactory.getConfiguration().getUnitItems() )
		{
			if(u.isPredefined() && u.getName().equalsIgnoreCase(Unit.BOOLEAN_UNIT_NAME))
				return u;
		}
		log.severe("No boolean Unit");
		throw new RuntimeException("No boolean Unit");
	}
	
}
