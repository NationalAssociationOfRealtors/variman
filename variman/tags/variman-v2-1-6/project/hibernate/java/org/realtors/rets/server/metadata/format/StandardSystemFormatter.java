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

public class StandardSystemFormatter extends MetadataFormatter
{
    public void format(FormatterContext context, Collection systems,
                       String[] levels)
    {
        PrintWriter out = context.getWriter();
        // Get first element
        MSystem system = (MSystem) systems.iterator().next();
        TagBuilder metadata = new TagBuilder(out, "METADATA-SYSTEM")
            .appendAttribute("Version", system.getVersionString())
            .appendAttribute("Date", system.getDate())
            .beginContentOnNewLine();

        TagBuilder tag = new TagBuilder(out, "System")
            .beginContentOnNewLine();

        TagBuilder.simpleTag(out, "SystemID", system.getSystemID());
        TagBuilder.simpleTag(out, "SystemDescription", system.getDescription());
        TagBuilder.simpleTag(out, "Comments", system.getComments());

        if (context.isRecursive())
        {
            context.format(system.getResources(), system.getPathAsArray());
        }

        tag.close();
        metadata.close();
    }
}