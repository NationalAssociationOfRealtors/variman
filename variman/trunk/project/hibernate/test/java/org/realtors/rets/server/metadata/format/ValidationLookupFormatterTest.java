/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.realtors.rets.server.metadata.ValidationLookup;

public class ValidationLookupFormatterTest extends FormatterTestCase
{
    protected void setUp()
    {
        mValidationLookups = new ArrayList();
        ValidationLookup validationLookup = new ValidationLookup();
        validationLookup.setValidationLookupName("School");
        validationLookup.setParent1Field("Area");
        validationLookup.setParent2Field("Subarea");
        mValidationLookups.add(validationLookup);
    }

    private ValidationLookupFormatter getFormatter(int format)
    {
        ValidationLookupFormatter formatter =
            ValidationLookupFormatter.getInstance(format);
        formatter.setVersion("1.00.001", getDate());
        formatter.setLevels(new String[] {"Property"});
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
        formatter.format(new PrintWriter(formatted), new ArrayList());
        assertEquals("", formatted.toString());
    }

    protected List mValidationLookups;
}
