/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004-2009, The National Association of REALTORS
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

public class StandardSystemFormatter extends MetadataFormatter
{
    public void format(FormatterContext context, Collection systems,
                       String[] levels)
    {
        RetsVersion retsVersion = context.getRetsVersion();
        PrintWriter out = context.getWriter();
        
        // Get first element
        MSystem system = (MSystem) systems.iterator().next();
        TagBuilder metadata = new TagBuilder(out, "METADATA-SYSTEM")
            .appendAttribute("Version", system.getVersionString())
            .appendAttribute("Date", system.getDate(), context.getRetsVersion())
            .beginContentOnNewLine();

        TagBuilder tag;
        
        // 1.7.2 DTD makes changes.
        if (retsVersion.equals(RetsVersion.RETS_1_0) || retsVersion.equals(RetsVersion.RETS_1_5) ||
                retsVersion.equals(RetsVersion.RETS_1_7))
        {
            tag = new TagBuilder(out, "System").beginContentOnNewLine();

            TagBuilder.simpleTag(out, "SystemID", system.getSystemID());
            TagBuilder.simpleTag(out, "SystemDescription", system.getDescription());
            TagBuilder.simpleTag(out, "Comments", system.getComments());
        }
        else
        {
            tag = new TagBuilder(out, "SYSTEM")
                        .appendAttribute("SystemID", system.getSystemID())
                        .appendAttribute("SystemDescription", system.getDescription())
                        .appendAttribute("TimeZoneOffset", system.getTimeZoneOffset())
                        .beginContentOnNewLine();

            TagBuilder.simpleTag(out, "Comments", system.getComments());
        }

        if (context.isRecursive())
        {
        	context.format(system.getForeignKeys(), system.getPathAsArray());
            context.format(system.getResources(), system.getPathAsArray());
        }

        tag.close();
        metadata.close();
    }
}