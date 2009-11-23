package org.realtors.rets.server.dmql;

import org.apache.commons.lang.StringUtils;

import junit.framework.TestCase;

public class TimeSqlConverterTest extends TestCase
{
    public void testToSql()
    {
        TimeSqlConverter time = new TimeSqlConverter("13:12:34.567");
        assertEquals("'13:12:34'", TestUtil.toSql(time));
        time = new TimeSqlConverter("12:34:56");
        assertEquals("'12:34:56'", TestUtil.toSql(time));
    }

    public void testToSqlWithCurrentTime()
    {
        // Since we cannot verify the current time, just make sure this does
        // throw any exceptions and produces some results.
        TimeSqlConverter time = new TimeSqlConverter();
        assertTrue(StringUtils.isNotBlank(TestUtil.toSql(time)));
    }

    public void testEquals()
    {
        TimeSqlConverter time1 = new TimeSqlConverter("12:00:00");
        TimeSqlConverter time2 = new TimeSqlConverter("12:00:00");
        TimeSqlConverter time3 = new TimeSqlConverter();
        TimeSqlConverter time4 = new TimeSqlConverter();

        assertEquals(time1, time2);
        assertEquals(time3, time4);
        assertFalse(time1.equals(time3));
    }
}
