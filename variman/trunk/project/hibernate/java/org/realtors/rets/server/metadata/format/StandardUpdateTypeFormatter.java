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
import org.realtors.rets.common.metadata.types.MTable;
import org.realtors.rets.common.metadata.types.MUpdateType;
import org.realtors.rets.common.util.TagBuilder;

public class StandardUpdateTypeFormatter extends BaseStandardFormatter
{
    public void format(FormatterContext context, Collection<MetaObject> updateTypes,
                       String[] levels)
    {
        RetsVersion retsVersion = context.getRetsVersion();
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

        for (Iterator<?> i = updateTypes.iterator(); i.hasNext();)
        {
            MUpdateType updateType = (MUpdateType) i.next();
            MTable table = updateType.getMTable();
            if (!context.isAccessibleTable(table, resource, retsClass))
            {
                continue;
            }

            TagBuilder tag = new TagBuilder(out, "UpdateField")
                .beginContentOnNewLine();

            if (!retsVersion.equals(RetsVersion.RETS_1_0) && !retsVersion.equals(RetsVersion.RETS_1_5))
            {
                // Added 1.7 DTD
                TagBuilder.simpleTag(out, "MetadataEntryID", updateType.getMetadataEntryID());
            }
            TagBuilder.simpleTag(out, "SystemName", updateType.getSystemName());
            TagBuilder.simpleTag(out, "Sequence", updateType.getSequence());
            TagBuilder.simpleTag(out, "Attributes", updateType.getAttributes());
            TagBuilder.simpleTag(out, "Default", updateType.getDefault());
            TagBuilder.simpleTag(out, "ValidationExpressionID",
                                 updateType.getValidationExpressionID());
            TagBuilder.simpleTag(out, "UpdateHelpID",
                                 updateType.getUpdateHelpID());
            TagBuilder.simpleTag(out, "ValidationLookupName",
                                 updateType.getValidationLookupName());
            TagBuilder.simpleTag(out, "ValidationExternalName",
                                 updateType.getValidationExternalName());
            if (!retsVersion.equals(RetsVersion.RETS_1_0) && !retsVersion.equals(RetsVersion.RETS_1_5))
            {
                // Added 1.7 DTD
                TagBuilder.simpleTag(out, "MaxUpdate", updateType.getMaxUpdate());
            }
            
            tag.close();
        }

        metadata.close();
    }
}
