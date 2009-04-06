package org.realtors.rets.server.dmql;

import org.apache.commons.lang.StringUtils;

import junit.framework.TestCase;

public class DateSqlConverterTest extends TestCase
{
    public void testToSql()
    {
        DateSqlConverter date = new DateSqlConverter("2004-01-08");
        assertEquals("'2004-01-08'", TestUtil.toSql(date));
    }

    public void testToSqlWithCurrentTime()
    {
        // This should give the current date.  Since we can't test directly,
        // this is just looking to make sure no exceptions get thrown.
        DateSqlConverter date = new DateSqlConverter();
        assertTrue(StringUtils.isNotBlank(TestUtil.toSql(date)));
    }

    public void testEquals()
    {
        DateSqlConverter date1 = new DateSqlConverter("2004-01-08");
        DateSqlConverter date2 = new DateSqlConverter("2004-01-08");
        assertEquals(date1, date2);
    }
}
