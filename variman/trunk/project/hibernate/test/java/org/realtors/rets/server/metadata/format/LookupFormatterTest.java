/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.realtors.rets.server.metadata.Lookup;

public class LookupFormatterTest extends FormatterTestCase
{
    protected void setUp()
    {
        Lookup lookup = new Lookup();
        lookup.setLookupName("E_SCHOOL");
        lookup.setVisibleName("Elementary School District");
        mLookups = new Lookup[] {lookup};
    }

    private LookupFormatter getFormatter(int format)
    {
        LookupFormatter formatter = LookupFormatter.getInstance(format);
        formatter.setVersion("1.00.001", getDate());
        formatter.setResourceName("Property");
        return formatter;
    }

    public void testCompactFormatLookup()
    {
        LookupFormatter formatter = getFormatter(MetadataFormatter.COMPACT);
        StringWriter formatted = new StringWriter();
        formatter.format(new PrintWriter(formatted), mLookups);
        assertEquals(
            "<METADATA-LOOKUP Resource=\"Property\" Version=\"" + VERSION +
            "\" Date=\"" + DATE + "\">\n" +

            "<COLUMNS>\tLookupName\tVisibleName\tVersion\tDate\t</COLUMNS>\n" +

            "<DATA>\tE_SCHOOL\tElementary School District" + VERSION_DATE +
            "\t</DATA>\n" +

            "</METADATA-LOOKUP>\n",

            formatted.toString());
    }

    public void testEmptyCompactFormatLookup()
    {
        LookupFormatter formatter = getFormatter(MetadataFormatter.COMPACT);
        StringWriter formatted = new StringWriter();
        formatter.format(new PrintWriter(formatted), new Lookup[0]);
        assertEquals("", formatted.toString());
    }

    private Lookup[] mLookups;
}
