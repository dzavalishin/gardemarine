package ru.dz.shipMaster.data.filter;

import ru.dz.shipMaster.config.ContainerInformer;

public class ApproximationPoint extends ContainerInformer implements Comparable<ApproximationPoint> {
	private double in, out;
	
	public ApproximationPoint() {
		in = 0;
		out = 0;
	}
	
	public ApproximationPoint(double in, double out) {
		this.in = in;
		this.out = out;
	}

	@Override
	public void destroy() {
		// Empty
	}

	@Override
	public void displayPropertiesDialog() {
	}

	@Override
	public String getName() {
		return "Approximation point ("+in+" -> "+out+")";
	}

	@Override
	public String toString() {
		return "Approximation point ("+in+" -> "+out+")";
	}

	public double getIn() {		return in;	}
	public void setIn(double in) {		this.in = in;	}

	public double getOut() {		return out;	}
	public void setOut(double out) {		this.out = out;	}

	@Override
	public int compareTo(ApproximationPoint o) {
		if( in < o.in ) return -1;
		if( in > o.in ) return 1;
		return 0;
	}

	@Override
	public int hashCode() {
		return ((Double)in).hashCode();
	}

	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ApproximationPoint) {
			ApproximationPoint ap = (ApproximationPoint) obj;
			return ap.in == in;
		}
		return false;
	}
	
}
