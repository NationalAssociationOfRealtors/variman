/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.realtors.rets.server.metadata.Lookup;

public class LookupFormatterTest extends FormatterTestCase
{
    protected void setUp()
    {
        mLookups = new ArrayList();
        Lookup lookup = new Lookup();
        lookup.setLookupName("E_SCHOOL");
        lookup.setVisibleName("Elementary School District");
        mLookups.add(lookup);
    }

    private LookupFormatter getCompactFormatter()
    {
        LookupFormatter formatter = new CompactLookupFormatter();
        formatter.setVersion("1.00.001", getDate());
        formatter.setLevels(new String[] {"Property"});
        return formatter;
    }

    public void testCompactFormatLookup()
    {
        LookupFormatter formatter = getCompactFormatter();
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
        LookupFormatter formatter = getCompactFormatter();
        StringWriter formatted = new StringWriter();
        formatter.format(new PrintWriter(formatted), new ArrayList());
        assertEquals("", formatted.toString());
    }

    private List mLookups;
}
