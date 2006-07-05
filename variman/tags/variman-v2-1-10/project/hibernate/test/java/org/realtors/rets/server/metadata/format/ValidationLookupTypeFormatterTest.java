/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.ArrayList;
import java.util.List;

import org.realtors.rets.server.metadata.ValidationLookupType;

public class ValidationLookupTypeFormatterTest extends FormatterTestCase
{
    protected List getData()
    {
        List validationLookupTypes = new ArrayList();
        ValidationLookupType validationLookupType = new ValidationLookupType();
        validationLookupType.setValidText("135");
        validationLookupType.setParent1Value("AREA2");
        validationLookupType.setParent2Value(null);
        validationLookupTypes.add(validationLookupType);
        return validationLookupTypes;
    }

    protected String[] getLevels()
    {
        return new String[] {"Property", "School"};
    }

    protected MetadataFormatter getCompactFormatter()
    {
        return new CompactValidationLookupTypeFormatter();
    }

    protected MetadataFormatter getStandardFormatter()
    {
        return new StandardValidationLookupTypeFormatter();
    }

    protected String getExpectedCompact()
    {
        return
            "<METADATA-VALIDATION_LOOKUP_TYPE Resource=\"Property\" " +
            "ValidationLookup=\"School\" Version=\"" + VERSION + "\" " +
            "Date=\"" + DATE + "\">\n" +

            "<COLUMNS>\tValidText\tParent1Value\tParent2Value\t</COLUMNS>\n" +

            "<DATA>\t135\tAREA2\t\t</DATA>\n" +

            "</METADATA-VALIDATION_LOOKUP_TYPE>\n";
    }

    protected String getExpectedCompactRecursive()
    {
        return getExpectedCompact();
    }

    protected String getExpectedStandard()
    {
        return
            "<METADATA-VALIDATION_LOOKUP_TYPE Resource=\"Property\" " +
            "ValidationLookup=\"School\" Version=\"" + VERSION + "\" " +
            "Date=\"" + DATE + "\">" + EOL +
            "<ValidationLookup>" + EOL +
            "<ValidText>135</ValidText>" + EOL +
            "<Parent1Value>AREA2</Parent1Value>" + EOL +
            "<Parent2Value></Parent2Value>" + EOL +
            "</ValidationLookup>" + EOL +
            "</METADATA-VALIDATION_LOOKUP_TYPE>" + EOL;
    }

    protected String getExpectedStandardRecursive()
    {
        return getExpectedStandard();
    }
}