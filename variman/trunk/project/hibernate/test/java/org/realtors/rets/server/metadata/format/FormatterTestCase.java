/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.Date;
import java.util.TimeZone;
import java.util.GregorianCalendar;

import junit.framework.TestCase;

public abstract class FormatterTestCase extends TestCase
{
    protected static final String VERSION = "1.00.001";
    protected static final String DATE = "Wed, 01 Jan 2003 00:01:00 GMT";
    protected static final String VERSION_DATE = "\t" + VERSION +  "\t" + DATE;

    protected Date getDate()
    {
        TimeZone gmt = TimeZone.getTimeZone("GMT");
        GregorianCalendar calendar = new GregorianCalendar(gmt);
        calendar.set(2003, 0, 01, 0, 1, 0);
        return calendar.getTime();
    }
}
