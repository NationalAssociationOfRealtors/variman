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
import org.realtors.rets.common.metadata.types.MValidationExternal;
import org.realtors.rets.common.util.DataRowBuilder;
import org.realtors.rets.common.util.TagBuilder;

public class CompactValidationExternalFormatter extends MetadataFormatter
{
    public void format(FormatterContext context, Collection<MetaObject> validationExternals,
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
            .appendAttribute("Date", context.getDate(), context.getRetsVersion())
            .beginContentOnNewLine()
            .appendColumns(COLUMNS);
        for (Iterator<?> i = validationExternals.iterator(); i.hasNext();)
        {
            MValidationExternal validationExternal =
                (MValidationExternal) i.next();
            appendDataRow(context, validationExternal);
        }
        tag.close();

        if (context.isRecursive())
        {
            for (Iterator<?> i = validationExternals.iterator(); i.hasNext();)
            {
                MValidationExternal validationExternal =
                    (MValidationExternal) i.next();
                String[] path = StringUtils.split(validationExternal.getPath(), ":");
                context.format(validationExternal.getChildren(MetadataType.VALIDATION_EXTERNAL_TYPE), path);
            }
        }
    }

    private void appendDataRow(FormatterContext context,
            MValidationExternal validationExternal)
    {
        DataRowBuilder row = new DataRowBuilder(context.getWriter());
        row.begin();
        row.append(validationExternal.getMetadataEntryID());
        row.append(validationExternal.getValidationExternalName());
        row.append(validationExternal.getSearchResource());
        row.append(validationExternal.getSearchClass());
        row.append(context.getVersion());
        row.append(context.getDate(), context.getRetsVersion());
        row.end();
    }

    // FIXME: MetaObject.getAttributeNames() but takes a RetsVersion so the
    // correct attribute names are returned.
    private static final String[] COLUMNS = new String[] {
        "MetadataEntryID",
        "ValidationExternalName", "SearchResource", "SearchClass", "Version",
        "Date",
    };
}
