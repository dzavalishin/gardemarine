package ru.dz.shipMasterTest;

import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;
import ru.dz.shipMaster.data.filter.ApproximationPoint;
import ru.dz.shipMaster.data.filter.ApproximatorFilter;

public class ApproximatorFilterTestcase extends TestCase {

	
	private static final double EPSILON = 0.0001;

	public void testIdentical() {
		ApproximatorFilter af = new ApproximatorFilter();

		Set<ApproximationPoint> points = new TreeSet<ApproximationPoint>();
		
		points.add(new ApproximationPoint(0.0,0.0));
		points.add(new ApproximationPoint(1.0,1.0));
		points.add(new ApproximationPoint(5.0,5.0));
		points.add(new ApproximationPoint(12.0,12.0));
		
		af.setPoints( points );

		assertNear( af, 0.0, 0.0 );
		assertNear( af, 0.5, 0.5 );
		assertNear( af, 1.1, 1.1 );
		assertNear( af, 2.8, 2.8 );
		assertNear( af, 5.025, 5.025 );
		assertNear( af, 8.0, 8.0 );
		assertNear( af, 11.99, 11.99 );
	}

	
	public void test2x() {
		ApproximatorFilter af = new ApproximatorFilter();

		Set<ApproximationPoint> points = new TreeSet<ApproximationPoint>();
		
		points.add(new ApproximationPoint(0.0,0.0));
		points.add(new ApproximationPoint(1.0,2.0));
		points.add(new ApproximationPoint(5.0,10.0));
		points.add(new ApproximationPoint(12.0,24.0));
		
		af.setPoints( points );

		assertNear( af, 0.0, 0.0 );
		assertNear( af, 0.5, 1.0 );
		assertNear( af, 1.1, 2.2 );
		assertNear( af, 2.8, 2.8*2 );
		assertNear( af, 5.025, 10.05 );
		assertNear( af, 8.0, 16.0 );
		assertNear( af, 11.99, 11.99*2 );
	}

	
	public void testMixed() {
		ApproximatorFilter af = new ApproximatorFilter();

		Set<ApproximationPoint> points = new TreeSet<ApproximationPoint>();
		
		points.add(new ApproximationPoint(0.0,0.0));
		points.add(new ApproximationPoint(1.0,1.0));
		points.add(new ApproximationPoint(5.0,5.0));
		points.add(new ApproximationPoint(12.0,24.0));
		
		af.setPoints( points );

		assertNear( af, 0.0, 0.0 );
		assertNear( af, 0.5, 0.5 );
		assertNear( af, 1.1, 1.1 );
		assertNear( af, 2.8, 2.8 );
		assertNear( af, 4.5, 4.5 );
		assertNear( af, 5.0, 5.0 );
		assertNear( af, 5.025, 5.067857 );
		assertNear( af, 8.0, 13.142857 );
		assertNear( af, 11.99, 23.972857 );
	}
	
	
	
	
	private void assertNear(ApproximatorFilter af, double i, double o) {
		double out = af.map(i);
		boolean condition = Math.abs( out - o ) < EPSILON;
		if(!condition)
			System.out.println(String.format("in %f out %f expected %f", i, out, o));
		assertTrue( condition );
	}
	
}
