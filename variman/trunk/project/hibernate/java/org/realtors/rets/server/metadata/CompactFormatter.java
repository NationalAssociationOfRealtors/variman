/*
 */
package org.realtors.rets.server.metadata;

import java.util.Date;

public class CompactFormatter
{
    public String format(MSystem system)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<SYSTEM ");
        buffer.append("SystemID=\"")
            .append(system.getSystemID())
            .append("\" ");
        buffer.append("SystemDescription=\"")
            .append(system.getDescription())
            .append("\"/>\n");
        buffer.append("<COMMENTS>")
            .append(system.getComments())
            .append("</COMMENTS>\n");
        return buffer.toString();
    }

    public String format(Resource resource, String version, Date date)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<DATA>\t");
        buffer.append(resource.getResourceID()).append("\t");
        buffer.append(resource.getStandardName()).append("\t");
        buffer.append(resource.getVisibleName()).append("\t");
        buffer.append(resource.getDescription()).append("\t");
        buffer.append(resource.getKeyField()).append("\t");
        buffer.append(resource.getClasses().size()).append("\t");
        // There are 9 version/date pairs for the following tables: class,
        // object, search help, edit mask, lookup, update help, validation
        // expression, validation lookup, validation external.
        for (int i = 0; i < 9; i++)
        {
            buffer.append(version).append("\t").append(date).append("\t");
        }
        buffer.append("</DATA>\n");
        return buffer.toString();
    }
}
