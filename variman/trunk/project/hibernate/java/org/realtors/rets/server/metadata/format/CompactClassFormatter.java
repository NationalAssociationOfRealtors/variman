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

import org.realtors.rets.server.metadata.MClass;

public class CompactClassFormatter extends MetadataFormatter
{
    public void format(FormatterContext context, Collection classes,
                       String[] levels)
    {
        if (classes.size() == 0)
        {
            return;
        }
        TagBuilder tag = new TagBuilder(context.getWriter(), "METADATA-CLASS")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate())
            .beginContentOnNewLine()
            .appendColumns(COLUMNS);
        for (Iterator iterator = classes.iterator(); iterator.hasNext();)
        {
            MClass clazz = (MClass) iterator.next();
            appendDataRow(context, clazz);

        }
        tag.close();

        if (context.isRecursive())
        {
            for (Iterator iterator = classes.iterator(); iterator.hasNext();)
            {
                MClass clazz = (MClass) iterator.next();
                String[] path = clazz.getPathAsArray();
                context.format(clazz.getTables(), path);
                context.format(clazz.getUpdates(), path);

            }
        }
    }

    private void appendDataRow(FormatterContext context, MClass clazz)
    {
        DataRowBuilder row = new DataRowBuilder(context.getWriter());
        row.begin();
        row.append(clazz.getClassName());
        row.append(clazz.getStandardName());
        row.append(clazz.getVisibleName());
        row.append(clazz.getDescription());
        // Table version and date
        row.append(context.getVersion());
        row.append(context.getDate());
        // Update version and date
        row.append(context.getVersion());
        row.append(context.getDate());
        row.end();
    }

    private static final String[] COLUMNS = {
        "ClassName", "StandardName", "VisibleName", "Description",
        "TableVersion", "TableDate", "UpdateVersion", "UpdateDate",
    };
}
