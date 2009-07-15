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
/*
 * Variman RETS Server
 *
 * Author: Mark Klein
 * Copyright (c) 2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.realtors.rets.common.util.DataRowBuilder;
import org.realtors.rets.common.util.TagBuilder;
import org.realtors.rets.server.metadata.ForeignKey;
import org.realtors.rets.server.metadata.Table;

public class CompactForeignKeyFormatter extends MetadataFormatter
{
    public void format(FormatterContext context, Collection foreignKeys,
                       String[] levels)
    {
        if (foreignKeys.size() == 0)
        {
            return;
        }
        TagBuilder tag = new TagBuilder(context.getWriter(),
                                        "METADATA-FOREIGN_KEYS")
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate(), context.getRetsVersion())
            .beginContentOnNewLine()
            .appendColumns(COLUMNS);
        for (Iterator iterator = foreignKeys.iterator(); iterator.hasNext();)
        {
            ForeignKey foreignKey = (ForeignKey) iterator.next();
            appendDataRow(context, foreignKey);
        }
        tag.close();
    }

    private void appendDataRow(FormatterContext context, ForeignKey foreignKey)
    {
        DataRowBuilder row = new DataRowBuilder(context.getWriter());
        row.begin();
   
        row.append(foreignKey.getForeignKeyID());
        
        String [] paths = new String[2];
        String name = null;
        
        Table table = foreignKey.getParentTable();
        /*
         *  Decompose the parent table information if one exists.
         */
        if (table != null)
        {
        	paths = StringUtils.split(table.getLevel(), ":");
            name = table.getSystemName();
        }
        row.append(paths[0]);
        row.append(paths[1]);
        row.append(name);
        
        paths[0] = null;
        paths[1] = null;
        name = null;
        
        /*
         * Decompose the child table information if one eixsts.
         */
        table = foreignKey.getChildTable();
        if (table != null)
        {
        	paths = StringUtils.split(table.getLevel(), ":");
            name = table.getSystemName();
        }
    	row.append(paths[0]);
        row.append(paths[1]);
        row.append(name);
        
        // 1.7.2
        row.append(foreignKey.getConditionalParentField());
        row.append(foreignKey.getConditionalParentValue());
        
        row.end();
    }

    private static final String[] COLUMNS = new String[] {
        "ForeignKeyID", "ParentResourceID", "ParentClassID", "ParentSystemName",
        "ChildResourceID", "ChildClassID", "ChildSystemName",
        // 1.7.2
        "ConditionalParentField", "ConditionalParentValue",
    };
}
