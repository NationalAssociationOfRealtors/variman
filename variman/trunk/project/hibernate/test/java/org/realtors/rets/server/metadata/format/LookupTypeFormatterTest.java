/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.realtors.rets.server.metadata.LookupType;

public class LookupTypeFormatterTest extends FormatterTestCase
{
    protected void setUp()
    {
        mLookupTypes = new ArrayList();
        LookupType lookupType = new LookupType();
        lookupType.setLongValue("Aurora 306");
        lookupType.setShortValue("306");
        lookupType.setValue("306");
        mLookupTypes.add(lookupType);
    }

    private LookupTypeFormatter getFormatter(int format)
    {
        LookupTypeFormatter formatter = LookupTypeFormatter.getInstance(format);
        formatter.setVersion("1.00.001", getDate());
        formatter.setLevels(new String[] {"Property", "E_SCHOOL"});
        return formatter;
    }

    public void testCompactFormatLookupType()
    {
        LookupTypeFormatter formatter = getFormatter(MetadataFormatter.COMPACT);
        StringWriter formatted = new StringWriter();
        formatter.format(new PrintWriter(formatted),  mLookupTypes);
        assertEquals(
            "<METADATA-LOOKUP_TYPE Resource=\"Property\" Lookup=\"E_SCHOOL\" " +
            "Version=\"" + VERSION + "\" Date=\"" + DATE + "\">\n" +

            "<COLUMNS>\tLongValue\tShortValue\tValue\t</COLUMNS>\n" +

            "<DATA>\tAurora 306\t306\t306\t</DATA>\n" +

            "</METADATA-LOOKUP_TYPE>\n",
            formatted.toString());
    }

    public void testEmptyCompactFormat()
    {
        LookupTypeFormatter formatter = getFormatter(MetadataFormatter.COMPACT);
        StringWriter formatted = new StringWriter();
        formatter.format(new PrintWriter(formatted), new ArrayList());
        assertEquals("", formatted.toString());
    }

    protected List mLookupTypes;
}
