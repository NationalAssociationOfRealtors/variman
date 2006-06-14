package org.realtors.rets.server;

import org.apache.commons.lang.time.DateUtils;

import junit.framework.TestCase;

public class QueryCountTest extends TestCase
{
    public static final long TEN_SEC = 30 * DateUtils.MILLIS_PER_SECOND;

    public void testNoLimit()
    {
        QueryCount count = new QueryCount();
        count.setNoQueryLimit();

        assertTrue(count.increment());
        assertTrue(count.increment());
        assertTrue(count.increment());
        assertTrue(count.increment());
        assertTrue(count.increment());
        assertTrue(count.increment());
    }

    public void testPerMinuteLimit()
    {
        QueryCount count =
            new QueryCount(3, QueryCount.PER_MINUTE);

        // Cause counter to fail due to reaching limit
        count.setLastResetTime(System.currentTimeMillis() -
                                    DateUtils.MILLIS_PER_MINUTE + TEN_SEC);
        assertEquals(0, count.getCurrentCount());
        assertTrue(count.increment());
        assertTrue(count.increment());
        assertTrue(count.increment());
        assertFalse(count.increment());
        assertFalse(count.increment());
        assertEquals(3, count.getCurrentCount());

        // Resetting time to more than 1 period ago causes count to reset
        // on next increment
        count.setLastResetTime(System.currentTimeMillis() -
                                    DateUtils.MILLIS_PER_MINUTE - TEN_SEC);
        assertEquals(3, count.getCurrentCount());
        assertTrue(count.increment());
        assertTrue(count.increment());
        assertTrue(count.increment());
        assertFalse(count.increment());
        assertFalse(count.increment());
        assertEquals(3, count.getCurrentCount());

        // Moving time past limit period should not cause a reset, again
        count.setLastResetTime(System.currentTimeMillis() -
                                    DateUtils.MILLIS_PER_MINUTE + TEN_SEC);
        assertFalse(count.increment());
        assertEquals(3, count.getCurrentCount());
    }

    public void testPerHourLimit()
    {
        QueryCount count =
            new QueryCount(3, QueryCount.PER_HOUR);

        // Cause counter to fail due to reaching limit
        count.setLastResetTime(System.currentTimeMillis() -
                                    DateUtils.MILLIS_PER_HOUR + TEN_SEC);
        assertEquals(0, count.getCurrentCount());
        assertTrue(count.increment());
        assertTrue(count.increment());
        assertTrue(count.increment());
        assertFalse(count.increment());
        assertFalse(count.increment());
        assertEquals(3, count.getCurrentCount());

        // Resetting time to more than 1 period ago causes count to reset
        // on next increment
        count.setLastResetTime(System.currentTimeMillis() -
                                    DateUtils.MILLIS_PER_HOUR - TEN_SEC);
        assertEquals(3, count.getCurrentCount());
        assertTrue(count.increment());
        assertTrue(count.increment());
        assertTrue(count.increment());
        assertFalse(count.increment());
        assertFalse(count.increment());
        assertEquals(3, count.getCurrentCount());

        // Moving time past limit period should not cause a reset, again
        count.setLastResetTime(System.currentTimeMillis() -
                                    DateUtils.MILLIS_PER_HOUR + TEN_SEC);
        assertFalse(count.increment());
        assertEquals(3, count.getCurrentCount());
    }

    public void testPerDayLimit()
    {
        QueryCount count =
            new QueryCount(3, QueryCount.PER_DAY);

        // Cause counter to fail due to reaching limit
        count.setLastResetTime(System.currentTimeMillis() -
                                    DateUtils.MILLIS_PER_DAY + TEN_SEC);
        assertEquals(0, count.getCurrentCount());
        assertTrue(count.increment());
        assertTrue(count.increment());
        assertTrue(count.increment());
        assertFalse(count.increment());
        assertFalse(count.increment());
        assertEquals(3, count.getCurrentCount());

        // Resetting time to more than 1 period ago causes count to reset
        // on next increment
        count.setLastResetTime(System.currentTimeMillis() -
                                    DateUtils.MILLIS_PER_DAY - TEN_SEC);
        assertEquals(3, count.getCurrentCount());
        assertTrue(count.increment());
        assertTrue(count.increment());
        assertTrue(count.increment());
        assertFalse(count.increment());
        assertFalse(count.increment());
        assertEquals(3, count.getCurrentCount());

        // Moving time past limit period should not cause a reset, again
        count.setLastResetTime(System.currentTimeMillis() -
                                    DateUtils.MILLIS_PER_DAY + TEN_SEC);
        assertFalse(count.increment());
        assertEquals(3, count.getCurrentCount());
    }

    public void testIsMoreRestrictiveThan()
    {
        QueryCount noLimit = new QueryCount();
        QueryCount twoPerDay = new QueryCount(2, QueryCount.PER_DAY);
        QueryCount onePerDay = new QueryCount(1, QueryCount.PER_DAY);
        QueryCount twoPerHour = new QueryCount(2, QueryCount.PER_HOUR);
        QueryCount onePerHour = new QueryCount(1, QueryCount.PER_HOUR);
        QueryCount twoPerMinute = new QueryCount(2, QueryCount.PER_MINUTE);
        QueryCount onePerMinute = new QueryCount(1, QueryCount.PER_MINUTE);

        assertFalse(noLimit.isMoreRestrictiveThan(noLimit));
        assertFalse(noLimit.isMoreRestrictiveThan(onePerDay));
        assertFalse(noLimit.isMoreRestrictiveThan(twoPerDay));
        assertFalse(noLimit.isMoreRestrictiveThan(onePerHour));
        assertFalse(noLimit.isMoreRestrictiveThan(twoPerHour));
        assertFalse(noLimit.isMoreRestrictiveThan(onePerMinute));
        assertFalse(noLimit.isMoreRestrictiveThan(twoPerMinute));

        assertTrue(twoPerDay.isMoreRestrictiveThan(noLimit));
        assertFalse(twoPerDay.isMoreRestrictiveThan(twoPerDay));
        assertFalse(twoPerDay.isMoreRestrictiveThan(onePerDay));
        assertFalse(twoPerDay.isMoreRestrictiveThan(twoPerHour));
        assertFalse(twoPerDay.isMoreRestrictiveThan(onePerHour));
        assertFalse(twoPerDay.isMoreRestrictiveThan(twoPerMinute));
        assertFalse(twoPerDay.isMoreRestrictiveThan(onePerMinute));

        assertTrue(onePerDay.isMoreRestrictiveThan(noLimit));
        assertTrue(onePerDay.isMoreRestrictiveThan(twoPerDay));
        assertFalse(onePerDay.isMoreRestrictiveThan(onePerDay));
        assertFalse(onePerDay.isMoreRestrictiveThan(twoPerHour));
        assertFalse(onePerDay.isMoreRestrictiveThan(onePerHour));
        assertFalse(onePerDay.isMoreRestrictiveThan(twoPerMinute));
        assertFalse(onePerDay.isMoreRestrictiveThan(onePerMinute));

        assertTrue(twoPerHour.isMoreRestrictiveThan(noLimit));
        assertTrue(twoPerHour.isMoreRestrictiveThan(twoPerDay));
        assertTrue(twoPerHour.isMoreRestrictiveThan(onePerDay));
        assertFalse(twoPerHour.isMoreRestrictiveThan(twoPerHour));
        assertFalse(twoPerHour.isMoreRestrictiveThan(onePerHour));
        assertFalse(twoPerHour.isMoreRestrictiveThan(twoPerMinute));
        assertFalse(twoPerHour.isMoreRestrictiveThan(onePerMinute));

        assertTrue(onePerHour.isMoreRestrictiveThan(noLimit));
        assertTrue(onePerHour.isMoreRestrictiveThan(twoPerDay));
        assertTrue(onePerHour.isMoreRestrictiveThan(onePerDay));
        assertTrue(onePerHour.isMoreRestrictiveThan(twoPerHour));
        assertFalse(onePerHour.isMoreRestrictiveThan(onePerHour));
        assertFalse(onePerHour.isMoreRestrictiveThan(twoPerMinute));
        assertFalse(onePerHour.isMoreRestrictiveThan(onePerMinute));

        assertTrue(twoPerMinute.isMoreRestrictiveThan(noLimit));
        assertTrue(twoPerMinute.isMoreRestrictiveThan(twoPerDay));
        assertTrue(twoPerMinute.isMoreRestrictiveThan(onePerDay));
        assertTrue(twoPerMinute.isMoreRestrictiveThan(twoPerHour));
        assertTrue(twoPerMinute.isMoreRestrictiveThan(onePerHour));
        assertFalse(twoPerMinute.isMoreRestrictiveThan(twoPerMinute));
        assertFalse(twoPerMinute.isMoreRestrictiveThan(onePerMinute));

        assertTrue(onePerMinute.isMoreRestrictiveThan(noLimit));
        assertTrue(onePerMinute.isMoreRestrictiveThan(twoPerDay));
        assertTrue(onePerMinute.isMoreRestrictiveThan(onePerDay));
        assertTrue(onePerMinute.isMoreRestrictiveThan(twoPerHour));
        assertTrue(onePerMinute.isMoreRestrictiveThan(onePerHour));
        assertTrue(onePerMinute.isMoreRestrictiveThan(twoPerMinute));
        assertFalse(onePerMinute.isMoreRestrictiveThan(onePerMinute));
    }
}
