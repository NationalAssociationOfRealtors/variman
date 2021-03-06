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

import org.realtors.rets.common.util.DataRowBuilder;
import org.realtors.rets.common.util.TagBuilder;
import org.realtors.rets.server.metadata.SearchHelp;

public class CompactSearchHelpFormatter extends MetadataFormatter
{
    public void format(FormatterContext context, Collection searchHelps,
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
        for (Iterator iterator = searchHelps.iterator(); iterator.hasNext();)
        {
            SearchHelp searchHelp = (SearchHelp) iterator.next();
            appendDataRow(context, searchHelp);
        }
        tag.close();
    }

    private void appendDataRow(FormatterContext context, SearchHelp searchHelp)
    {
        DataRowBuilder row = new DataRowBuilder(context.getWriter());
        row.begin();
        row.append(searchHelp.getMetadataEntryID());
        row.append(searchHelp.getSearchHelpID());
        row.append(searchHelp.getValue());
        row.end();
     }

    private static final String[] COLUMNS = new String[] {
        "MetadataEntryID", "SearchHelpID", "Value",
    };
}
