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
import java.io.PrintWriter;

import org.realtors.rets.server.metadata.LookupType;

public class StandardLookupTypeFormatter extends BaseStandardFormatter
{
    public void format(FormatterContext context, Collection lookupTypes,
                       String[] levels)
    {
        PrintWriter out = context.getWriter();
        TagBuilder metadata = new TagBuilder(out, "METADATA-LOOKUP_TYPE")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("Lookup", levels[LOOKUP_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate())
            .beginContentOnNewLine();

        for (Iterator i = lookupTypes.iterator(); i.hasNext();)
        {
            LookupType lookupType = (LookupType) i.next();
            TagBuilder tag = new TagBuilder(out, "Lookup")
                .beginContentOnNewLine();

            TagBuilder.simpleTag(out, "LongValue", lookupType.getLongValue());
            TagBuilder.simpleTag(out, "ShortValue", lookupType.getShortValue());
            TagBuilder.simpleTag(out, "Value", lookupType.getValue());

            tag.close();
        }

        metadata.close();
    }
}
