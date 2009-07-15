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

import org.realtors.rets.common.util.DataRowBuilder;
import org.realtors.rets.common.util.TagBuilder;
import org.realtors.rets.server.metadata.ValidationExpression;

public class CompactValidationExpressionFormatter extends MetadataFormatter
{
    public void format(FormatterContext context,
                       Collection validationExpressions, String[] levels)
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
        for (Iterator i = validationExpressions.iterator(); i.hasNext();)
        {
            ValidationExpression validationExpression =
                (ValidationExpression) i.next();
            appendDataRow(context, validationExpression);

        }
        tag.end();
    }

    private void appendDataRow(FormatterContext context,
                               ValidationExpression validationExpression)
    {
        DataRowBuilder row = new DataRowBuilder(context.getWriter());
        row.begin();
        row.append(validationExpression.getMetadataEntryID());
        row.append(validationExpression.getValidationExpressionID());
        row.append(validationExpression.getValidationExpressionType());
        row.append(validationExpression.getValue());
        row.append(context.getVersion());
        row.append(context.getDate(), context.getRetsVersion());
        row.end();
    }

    private static final String[] COLUMNS = {
        "MetadataEntryID", "ValidationExpressionID", "ValidationExpressionType", "Value",
        "Version", "Date",
    };
}
