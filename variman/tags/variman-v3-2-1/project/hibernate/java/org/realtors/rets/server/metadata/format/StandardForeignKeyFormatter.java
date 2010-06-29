/*
 * Variman RETS Server
 *
 * Author: Mark Klein
 * Copyright (c) 2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

import org.realtors.rets.client.RetsVersion;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.types.MForeignKey;
import org.realtors.rets.common.util.TagBuilder;

public class StandardForeignKeyFormatter extends BaseStandardFormatter
{
    public void format(FormatterContext context, Collection<MetaObject> foreignKeys,
            String[] levels)
    {
        if (foreignKeys.size() == 0)
        {
            return;
        }
        
        RetsVersion retsVersion = context.getRetsVersion();
        PrintWriter out = context.getWriter();
        TagBuilder metadata;
        
        // 1.7.2 DTD makes changes.
        if (retsVersion.equals(RetsVersion.RETS_1_0) || retsVersion.equals(RetsVersion.RETS_1_5) ||
                retsVersion.equals(RetsVersion.RETS_1_7))
        {
            /*
             * RETS 1.5 spec indicates that Version and Date should be
             * attributes of the METADATA-FOREIGN_KEY tag. However, the 1.5 DTD
             * does not list these in the attribute list.
             */
            metadata = new TagBuilder(context.getWriter(),
                                "METADATA-FOREIGN_KEY")
                                .beginContentOnNewLine();
        }
        else
        {
            metadata = new TagBuilder(context.getWriter(),
                                     "METADATA-FOREIGN_KEYS")
                                     .appendAttribute("Version", context.getVersion())
                                     .appendAttribute("Date", context.getDate(), context.getRetsVersion())
                                     .beginContentOnNewLine();
        }
        
        for (Iterator<?> iterator = foreignKeys.iterator(); iterator.hasNext();)
        {
            MForeignKey foreignKey = (MForeignKey) iterator.next();
            
            TagBuilder tag = new TagBuilder(out, "ForeignKey")
                .beginContentOnNewLine();
            
            TagBuilder.simpleTag(out, "ForeignKeyID", foreignKey.getForeignKeyID());
            
            TagBuilder.simpleTag(out, "ParentResourceID", foreignKey.getParentResourceID());
            TagBuilder.simpleTag(out, "ParentClassID", foreignKey.getParentClassID());
            TagBuilder.simpleTag(out, "ParentSystemName", foreignKey.getParentSystemName());

            TagBuilder.simpleTag(out, "ChildResourceID", foreignKey.getChildResourceID());
            TagBuilder.simpleTag(out, "ChildClassID", foreignKey.getChildClassID());
            TagBuilder.simpleTag(out, "ChildSystemName", foreignKey.getChildSystemName());

            if (!retsVersion.equals(RetsVersion.RETS_1_0) && !retsVersion.equals(RetsVersion.RETS_1_5))
            {
                // Added in 1.7 DTD
                TagBuilder.simpleTag(out, "ConditionalParentField", foreignKey.getConditionalParentField());
                TagBuilder.simpleTag(out, "ConditionalParentValue", foreignKey.getConditionalParentValue());
            }
            
            tag.close();
        }
        
        metadata.close();
    }
}
