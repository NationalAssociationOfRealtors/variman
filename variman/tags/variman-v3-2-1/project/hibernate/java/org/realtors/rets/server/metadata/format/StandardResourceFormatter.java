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

import org.apache.commons.lang.StringUtils;

import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataType;
import org.realtors.rets.common.metadata.types.MResource;
import org.realtors.rets.common.util.TagBuilder;

public class StandardResourceFormatter extends BaseStandardFormatter
{
    public void format(FormatterContext context, Collection<MetaObject> resources,
                       String[] levels)
    {
        PrintWriter out = context.getWriter();

        TagBuilder metadata = new TagBuilder(out, "METADATA-RESOURCE")
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate(), context.getRetsVersion())
            .beginContentOnNewLine();

        for (Iterator<?> i = resources.iterator(); i.hasNext();)
        {
            MResource resource = (MResource) i.next();
            TagBuilder tag = new TagBuilder(out, "Resource")
                .beginContentOnNewLine();

            TagBuilder.simpleTag(out, "ResourceID", resource.getResourceID());
            TagBuilder.simpleTag(out, "StandardName",
                                 resource.getStandardName());
            TagBuilder.simpleTag(out, "VisibleName", resource.getVisibleName());
            TagBuilder.simpleTag(out, "Description", resource.getDescription());
            TagBuilder.simpleTag(out, "KeyField", resource.getKeyField());
            TagBuilder.simpleTag(out, "ClassCount", resource.getClassCount());
            // FIXME: The actual version/date pairs are available in the common
            // MResource class. Consider using those instead of the context's
            // version and date.
            formatVersionDateTags(context, VERSION_DATE_TAGS);

            if (context.isRecursive())
            {
                String[] path = StringUtils.split(resource.getPath(), ":");
                context.format(resource.getChildren(MetadataType.CLASS), path);
                context.format(resource.getChildren(MetadataType.OBJECT), path);
                context.format(resource.getChildren(MetadataType.SEARCH_HELP), path);
                context.format(resource.getChildren(MetadataType.EDITMASK), path);
                context.format(resource.getChildren(MetadataType.LOOKUP), path);
                context.format(resource.getChildren(MetadataType.UPDATE_HELP), path);
                context.format(resource.getChildren(MetadataType.VALIDATION_LOOKUP), path);
                context.format(resource.getChildren(MetadataType.VALIDATION_EXPRESSION), path);
                context.format(resource.getChildren(MetadataType.VALIDATION_EXTERNAL), path);
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
