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

import org.realtors.rets.server.metadata.ValidationLookupType;

public class CompactValidationLookupTypeFormatter extends MetadataFormatter
{
    public void format(FormatterContext context,
                       Collection validationLookupTypes, String[] levels)
    {
        if (validationLookupTypes.size() == 0)
        {
            return;
        }
        TagBuilder tag = new TagBuilder(context.getWriter(),
                                        "METADATA-VALIDATION_LOOKUP_TYPE")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("ValidationLookup",
                             levels[VALIDATION_LOOKUP_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate())
            .beginContentOnNewLine()
            .appendColumns(COLUMNS);
        for (Iterator i = validationLookupTypes.iterator(); i.hasNext();)
        {
            ValidationLookupType validationLookupType =
                (ValidationLookupType) i.next();
            appendDataRow(context, validationLookupType);
        }
        tag.close();
    }

    private void appendDataRow(FormatterContext context,
                               ValidationLookupType validationLookupType)
    {
        DataRowBuilder row = new DataRowBuilder(context.getWriter());
        row.begin();
        row.append(validationLookupType.getValidText());
        row.append(validationLookupType.getParent1Value());
        row.append(validationLookupType.getParent2Value());
        row.end();
    }

    private static final String[] COLUMNS = new String[] {
        "ValidText", "Parent1Value", "Parent2Value",
    };
}
