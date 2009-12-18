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

import org.realtors.rets.client.RetsVersion;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataType;
import org.realtors.rets.common.metadata.types.MClass;
import org.realtors.rets.common.util.DataRowBuilder;
import org.realtors.rets.common.util.TagBuilder;

public class CompactClassFormatter extends MetadataFormatter
{
    public void format(FormatterContext context, Collection<MetaObject> classes,
                       String[] levels)
    {
        if (classes.size() == 0)
        {
            return;
        }
        RetsVersion retsVersion = context.getRetsVersion();
        
        TagBuilder tag = new TagBuilder(context.getWriter(), "METADATA-CLASS")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate(), retsVersion)
            .beginContentOnNewLine();
        // FIXME: Ticket #23 - Support X- extensions.
        // In order to support X- extensions, we need to fetch the column names and then
        // use them in order to fetch their values.
        //if (classes.iterator().hasNext())
        //{
        //    columnNames = ((MClass) classes.iterator().next()).getColumnNames();
        //    tag.appendColumns(columnNames);
        //}
 
        tag.appendColumns(COLUMNS);

        for (Iterator<?> iterator = classes.iterator(); iterator.hasNext();)
        {
            MClass clazz = (MClass) iterator.next();
            appendDataRow(context, clazz);
        }
        tag.close();

        if (context.isRecursive())
        {
            for (Iterator<?> iterator = classes.iterator(); iterator.hasNext();)
            {
                MClass clazz = (MClass) iterator.next();
                String[] path = StringUtils.split(clazz.getPath(), ":");
                context.format(clazz.getChildren(MetadataType.TABLE), path);
                context.format(clazz.getChildren(MetadataType.UPDATE), path);
            }
        }
    }

    // TODO: May be able to replace with a method similar to
    // org.realtors.rets.common.metadata.formatDataRow(MetaObject).
    private void appendDataRow(FormatterContext context, MClass clazz)
    {
        DataRowBuilder row = new DataRowBuilder(context.getWriter());
        RetsVersion retsVersion = context.getRetsVersion();
        
        row.begin();
        row.append(clazz.getClassName());
        row.append(clazz.getStandardName());
        row.append(clazz.getVisibleName());
        row.append(clazz.getDescription());
        // FIXME: The actual table and update version/date pairs are available
        // in the common MClass class. Consider using those instead of the
        // context's version and date.
        // Table version and date
        row.append(context.getVersion());
        row.append(context.getDate(), retsVersion);
        // Update version and date
        row.append(context.getVersion());
        row.append(context.getDate(), retsVersion);
        // 1.7.2
        row.append(clazz.getClassTimeStamp());
        row.append(clazz.getDeletedFlagField());
        row.append(clazz.getDeletedFlagValue());
        row.append(clazz.getHasKeyIndex());
        row.end();
    }

    // FIXME: MetaObject.getAttributeNames() but takes a RetsVersion so the
    // correct attribute names are returned.
    private static final String[] COLUMNS = {
        "ClassName", "StandardName", "VisibleName", "Description",
        "TableVersion", "TableDate", "UpdateVersion", "UpdateDate",
        "ClassTimeStamp", "DeletedFlagField", "DeletedFlagValue", "HasKeyIndex",
    };
}
