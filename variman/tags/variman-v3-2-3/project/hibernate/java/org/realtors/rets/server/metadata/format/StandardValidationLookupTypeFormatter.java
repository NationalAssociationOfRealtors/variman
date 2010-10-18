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
import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.types.MValidationLookupType;
import org.realtors.rets.common.util.TagBuilder;

public class StandardValidationLookupTypeFormatter extends BaseStandardFormatter
{
    public void format(FormatterContext context,
                       Collection<MetaObject> validationLookupTypes, String[] levels)
    {
        RetsVersion retsVersion = context.getRetsVersion();
        PrintWriter out = context.getWriter();
        
        TagBuilder metadata =
            new TagBuilder(out, "METADATA-VALIDATION_LOOKUP_TYPE")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("ValidationLookup",
                             levels[VALIDATION_LOOKUP_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate(), context.getRetsVersion())
            .beginContentOnNewLine();

        for (Iterator<?> i = validationLookupTypes.iterator(); i.hasNext();)
        {
            MValidationLookupType validationLookupType =
                (MValidationLookupType) i.next();
            TagBuilder tag = new TagBuilder(out, "ValidationLookup")
                .beginContentOnNewLine();

            if (!retsVersion.equals(RetsVersion.RETS_1_0) && !retsVersion.equals(RetsVersion.RETS_1_5))
            {
                // Added 1.7 DTD
                TagBuilder.simpleTag(out, "MetadataEntryID", 
                                validationLookupType.getMetadataEntryID());
            }
            TagBuilder.simpleTag(out, "ValidText",
                                 validationLookupType.getValidText());
            TagBuilder.simpleTag(out, "Parent1Value",
                                 validationLookupType.getParent1Value());
            TagBuilder.simpleTag(out, "Parent2Value",
                                 validationLookupType.getParent2Value());

            tag.close();
        }

        metadata.close();
    }
}
