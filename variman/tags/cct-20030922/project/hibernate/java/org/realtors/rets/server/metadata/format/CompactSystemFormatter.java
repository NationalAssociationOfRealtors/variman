/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.util.List;

import org.realtors.rets.server.metadata.MSystem;

public class CompactSystemFormatter extends SystemFormatter
{
    public void format(PrintWriter out, List systems)
    {
        MSystem system = (MSystem) systems.get(0);
        TagBuilder tag = new TagBuilder(out);
        tag.begin("METADATA-SYSTEM");
        tag.appendAttribute("Version", system.getVersionString());
        tag.appendAttribute("Date", system.getDate());
        tag.endAttributes();
        out.print("<SYSTEM ");
        out.print("SystemID=\"");
        out.print(system.getSystemID());
        out.print("\" ");
        out.print("SystemDescription=\"");
        out.print(system.getDescription());
        out.print("\"/>\n");
        out.print("<COMMENTS>");
        out.print(system.getComments());
        out.print("</COMMENTS>\n");
        tag.end();
    }
}
