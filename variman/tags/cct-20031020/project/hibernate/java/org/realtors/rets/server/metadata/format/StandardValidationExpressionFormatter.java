/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.Collection;
import java.util.Iterator;
import java.io.PrintWriter;

import org.realtors.rets.server.metadata.ValidationExpression;

public class StandardValidationExpressionFormatter extends BaseStandardFormatter
{
    public void format(FormatterContext context,
                       Collection validationExpressions, String[] levels)
    {
        PrintWriter out = context.getWriter();
        TagBuilder metadata = new TagBuilder(out,
                                             "METADATA-VALIDATION_EXPRESSION")
            .appendAttribute("Resource", levels[RESOURCE_LEVEL])
            .appendAttribute("Version", context.getVersion())
            .appendAttribute("Date", context.getDate())
            .beginContentOnNewLine();

        for (Iterator i = validationExpressions.iterator(); i.hasNext();)
        {
            ValidationExpression validationExpression =
                (ValidationExpression) i.next();
            TagBuilder tag = new TagBuilder(out, "ValidationExpression")
                .beginContentOnNewLine();

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
