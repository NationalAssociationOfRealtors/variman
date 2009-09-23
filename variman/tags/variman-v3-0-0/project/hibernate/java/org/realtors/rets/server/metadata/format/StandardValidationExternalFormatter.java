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
import org.realtors.rets.server.metadata.ValidationExternal;

public class StandardValidationExternalFormatter extends BaseStandardFormatter
{
    public void format(FormatterContext context, Collection validationExternals,
                       String[] levels)
    {
        RetsVersion retsVersion = context.getRetsVersion();
        PrintWriter out = context.getWriter();
        TagBuilder metadata = new TagBuilder(out,
                                             "METADATA-VALIDATION_EXTERNAL")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate(), context.getRetsVersion())
            .beginContentOnNewLine();

        for (Iterator i = validationExternals.iterator(); i.hasNext();)
        {
            ValidationExternal validationExternal =
                (ValidationExternal) i.next();
            TagBuilder tag = new TagBuilder(out, "ValidationExternalType")
                .beginContentOnNewLine();

            if (!retsVersion.equals(RetsVersion.RETS_1_0) && !retsVersion.equals(RetsVersion.RETS_1_5))
            {
                // Added 1.7 DTD
                TagBuilder.simpleTag(out, "MetadataEntryID", 
            		validationExternal.getMetadataEntryID());
            }
            TagBuilder.simpleTag(
                out, "ValidationExternalName",
                validationExternal.getValidationExternalName());
            TagBuilder.simpleTag(out, "SearchResource",
                                 validationExternal.getSearchResource());
            TagBuilder.simpleTag(out, "SearchClass",
                                 validationExternal.getSearchClass());
            formatVersionDateTags(context, VERSION_DATE_TAGS);

            if (context.isRecursive())
            {
                context.format(validationExternal.getValidationExternalTypes(),
                               validationExternal.getPathAsArray());
            }

            tag.close();
        }

        metadata.close();
    }

    private static final String[] VERSION_DATE_TAGS = {
        "ValidationExternalType"
    };
}
