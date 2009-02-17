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

import org.realtors.rets.server.Util;
import org.realtors.rets.server.metadata.Update;

public class CompactUpdateFormatter extends MetadataFormatter
{
    public void format(FormatterContext context, Collection updates,
                       String[] levels)
    {
        if (updates.size() == 0)
        {
            return;
        }
        TagBuilder tag = new TagBuilder(context.getWriter(), "METADATA-UPDATE")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("Class", levels[CLASS_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate(), context.getRetsVersion())
            .beginContentOnNewLine()
            .appendColumns(COLUMNS);
        for (Iterator iterator = updates.iterator(); iterator.hasNext();)
        {
            Update update = (Update) iterator.next();
            appendDataRow(context, update);
        }
        tag.close();

        if (context.isRecursive())
        {
            for (Iterator iterator = updates.iterator(); iterator.hasNext();)
            {
                Update update = (Update) iterator.next();
                context.format(update.getUpdateTypes(),
                               update.getPathAsArray());
            }
        }
    }

    private void appendDataRow(FormatterContext context, Update update)
    {
        DataRowBuilder row = new DataRowBuilder(context.getWriter());
        row.begin();
        row.append(update.getMetadataEntryID());
        row.append(update.getUpdateName());
        row.append(update.getDescription());
        row.append(update.getKeyField());
        row.append(Util.getVersionString(update.getUpdateTypeVersion()));
        row.append(update.getUpdateTypeDate(), context.getRetsVersion());
        row.end();
    }

    private static final String[] COLUMNS = new String[] {
    	"MetadataEntryID",
        "UpdateName", "Description", "KeyField", "UpdateTypeVersion", "UpdateTypeDate",
    };
}
