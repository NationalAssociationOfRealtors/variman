package org.realtors.rets.server.config;

import java.util.Calendar;

import junit.framework.TestCase;
import org.realtors.rets.server.CalendarUtils;

public class TimeRestrictionTest extends TestCase
{
    private static final int AM = Calendar.AM;
    private static final int PM = Calendar.PM;

    private Calendar time(int hour, int minutes, int amPm)
    {
        return CalendarUtils.createCalendar(hour, minutes, amPm);
    }

    public void testAllowPolicy()
    {
        TimeRestriction restriction = new TimeRestriction(TimeRestriction.ALLOW,
                                                         9, 00, 17, 30);
        assertFalse(restriction.isAllowed(time(12, 00, AM)));
        assertFalse(restriction.isAllowed(time(8, 59, AM)));
        assertTrue(restriction.isAllowed(time(9, 00, AM)));
        assertTrue(restriction.isAllowed(time(12, 00, PM)));
        assertTrue(restriction.isAllowed(time(5, 30, PM)));
        assertFalse(restriction.isAllowed(time(5, 31, PM)));
        assertFalse(restriction.isAllowed(time(11, 59, PM)));
    }

    public void testDenyPolicy()
    {
        TimeRestriction restriction = new TimeRestriction(TimeRestriction.DENY,
                                                         9, 0, 17, 30);
        assertTrue(restriction.isAllowed(time(12, 00, AM)));
        assertTrue(restriction.isAllowed(time(8, 59, AM)));
        assertFalse(restriction.isAllowed(time(9, 00, AM)));
        assertFalse(restriction.isAllowed(time(12, 00, PM)));
        assertFalse(restriction.isAllowed(time(5, 30, PM)));
        assertTrue(restriction.isAllowed(time(5, 31, PM)));
        assertTrue(restriction.isAllowed(time(11, 59, PM)));
    }

    public void testCalendarGetters()
    {
        TimeRestriction restriction = new TimeRestriction(TimeRestriction.ALLOW,
                                                         9, 00, 17, 30);

        Calendar start = restriction.getStartAsCalendar();
        Calendar expected = time(9, 00, AM);
        assertEquals(expected, start);

        Calendar end = restriction.getEndAsCalendar();
        expected = time(5, 30, PM);
        assertEquals(expected, end);
    }

    public void testEquals()
    {
        TimeRestriction a;
        TimeRestriction b;

        a = new TimeRestriction(TimeRestriction.ALLOW, 9, 00, 17, 00);
        b = new TimeRestriction(TimeRestriction.ALLOW, 9, 00, 17, 00);
        assertTrue(a.equals(b));
        b = new TimeRestriction(TimeRestriction.DENY, 9, 00, 17, 00);
        assertFalse(a.equals(b));
        b = new TimeRestriction(TimeRestriction.ALLOW, 8, 00, 17, 00);
        assertFalse(a.equals(b));
        b = new TimeRestriction(TimeRestriction.ALLOW, 9, 30, 17, 00);
        assertFalse(a.equals(b));
        b = new TimeRestriction(TimeRestriction.ALLOW, 9, 00, 18, 00);
        assertFalse(a.equals(b));
        b = new TimeRestriction(TimeRestriction.ALLOW, 9, 00, 17, 30);
        assertFalse(a.equals(b));
    }
}
