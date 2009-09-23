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

import org.realtors.rets.client.RetsVersion;
import org.realtors.rets.common.util.TagBuilder;
import org.realtors.rets.server.metadata.MSystem;

public class CompactSystemFormatter extends MetadataFormatter
{
    public void format(FormatterContext context, Collection systems,
                       String[] levels)
    {
        RetsVersion retsVersion = context.getRetsVersion();
        if (systems.size() == 0)
        {
            return;
        }
        PrintWriter out = context.getWriter();
            
        // Get first element
        MSystem system = (MSystem) systems.iterator().next();
        TagBuilder tag = new TagBuilder(out, "METADATA-SYSTEM")
            .appendAttribute("Version", system.getVersionString())
            .appendAttribute("Date", system.getDate(), context.getRetsVersion())
            .beginContentOnNewLine();

        // 1.7.2 DTD makes changes.
        if (retsVersion.equals(RetsVersion.RETS_1_0) || retsVersion.equals(RetsVersion.RETS_1_5) ||
                retsVersion.equals(RetsVersion.RETS_1_7))
        {
            new TagBuilder(out, "SYSTEM")
                .appendAttribute("SystemID", system.getSystemID())
                .appendAttribute("SystemDescription", system.getDescription())
                .close();
        }
        else
        {
            new TagBuilder(out, "SYSTEM")
                .appendAttribute("SystemID", system.getSystemID())
                .appendAttribute("SystemDescription", system.getDescription())
                .appendAttribute("TimeZoneOffset", system.getTimeZoneOffset())
                .close();
        }
        TagBuilder.simpleTag(out, "COMMENTS", system.getComments());
        
        tag.end();

        if (context.isRecursive())
        {
           	context.format(system.getForeignKeys(), system.getPathAsArray());
            context.format(system.getResources(),
                           system.getPathAsArray());
        }
    }
}
