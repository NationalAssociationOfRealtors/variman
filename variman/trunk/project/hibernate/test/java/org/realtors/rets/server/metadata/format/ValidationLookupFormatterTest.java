/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.ArrayList;
import java.util.List;

import org.realtors.rets.server.metadata.ValidationLookup;
import org.realtors.rets.server.metadata.ValidationLookupType;

public class ValidationLookupFormatterTest extends FormatterTestCase
{
    protected List getData()
    {
        List validationLookups = new ArrayList();
        ValidationLookup validationLookup = new ValidationLookup();
        validationLookup.setValidationLookupName("School");
        validationLookup.setParent1Field("Area");
        validationLookup.setParent2Field("Subarea");
        validationLookup.addValidationLookupType(new ValidationLookupType(1));
        validationLookups.add(validationLookup);
        return validationLookups;
    }

    protected String[] getLevels()
    {
        return new String[] {"Property"};
    }

    protected MetadataFormatter getCompactFormatter()
    {
        return new CompactValidationLookupFormatter();
    }

    protected String getExpectedCompact()
    {
        return 
            "<METADATA-VALIDATION_LOOKUP Resource=\"Property\" " +
            "Version=\"" + VERSION + "\" Date=\"" + DATE + "\">\n" +

            "<COLUMNS>\tValidationLookupName\tParent1Field\tParent2Field\t" +
            "Version\tDate\t</COLUMNS>\n" +

            "<DATA>\tSchool\tArea\tSubarea" + VERSION_DATE + "\t</DATA>\n" +

            "</METADATA-VALIDATION_LOOKUP>\n";
    }

    protected String getExpectedCompactRecursive()
    {
        return
            "<METADATA-VALIDATION_LOOKUP Resource=\"Property\" " +
            "Version=\"" + VERSION + "\" Date=\"" + DATE + "\">\n" +

            "<COLUMNS>\tValidationLookupName\tParent1Field\tParent2Field\t" +
            "Version\tDate\t</COLUMNS>\n" +

            "<DATA>\tSchool\tArea\tSubarea" + VERSION_DATE + "\t</DATA>\n" +

            "</METADATA-VALIDATION_LOOKUP>\n" +

            ValidationLookupType.TABLE + "\n";
    }
}
