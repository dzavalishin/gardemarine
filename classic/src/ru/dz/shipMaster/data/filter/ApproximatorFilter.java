package ru.dz.shipMaster.data.filter;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import ru.dz.shipMaster.ui.config.filter.CieApproximatorFilter;

public class ApproximatorFilter extends GeneralFilterDataSource {

	//Map<Double,Double> curve = new TreeMap<Double, Double>();
	Set <ApproximationPoint> points = new TreeSet<ApproximationPoint>();
	
	@Override
	public void performMeasure() {
		translateToMeters(map(currInVal), null);
	}
	
	// This is public just for unit tests
	public double map(double in)
	{
		// Mapping is incomplete, pass through
		if( points.size() < 2 )
			return in;

		//Iterator<Double> i = curve.keySet().iterator();
		Iterator<ApproximationPoint> i = points.iterator();
			
		double prev = 0;
		double prevOut = 0;
		
		while(i.hasNext())
		{
			ApproximationPoint next = i.next();
			double point = next.getIn();
			double outPoint = next.getOut();
			
			if(in == point)
				return outPoint;
			
			if(in > point)
			{
				prev = point;
				prevOut = outPoint;
				continue;
			}
			
			// Current in is between prev and point
			
			double pos = (in - prev) / (point-prev);
			
			return ((outPoint-prevOut)*pos) + prevOut;			
		}

		// Out of mapping
		return Double.MAX_VALUE;
	}

	CieApproximatorFilter dialog;
	@Override
	public void displayPropertiesDialog() {
		if(dialog == null) dialog = new CieApproximatorFilter(this);
		dialog.displayPropertiesDialog(); 
	}

	@Override
	public void destroy() {
		dialog = null;
		points = null;
		super.destroy();
	}
	
	
	public Set<ApproximationPoint> getPoints() {		return points;	}
	public void setPoints(Set<ApproximationPoint> points) {		this.points = points;	}

	
	
	
	
	

	
}
