/*
 */
package org.realtors.rets.server.metadata.format;

import org.realtors.rets.server.metadata.format.SystemFormatter;
import org.realtors.rets.server.metadata.MSystem;

public class CompactSystemFormatter
    extends SystemFormatter
{
    public String format(MSystem system)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<METADATA-SYSTEM ");
        buffer.append("Version=\"")
            .append(system.getVersionString())
            .append("\" ");
        buffer.append("Date=\"")
            .append(format(system.getDate()))
            .append("\">\n");
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
        buffer.append("</METADATA-SYSTEM>\n");
        return buffer.toString();
    }
}
