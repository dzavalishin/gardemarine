package ru.dz.shipMaster.data.filter;

import java.awt.Image;

import ru.dz.shipMaster.config.ContainerList;
import ru.dz.shipMaster.config.IConfigListItem;
import ru.dz.shipMaster.data.AbstractMinimalDataSink;
import ru.dz.shipMaster.data.DataGeneration;
import ru.dz.shipMaster.data.DataSource;
import ru.dz.shipMaster.data.GeneralDataSource;
import ru.dz.shipMaster.ui.config.BasicItemContainer;

/**
 * Filters are used to process data.
 * @author dz
 */
public abstract class GeneralFilterDataSource extends GeneralDataSource implements IConfigListItem {

	private String name = "New filter";
	
	protected DataSource src;
	
	@Override
	public void destroy() {
		src = null;
	}

	
	/**
	 * @return Current input for filter.
	 */
	public DataSource getDataSource() { return src; }
	
	/**
	 * Set filter input.
	 * @param ds Input data source.
	 */
	public void setDataSource( DataSource ds )
	{
		if(src != null)
			src.removeMeter(sink);
		src = ds;
		this.min = calculateOutMin( src.getMin(), src.getMax());
		this.max = calculateOutMax(src.getMin(),src.getMax());
		this.legend = src.getLegend();
		this.units = src.getUnits();		
		src.addMeter(sink);
	}
	
	/**
	 * Supposed to be redefined in subclass. Returns minimal output value of
	 * filter.
	 * @param inMin Input minimum value.
	 * @param inMax Input maximum value.
	 * @return Minimal filter output value.
	 */
	protected double calculateOutMin( double inMin, double inMax ) 
	{ return inMin; }
	
	protected double calculateOutMax( double inMin, double inMax ) 
	{ return inMax; }
	
	/** Used to receive data from source */ 
	private final FilterDataSink sink = new FilterDataSink(); 

	protected double currInVal = 0;
	
	protected GeneralFilterDataSource() {/*empty*/}
	
	public GeneralFilterDataSource(DataSource src) {
		super( src.getMin(), src.getMax(), 
				//src.getWarn(), src.getCrit(), 
				src.getLegend(), src.getUnits());
		this.src = src;
		src.addMeter(sink);
	}

	public GeneralFilterDataSource(DataSource src, double imin, double imax ) {
		super( imin, imax, 
				//src.getWarn(), src.getCrit(), 
				src.getLegend(), src.getUnits());
		this.src = src;
		src.addMeter(sink);
	}

	protected void callSourceDataPumpEntry(DataGeneration currentGeneration)
	{
		//if(src != null) src.performMeasure();
		if(src != null) src.dataPumpEntry(currentGeneration);
	}

	
	/** Called by timer */
	@Override
	abstract public void performMeasure();

	

	/* * Called by underlying source on data available */
	//@Deprecated	abstract protected void pushValue( double val );
	
	
	protected class FilterDataSink extends AbstractMinimalDataSink
	{
		@Override
		public void setCurrent(double newCurr) 
		{ 			
			currInVal = newCurr; 
			//pushValue(newCurr); 
		}
		
		// Just pass by synchronously
		@Override
		public void receiveImage(Image val) {
			translateImageToMeters(val);			
		}

		@Override
		public void receiveString(String val) {
			translateStringToMeters(val);			
		}
	}
	

	/** 
	 * Must be implemented due to IConfigListItem rules 
	 * @return Name of filter.
	 */
	public final String getName() { return name; }
	public final void setName(String name) {		this.name = name;	}
	
	@Override
	public String toString() { return getName() + " ("+this.getClass().getSimpleName() + ")"; }


	private ContainerList clist = new ContainerList();

	@Override
	public void addContainer(BasicItemContainer<? extends IConfigListItem> c) {		clist.addContainer(c);	}

	@Override
	public void informContainers() {		clist.informContainers(this);	}

	@Override
	public void removeContainer(BasicItemContainer<? extends IConfigListItem> c) {		clist.removeContainer(c);	} 
		
}


