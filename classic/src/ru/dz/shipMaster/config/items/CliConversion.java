package ru.dz.shipMaster.config.items;

import java.util.Vector;

import ru.dz.shipMaster.config.ConfigListItem;
import ru.dz.shipMaster.data.DataSource;
import ru.dz.shipMaster.data.filter.GeneralFilterDataSource;
import ru.dz.shipMaster.data.filter.LowPassFilter;
import ru.dz.shipMaster.data.filter.NormalizeFilter;
import ru.dz.shipMaster.ui.config.item.CieConversion;
/**
 * Conversion is used to process input data and convert it
 * to internal representation suitable for displaying.
 * @author dz
 *
 */
public class CliConversion extends ConfigListItem {
	private String name = "group.name";

	private double inMin = 0, inMax = 1, outMin = 0, outMax = 1;
	private int outputLPFilterWindowSize = 4;
	private Vector<GeneralFilterDataSource> filters = new Vector<GeneralFilterDataSource>();


	
	private boolean limitInput = true;

	
	private CieConversion dialog = null;
	
	
	@Override
	public void destroy() {
		filters = null;
		dialog = null;			
	}

	
	@Override
	public void displayPropertiesDialog() {
		if(!hasEditRight()) return;
		if(dialog == null) dialog = new CieConversion(this);
		dialog.displayPropertiesDialog(); 
		}

	
	// Getters/setters
	
	public String getName() {		return name;	}
	public void setName(String name) {		this.name = name;	}

	public boolean isLimitInput() {		return limitInput; 	}
	public void setLimitInput(boolean translateToNet) {		this.limitInput = translateToNet; 	}


	public double getInMax() {		return inMax;	}
	public void setInMax(double inMax) {		this.inMax = inMax;	}

	public double getInMin() {		return inMin;	}
	public void setInMin(double inMin) {		this.inMin = inMin;	}

	public double getOutMax() {		return outMax;	}
	public void setOutMax(double outMax) {		this.outMax = outMax;	}

	public double getOutMin() {		return outMin;	}
	public void setOutMin(double outMin) {		this.outMin = outMin;	}

	
	public int getOutputLPFilterWindowSize() {		return outputLPFilterWindowSize;	}
	/**
	 * Each conversion has low pass filter connected at the output. 
	 * Here filter window size can be set.
	 * @param outputLPFilterWindowSize
	 */
	public void setOutputLPFilterWindowSize(int outputLPFilterWindowSize) {		this.outputLPFilterWindowSize = outputLPFilterWindowSize;	}


	public Vector<GeneralFilterDataSource> getFilters() {
		return (Vector<GeneralFilterDataSource>)filters.clone();
	}


	public void setFilters(Vector<GeneralFilterDataSource> filters) {
		this.filters.clear();
		this.filters.addAll( filters );
	}


	public DataSource createChain(DataSource portDataSource) {
		DataSource ds = portDataSource;
		
		ds = new NormalizeFilter(ds, inMin, inMax, outMin, outMax, limitInput );
		//ds = new LowPassFilter(ds, 22);
		
		// TODO dount use original filter object, instantiate new one
		for(GeneralFilterDataSource f : filters)
		{
			f.setDataSource(ds);
			ds = f;
		}
		
		return new LowPassFilter(ds, outputLPFilterWindowSize);
	}

}
