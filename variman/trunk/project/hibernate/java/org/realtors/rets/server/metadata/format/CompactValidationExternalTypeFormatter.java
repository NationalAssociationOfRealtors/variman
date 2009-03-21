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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.realtors.rets.common.util.DataRowBuilder;
import org.realtors.rets.common.util.TagBuilder;
import org.realtors.rets.server.metadata.ValidationExternalType;

public class CompactValidationExternalTypeFormatter extends MetadataFormatter
{
    public void format(FormatterContext context,
                       Collection validationExternalTypes, String[] levels)
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
        for (Iterator i = validationExternalTypes.iterator(); i.hasNext();)
        {
            ValidationExternalType validationExternalType =
                (ValidationExternalType) i.next();
            appendDataRow(context, validationExternalType);
        }
        tag.close();
    }

    private void appendDataRow(FormatterContext context,
                               ValidationExternalType validationExternalType)
    {
        DataRowBuilder row = new DataRowBuilder(context.getWriter());
        row.begin();
        row.append(validationExternalType.getMetadataEntryID());
        row.append(validationExternalType.getSearchField());
        row.append(validationExternalType.getDisplayField());
        List formattedResultFields =
            mapToKeyValues(validationExternalType.getResultFields());
        row.append(formattedResultFields);
        row.end();
    }

    private List mapToKeyValues(Map resultFields)
    {
        Set keys = resultFields.keySet();
        List keyValues = new ArrayList();
        for (Iterator iterator = keys.iterator(); iterator.hasNext();)
        {
            String key = (String) iterator.next();
            String value = (String) resultFields.get(key);
            keyValues.add(key + "=" + value);
        }
        return keyValues;
    }

    private static final String[] COLUMNS = new String[] {
        "MetadataEntryID", "SearchField", "DisplayField", "ResultFields",
    };
}
