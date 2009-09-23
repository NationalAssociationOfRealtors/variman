package org.realtors.rets.server.dmql;

import org.apache.commons.lang.StringUtils;

import junit.framework.TestCase;

public class DateTimeSqlConverterTest extends TestCase
{
    public void testToSql()
    {
        DateTimeSqlConverter dateTime =
            new DateTimeSqlConverter("1984-01-24T13:57:02.468");
        assertEquals("'1984-01-24 13:57:02'", TestUtil.toSql(dateTime));
        dateTime = new DateTimeSqlConverter("1984-01-24T13:57:02");
        assertEquals("'1984-01-24 13:57:02'", TestUtil.toSql(dateTime));
    }

    public void testToSqlWithCurrentTime()
    {
        DateTimeSqlConverter dateTime = new DateTimeSqlConverter();
        assertTrue(StringUtils.isNotBlank(TestUtil.toSql(dateTime)));
    }

    public void testEquals()
    {
        DateTimeSqlConverter dateTime1 =
            new DateTimeSqlConverter("1984-01-24T13:57:02.468");
        DateTimeSqlConverter dateTime2 =
            new DateTimeSqlConverter("1984-01-24T13:57:02.468");
        DateTimeSqlConverter dateTime3 = new DateTimeSqlConverter();
        DateTimeSqlConverter dateTime4 = new DateTimeSqlConverter();

        assertEquals(dateTime1, dateTime2);
        assertEquals(dateTime3, dateTime4);
        assertFalse(dateTime1.equals(dateTime3));
    }
}
