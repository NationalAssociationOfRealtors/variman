/*
 * Rex RETS Server
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

import org.realtors.rets.server.metadata.ValidationExternal;

public class CompactValidationExternalFormatter extends MetadataFormatter
{
    public void format(FormatterContext context, Collection validationExternals,
                       String[] levels)
    {
        if (validationExternals.size() == 0)
        {
            return;
        }
        TagBuilder tag = new TagBuilder(context.getWriter(),
                                        "METADATA-VALIDATION_EXTERNAL")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate())
            .beginContentOnNewLine()
            .appendColumns(COLUMNS);
        for (Iterator i = validationExternals.iterator(); i.hasNext();)
        {
            ValidationExternal validationExternal =
                (ValidationExternal) i.next();
            appendDataRow(context, validationExternal);
        }
        tag.close();

        if (context.isRecursive())
        {
            for (Iterator i = validationExternals.iterator(); i.hasNext();)
            {
                ValidationExternal validationExternal =
                    (ValidationExternal) i.next();
                context.format(validationExternal.getValidationExternalTypes(),
                               validationExternal.getPathAsArray());
            }
        }
    }

    private void appendDataRow(FormatterContext context,
                               ValidationExternal validationExternal)
    {
        DataRowBuilder row = new DataRowBuilder(context.getWriter());
        row.begin();
        row.append(validationExternal.getValidationExternalName());
        row.append(validationExternal.getSearchResource());
        row.append(validationExternal.getSearchClass());
        row.append(context.getVersion());
        row.append(context.getDate());
        row.end();
    }

    private static final String[] COLUMNS = new String[] {
        "ValidationExternalName", "SearchResource", "SearchClass", "Version",
        "Date",
    };
}
