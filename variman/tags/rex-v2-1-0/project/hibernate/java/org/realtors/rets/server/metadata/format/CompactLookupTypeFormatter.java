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

import org.realtors.rets.server.metadata.LookupType;

public class CompactLookupTypeFormatter extends MetadataFormatter
{
    public void format(FormatterContext context, Collection lookupTypes,
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
            .appendAttribute("Date", context.getDate())
            .beginContentOnNewLine()
            .appendColumns(COLUMNS);
        for (Iterator i = lookupTypes.iterator(); i.hasNext();)
        {
            LookupType lookupType = (LookupType) i.next();
            appendDataRow(context, lookupType);
        }
        tag.close();
    }

    private void appendDataRow(FormatterContext context, LookupType lookupType)
    {
        DataRowBuilder row = new DataRowBuilder(context.getWriter());
        row.begin();
        row.append(lookupType.getLongValue());
        row.append(lookupType.getShortValue());
        row.append(lookupType.getValue());
        row.end();
    }

    private static final String[] COLUMNS = new String[] {
        "LongValue", "ShortValue", "Value",
    };
}
