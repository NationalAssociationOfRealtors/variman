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
import org.realtors.rets.common.metadata.types.MLookupType;
import org.realtors.rets.common.util.DataRowBuilder;
import org.realtors.rets.common.util.TagBuilder;

public class CompactLookupTypeFormatter extends MetadataFormatter
{
    public void format(FormatterContext context, Collection<MetaObject> lookupTypes,
                       String[] levels)
    {
        if (lookupTypes.size() == 0)
        {
            return;
        }
        TagBuilder tag = new TagBuilder(context.getWriter(),
                                        "METADATA-LOOKUP_TYPE")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("Lookup", levels[LOOKUP_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate(), context.getRetsVersion())
            .beginContentOnNewLine()
            .appendColumns(COLUMNS);
        for (Iterator<?> i = lookupTypes.iterator(); i.hasNext();)
        {
            MLookupType lookupType = (MLookupType) i.next();
            appendDataRow(context, lookupType);
        }
        tag.close();
    }

    private void appendDataRow(FormatterContext context, MLookupType lookupType)
    {
        DataRowBuilder row = new DataRowBuilder(context.getWriter());
        row.begin();
        row.append(lookupType.getMetadataEntryID());
        row.append(lookupType.getLongValue());
        row.append(lookupType.getShortValue());
        row.append(lookupType.getValue());
        row.end();
    }

    // FIXME: MetaObject.getAttributeNames() but takes a RetsVersion so the
    // correct attribute names are returned.
    private static final String[] COLUMNS = new String[] {
        "MetadataEntryID", "LongValue", "ShortValue", "Value",
    };
}
