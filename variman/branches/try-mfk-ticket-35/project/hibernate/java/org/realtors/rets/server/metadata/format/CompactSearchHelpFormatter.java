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
import org.realtors.rets.common.metadata.types.MSearchHelp;
import org.realtors.rets.common.util.DataRowBuilder;
import org.realtors.rets.common.util.TagBuilder;

public class CompactSearchHelpFormatter extends MetadataFormatter
{
    public void format(FormatterContext context, Collection<MetaObject> searchHelps,
                       String[] levels)
    {
        if (searchHelps.size() == 0)
        {
            return;
        }
        TagBuilder tag = new TagBuilder(context.getWriter(),
                                        "METADATA-SEARCH_HELP")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate(), context.getRetsVersion())
            .beginContentOnNewLine()
            .appendColumns(COLUMNS);
        for (Iterator<?> iterator = searchHelps.iterator(); iterator.hasNext();)
        {
            MSearchHelp searchHelp = (MSearchHelp) iterator.next();
            appendDataRow(context, searchHelp);
        }
        tag.close();
    }

    private void appendDataRow(FormatterContext context, MSearchHelp searchHelp)
    {
        DataRowBuilder row = new DataRowBuilder(context.getWriter());
        row.begin();
        row.append(searchHelp.getMetadataEntryID());
        row.append(searchHelp.getSearchHelpID());
        row.append(searchHelp.getValue());
        row.end();
     }

    // FIXME: MetaObject.getAttributeNames() but takes a RetsVersion so the
    // correct attribute names are returned.
    private static final String[] COLUMNS = new String[] {
        "MetadataEntryID", "SearchHelpID", "Value",
    };
}
