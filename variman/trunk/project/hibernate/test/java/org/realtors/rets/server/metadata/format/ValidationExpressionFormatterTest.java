/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.ArrayList;
import java.util.List;

import org.realtors.rets.server.metadata.ValidationExpression;
import org.realtors.rets.server.metadata.ValidationExpressionTypeEnum;

public class ValidationExpressionFormatterTest extends FormatterTestCase
{
    protected List getData()
    {
        List validationExpressions = new ArrayList();
        ValidationExpression validationExpression = new ValidationExpression();
        validationExpression.setValidationExpressionID("LD_DATE");
        validationExpression.setValidationExpressionType(
            ValidationExpressionTypeEnum.SET);
        validationExpression.setValue("LD=.TODAY.");
        validationExpressions.add(validationExpression);
        return validationExpressions;
    }

    protected String[] getLevels()
    {
        return new String[] {"Property"};
    }

    protected MetadataFormatter getCompactFormatter()
    {
        return new CompactValidationExpressionFormatter();
    }

    protected String getExpectedCompact()
    {
        return
            "<METADATA-VALIDATION_EXPRESSION Resource=\"Property\" " +
            "Version=\"" + VERSION + "\" Date=\"" + DATE + "\">\n" +

            "<COLUMNS>\tValidationExpressionID\tValidationExpressionType\t" +
            "Value\tVersion\tDate\t</COLUMNS>\n" +

            "<DATA>\tLD_DATE\tSET\tLD=.TODAY." + VERSION_DATE + "\t</DATA>\n" +

            "</METADATA-VALIDATION_EXPRESSION>\n";
    }

    protected String getExpectedCompactRecursive()
    {
        return
            "<METADATA-VALIDATION_EXPRESSION Resource=\"Property\" " +
            "Version=\"" + VERSION + "\" Date=\"" + DATE + "\">\n" +

            "<COLUMNS>\tValidationExpressionID\tValidationExpressionType\t" +
            "Value\tVersion\tDate\t</COLUMNS>\n" +

            "<DATA>\tLD_DATE\tSET\tLD=.TODAY." + VERSION_DATE + "\t</DATA>\n" +

            "</METADATA-VALIDATION_EXPRESSION>\n";
    }
}
