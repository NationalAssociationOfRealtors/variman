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

import java.util.Collection;
import java.util.Iterator;
import java.io.PrintWriter;

import org.realtors.rets.common.util.TagBuilder;
import org.realtors.rets.server.metadata.UpdateType;
import org.realtors.rets.server.metadata.Table;

public class StandardUpdateTypeFormatter extends BaseStandardFormatter
{
    public void format(FormatterContext context, Collection updateTypes,
                       String[] levels)
    {
        PrintWriter out = context.getWriter();
        String resource = levels[RESOURCE_LEVEL];
        String retsClass = levels[CLASS_LEVEL];
        TagBuilder metadata = new TagBuilder(out, "METADATA-UPDATE_TYPE")
            .appendAttribute("Resource", resource)
            .appendAttribute("Class", retsClass)
            .appendAttribute("Update", levels[UPDATE_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate(), context.getRetsVersion())
            .beginContentOnNewLine();

        for (Iterator i = updateTypes.iterator(); i.hasNext();)
        {
            UpdateType updateType = (UpdateType) i.next();
            Table table = updateType.getTable();
            if (!context.isAccessibleTable(table, resource, retsClass))
            {
                continue;
            }

            TagBuilder tag = new TagBuilder(out, "UpdateField")
                .beginContentOnNewLine();

            TagBuilder.simpleTag(out, "MetadataEntryID", updateType.getMetadataEntryID());
            TagBuilder.simpleTag(out, "SystemName",
                                 table.getSystemName());
            TagBuilder.simpleTag(out, "Sequence", updateType.getSequence());
            TagBuilder.simpleTag(out, "Attributes", updateType.getAttributes());
            TagBuilder.simpleTag(out, "Default", updateType.getDefault());
            TagBuilder.simpleTag(out, "ValidationExpressionID",
                                 updateType.getValidationExpressions());
            TagBuilder.simpleTag(out, "UpdateHelpID",
                                 updateType.getUpdateHelp());
            TagBuilder.simpleTag(out, "ValidationLookupName",
                                 updateType.getValidationLookup());
            TagBuilder.simpleTag(out, "ValidationExternalName",
                                 updateType.getValidationExternal());
            TagBuilder.simpleTag(out, "MaxUpdate", updateType.getMaxUpdate());
            
            tag.close();
        }

        metadata.close();
    }
}
