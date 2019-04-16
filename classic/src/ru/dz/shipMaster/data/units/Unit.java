package ru.dz.shipMaster.data.units;

import java.util.Vector;

import ru.dz.shipMaster.config.ConfigListItem;
import ru.dz.shipMaster.misc.UnitConversionException;
import ru.dz.shipMaster.ui.config.item.CieUnit;

public class Unit extends ConfigListItem {
	public static final String BOOLEAN_UNIT_NAME = "Boolean";
	private UnitGroup group;
	private String name = "New unit";
	private String longName = name; // Will be the same as name until changed

	// Conversion to reference unit
	private double firstShift = 0;
	private double firstMultiplicator = 1;
	private boolean oneDividedBy = false;
	private double secondMultiplicator = 1;
	private double secondShift = 0;

	private Unit(String name, UnitGroup group) {
		this.name = name;
		this.longName = name;
		this.group = group;
	}


	public Unit() {
		// for tests and xml loader only
	}


	@Override
	public void destroy() {
		group = null;
		dialog = null;			
	}


	public double convertToReference(double value)
	{
		if(oneDividedBy)
			return secondShift+(secondMultiplicator*(1/(firstMultiplicator*(value+firstShift))));
		else
		{
			return secondShift+(secondMultiplicator*firstMultiplicator*(value+firstShift));
		}
	}

	public double convertFromReference(double value)
	{
		if(oneDividedBy)
			return ((1/((value-secondShift)/secondMultiplicator)) / firstMultiplicator) - firstShift;
		else
		{
			return (((value-secondShift)/secondMultiplicator)/firstMultiplicator)-firstShift;
		}
	}

	static public double convertFromTo(Unit fromUnit, Unit toUnit, double value) throws UnitConversionException
	{
		if( toUnit.getGroup() != fromUnit.getGroup() || toUnit.getGroup() == null )
			throw new UnitConversionException("Can't convert from "+fromUnit.getName()+" to "+toUnit.getName());
		return toUnit.convertFromReference(fromUnit.convertToReference(value));
	}

	private CieUnit dialog = null;
	private boolean predefined = false;

	@Override
	public void displayPropertiesDialog() {
		if(dialog == null) dialog = new CieUnit(this);
		dialog.displayPropertiesDialog(); 
	}


	
	
	
	
	
	
	
	
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Unit) {
			Unit u = (Unit) obj;
			return 
			(name.equals(u.name)) &&
			predefined == u.predefined
			;
		}
		return false;
	}
	
	
	@Override
	public int hashCode() {
		return name.hashCode()+ (predefined ? 1 : 0);
	}
	
	
	
	
	
	
	
	private static UnitGroup boolUG = new UnitGroup();
	private static Unit boolUnit = new Unit(BOOLEAN_UNIT_NAME, boolUG);

	static { 
		boolUG.setReference(boolUnit);
		boolUG.setMetricDefault(boolUnit);
		boolUG.setNauticalDefault(boolUnit);
		boolUG.setImperialDefault(boolUnit);
		boolUnit.predefined = true;
	}

	
	private static Vector<Unit> out = null;	
	/**
	 * Get list of predefined fixed units.
	 * @return Units.
	 */
	public static Vector<Unit> getPredefinedUnits() {

		if(out == null)
		{
			out = new Vector<Unit>();


			out.add(boolUnit);
		}

		// TODO NLS? How?		
		return out;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	public double getFirstShift() {		return firstShift;	}
	public void setFirstShift(double fisrtShift) {		this.firstShift = fisrtShift;	}

	public double getSecondShift() {		return secondShift;	}
	public void setSecondShift(double secondShift) {		this.secondShift = secondShift;	}

	public double getFirstMultiplicator() {		return firstMultiplicator;	}
	public void setFirstMultiplicator(double multiplicator) {		this.firstMultiplicator = multiplicator;	}

	public UnitGroup getGroup() {		
		/*if(group == null) {
			group = new UnitGroup();
			group.setReference(this);
			//ConfigurationFactory.getConfiguration().getUnitGroupItems().add(group);
			}*/
		return group;
	}

	public void setGroup(UnitGroup group) {		
		this.group = group;	
	}

	@Override
	public String toString() { return longName == null ? name : longName; }

	public String getName() {		return name;	}
	public void setName(String name) {
		if(this.longName == this.name)
			this.longName = name;
		this.name = name;	
	}

	public boolean isOneDividedBy() {		return oneDividedBy;	}
	public void setOneDividedBy(boolean oneDividedBy) {		this.oneDividedBy = oneDividedBy;	}

	public double getSecondMultiplicator() {		return secondMultiplicator;	}
	public void setSecondMultiplicator(double secondMultiplicator) {		this.secondMultiplicator = secondMultiplicator;	}

	public String getLongName() {		return longName;	}
	public void setLongName(String longName) {		this.longName = longName == null ? name : longName;	}


	public boolean isPredefined() {		return predefined ;	}
	public void setPredefined(boolean predefined) {		this.predefined = predefined;	}
	
	
	
	



}
