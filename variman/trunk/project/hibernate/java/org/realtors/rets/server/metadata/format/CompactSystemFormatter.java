/*
 */
package org.realtors.rets.server.metadata.format;

import org.realtors.rets.server.metadata.MSystem;

public class CompactSystemFormatter
    extends SystemFormatter
{
    public String format(MSystem system)
    {
        StringBuffer buffer = new StringBuffer();
        TagBuilder tag = new TagBuilder(buffer);
        tag.begin("METADATA-SYSTEM");
        tag.appendAttribute("Version", system.getVersionString());
        tag.appendAttribute("Date", system.getDate());
        tag.endAttributes();
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
        tag.end();
        return buffer.toString();
    }
}
