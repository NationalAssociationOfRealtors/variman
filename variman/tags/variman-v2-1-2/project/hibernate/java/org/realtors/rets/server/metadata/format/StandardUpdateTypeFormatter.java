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

import org.realtors.rets.server.metadata.UpdateType;

public class StandardUpdateTypeFormatter extends BaseStandardFormatter
{
    public void format(FormatterContext context, Collection updateTypes,
                       String[] levels)
    {
        PrintWriter out = context.getWriter();
        TagBuilder metadata = new TagBuilder(out, "METADATA-UPDATE_TYPE")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("Class", levels[CLASS_LEVEL])
            .appendAttribute("Update", levels[UPDATE_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate())
            .beginContentOnNewLine();

        for (Iterator i = updateTypes.iterator(); i.hasNext();)
        {
            UpdateType updateType = (UpdateType) i.next();
            TagBuilder tag = new TagBuilder(out, "UpdateField")
                .beginContentOnNewLine();

            TagBuilder.simpleTag(out, "SystemName",
                                 updateType.getTable().getSystemName());
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
            
            tag.close();
        }

        metadata.close();
    }
}
