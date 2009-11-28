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
import org.realtors.rets.common.metadata.types.MUpdate;
import org.realtors.rets.common.util.DataRowBuilder;
import org.realtors.rets.common.util.TagBuilder;
import org.realtors.rets.server.Util;

public class CompactUpdateFormatter extends MetadataFormatter
{
    public void format(FormatterContext context, Collection<MetaObject> updates,
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
        for (Iterator<?> iterator = updates.iterator(); iterator.hasNext();)
        {
            MUpdate update = (MUpdate) iterator.next();
            appendDataRow(context, update);
        }
        tag.close();

        if (context.isRecursive())
        {
            for (Iterator<?> iterator = updates.iterator(); iterator.hasNext();)
            {
                MUpdate update = (MUpdate) iterator.next();
                String[] path = StringUtils.split(update.getPath(), ":");
                context.format(update.getChildren(MetadataType.UPDATE_TYPE), path);
            }
        }
    }

    private void appendDataRow(FormatterContext context, MUpdate update)
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

    // FIXME: MetaObject.getAttributeNames() but takes a RetsVersion so the
    // correct attribute names are returned.
    private static final String[] COLUMNS = new String[] {
        "MetadataEntryID",
        "UpdateName", "Description", "KeyField", "UpdateTypeVersion", "UpdateTypeDate",
    };
}
