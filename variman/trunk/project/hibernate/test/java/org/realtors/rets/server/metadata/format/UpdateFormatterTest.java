/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.ArrayList;

import org.realtors.rets.server.metadata.Update;

public class UpdateFormatterTest extends FormatterTestCase
{
    protected void setUp()
    {
        mUpdates = new ArrayList();
        Update update = new Update();
        update.setUpdateName("Add");
        update.setDescription("Add a new Residential Listing");
        update.setKeyField("key");
        mUpdates.add(update);
    }

    private UpdateFormatter getFormatter(int format)
    {
        UpdateFormatter formatter = UpdateFormatter.getInstance(format);
        formatter.setVersion("1.00.001", getDate());
        formatter.setClassName("RES");
        formatter.setResourceName("Property");
        return formatter;
    }

    public void testCompactFormatUpdate()
    {
        UpdateFormatter formatter = getFormatter(MetadataFormatter.COMPACT);
        StringWriter formatted = new StringWriter();
        formatter.format(new PrintWriter(formatted), mUpdates);
        assertEquals(
            "<METADATA-UPDATE Resource=\"Property\" Class=\"RES\" " +
            "Version=\"" + VERSION + "\" Date=\"" + DATE + "\">\n" +

            "<COLUMNS>\tUpdateName\tDescription\tKeyField\tVersion\tDate\t" +
            "</COLUMNS>\n" +

            "<DATA>\tAdd\tAdd a new Residential Listing\tkey" + VERSION_DATE +
            "\t</DATA>\n" +

            "</METADATA-UPDATE>\n",
            formatted.toString());
    }

    public void testEmptyCompactFormatUpdate()
    {
        UpdateFormatter formatter = getFormatter(MetadataFormatter.COMPACT);
        StringWriter formatted = new StringWriter();
        formatter.format(new PrintWriter(formatted), new Update[0]);
        assertEquals("", formatted.toString());
    }

    private List mUpdates;
}
