/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.ArrayList;
import java.util.List;

import org.realtors.rets.common.metadata.MetadataType;
import org.realtors.rets.common.metadata.types.MResource;
import org.realtors.rets.common.metadata.types.MSystem;

public class SystemFormatterTest extends FormatterTestCase
{
    protected List<MSystem> getData()
    {
        List<MSystem> systems = new ArrayList<MSystem>();
        MSystem system = new MSystem();
        system.setSystemID("CRTRETS");
        system.setSystemDescription("Center for REALTOR Technology");
        system.setComments("The reference implementation of a RETS Server");
        system.setTimeZoneOffset("-03:00");
        system.setVersion(10000001);
        system.setDate(getDate());
        MResource resource = new MResource();
        resource.setUniqueId(Long.valueOf(1));
        resource.setResourceID("Property");
        system.addChild(MetadataType.RESOURCE, resource);
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
            "<SYSTEM SystemID=\"CRTRETS\" " +
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
            "<SYSTEM SystemID=\"CRTRETS\" " +
            "SystemDescription=\"Center for REALTOR Technology\" " +
            "TimeZoneOffset=\"-03:00\"/>\n" +
            "<COMMENTS>The reference implementation of a RETS Server" +
            "</COMMENTS>\n" +
            "</METADATA-SYSTEM>\n" +
            MetadataType.RESOURCE.name() + "\n" +
           "Empty list\n"; // Place holder for foreign keys
    }

    protected String getExpectedStandard()
    {
        return
            "<METADATA-SYSTEM Version=\"" + VERSION + "\" " +
            "Date=\"" + DATE + "\">" + EOL +
            "<SYSTEM SystemID=\"CRTRETS\" " +
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
            "<SYSTEM SystemID=\"CRTRETS\" " +
            "SystemDescription=\"Center for REALTOR Technology\" " +
            "TimeZoneOffset=\"-03:00\">" + EOL +
            "<Comments>The reference implementation of a RETS Server" +
            "</Comments>" + EOL +
            MetadataType.RESOURCE.name() + EOL +
            "Empty list" + EOL + // Place holder for foreign keys
            "</SYSTEM>" + EOL +
            "</METADATA-SYSTEM>" + EOL;
    }
}
