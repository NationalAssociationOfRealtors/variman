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

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

import org.realtors.rets.server.metadata.Resource;

public class StandardResourceFormatter extends BaseStandardFormatter
{
    public void format(FormatterContext context, Collection resources,
                       String[] levels)
    {
        PrintWriter out = context.getWriter();

        TagBuilder metadata = new TagBuilder(out, "METADATA-RESOURCE")
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate())
            .beginContentOnNewLine();

        for (Iterator i = resources.iterator(); i.hasNext();)
        {
            Resource resource = (Resource) i.next();
            TagBuilder tag = new TagBuilder(out, "Resource")
                .beginContentOnNewLine();

            TagBuilder.simpleTag(out, "ResourceID", resource.getResourceID());
            TagBuilder.simpleTag(out, "StandardName",
                                 resource.getStandardName());
            TagBuilder.simpleTag(out, "VisibleName", resource.getVisibleName());
            TagBuilder.simpleTag(out, "Description", resource.getDescription());
            TagBuilder.simpleTag(out, "KeyField", resource.getKeyField());
            TagBuilder.simpleTag(out, "ClassCount",
                                 resource.getClasses().size());
            formatVersionDateTags(context, VERSION_DATE_TAGS);

            if (context.isRecursive())
            {
                String[] path = resource.getPathAsArray();
                context.format(resource.getClasses(), path);
                context.format(resource.getObjects(), path);
                context.format(resource.getSearchHelps(), path);
                context.format(resource.getEditMasks(), path);
                context.format(resource.getLookups(), path);
                context.format(resource.getUpdateHelps(), path);
                context.format(resource.getValidationLookups(), path);
                context.format(resource.getValidationExpressions(), path);
                context.format(resource.getValidationExternals(), path);
            }

            tag.close();
        }

        metadata.close();
    }

    private static final String[] VERSION_DATE_TAGS = {
        "Class", "Object", "SearchHelp", "EditMask", "Lookup", "UpdateHelp",
        "ValidationExpression", "ValidationLookup", "ValidationExternal"
    };
}
