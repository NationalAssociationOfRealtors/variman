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

import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.types.MValidationExternalType;
import org.realtors.rets.common.util.DataRowBuilder;
import org.realtors.rets.common.util.TagBuilder;

public class CompactValidationExternalTypeFormatter extends MetadataFormatter
{
    public void format(FormatterContext context,
                       Collection<MetaObject> validationExternalTypes, String[] levels)
    {
        if (validationExternalTypes.size() == 0)
        {
            return;
        }
        TagBuilder tag = new TagBuilder(context.getWriter(),
                                        "METADATA-VALIDATION_EXTERNAL_TYPE")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("ValidationExternalName",
                             levels[VALIDATION_EXTERNAL_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate(), context.getRetsVersion())
            .beginContentOnNewLine()
            .appendColumns(COLUMNS);
        for (Iterator<?> i = validationExternalTypes.iterator(); i.hasNext();)
        {
            MValidationExternalType validationExternalType =
                (MValidationExternalType) i.next();
            appendDataRow(context, validationExternalType);
        }
        tag.close();
    }

    private void appendDataRow(FormatterContext context,
            MValidationExternalType validationExternalType)
    {
        DataRowBuilder row = new DataRowBuilder(context.getWriter());
        row.begin();
        row.append(validationExternalType.getMetadataEntryID());
        row.append(validationExternalType.getSearchField());
        row.append(validationExternalType.getDisplayField());
        row.append(validationExternalType.getResultFields());
        row.end();
    }

    // FIXME: MetaObject.getAttributeNames() but takes a RetsVersion so the
    // correct attribute names are returned.
    private static final String[] COLUMNS = new String[] {
        "MetadataEntryID", "SearchField", "DisplayField", "ResultFields",
    };
}
