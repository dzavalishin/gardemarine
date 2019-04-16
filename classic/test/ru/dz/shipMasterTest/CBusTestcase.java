package ru.dz.shipMasterTest;

import junit.framework.TestCase;
import ru.dz.shipMaster.dev.controller.CBusProtocol;


public class CBusTestcase extends TestCase {

	public void testCBusChecksum() {
		byte [] test = {0x05, 0x38, 0x79, (byte)0x88 };
		
		assertEquals((byte)0xC2, CBusProtocol.calculateChecksum(test,test.length));
	}
	
}
