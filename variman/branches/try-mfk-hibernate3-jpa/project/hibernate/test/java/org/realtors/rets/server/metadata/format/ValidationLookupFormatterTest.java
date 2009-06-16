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
        validationLookup.setMetadataEntryID("SchoolLookup");
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

    protected MetadataFormatter getStandardFormatter()
    {
        return new StandardValidationLookupFormatter();
    }

    protected String getExpectedCompact()
    {
        return 
            "<METADATA-VALIDATION_LOOKUP Resource=\"Property\" " +
            "Version=\"" + VERSION + "\" Date=\"" + DATE + "\">\n" +

            "<COLUMNS>\tMetadataEntryID\tValidationLookupName\tParent1Field\tParent2Field\t" +
            "Version\tDate\t</COLUMNS>\n" +

            "<DATA>\tSchoolLookup\tSchool\tArea\tSubarea" + VERSION_DATE + "\t</DATA>\n" +

            "</METADATA-VALIDATION_LOOKUP>\n";
    }

    protected String getExpectedCompactRecursive()
    {
        return
            "<METADATA-VALIDATION_LOOKUP Resource=\"Property\" " +
            "Version=\"" + VERSION + "\" Date=\"" + DATE + "\">\n" +

            "<COLUMNS>\tMetadataEntryID\tValidationLookupName\tParent1Field\tParent2Field\t" +
            "Version\tDate\t</COLUMNS>\n" +

            "<DATA>\tSchoolLookup\tSchool\tArea\tSubarea" + VERSION_DATE + "\t</DATA>\n" +

            "</METADATA-VALIDATION_LOOKUP>\n" +

            ValidationLookupType.TABLE + "\n";
    }

    protected String getExpectedStandard()
    {
        return
            "<METADATA-VALIDATION_LOOKUP Resource=\"Property\" " +
            "Version=\"" + VERSION + "\" Date=\"" + DATE + "\">" + EOL +
            "<ValidationLookupType>" + EOL +
            "<MetadataEntryID>SchoolLookup</MetadataEntryID>" + EOL +
            "<ValidationLookupName>School</ValidationLookupName>" + EOL +
            "<Parent1Field>Area</Parent1Field>" + EOL +
            "<Parent2Field>Subarea</Parent2Field>" + EOL +
            "<ValidationLookupTypeVersion>" + VERSION +
            "</ValidationLookupTypeVersion>" + EOL +
            "<ValidationLookupTypeDate>" + DATE +
            "</ValidationLookupTypeDate>" + EOL +
            "</ValidationLookupType>" + EOL +
            "</METADATA-VALIDATION_LOOKUP>" + EOL;
    }

    protected String getExpectedStandardRecursive()
    {
        return
            "<METADATA-VALIDATION_LOOKUP Resource=\"Property\" " +
            "Version=\"" + VERSION + "\" Date=\"" + DATE + "\">" + EOL +
            "<ValidationLookupType>" + EOL +
            "<MetadataEntryID>SchoolLookup</MetadataEntryID>" + EOL +
            "<ValidationLookupName>School</ValidationLookupName>" + EOL +
            "<Parent1Field>Area</Parent1Field>" + EOL +
            "<Parent2Field>Subarea</Parent2Field>" + EOL +
            "<ValidationLookupTypeVersion>" + VERSION +
            "</ValidationLookupTypeVersion>" + EOL +
            "<ValidationLookupTypeDate>" + DATE +
            "</ValidationLookupTypeDate>" + EOL +
            ValidationLookupType.TABLE + EOL +
            "</ValidationLookupType>" + EOL +
            "</METADATA-VALIDATION_LOOKUP>" + EOL;
    }
}
