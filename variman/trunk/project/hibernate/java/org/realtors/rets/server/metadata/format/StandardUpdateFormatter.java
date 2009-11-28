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

import org.apache.commons.lang.StringUtils;

import org.realtors.rets.client.RetsVersion;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataType;
import org.realtors.rets.common.metadata.types.MUpdate;
import org.realtors.rets.common.util.TagBuilder;

public class StandardUpdateFormatter extends BaseStandardFormatter
{
    public void format(FormatterContext context, Collection<MetaObject> updates,
                       String[] levels)
    {
        RetsVersion retsVersion = context.getRetsVersion();
        PrintWriter out = context.getWriter();
        TagBuilder metadata = new TagBuilder(out, "METADATA-UPDATE")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("Class", levels[CLASS_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate(), context.getRetsVersion())
            .beginContentOnNewLine();

        for (Iterator<?> i = updates.iterator(); i.hasNext();)
        {
            MUpdate update = (MUpdate) i.next();
            TagBuilder tag = new TagBuilder(out, "UpdateType")
                .beginContentOnNewLine();
 
            if (!retsVersion.equals(RetsVersion.RETS_1_0) && !retsVersion.equals(RetsVersion.RETS_1_5))
            {
                // Added 1.7 DTD
                TagBuilder.simpleTag(out, "MetadataEntryID", update.getMetadataEntryID());
            }
            TagBuilder.simpleTag(out, "UpdateName", update.getUpdateName());
            TagBuilder.simpleTag(out, "Description", update.getDescription());
            TagBuilder.simpleTag(out, "KeyField", update.getKeyField());
            
            formatVersionDateTags(context, VERSION_DATE_TAGS);

            if (context.isRecursive())
            {
                String[] path = StringUtils.split(update.getPath(), ":");
                context.format(update.getChildren(MetadataType.UPDATE_TYPE), path);
            }

            tag.close();
        }

        metadata.close();
    }

    private static final String[] VERSION_DATE_TAGS = {"UpdateType"};
}
