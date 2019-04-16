package ru.dz.shipMaster.config.items;

import java.util.Vector;

import ru.dz.shipMaster.config.ConfigListItem;
import ru.dz.shipMaster.data.DataSink;
import ru.dz.shipMaster.data.DataSource;
import ru.dz.shipMaster.data.filter.ParameterDataSource;
import ru.dz.shipMaster.data.history.ParameterHistory;
import ru.dz.shipMaster.data.units.Unit;
import ru.dz.shipMaster.ui.config.item.CieParameter;
import ru.dz.shipMaster.ui.meter.GeneralMeter;

/**
 * This class represents a named instance used to communicate data between 
 * indicator and driver. Network can be source or destination for parameters 
 * too.
 * @author dz
 *
 */

public class CliParameter extends ConfigListItem {
	public enum Type {Numeric, Boolean, String, Image };
	public enum Direction { /** Is from outer world to us. */ Input, /** Is from us to them. */ Output };
	
	private String name = "group.name";
	private String legend = "";
	private boolean translateToNet = true;
	private Type type = Type.Numeric;
	private Direction direction = Direction.Input;
	private CliAlarm alarm;
	private Vector<CliConversion> conversions = new Vector<CliConversion>(); 
	private double minValue = 0;
	private double maxValue = 100;
	private Unit unit = null;
	private boolean checkTimeout = false;
	private boolean debug = false;

		
	
	@Override
	public void destroy() {
		conversions.removeAllElements();
		unit = null;
		dialog = null;			
	}

	
	private CieParameter dialog = null;
	
	@Override
	public void displayPropertiesDialog() {
		if(!hasEditRight()) return;
		if(dialog == null) dialog = new CieParameter(this);
		dialog.displayPropertiesDialog(); 
		}

	// Meters interface
	
	private ParameterDataSource ds = new ParameterDataSource(this);
	
	public void detachMeter(DataSink sink) {
		ds.removeMeter(sink);
	}

	public void attachMeter(DataSink sink) {
		ds.addMeter(sink);
		
		if (sink instanceof GeneralMeter) {
			GeneralMeter meter = (GeneralMeter) sink;
			
			if(alarm != null)
				meter.setAlarmRegions(alarm.getRegions(false));
			meter.setInputUnit(unit);
		}		
	}
	
	// Drivers interface
	
	public void setDataSource(DataSource ids)
	{
		ds.setDataSource(ids);
	}
	
	// system start/stop
	
	public void start()
	{
		ds.reactivate();
		ds.setDebug(debug);
	}
	
	public void stop()
	{
		ds = null;
		ds = new ParameterDataSource(this);
		ds.setDebug(debug);
	}
	
	// Getters/setters
	
	public Direction getDirection() {	return direction;	}
	public void setDirection(Direction direction) {		this.direction = direction;	}
	
	public String getName() {		return name;	}
	public void setName(String name) {		this.name = name;	}

	public boolean isTranslateToNet() {		return translateToNet; 	}
	public void setTranslateToNet(boolean translateToNet) {		this.translateToNet = translateToNet; 	}

	public Type getType() {		return type;	}
	public void setType(Type type) {		this.type = type; ds.reactivate();	}

	public CliAlarm getAlarm() { return alarm; }
	public void setAlarm(CliAlarm alarm) {		this.alarm = alarm;	}

	public Vector<CliConversion> getConversions() {		return conversions;	}
	public void setConversions(Vector<CliConversion> conversions) {		this.conversions = conversions;	}

	public double getMaxValue() {		return maxValue;	}
	public void setMaxValue(double maxValue) {		this.maxValue = maxValue; ds.reactivate();	}

	public double getMinValue() {		return minValue;	}
	public void setMinValue(double minValue) {		this.minValue = minValue;  ds.reactivate();	}

	public Unit getUnit() {		return unit;	}
	public void setUnit(Unit unit) {		this.unit = unit;	}

	public boolean isDebug() {		return debug;	}
	public void setDebug(boolean debug) 
	{		
		this.debug = debug;
		ds.setDebug(debug);
	}

	
	
	/**
	 * @return the legend
	 */
	public String getLegend() {
		return (legend.length() == 0) ? name : legend;
	}

	public boolean haveLegend() { return legend.length() > 0; }
	
	/**
	 * @param legend the legend to set
	 */
	public void setLegend(String legend) {
		this.legend = legend;
	}

	public DataSource getDataSource() {
		return ds;
	}

	public DataSink getSink() {

		return ds.getSink();
	}
	
	
	public boolean isCheckTimeout() {		return checkTimeout;	}
	public void setCheckTimeout(boolean checkTimeout) {		this.checkTimeout = checkTimeout;	}


	public ParameterHistory getHistory() {
		return ds.getHistory();
	}


}
