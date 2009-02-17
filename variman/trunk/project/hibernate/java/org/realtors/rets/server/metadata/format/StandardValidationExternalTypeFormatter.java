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
import java.util.Map;
import java.util.List;
import java.io.PrintWriter;

import org.realtors.rets.server.metadata.ValidationExternalType;

public class StandardValidationExternalTypeFormatter
    extends BaseStandardFormatter
{
    public void format(FormatterContext context,
                       Collection validationExternalTypes, String[] levels)
    {
        PrintWriter out = context.getWriter();
        TagBuilder metadata =
            new TagBuilder(out, "METADATA-VALIDATION_EXTERNAL_TYPE")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("ValidationExternalName",
                             levels[VALIDATION_EXTERNAL_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate(), context.getRetsVersion())
            .beginContentOnNewLine();

        for (Iterator i = validationExternalTypes.iterator(); i.hasNext();)
        {
            ValidationExternalType validationExternalType =
                (ValidationExternalType) i.next();
            TagBuilder tag = new TagBuilder(out, "ValidationExternal")
                .beginContentOnNewLine();
            
            // 1.7.2
            TagBuilder.simpleTag(out, "MetadataEntryID", validationExternalType.getMetadataEntryID());

            List sorted = FormatUtil.toSortedStringList(
                validationExternalType.getSearchField());
            for (int j = 0; j < sorted.size(); j++)
            {
                TagBuilder.simpleTag(out, "SearchField", sorted.get(j));
            }

            sorted = FormatUtil.toSortedStringList(
                validationExternalType.getDisplayField());
            for (int j = 0; j < sorted.size(); j++)
            {
                TagBuilder.simpleTag(out, "DisplayField", sorted.get(j));
            }

            Map resultFields = validationExternalType.getResultFields();
            sorted = FormatUtil.toSortedStringList(resultFields.keySet());
            for (int j = 0; j < sorted.size(); j++)
            {
                TagBuilder resultFieldsTag = new TagBuilder(out, "ResultFields")
                    .beginContentOnNewLine();
                String source = (String) sorted.get(j);
                String target = (String) resultFields.get(source);
                TagBuilder.simpleTag(out, "Source", source);
                TagBuilder.simpleTag(out, "Target", target);
                resultFieldsTag.close();
            }

            tag.close();
        }

        metadata.close();
    }
}
