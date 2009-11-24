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

import org.realtors.rets.common.metadata.MetaObject;
import org.realtors.rets.common.metadata.types.MValidationExpression;
import org.realtors.rets.common.util.DataRowBuilder;
import org.realtors.rets.common.util.TagBuilder;

public class CompactValidationExpressionFormatter extends MetadataFormatter
{
    public void format(FormatterContext context,
                       Collection<MetaObject> validationExpressions, String[] levels)
    {
        if (validationExpressions.size() == 0)
        {
            return;
        }
        TagBuilder tag = new TagBuilder(context.getWriter());
        tag.begin("METADATA-VALIDATION_EXPRESSION");
        tag.appendAttribute("Resource", levels[RESOURCE_LEVEL]);
        tag.appendAttribute("Version", context.getVersion());
        tag.appendAttribute("Date", context.getDate(), context.getRetsVersion());
        tag.endAttributes();
        tag.appendColumns(COLUMNS);
        for (Iterator<?> i = validationExpressions.iterator(); i.hasNext();)
        {
            MValidationExpression validationExpression =
                (MValidationExpression) i.next();
            appendDataRow(context, validationExpression);

        }
        tag.end();
    }

    private void appendDataRow(FormatterContext context,
            MValidationExpression validationExpression)
    {
        DataRowBuilder row = new DataRowBuilder(context.getWriter());
        row.begin();
        row.append(validationExpression.getMetadataEntryID());
        row.append(validationExpression.getValidationExpressionID());
        row.append(validationExpression.getValidationExpressionType());
        row.append(validationExpression.getValue());
        // FIXME: The RETS spec does not show that version and date are valid
        // columns. They are valid tag attributes. ValidationExternal has them
        // but ValidationExpression does not.
        row.append(context.getVersion());
        row.append(context.getDate(), context.getRetsVersion());
        row.end();
    }

    // FIXME: MetaObject.getAttributeNames() but takes a RetsVersion so the
    // correct attribute names are returned.
    // TODO: Remove version and date if they are not suppose to be here.
    private static final String[] COLUMNS = {
        "MetadataEntryID", "ValidationExpressionID", "ValidationExpressionType", "Value",
        "Version", "Date",
    };
}
