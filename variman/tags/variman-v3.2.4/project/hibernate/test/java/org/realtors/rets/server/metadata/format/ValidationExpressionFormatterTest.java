/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.ArrayList;
import java.util.List;

import org.realtors.rets.common.metadata.types.MValidationExpression;
import org.realtors.rets.server.metadata.ValidationExpressionTypeEnum;

public class ValidationExpressionFormatterTest extends FormatterTestCase
{
    protected List<MValidationExpression> getData()
    {
        List<MValidationExpression> validationExpressions = new ArrayList<MValidationExpression>();
        MValidationExpression validationExpression = new MValidationExpression();
        validationExpression.setMetadataEntryID("ListingDate");
        validationExpression.setValidationExpressionID("LD_DATE");
        validationExpression.setValidationExpressionType(
            ValidationExpressionTypeEnum.SET.toString());
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

    protected MetadataFormatter getStandardFormatter()
    {
        return new StandardValidationExpressionFormatter();
    }

    protected String getExpectedCompact()
    {
        return
            "<METADATA-VALIDATION_EXPRESSION Resource=\"Property\" " +
            "Version=\"" + VERSION + "\" Date=\"" + DATE + "\">\n" +

            "<COLUMNS>\tMetadataEntryID\tValidationExpressionID\tValidationExpressionType\t" +
            "Value\tVersion\tDate\t</COLUMNS>\n" +

            "<DATA>\tListingDate\tLD_DATE\tSET\tLD=.TODAY." + VERSION_DATE + "\t</DATA>\n" +

            "</METADATA-VALIDATION_EXPRESSION>\n";
    }

    protected String getExpectedCompactRecursive()
    {
        return getExpectedCompact();
    }

    protected String getExpectedStandard()
    {
        return
            "<METADATA-VALIDATION_EXPRESSION Resource=\"Property\" " +
            "Version=\"" + VERSION + "\" Date=\"" + DATE + "\">" + EOL +
            "<ValidationExpression>" + EOL +
            "<MetadataEntryID>ListingDate</MetadataEntryID>" + EOL +
            "<ValidationExpressionID>LD_DATE</ValidationExpressionID>" + EOL +
            "<ValidationExpressionType>SET</ValidationExpressionType>" + EOL +
            "<Value>LD=.TODAY.</Value>" + EOL +
            "</ValidationExpression>" + EOL +
            "</METADATA-VALIDATION_EXPRESSION>" + EOL;
    }

    protected String getExpectedStandardRecursive()
    {
        return getExpectedStandard();
    }
}
