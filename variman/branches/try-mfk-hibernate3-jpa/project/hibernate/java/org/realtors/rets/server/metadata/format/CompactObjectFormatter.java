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
import org.realtors.rets.server.metadata.MObject;
import org.realtors.rets.server.metadata.ObjectTypeEnum;

public class CompactObjectFormatter extends MetadataFormatter
{
    public void format(FormatterContext context, Collection objects,
                       String[] levels)
    {
        if (objects.size() == 0)
        {
            return;
        }
        TagBuilder tag = new TagBuilder(context.getWriter(), "METADATA-OBJECT")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate(), context.getRetsVersion())
            .beginContentOnNewLine()
            .appendColumns(COLUMNS);
        for (Iterator iterator = objects.iterator(); iterator.hasNext();)
        {
            MObject object = (MObject) iterator.next();
            appendDataRow(context, object);
        }
        tag.close();
    }

    private void appendDataRow(FormatterContext context, MObject object)
    {
        DataRowBuilder row = new DataRowBuilder(context.getWriter());
        row.begin();
        row.append(object.getMetadataEntryID());
        row.append(ObjectTypeEnum.toString(object.getObjectType()));
        row.append(object.getMimeType());
        row.append(object.getVisibleName());
        row.append(object.getDescription());
        row.append(object.getObjectTimeStamp());
        row.append(object.getObjectCount());
        row.end();
    }

    private static final String[] COLUMNS = new String[] {
        "MetadataEntryID", "ObjectType", "MimeType", "VisibleName", 
        "Description", "ObjectTimeStamp", "ObjectCount",
    };
}
