/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.ArrayList;
import java.util.List;

import org.realtors.rets.server.metadata.MSystem;
import org.realtors.rets.server.metadata.Resource;

public class SystemFormatterTest extends FormatterTestCase
{
    protected List getData()
    {
        ArrayList systems = new ArrayList();
        MSystem system = new MSystem();
        system.setSystemID("CRT_RETS");
        system.setDescription("Center for REALTOR Technology");
        system.setComments("The reference implementation of a RETS Server");
        system.setTimeZoneOffset("-03:00");
        system.setVersion(10000001);
        system.setDate(getDate());
        system.addResource(new Resource(1));
        systems.add(system);
        return systems;
    }

    protected String[] getLevels()
    {
        return new String[0];
    }

    protected MetadataFormatter getCompactFormatter()
    {
        return new CompactSystemFormatter();
    }

    protected MetadataFormatter getStandardFormatter()
    {
        return new StandardSystemFormatter();
    }

    protected String getExpectedCompact()
    {
        return
            "<METADATA-SYSTEM Version=\"" + VERSION + "\" " +
            "Date=\"" + DATE + "\">\n" +
            "<SYSTEM SystemID=\"CRT_RETS\" " +
            "SystemDescription=\"Center for REALTOR Technology\" " +
            "TimeZoneOffset=\"-03:00\"/>\n" +
            "<COMMENTS>The reference implementation of a RETS Server" +
            "</COMMENTS>\n" +
            "</METADATA-SYSTEM>\n";
    }

    protected String getExpectedCompactRecursive()
    {
        return
            "<METADATA-SYSTEM Version=\"" + VERSION + "\" " +
            "Date=\"" + DATE + "\">\n" +
            "<SYSTEM SystemID=\"CRT_RETS\" " +
            "SystemDescription=\"Center for REALTOR Technology\" " +
            "TimeZoneOffset=\"-03:00\"/>\n" +
            "<COMMENTS>The reference implementation of a RETS Server" +
            "</COMMENTS>\n" +
            "</METADATA-SYSTEM>\n" +
            "Empty list\n" + 		// Place holder for foreign keys
            Resource.TABLE + "\n";
    }

    protected String getExpectedStandard()
    {
        return
            "<METADATA-SYSTEM Version=\"" + VERSION + "\" " +
            "Date=\"" + DATE + "\">" + EOL +
            "<SYSTEM SystemID=\"CRT_RETS\" " +
            "SystemDescription=\"Center for REALTOR Technology\" " +
            "TimeZoneOffset=\"-03:00\">" + EOL +
            "<Comments>The reference implementation of a RETS Server" +
            "</Comments>" + EOL +
            "</SYSTEM>" + EOL +
            "</METADATA-SYSTEM>" + EOL;
    }

    protected String getExpectedStandardRecursive()
    {
        return
            "<METADATA-SYSTEM Version=\"" + VERSION + "\" " +
            "Date=\"" + DATE + "\">" + EOL +
            "<SYSTEM SystemID=\"CRT_RETS\" " +
            "SystemDescription=\"Center for REALTOR Technology\" " +
            "TimeZoneOffset=\"-03:00\">" + EOL +
            "<Comments>The reference implementation of a RETS Server" +
            "</Comments>" + EOL +
            "Empty list" + EOL + 	// Place holder for foreign keys
            Resource.TABLE + EOL +
            "</SYSTEM>" + EOL +
            "</METADATA-SYSTEM>" + EOL;
    }
}
