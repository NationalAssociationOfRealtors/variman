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
import org.realtors.rets.common.metadata.types.MValidationLookupType;
import org.realtors.rets.common.util.DataRowBuilder;
import org.realtors.rets.common.util.TagBuilder;

public class CompactValidationLookupTypeFormatter extends MetadataFormatter
{
    public void format(FormatterContext context,
                       Collection<MetaObject> validationLookupTypes, String[] levels)
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
            .appendAttribute("Date", context.getDate(), context.getRetsVersion())
            .beginContentOnNewLine()
            .appendColumns(COLUMNS);
        for (Iterator<?> i = validationLookupTypes.iterator(); i.hasNext();)
        {
            MValidationLookupType validationLookupType =
                (MValidationLookupType) i.next();
            appendDataRow(context, validationLookupType);
        }
        tag.close();
    }

    private void appendDataRow(FormatterContext context,
            MValidationLookupType validationLookupType)
    {
        DataRowBuilder row = new DataRowBuilder(context.getWriter());
        row.begin();
        row.append(validationLookupType.getMetadataEntryID());
        row.append(validationLookupType.getValidText());
        row.append(validationLookupType.getParent1Value());
        row.append(validationLookupType.getParent2Value());
        row.end();
    }

    // FIXME: MetaObject.getAttributeNames() but takes a RetsVersion so the
    // correct attribute names are returned.
    private static final String[] COLUMNS = new String[] {
        "MetadataEntryID", "ValidText", "Parent1Value", "Parent2Value",
    };
}
