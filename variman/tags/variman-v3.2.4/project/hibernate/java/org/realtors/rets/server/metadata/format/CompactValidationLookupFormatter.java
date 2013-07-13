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

import org.apache.commons.lang.StringUtils;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataType;
import org.realtors.rets.common.metadata.types.MValidationLookup;
import org.realtors.rets.common.util.DataRowBuilder;
import org.realtors.rets.common.util.TagBuilder;

public class CompactValidationLookupFormatter extends MetadataFormatter
{
    public void format(FormatterContext context, Collection<MetaObject> validationLookups,
                       String[] levels)
    {
        if (validationLookups.size() == 0)
        {
            return;
        }
        TagBuilder tag = new TagBuilder(context.getWriter(),
                                        "METADATA-VALIDATION_LOOKUP")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate(), context.getRetsVersion())
            .beginContentOnNewLine()
            .appendColumns(COLUMNS);
        for (Iterator<?> i = validationLookups.iterator(); i.hasNext();)
        {
            MValidationLookup validationLookup = (MValidationLookup) i.next();
            appendDataRow(context, validationLookup);
        }
        tag.close();

        if (context.isRecursive())
        {
            for (Iterator<?> i = validationLookups.iterator(); i.hasNext();)
            {
                MValidationLookup validationLookup = (MValidationLookup) i.next();
                String[] path = StringUtils.split(validationLookup.getPath(), ":");
                context.format(validationLookup.getChildren(MetadataType.VALIDATION_LOOKUP_TYPE), path);
            }
        }
    }

    private void appendDataRow(FormatterContext context,
            MValidationLookup validationLookup)
    {
        DataRowBuilder row = new DataRowBuilder(context.getWriter());
        row.begin();
        row.append(validationLookup.getMetadataEntryID());
        row.append(validationLookup.getValidationLookupName());
        row.append(validationLookup.getParent1Field());
        row.append(validationLookup.getParent2Field());
        row.append(context.getVersion());
        row.append(context.getDate(), context.getRetsVersion());
        row.end();
    }

    // FIXME: MetaObject.getAttributeNames() but takes a RetsVersion so the
    // correct attribute names are returned.
    private static final String[] COLUMNS = new String[] {
        "MetadataEntryID", "ValidationLookupName", "Parent1Field", "Parent2Field", "Version",
        "Date",
    };
}
