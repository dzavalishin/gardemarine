package ru.dz.shipMasterTest;

import junit.framework.TestCase;
import ru.dz.shipMaster.data.units.Unit;
import ru.dz.shipMaster.data.units.UnitGroup;
import ru.dz.shipMaster.misc.UnitConversionException;

public class UnitTestCase extends TestCase {

	public void testConvertToReference() {
		Unit unit = new Unit();
		
		unit.setFirstShift(1);
		unit.setFirstMultiplicator(10);
		unit.setSecondMultiplicator(0.1);
		unit.setSecondShift(-1);
		
		assertEquals(0.0, unit.convertToReference(0), 0.001);
		
		//fail("Not yet implemented"); 
	}

	public void testConvertFromReference() {
		Unit unit = new Unit();
		
		unit.setFirstShift(1);
		unit.setFirstMultiplicator(10);
		unit.setSecondMultiplicator(0.1);
		unit.setSecondShift(-1);
		
		assertEquals(0.0, unit.convertFromReference(0), 0.001);
	}

	public void testConvertReversability() {

		Unit unit = new Unit();

		assertEquals("uninited from to 1", 1.0, unit.convertToReference(unit.convertFromReference(1)), 0.001);
		assertEquals("uninited from to 0", 0.0, unit.convertToReference(unit.convertFromReference(0)), 0.001);
		
		assertEquals("uninited to from 1", 1.0, unit.convertFromReference(unit.convertToReference(1)), 0.001);
		assertEquals("uninited to from 0", 0.0, unit.convertFromReference(unit.convertToReference(0)), 0.001);
		
		unit.setFirstShift(5);
		unit.setFirstMultiplicator(0.22);
		unit.setSecondMultiplicator(10.1);
		unit.setSecondShift(-13);

		assertEquals("inited from to 1", 1.0, unit.convertToReference(unit.convertFromReference(1)), 0.001);
		assertEquals("inited from to 0", 0.0, unit.convertToReference(unit.convertFromReference(0)), 0.001);
		
		assertEquals("inited to from 1", 1.0, unit.convertFromReference(unit.convertToReference(1)), 0.001);
		assertEquals("inited to from 0", 0.0, unit.convertFromReference(unit.convertToReference(0)), 0.001);
		
		unit.setOneDividedBy(true);
		
		assertEquals("1/x from to 1", 1.0, unit.convertToReference(unit.convertFromReference(1)), 0.001);
		assertEquals("1/x from to 0", 0.0, unit.convertToReference(unit.convertFromReference(0)), 0.001);
		
		assertEquals("1/x to from 1", 1.0, unit.convertFromReference(unit.convertToReference(1)), 0.001);
		assertEquals("1/x to from 0", 0.0, unit.convertFromReference(unit.convertToReference(0)), 0.001);
		
		
	}
	
	
	public void testConvertFromTo() {
		Unit centimeter = new Unit();
		Unit inch = new Unit();
		
		centimeter.setFirstMultiplicator(0.01);
		inch.setFirstMultiplicator(0.01*2.54);
		
		assertEquals("cm to meter", 1.0, centimeter.convertToReference(100), 0.001 );
		assertEquals("inch to meter", 2.54, inch.convertToReference(100.0), 0.001 );
		
		assertEquals("meter to inch", 100, inch.convertFromReference(2.54), 0.001 );
		
		try {
			Unit.convertFromTo(inch, centimeter, 1);
			fail("No exception for units from different groups");
		} catch (UnitConversionException e) {
		}
		
		UnitGroup group = new UnitGroup();
		inch.setGroup(group);
		centimeter.setGroup(group);
		
		try {
			assertEquals(2.54, Unit.convertFromTo(inch, centimeter, 1), 0.001);
		} catch (UnitConversionException e) {
			fail("Exception for units from same group");
		}
		
	}

}
