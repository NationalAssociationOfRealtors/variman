/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.realtors.rets.server.metadata.ValidationLookup;

public class ValidationLookupFormatterTest extends FormatterTestCase
{
    protected void setUp()
    {
        ValidationLookup validationLookup = new ValidationLookup();
        validationLookup.setValidationLookupName("School");
        validationLookup.setParent1Field("Area");
        validationLookup.setParent2Field("Subarea");
        mValidationLookups = new ValidationLookup[] {validationLookup};
    }

    private ValidationLookupFormatter getFormatter(int format)
    {
        ValidationLookupFormatter formatter =
            ValidationLookupFormatter.getInstance(format);
        formatter.setVersion("1.00.001", getDate());
        formatter.setResourceName("Property");
        return formatter;
    }

    public void testCompactFormatValidationLookup()
    {
        ValidationLookupFormatter formatter =
            getFormatter(MetadataFormatter.COMPACT);
        StringWriter formatted = new StringWriter();
        formatter.format(new PrintWriter(formatted), mValidationLookups);
        assertEquals(
            "<METADATA-VALIDATION_LOOKUP Resource=\"Property\" " +
            "Version=\"" + VERSION + "\" Date=\"" + DATE + "\">\n" +

            "<COLUMNS>\tValidationLookupName\tParent1Field\tParent2Field\t" +
            "Version\tDate\t</COLUMNS>\n" +

            "<DATA>\tSchool\tArea\tSubarea" + VERSION_DATE + "\t</DATA>\n" +

            "</METADATA-VALIDATION_LOOKUP>\n",
            formatted.toString());
    }

    public void testEmptyCompactFormat()
    {
        ValidationLookupFormatter formatter =
            getFormatter(MetadataFormatter.COMPACT);
        StringWriter formatted = new StringWriter();
        formatter.format(new PrintWriter(formatted), new ValidationLookup[0]);
        assertEquals("", formatted.toString());
    }

    protected ValidationLookup[] mValidationLookups;
}
