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
import java.util.Set;

import org.realtors.rets.client.RetsVersion;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.util.DataRowBuilder;
import org.realtors.rets.common.util.TagBuilder;
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
        //	columnNames = ((MClass) classes.iterator().next()).getColumnNames();
        //	tag.appendColumns(columnNames);
    	//}
 
      	tag.appendColumns(COLUMNS);

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
        RetsVersion retsVersion = context.getRetsVersion();
        
        row.begin();
        row.append(clazz.getClassName());
        row.append(clazz.getStandardName());
        row.append(clazz.getVisibleName());
        row.append(clazz.getDescription());
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

    private String [] columnNames;
    
    private static final String[] COLUMNS = {
        "ClassName", "StandardName", "VisibleName", "Description",
        "TableVersion", "TableDate", "UpdateVersion", "UpdateDate",
    	"ClassTimeStamp", "DeletedFlagField", "DeletedFlagValue", "HasKeyIndex",
    };
}
