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

import org.apache.commons.lang.StringUtils;

import org.realtors.rets.client.RetsVersion;
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.MetadataType;
import org.realtors.rets.common.metadata.types.MValidationLookup;
import org.realtors.rets.common.util.TagBuilder;

public class StandardValidationLookupFormatter extends BaseStandardFormatter
{
    public void format(FormatterContext context, Collection<MetaObject> validationLookups,
                       String[] levels)
    {
        RetsVersion retsVersion = context.getRetsVersion();
        PrintWriter out = context.getWriter();
        
        TagBuilder metadata = new TagBuilder(out, "METADATA-VALIDATION_LOOKUP")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate(), context.getRetsVersion())
            .beginContentOnNewLine();

        for (Iterator<?> i = validationLookups.iterator(); i.hasNext();)
        {
            MValidationLookup validationLookup = (MValidationLookup) i.next();
            TagBuilder tag = new TagBuilder(out, "ValidationLookupType")
                .beginContentOnNewLine();

            if (!retsVersion.equals(RetsVersion.RETS_1_0) && !retsVersion.equals(RetsVersion.RETS_1_5))
            {
                // Added 1.7 DTD
                TagBuilder.simpleTag(out, "MetadataEntryID", 
                                validationLookup.getMetadataEntryID());
            }
            TagBuilder.simpleTag(out, "ValidationLookupName",
                                 validationLookup.getValidationLookupName());
            TagBuilder.simpleTag(out, "Parent1Field",
                                 validationLookup.getParent1Field());
            TagBuilder.simpleTag(out, "Parent2Field",
                                 validationLookup.getParent2Field());
            // TODO: Check if these are suppose to be Version and Date or
            // ValidationLookupTypeVersion and ValidationLookupTypeDate.
            formatVersionDateTags(context, VERSION_DATE_TAGS);

            if (context.isRecursive())
            {
                String[] path = StringUtils.split(validationLookup.getPath(), ":");
                context.format(validationLookup.getChildren(MetadataType.VALIDATION_LOOKUP_TYPE), path);
            }

            tag.close();
        }

        metadata.close();
    }

    private static final String[] VERSION_DATE_TAGS = {"ValidationLookupType"};
}
