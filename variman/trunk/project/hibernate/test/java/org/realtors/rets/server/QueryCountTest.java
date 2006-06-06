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
}
