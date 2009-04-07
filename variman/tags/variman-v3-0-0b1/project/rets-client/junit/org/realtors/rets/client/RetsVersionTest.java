package org.realtors.rets.client;

public class RetsVersionTest extends RetsTestCase {
	public void testEquals() {
		assertEquals("Checking 1.0", RetsVersion.RETS_1_0, new RetsVersion(1, 0));

		assertEquals("Checking 1.5", RetsVersion.RETS_1_5, new RetsVersion(1, 5));

		assertFalse("Checking draft support", RetsVersion.RETS_1_5.equals(new RetsVersion(1, 5, 1)));
		
		assertEquals ("Checking 1.7", RetsVersion.RETS_1_7, new RetsVersion(1, 7));
		
		assertEquals("Checking 1.7.2", RetsVersion.RETS_1_7_2, new RetsVersion(1, 7, 2));
		
		assertTrue("Checking equals operator", RetsVersion.RETS_1_7_2.equals(new RetsVersion(1, 7, 2)));
	}

	public void testToString() {
		assertEquals("Checking toString() 1.0", "RETS/1.0", RetsVersion.RETS_1_0.toString());
		assertEquals("Checking toString() 1.5", "RETS/1.5", RetsVersion.RETS_1_5.toString());
		assertEquals("Checking toString() draft", "RETS/1.5.1", new RetsVersion(1, 5, 1).toString());
		assertEquals("Checking toString() 1.7.2", "RETS/1.7.2", RetsVersion.RETS_1_7_2.toString());
	}
}
