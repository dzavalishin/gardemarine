package ru.dz.shipMaster.ui.control;

import java.awt.Image;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

import ru.dz.shipMaster.data.DataGeneration;
import ru.dz.shipMaster.data.GeneralDataSource;
import ru.dz.shipMaster.dev.PortDataSource;

/**
 * To be used by controls, such as buttons.
 * @author dz
 *
 */
public class ControlDataSource extends GeneralDataSource {
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(PortDataSource.class.getName());
	//private double currVal = 0, nextVal = 0;
	//private boolean currUsed = false, nextUsed = false;
	private BlockingQueue<Double> values = new ArrayBlockingQueue<Double>(10);


	public ControlDataSource(double min, double max, String legend, String units) {
		super(min, max, legend, units);
	}

	public synchronized void setValue(double value)
	{
		values.offer(value);
	}

	public void setValue(Image val) {
		translateImageToMeters(val);		
	}


	/** called from timer */
	@Override
	public synchronized void performMeasure() {
		if( values.isEmpty() )
			return;

		if(true)
		{
			Double poll = values.poll();
			while( values.size() > 0 )
			{
				if( poll == null ) break; // can't happen
				Double peek = values.peek();
				if( peek == null ) break; // can't happen

				// Eat off all the duplicates
				if( peek.doubleValue() == poll.doubleValue() )
				{
					poll = values.poll();
					continue;
				}
				break;
			}

			translateToMeters(poll,null);
		}
		/*
		else
		{
			while( values.size() > 1 )
			{
				Double poll = values.poll();
				if( poll == null ) break; // can't happen
				Double peek = values.peek();
				if( peek == null ) break; // can't happen

				// Eat off all the duplicates
				if( peek.doubleValue() == poll.doubleValue() )
					continue;
				break;
			}

			translateToMeters(values.peek(),null);
		}
		*/
	}

	/**
	 * Nobody above us, so we don't need to ask them to prepare data.
	 */
	protected void callSourceDataPumpEntry(DataGeneration currentGeneration)
	{
	}

	@Override
	public String getName() {
		return legend;
	}

}
