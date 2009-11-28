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

import org.apache.commons.lang.StringUtils;

import org.realtors.rets.client.RetsVersion;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataType;
import org.realtors.rets.common.metadata.types.MSystem;
import org.realtors.rets.common.util.TagBuilder;
import org.realtors.rets.server.Util;

public class StandardSystemFormatter extends BaseStandardFormatter
{
    public void format(FormatterContext context, Collection<MetaObject> systems,
                       String[] levels)
    {
        RetsVersion retsVersion = context.getRetsVersion();
        PrintWriter out = context.getWriter();
        
        // Get first element
        MSystem system = (MSystem) systems.iterator().next();
        TagBuilder metadata = new TagBuilder(out, "METADATA-SYSTEM")
            .appendAttribute("Version", Util.getVersionString(system.getVersion()))
            .appendAttribute("Date", system.getDate(), context.getRetsVersion())
            .beginContentOnNewLine();

        TagBuilder tag;
        
        // 1.7.2 DTD makes changes.
        if (retsVersion.equals(RetsVersion.RETS_1_0) || retsVersion.equals(RetsVersion.RETS_1_5) ||
                retsVersion.equals(RetsVersion.RETS_1_7))
        {
            tag = new TagBuilder(out, "System").beginContentOnNewLine();

            TagBuilder.simpleTag(out, "SystemID", system.getSystemID());
            TagBuilder.simpleTag(out, "SystemDescription", system.getSystemDescription());
            TagBuilder.simpleTag(out, "Comments", system.getComments());
        }
        else
        {
            tag = new TagBuilder(out, "SYSTEM")
                        .appendAttribute("SystemID", system.getSystemID())
                        .appendAttribute("SystemDescription", system.getSystemDescription())
                        .appendAttribute("TimeZoneOffset", system.getTimeZoneOffset())
                        .beginContentOnNewLine();

            TagBuilder.simpleTag(out, "Comments", system.getComments());
        }

        if (context.isRecursive())
        {
            String[] path = StringUtils.split(system.getPath(), ":");
            context.format(system.getChildren(MetadataType.RESOURCE), path);
            context.format(system.getChildren(MetadataType.FOREIGN_KEYS), path);
        }

        tag.close();
        metadata.close();
    }
}