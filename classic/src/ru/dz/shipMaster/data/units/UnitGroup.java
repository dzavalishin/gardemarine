package ru.dz.shipMaster.data.units;

import java.util.Vector;

import ru.dz.shipMaster.config.ConfigListItem;
import ru.dz.shipMaster.ui.config.item.CieUnitGroup;

public class UnitGroup extends ConfigListItem {
	/** Unit to which other units define conversions */
	private Unit reference;
	private Unit metricDefault;
	private Unit imperialDefault;
	private Unit nauticalDefault;
	private Vector<Unit> units;

	@Override
	public void destroy() {
		reference = null;
		metricDefault = null;
		imperialDefault = null;
		nauticalDefault = null;
		units.removeAllElements();
		dialog = null;			
	}

	
	public void addUnit(Unit u)
	{
		units.add(u);
		u.setGroup(this);
		if(reference == null)
			reference = u;
	}

	private CieUnitGroup dialog = null;

	@Override
	public void displayPropertiesDialog() {
		if(dialog == null) dialog = new CieUnitGroup(this);
		dialog.displayPropertiesDialog(); 
	}

	@Override
	public String getName() { return reference == null ? "Unknown unit group" : reference.getLongName(); }

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		//sb.append(reference == null ? "Unknown unit group" : reference.getLongName());

		if(units != null)
		{
			sb.append(" (");
			boolean first = true;
			for(Unit u : units)
			{
				if(u == null)
					continue;
				if(!first)
					sb.append(", ");
				first = false;
				sb.append(u.getName());
			}
			sb.append(")");
		}

		return sb.toString();

		//return super.toString();
	}

	public Unit getReference() {		return reference;	}
	public void setReference(Unit reference) {		this.reference = reference;	}

	public Vector<Unit> getUnits() {		return units;	}
	public void setUnits(Vector<Unit> units) {		this.units = units;	}

	/**
	 * @return the imperialDefault
	 */
	public Unit getImperialDefault() {
		return imperialDefault;
	}

	/**
	 * @param imperialDefault Default unit for imperial system.
	 */
	public void setImperialDefault(Unit imperialDefault) {
		this.imperialDefault = imperialDefault;
	}

	/**
	 * @return the metricDefault
	 */
	public Unit getMetricDefault() {
		return metricDefault;
	}

	/**
	 * @param metricDefault Default for metric system.
	 */
	public void setMetricDefault(Unit metricDefault) {
		this.metricDefault = metricDefault;
	}

	public Unit getNauticalDefault() {
		return nauticalDefault;
	}

	/**
	 * 
	 * @param nauticalDefault default unit for nautical system.
	 */
	public void setNauticalDefault(Unit nauticalDefault) {
		this.nauticalDefault = nauticalDefault;
	}


}
