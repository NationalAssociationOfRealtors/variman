/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.realtors.rets.server.metadata.MSystem;

public class SystemFormatterTest extends FormatterTestCase
{
    protected void setUp()
    {
        mSystem = new MSystem();
        mSystem.setSystemID("CRT_RETS");
        mSystem.setDescription("Center for REALTOR Technology");
        mSystem.setComments("The reference implementation of a RETS Server");
        mSystem.setVersion(100001);
        mSystem.setDate(getDate());
    }

    public void testCompactFormatSystem()
    {
        SystemFormatter formatter =
            SystemFormatter.getInstance(MetadataFormatter.COMPACT);
        StringWriter formatted = new StringWriter();
        formatter.format(new PrintWriter(formatted), mSystem);
        assertEquals(
            "<METADATA-SYSTEM Version=\"" + VERSION + "\" " +
            "Date=\"" + DATE + "\">\n" +
            "<SYSTEM SystemID=\"CRT_RETS\" " +
            "SystemDescription=\"Center for REALTOR Technology\"/>\n" +
            "<COMMENTS>The reference implementation of a RETS Server" +
            "</COMMENTS>\n" +
            "</METADATA-SYSTEM>\n",
            formatted.toString());
    }

    private MSystem mSystem;
}
