/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.ArrayList;

import org.realtors.rets.server.metadata.MSystem;

public class SystemFormatterTest extends FormatterTestCase
{
    protected void setUp()
    {
        mSystems = new ArrayList();
        MSystem system = new MSystem();
        system.setSystemID("CRT_RETS");
        system.setDescription("Center for REALTOR Technology");
        system.setComments("The reference implementation of a RETS Server");
        system.setVersion(100001);
        system.setDate(getDate());
        mSystems.add(system);
    }

    public void testCompactFormatSystem()
    {
        SystemFormatter formatter =
            SystemFormatter.getInstance(MetadataFormatter.COMPACT);
        StringWriter formatted = new StringWriter();
        formatter.format(new PrintWriter(formatted), mSystems);
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

    private List mSystems;
}
