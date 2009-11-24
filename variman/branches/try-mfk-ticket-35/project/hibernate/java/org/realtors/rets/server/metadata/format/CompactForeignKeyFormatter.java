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

import org.realtors.rets.client.RetsVersion;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.types.MForeignKey;
import org.realtors.rets.common.util.DataRowBuilder;
import org.realtors.rets.common.util.TagBuilder;

public class CompactForeignKeyFormatter extends MetadataFormatter
{
    public void format(FormatterContext context, Collection<MetaObject> foreignKeys,
                       String[] levels)
    {
        RetsVersion retsVersion = context.getRetsVersion();
        if (foreignKeys.size() == 0)
        {
            return;
        }
        TagBuilder tag;
        
        // 1.7.2 DTD makes changes.
        if (retsVersion.equals(RetsVersion.RETS_1_0) || retsVersion.equals(RetsVersion.RETS_1_5) ||
                retsVersion.equals(RetsVersion.RETS_1_7))
        {
            /*
             * RETS 1.5 spec indicates that Version and Date should be
             * attributes of the METADATA-FOREIGNKEYS tag. However, the 1.5 DTD
             * does not list these in the attribute list.
             */
            tag = new TagBuilder(context.getWriter(),
                                "METADATA-FOREIGNKEYS")
                                .beginContentOnNewLine()
                                .appendColumns(COLUMNS);
        }
        else
        {
            // TODO: RETS 1.7.2 spec says tag should be METADATA-FOREIGN_KEYS
            // instead of METADATA-FOREIGNKEYS.
            tag = new TagBuilder(context.getWriter(),
                                "METADATA-FOREIGNKEYS")
                                .appendAttribute("Version", context.getVersion())
                                .appendAttribute("Date", context.getDate(), context.getRetsVersion())
                                .beginContentOnNewLine()
                                .appendColumns(COLUMNS);
        }
        for (Iterator<?> iterator = foreignKeys.iterator(); iterator.hasNext();)
        {
            MForeignKey foreignKey = (MForeignKey) iterator.next();
            appendDataRow(context, foreignKey);
        }
        tag.close();
    }

    private void appendDataRow(FormatterContext context, MForeignKey foreignKey)
    {
        DataRowBuilder row = new DataRowBuilder(context.getWriter());
        row.begin();
   
        row.append(foreignKey.getForeignKeyID());
        
        row.append(foreignKey.getParentResourceID());
        row.append(foreignKey.getParentClassID());
        row.append(foreignKey.getParentSystemName());
        
        row.append(foreignKey.getChildResourceID());
        row.append(foreignKey.getChildClassID());
        row.append(foreignKey.getChildSystemName());
        
        // 1.7.2
        row.append(foreignKey.getConditionalParentField());
        row.append(foreignKey.getConditionalParentValue());
        
        row.end();
    }

    // FIXME: MetaObject.getAttributeNames() but takes a RetsVersion so the
    // correct attribute names are returned.
    private static final String[] COLUMNS = new String[] {
        "ForeignKeyID", "ParentResourceID", "ParentClassID", "ParentSystemName",
        "ChildResourceID", "ChildClassID", "ChildSystemName",
        // 1.7.2
        "ConditionalParentField", "ConditionalParentValue",
    };
}
