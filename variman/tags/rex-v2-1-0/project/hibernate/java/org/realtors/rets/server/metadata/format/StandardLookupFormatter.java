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

import org.realtors.rets.server.metadata.Lookup;

public class StandardLookupFormatter extends BaseStandardFormatter
{
    public void format(FormatterContext context, Collection lookups,
                       String[] levels)
    {
        PrintWriter out = context.getWriter();
        TagBuilder metadata = new TagBuilder(out, "METADATA-LOOKUP")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate())
            .beginContentOnNewLine();

        for (Iterator i = lookups.iterator(); i.hasNext();)
        {
            Lookup lookup = (Lookup) i.next();
            TagBuilder tag = new TagBuilder(out, "LookupType")
                .beginContentOnNewLine();

            TagBuilder.simpleTag(out, "LookupName", lookup.getLookupName());
            TagBuilder.simpleTag(out, "VisibleName", lookup.getVisibleName());
            formatVersionDateTags(context, VERSION_DATE_TAGS);

            if (context.isRecursive())
            {
                context.format(lookup.getLookupTypes(),
                               lookup.getPathAsArray());
            }

            tag.close();
        }

        metadata.close();
    }

    private static final String[] VERSION_DATE_TAGS = {"LookupType"};
}
