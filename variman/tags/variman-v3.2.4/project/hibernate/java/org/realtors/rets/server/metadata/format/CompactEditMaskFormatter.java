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
import org.realtors.rets.common.metadata.types.MEditMask;
import org.realtors.rets.common.util.DataRowBuilder;
import org.realtors.rets.common.util.TagBuilder;

public class CompactEditMaskFormatter extends MetadataFormatter
{
    public void format(FormatterContext context, Collection<MetaObject> editMasks,
                       String[] levels)
    {
        if (editMasks.size() == 0)
        {
            return;
        }
        TagBuilder tag = new TagBuilder(context.getWriter(),
                                        "METADATA-EDITMASK")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate(), context.getRetsVersion())
            .beginContentOnNewLine()
            .appendColumns(COLUMNS);
        for (Iterator<?> iterator = editMasks.iterator(); iterator.hasNext();)
        {
            MEditMask editMask = (MEditMask) iterator.next();
            appendDataRow(context, editMask);
        }
        tag.close();
    }

    private void appendDataRow(FormatterContext context, MEditMask editMask)
    {
        DataRowBuilder row = new DataRowBuilder(context.getWriter());
        row.begin();
        row.append(editMask.getMetadataEntryID());
        row.append(editMask.getEditMaskID());
        row.append(editMask.getValue());
        row.end();
    }

    // FIXME: MetaObject.getAttributeNames() but takes a RetsVersion so the
    // correct attribute names are returned.
    private static final String[] COLUMNS = new String[] {
        "MetadataEntryID", "EditMaskID", "Value",
    };
}
