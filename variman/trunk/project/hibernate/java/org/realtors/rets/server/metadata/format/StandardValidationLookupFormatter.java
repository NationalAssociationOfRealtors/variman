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

import org.realtors.rets.server.metadata.ValidationLookup;

public class StandardValidationLookupFormatter extends BaseStandardFormatter
{
    public void format(FormatterContext context, Collection validationLookups,
                       String[] levels)
    {
        PrintWriter out = context.getWriter();
        TagBuilder metadata = new TagBuilder(out, "METADATA-VALIDATION_LOOKUP")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate())
            .beginContentOnNewLine();

        for (Iterator i = validationLookups.iterator(); i.hasNext();)
        {
            ValidationLookup validationLookup = (ValidationLookup) i.next();
            TagBuilder tag = new TagBuilder(out, "ValidationLookupType")
                .beginContentOnNewLine();

            TagBuilder.simpleTag(out, "ValidationLookupName",
                                 validationLookup.getValidationLookupName());
            TagBuilder.simpleTag(out, "Parent1Field",
                                 validationLookup.getParent1Field());
            TagBuilder.simpleTag(out, "Parent2Field",
                                 validationLookup.getParent2Field());
            formatVersionDateTags(context, VERSION_DATE_TAGS);

            if (context.isRecursive())
            {
                context.format(validationLookup.getValidationLookupTypes(),
                               validationLookup.getPathAsArray());
            }

            tag.close();
        }

        metadata.close();
    }

    private static final String[] VERSION_DATE_TAGS = {"ValidationLookupType"};
}
