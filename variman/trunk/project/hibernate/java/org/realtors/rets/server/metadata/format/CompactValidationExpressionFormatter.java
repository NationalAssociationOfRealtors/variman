/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.util.List;

import org.realtors.rets.server.metadata.ValidationExpression;

public class CompactValidationExpressionFormatter
    extends ValidationExpressionFormatter
{
    public void format(PrintWriter out, List validationExpressions)
    {
        if (validationExpressions.size() == 0)
        {
            return;
        }
        TagBuilder tag = new TagBuilder(out);
        tag.begin("METADATA-VALIDATION_EXPRESSION");
        tag.appendAttribute("Resource", mResourceName);
        tag.appendAttribute("Version", mVersion);
        tag.appendAttribute("Date", mDate);
        tag.endAttributes();
        tag.appendColumns(COLUMNS);
        for (int i = 0; i < validationExpressions.size(); i++)
        {
            ValidationExpression validationExpression =
                (ValidationExpression) validationExpressions.get(i);
            appendDataRow(out, validationExpression);
            
        }
        tag.end();
    }

    private void appendDataRow(PrintWriter out,
                               ValidationExpression validationExpression)
    {
        DataRowBuilder row = new DataRowBuilder(out);
        row.begin();
        row.append(validationExpression.getValidationExpressionID());
        row.append(validationExpression.getValidationExpressionType());
        row.append(validationExpression.getValue());
        row.append(mVersion);
        row.append(mDate);
        row.end();
    }

    private static final String[] COLUMNS = {
        "ValidationExpressionID", "ValidationExpressionType", "Value",
        "Version", "Date",
    };
}
