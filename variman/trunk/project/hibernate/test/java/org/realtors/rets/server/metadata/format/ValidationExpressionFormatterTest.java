/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.StringWriter;
import java.io.PrintWriter;

import org.realtors.rets.server.metadata.ValidationExpression;
import org.realtors.rets.server.metadata.ValidationExpressionTypeEnum;

public class ValidationExpressionFormatterTest extends FormatterTestCase
{
    protected void setUp()
    {
        ValidationExpression validationExpression = new ValidationExpression();
        validationExpression.setValidationExpressionID("LD_DATE");
        validationExpression.setValidationExpression(
            ValidationExpressionTypeEnum.SET);
        validationExpression.setValue("LD=.TODAY.");
        mValidationExpressions =
            new ValidationExpression[] {validationExpression};
    }

    private ValidationExpressionFormatter getFormatter(int format)
    {
        ValidationExpressionFormatter formatter =
            ValidationExpressionFormatter.getInstance(format);
        formatter.setVersion("1.00.001", getDate());
        formatter.setResourceName("Property");
        return formatter;
    }

    public void testCompactFormatValidationExpression()
    {
        ValidationExpressionFormatter formatter =
            getFormatter(MetadataFormatter.COMPACT);
        StringWriter formatted = new StringWriter();
        formatter.format(new PrintWriter(formatted), mValidationExpressions);
        assertEquals(
            "<METADATA-VALIDATION_EXPRESSION Resource=\"Property\" " +
            "Version=\"" + VERSION + "\" Date=\"" + DATE + "\">\n" +

            "<COLUMNS>\tValidationExpressionID\tValidationExpressionType\t" +
            "Value\tVersion\tDate\t</COLUMNS>\n" +

            "<DATA>\tLD_DATE\tSET\tLD=.TODAY." + VERSION_DATE + "\t</DATA>\n" +

            "</METADATA-VALIDATION_EXPRESSION>\n",
            formatted.toString());
    }

    private ValidationExpression[] mValidationExpressions;
}
