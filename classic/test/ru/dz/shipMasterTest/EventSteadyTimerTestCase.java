package ru.dz.shipMasterTest;

import junit.framework.TestCase;
import ru.dz.shipMaster.dev.EventSteadyTimer;

public class EventSteadyTimerTestCase extends TestCase {

	public void testSetSteadyTime() {
		EventSteadyTimer t = new EventSteadyTimer(500);
		assertEquals(500, t.getSteadyTime());
		t.setSteadyTime(1000);
		assertEquals(1000, t.getSteadyTime());
	}

	public void testCheckEvent() {
		EventSteadyTimer t = new EventSteadyTimer(500);
		assertFalse(t.checkEvent(false));
		assertFalse(t.checkEvent(true));
		synchronized (t) {
			try {
				t.wait(510);
			} catch (InterruptedException e) {
				fail("Exception"+ e);
			}
		}
		assertTrue(t.checkEvent(true));
		assertFalse(t.checkEvent(false));

		t = new EventSteadyTimer(500);
		assertFalse(t.checkEvent(false));
		assertFalse(t.checkEvent(true));
		synchronized (t) {
			try {
				t.wait(510);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		assertFalse(t.checkEvent(false));
		assertFalse(t.checkEvent(true));

	}

}
