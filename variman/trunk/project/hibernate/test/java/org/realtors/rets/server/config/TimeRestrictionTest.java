package org.realtors.rets.server.config;

import java.util.Calendar;

import junit.framework.TestCase;

public class TimeRestrictionTest extends TestCase
{
    public void testAllowPolicy()
    {
        TimeRestriction restriction = new TimeRestriction(TimeRestriction.ALLOW,
                                                         9, 0, 17, 30);
        assertFalse(restriction.isAllowed(0, 0));
        assertFalse(restriction.isAllowed(8, 59));
        assertTrue(restriction.isAllowed(9, 0));
        assertTrue(restriction.isAllowed(12, 0));
        assertTrue(restriction.isAllowed(17, 30));
        assertFalse(restriction.isAllowed(17, 31));
        assertFalse(restriction.isAllowed(23, 59));
    }

    public void testDenyPolicy()
    {
        TimeRestriction restriction = new TimeRestriction(TimeRestriction.DENY,
                                                         9, 0, 17, 30);
        assertTrue(restriction.isAllowed(0, 0));
        assertTrue(restriction.isAllowed(8, 59));
        assertFalse(restriction.isAllowed(9, 0));
        assertFalse(restriction.isAllowed(12, 0));
        assertFalse(restriction.isAllowed(17, 30));
        assertTrue(restriction.isAllowed(17, 31));
        assertTrue(restriction.isAllowed(23, 59));
    }

    public void testCalendarGetters()
    {
        TimeRestriction restriction = new TimeRestriction(TimeRestriction.ALLOW,
                                                         9, 0, 17, 30);

        Calendar start = restriction.getStartAsCalendar();
        // Clone to make sure second and millisecond stay the same
        Calendar expected = (Calendar) start.clone();
        expected.set(Calendar.HOUR, 9);
        expected.set(Calendar.MINUTE, 0);
        expected.set(Calendar.AM_PM, Calendar.AM);
        assertEquals(expected, start);

        Calendar end = restriction.getEndAsCalendar();
        expected = (Calendar) end.clone();
        expected.set(Calendar.HOUR, 5);
        expected.set(Calendar.MINUTE, 30);
        expected.set(Calendar.AM_PM, Calendar.PM);
        assertEquals(expected, end);
    }

    public void testEquals()
    {
        TimeRestriction a;
        TimeRestriction b;

        a = new TimeRestriction(TimeRestriction.ALLOW, 9, 0, 17, 0);
        b = new TimeRestriction(TimeRestriction.ALLOW, 9, 0, 17, 0);
        assertTrue(a.equals(b));
        b = new TimeRestriction(TimeRestriction.DENY, 9, 0, 17, 0);
        assertFalse(a.equals(b));
        b = new TimeRestriction(TimeRestriction.ALLOW, 8, 0, 17, 0);
        assertFalse(a.equals(b));
        b = new TimeRestriction(TimeRestriction.ALLOW, 9, 30, 17, 0);
        assertFalse(a.equals(b));
        b = new TimeRestriction(TimeRestriction.ALLOW, 9, 0, 18, 0);
        assertFalse(a.equals(b));
        b = new TimeRestriction(TimeRestriction.ALLOW, 9, 0, 17, 30);
        assertFalse(a.equals(b));
    }
}
