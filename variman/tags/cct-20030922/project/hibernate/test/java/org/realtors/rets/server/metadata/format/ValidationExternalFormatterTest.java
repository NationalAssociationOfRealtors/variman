/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.realtors.rets.server.metadata.MClass;
import org.realtors.rets.server.metadata.Resource;
import org.realtors.rets.server.metadata.ValidationExternal;

public class ValidationExternalFormatterTest extends FormatterTestCase
{
    protected void setUp()
    {
        mValidationExternals = new ArrayList();
        ValidationExternal validationExternal = new ValidationExternal();
        validationExternal.setValidationExternalName("VET1");

        MClass res = new MClass(1);
        res.setClassName("RES");
        Resource property = new Resource(1);
        property.setResourceID("Property");
        res.setResource(property);
        validationExternal.setSearchClass(res);

        mValidationExternals.add(validationExternal);
    }

    private ValidationExternalFormatter getCompactFormatter()
    {
        ValidationExternalFormatter formatter =
            new CompactValidationExternalFormatter();
        formatter.setVersion("1.00.001", getDate());
        formatter.setLevels(new String[] {"Property"});
        return formatter;
    }

    public void testCompactFormatValidationExternal()
    {
        ValidationExternalFormatter formatter = getCompactFormatter();
        StringWriter formatted = new StringWriter();
        formatter.format(new PrintWriter(formatted), mValidationExternals);
        assertEquals(
            "<METADATA-VALIDATION_EXTERNAL Resource=\"Property\" " +
            "Version=\"" + VERSION + "\" Date=\"" + DATE + "\">\n" +

            "<COLUMNS>\tValidationExternalName\tSearchResource\tSearchClass\t" +
            "Version\tDate\t</COLUMNS>\n" +

            "<DATA>\tVET1\tProperty\tRES" + VERSION_DATE + "\t</DATA>\n" +

            "</METADATA-VALIDATION_EXTERNAL>\n",
            formatted.toString());
    }

    public void testCompactFormat()
    {
        ValidationExternalFormatter formatter = getCompactFormatter();
        StringWriter formatted = new StringWriter();
        formatter.format(new PrintWriter(formatted), new ArrayList());
        assertEquals("", formatted.toString());
    }

    private List mValidationExternals;
}
