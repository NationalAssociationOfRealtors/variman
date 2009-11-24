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
import org.realtors.rets.common.metadata.types.MValidationExpression;
import org.realtors.rets.common.util.TagBuilder;

public class StandardValidationExpressionFormatter extends BaseStandardFormatter
{
    public void format(FormatterContext context,
                       Collection<MetaObject> validationExpressions, String[] levels)
    {
        RetsVersion retsVersion = context.getRetsVersion();
        PrintWriter out = context.getWriter();
        TagBuilder metadata = new TagBuilder(out,
                                             "METADATA-VALIDATION_EXPRESSION")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate(), context.getRetsVersion())
            .beginContentOnNewLine();

        for (Iterator<?> i = validationExpressions.iterator(); i.hasNext();)
        {
            MValidationExpression validationExpression =
                (MValidationExpression) i.next();
            TagBuilder tag = new TagBuilder(out, "ValidationExpression")
                .beginContentOnNewLine();

            if (!retsVersion.equals(RetsVersion.RETS_1_0) && !retsVersion.equals(RetsVersion.RETS_1_5))
            {
                // Added 1.7 DTD
                TagBuilder.simpleTag(out, "MetadataEntryID", 
                            validationExpression.getMetadataEntryID());
            }
            TagBuilder.simpleTag(
                out, "ValidationExpressionID",
                validationExpression.getValidationExpressionID());
            TagBuilder.simpleTag(
                out, "ValidationExpressionType",
                validationExpression.getValidationExpressionType());
            TagBuilder.simpleTag(out, "Value", validationExpression.getValue());

            tag.close();
        }

        metadata.close();
    }
}
