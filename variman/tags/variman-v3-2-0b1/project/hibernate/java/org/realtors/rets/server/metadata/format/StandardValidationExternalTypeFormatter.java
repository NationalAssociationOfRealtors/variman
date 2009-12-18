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
import java.util.Map;
import java.util.List;
import java.io.PrintWriter;

import org.realtors.rets.client.RetsVersion;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.types.MValidationExternalType;
import org.realtors.rets.common.util.TagBuilder;

public class StandardValidationExternalTypeFormatter
    extends BaseStandardFormatter
{
    public void format(FormatterContext context,
                       Collection<MetaObject> validationExternalTypes, String[] levels)
    {
        RetsVersion retsVersion = context.getRetsVersion();
        PrintWriter out = context.getWriter();
        TagBuilder metadata =
            new TagBuilder(out, "METADATA-VALIDATION_EXTERNAL_TYPE")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("ValidationExternalName",
                             levels[VALIDATION_EXTERNAL_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate(), context.getRetsVersion())
            .beginContentOnNewLine();

        for (Iterator<?> i = validationExternalTypes.iterator(); i.hasNext();)
        {
            MValidationExternalType validationExternalType =
                (MValidationExternalType) i.next();
            TagBuilder tag = new TagBuilder(out, "ValidationExternal")
                .beginContentOnNewLine();
            
            if (!retsVersion.equals(RetsVersion.RETS_1_0) && !retsVersion.equals(RetsVersion.RETS_1_5))
            {
                // Added 1.7 DTD
                TagBuilder.simpleTag(out, "MetadataEntryID", validationExternalType.getMetadataEntryID());
            }

            String searchFieldCsv = validationExternalType.getSearchField();
            List<?> sorted = FormatUtil.toSortedStringList(
                    MValidationExternalType.toSearchFields(searchFieldCsv));
            for (int j = 0; j < sorted.size(); j++)
            {
                TagBuilder.simpleTag(out, "SearchField", sorted.get(j));
            }

            String displayFieldCsv = validationExternalType.getDisplayField();
            sorted = FormatUtil.toSortedStringList(
                    MValidationExternalType.toDisplayFields(displayFieldCsv));
            for (int j = 0; j < sorted.size(); j++)
            {
                TagBuilder.simpleTag(out, "DisplayField", sorted.get(j));
            }

            String resultFieldsCsv = validationExternalType.getResultFields();
            Map<String, String> resultFieldsMap = MValidationExternalType.toResultFieldsMap(resultFieldsCsv);
            sorted = FormatUtil.toSortedStringList(resultFieldsMap.keySet());
            for (int j = 0; j < sorted.size(); j++)
            {
                TagBuilder resultFieldsTag = new TagBuilder(out, "ResultFields")
                    .beginContentOnNewLine();
                String source = (String) sorted.get(j);
                String target = resultFieldsMap.get(source);
                TagBuilder.simpleTag(out, "Source", source);
                TagBuilder.simpleTag(out, "Target", target);
                resultFieldsTag.close();
            }

            tag.close();
        }

        metadata.close();
    }
}
