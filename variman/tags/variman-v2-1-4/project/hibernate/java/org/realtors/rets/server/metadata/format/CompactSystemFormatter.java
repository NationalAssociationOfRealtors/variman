/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.util.Collection;

import org.realtors.rets.server.metadata.MSystem;

public class CompactSystemFormatter extends MetadataFormatter
{
    public void format(FormatterContext context, Collection systems,
                       String[] levels)
    {
        if (systems.size() == 0)
        {
            return;
        }
        PrintWriter out = context.getWriter();
        // Get first element
        MSystem system = (MSystem) systems.iterator().next();
        TagBuilder tag = new TagBuilder(out, "METADATA-SYSTEM")
            .appendAttribute("Version", system.getVersionString())
            .appendAttribute("Date", system.getDate())
            .beginContentOnNewLine();

        new TagBuilder(out, "SYSTEM")
            .appendAttribute("SystemID", system.getSystemID())
            .appendAttribute("SystemDescription", system.getDescription())
            .close();

        TagBuilder.simpleTag(out, "COMMENTS", system.getComments());
        tag.end();

        if (context.isRecursive())
        {
            context.format(system.getResources(),
                           system.getPathAsArray());
        }
    }
}
