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
import java.io.PrintWriter;

import org.realtors.rets.server.metadata.Update;

public class StandardUpdateFormatter extends BaseStandardFormatter
{
    public void format(FormatterContext context, Collection updates,
                       String[] levels)
    {
        PrintWriter out = context.getWriter();
        TagBuilder metadata = new TagBuilder(out, "METADATA-UPDATE")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("Class", levels[CLASS_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate())
            .beginContentOnNewLine();

        for (Iterator i = updates.iterator(); i.hasNext();)
        {
            Update update = (Update) i.next();
            TagBuilder tag = new TagBuilder(out, "UpdateType")
                .beginContentOnNewLine();

            TagBuilder.simpleTag(out, "UpdateName", update.getUpdateName());
            TagBuilder.simpleTag(out, "Description", update.getDescription());
            TagBuilder.simpleTag(out, "KeyField", update.getKeyField());
            formatVersionDateTags(context, VERSION_DATE_TAGS);

            if (context.isRecursive())
            {
                context.format(update.getUpdateTypes(),
                               update.getPathAsArray());
            }

            tag.close();
        }

        metadata.close();
    }

    private static final String[] VERSION_DATE_TAGS = {"UpdateType"};
}
