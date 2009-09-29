/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004-2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.Collection;
import java.util.Iterator;
import java.io.PrintWriter;

import org.realtors.rets.client.RetsVersion;
import org.realtors.rets.common.util.TagBuilder;
import org.realtors.rets.server.metadata.MClass;

public class StandardClassFormatter extends BaseStandardFormatter
{
    public void format(FormatterContext context, Collection classes,
                       String[] levels)
    {
        RetsVersion retsVersion = context.getRetsVersion();
        PrintWriter out = context.getWriter();
        
        TagBuilder metadata = new TagBuilder(out, "METADATA-CLASS")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate(), context.getRetsVersion())
            .beginContentOnNewLine();

        for (Iterator i = classes.iterator(); i.hasNext();)
        {
            MClass clazz = (MClass) i.next();
            TagBuilder tag = new TagBuilder(out, "Class")
                .beginContentOnNewLine();

            TagBuilder.simpleTag(out, "ClassName", clazz.getClassName());
            TagBuilder.simpleTag(out, "StandardName", clazz.getStandardName());
            TagBuilder.simpleTag(out, "VisibleName", clazz.getVisibleName());
            TagBuilder.simpleTag(out, "Description", clazz.getDescription());
            formatVersionDateTags(context, VERSION_DATE_TAGS);
            
            if (!retsVersion.equals(RetsVersion.RETS_1_0) && !retsVersion.equals(RetsVersion.RETS_1_5))
            {
                // Added in 1.7 DTD
                TagBuilder.simpleTag(out, "ClassTimeStamp", clazz.getClassTimeStamp());
                TagBuilder.simpleTag(out, "DeletedFlagField", clazz.getDeletedFlagField());
                TagBuilder.simpleTag(out, "DeletedFlagValue", clazz.getDeletedFlagValue());
 
                if (!retsVersion.equals(RetsVersion.RETS_1_7))
                {
                    // Added in 1.7.2 DTD
                    TagBuilder.simpleTag(out, "HasKeyIndex", clazz.getHasKeyIndex());
                }
            }

            if (context.isRecursive())
            {
                String[] path = clazz.getPathAsArray();
                context.format(clazz.getTables(), path);
                context.format(clazz.getUpdates(), path);
            }

            tag.close();
        }

        metadata.close();
    }

    private static final String[] VERSION_DATE_TAGS = {
        "Table", "Update"
    };
}
