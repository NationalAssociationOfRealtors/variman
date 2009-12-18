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
import java.util.Set;
import java.util.TimeZone;

import org.realtors.rets.client.RetsVersion;
import org.realtors.rets.server.LinesEqualTestCase;
import org.realtors.rets.server.protocol.TableGroupFilter;

public abstract class FormatterTestCase extends LinesEqualTestCase
{
    protected Date getDate()
    {
        return DATE_OBJECT;
    }

    protected String format(MetadataFormatter formatter, Collection data,
                            String[] levels, boolean recursive)
    {
        TableGroupFilter groupFilter = getGroupFilter();
        Set groups = getGroups();
        return format(formatter, data, levels, recursive, groupFilter, groups);
    }

    protected String format(MetadataFormatter formatter, Collection data,
                            String[] levels, boolean recursive,
                            TableGroupFilter groupFilter, Set groups)
    {
        FormatterLookup lookup = new TestFormatterLookup();
        StringWriter formatted = new StringWriter();
        PrintWriter writer = new PrintWriter(formatted);
        MutableFormatterContext context =
            new MutableFormatterContext("1.00.00001", DATE_OBJECT, recursive,
                                        writer, lookup, RetsVersion.RETS_1_7_2);
        context.setRetsVersion(RetsVersion.RETS_1_7_2);
        context.setTableFilter(groupFilter, groups);
        formatter.format(context, data, levels);
        return formatted.toString();
    }

    protected abstract List getData();

    protected Set getGroups()
    {
        return Collections.EMPTY_SET;
    }

    protected TableGroupFilter getGroupFilter()
    {
        return null;
    }

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

    protected abstract MetadataFormatter getStandardFormatter();

    protected abstract String getExpectedStandard();

    protected abstract String getExpectedStandardRecursive();

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

    protected static final String VERSION = "1.00.00001";
    protected static final String DATE = "2003-01-01T00:01:00Z";
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
