/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import org.realtors.rets.server.LinesEqualTestCase;

public abstract class FormatterTestCase extends LinesEqualTestCase
{
    protected Date getDate()
    {
        return DATE_OBJECT;
    }

    protected String format(MetadataFormatter formatter, Collection data,
                          String[] levels, boolean recursive)
    {
        TestMetadataSegmentFormatter dispatcher =
            new TestMetadataSegmentFormatter();
        StringWriter formatted = new StringWriter();
        PrintWriter writer = new PrintWriter(formatted);
        FormatterContext context =
            new FormatterContext("1.00.001", DATE_OBJECT, recursive, writer,
                                 dispatcher);
        formatter.format(context, data, levels);
        return formatted.toString();
    }

    protected abstract List getData();

    protected abstract String[] getLevels();

    protected abstract MetadataFormatter getCompactFormatter();

    protected abstract String getExpectedCompact();

    protected abstract String getExpectedCompactRecursive();

    public void testCompactFormat()
    {
        String formatted = format(getCompactFormatter(), getData(),
                                  getLevels(), FormatterContext.NOT_RECURSIVE);
        assertLinesEqual(getExpectedCompact(), formatted);
    }

    public void testCompactFormatRecursive()
    {
        String formatted = format(getCompactFormatter(), getData(),
                                  getLevels(), FormatterContext.RECURSIVE);
        assertLinesEqual(getExpectedCompactRecursive(), formatted);
    }

    public void testEmptyCompactFormat()
    {
        String formatted = format(getCompactFormatter(), EMPTY_LIST,
                                  getLevels(), FormatterContext.NOT_RECURSIVE);
        assertLinesEqual("", formatted);
    }

    protected MetadataFormatter getStandardFormatter()
    {
        return new NullMetadataFormatter();
    }

    protected String getExpectedStandard()
    {
        return "";
    }

    protected String getExpectedStandardRecursive()
    {
        return "";
    }

    public void testStandardFormat()
    {
        String formatted = format(getStandardFormatter(), getData(),
                                  getLevels(), FormatterContext.NOT_RECURSIVE);
        assertLinesEqual(getExpectedStandard(), formatted);
    }

    public void testStandardFormatRecursive()
    {
        String formatted = format(getStandardFormatter(), getData(),
                                  getLevels(), FormatterContext.RECURSIVE);
        assertLinesEqual(getExpectedStandardRecursive(), formatted);
    }

    protected static final String VERSION = "1.00.001";
    protected static final String DATE = "Wed, 01 Jan 2003 00:01:00 GMT";
    protected static final String VERSION_DATE = "\t" + VERSION +  "\t" + DATE;
    protected static final String EOL = "\n";
    protected static final Date DATE_OBJECT;
    protected static final List EMPTY_LIST = Collections.EMPTY_LIST;

    static
    {
        TimeZone gmt = TimeZone.getTimeZone("GMT");
        GregorianCalendar calendar = new GregorianCalendar(gmt);
        calendar.set(2003, 0, 01, 0, 1, 0);
        DATE_OBJECT = calendar.getTime();
    }
}
