/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.util.Collection;

import org.realtors.rets.server.metadata.MSystem;

public class StandardSystemFormatter extends MetadataFormatter
{
    public void format(FormatterContext context, Collection systems,
                       String[] levels)
    {
        PrintWriter out = context.getWriter();
        // Get first element
        MSystem system = (MSystem) systems.iterator().next();
        TagBuilder metadataSystem = new TagBuilder(out, "METADATA-SYSTEM")
            .appendAttribute("Version", system.getVersionString())
            .appendAttribute("Date", system.getDate())
            .beginContentOnNewLine();

        TagBuilder systemTag = new TagBuilder(out, "System")
            .beginContentOnNewLine();

        new TagBuilder(out, "SystemID")
            .beginContent()
            .print(system.getSystemID())
            .close();

        new TagBuilder(out, "SystemDescription")
            .beginContent()
            .print(system.getDescription())
            .close();

        new TagBuilder(out, "Comments")
            .beginContent()
            .print(system.getComments())
            .close();

        if (context.isRecursive())
        {
            context.format(system.getResources(),
                           system.getPathAsArray());
        }

        systemTag.close();
        metadataSystem.close();
    }
}