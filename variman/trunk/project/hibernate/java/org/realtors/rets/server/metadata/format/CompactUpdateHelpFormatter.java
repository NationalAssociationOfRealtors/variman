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

import org.realtors.rets.server.metadata.UpdateHelp;

public class CompactUpdateHelpFormatter extends MetadataFormatter
{
    public void format(FormatterContext context, Collection updateHelps,
                       String[] levels)
    {
        if (updateHelps.size() == 0)
        {
            return;
        }
        TagBuilder tag = new TagBuilder(context.getWriter(),
                                        "METADATA-UPDATE_HELP")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate())
            .beginContentOnNewLine()
            .appendColumns(COLUMNS);
        for (Iterator i = updateHelps.iterator(); i.hasNext();)
        {
            UpdateHelp updateHelp = (UpdateHelp) i.next();
            appendDataRow(context, updateHelp);
        }
        tag.close();
    }

    private void appendDataRow(FormatterContext context, UpdateHelp updateHelp)
    {
        DataRowBuilder row = new DataRowBuilder(context.getWriter());
        row.begin();
        row.append(updateHelp.getUpdateHelpID());
        row.append(updateHelp.getValue());
        row.end();
    }

    private static final String[] COLUMNS = {
        "UpdateHelpID", "Value"
    };
}
