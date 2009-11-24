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

import java.util.Collection;
import java.util.Iterator;
import java.io.PrintWriter;

import org.realtors.rets.client.RetsVersion;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.types.MUpdateHelp;
import org.realtors.rets.common.util.TagBuilder;

public class StandardUpdateHelpFormatter extends BaseStandardFormatter
{
    public void format(FormatterContext context, Collection<MetaObject> updateHelps,
                       String[] levels)
    {
        RetsVersion retsVersion = context.getRetsVersion();
        PrintWriter out = context.getWriter();
        TagBuilder metadata = new TagBuilder(out, "METADATA-UPDATE_HELP")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate(), context.getRetsVersion())
            .beginContentOnNewLine();

        for (Iterator<?> i = updateHelps.iterator(); i.hasNext();)
        {
            MUpdateHelp updateHelp = (MUpdateHelp) i.next();
            TagBuilder tag = new TagBuilder(out, "UpdateHelp")
                .beginContentOnNewLine();

            if (!retsVersion.equals(RetsVersion.RETS_1_0) && !retsVersion.equals(RetsVersion.RETS_1_5))
            {
                // Added 1.7 DTD
                TagBuilder.simpleTag(out, "MetadataEntryID", updateHelp.getMetadataEntryID());
            }            
            TagBuilder.simpleTag(out, "UpdateHelpID", updateHelp.getUpdateHelpID());
            TagBuilder.simpleTag(out, "Value", updateHelp.getValue());

            tag.close();
        }

        metadata.close();
    }
}
