/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.StringWriter;
import java.io.PrintWriter;

import org.realtors.rets.server.metadata.ValidationExternal;
import org.realtors.rets.server.metadata.MClass;
import org.realtors.rets.server.metadata.Resource;

public class ValidationExternalFormatterTest extends FormatterTestCase
{
    protected void setUp()
    {
        ValidationExternal validationExternal = new ValidationExternal();
        validationExternal.setValidationExternalName("VET1");

        MClass res = new MClass(1);
        res.setClassName("RES");
        Resource property = new Resource(1);
        property.setResourceID("Property");
        res.setResourceid(property);
        validationExternal.setSearchClass(res);

        mValidationExternals = new ValidationExternal[] {validationExternal};
    }

    private ValidationExternalFormatter getFormatter(int format)
    {
        ValidationExternalFormatter formatter =
            ValidationExternalFormatter.getInstance(format);
        formatter.setVersion("1.00.001", getDate());
        formatter.setResourceName("Property");
        return formatter;
    }

    public void testCompactFormatValidationExternal()
    {
        ValidationExternalFormatter formatter =
            getFormatter(MetadataFormatter.COMPACT);
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

    private ValidationExternal[] mValidationExternals;
}
