/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.realtors.rets.server.metadata.MClass;
import org.realtors.rets.server.metadata.Resource;
import org.realtors.rets.server.metadata.ResourceStandardNameEnum;

public class ResourceFormatterTest extends FormatterTestCase
{
    protected void setUp()
    {
        mResources = new ArrayList();
        Resource resource = new Resource();
        resource.setResourceID("PropertyID");
        resource.setStandardName(ResourceStandardNameEnum.PROPERTY);
        resource.setVisibleName("Prop");
        resource.setDescription("Property Database");
        resource.setKeyField("LN");
        HashSet classes = new HashSet();
        classes.add(new MClass(1));
        classes.add(new MClass(2));
        resource.setClasses(classes);
        mResources.add(resource);
    }

    private ResourceFormatter getFormatter(int format)
    {
        ResourceFormatter formatter =
            ResourceFormatter.getInstance(format);
        formatter.setVersion("1.00.001", getDate());
        return formatter;
    }

    public void testCompactFormatResource()
    {
        ResourceFormatter formatter = getFormatter(MetadataFormatter.COMPACT);
        StringWriter formatted = new StringWriter();
        formatter.format(new PrintWriter(formatted), mResources);
        assertEquals(
            "<METADATA-RESOURCE Version=\"" + VERSION + "\" " +
            "Date=\"" + DATE + "\">\n" +

            "<COLUMNS>\tResourceID\tStandardName\tVisibleName\tDescription\t" +
            "KeyField\tClassCount\tClassVersion\tClassDate\tObjectVersion\t" +
            "ObjectDate\tSearchHelpVersion\tSearchHelpDate\tEditMaskVersion\t" +
            "EditMaskDate\tLookupVersion\tLookupDate\tUpdateHelpVersion\t" +
            "UpdateHelpDate\tValidationExpressionVersion\t" +
            "ValidationExpressionDate\tValidationLookupVersion\t" +
            "ValidationLookupDate\tValidationExternalVersion\t" +
            "ValidationExternalDate\t</COLUMNS>\n" +

            "<DATA>\tPropertyID\tProperty\tProp\tProperty Database\t" +
            "LN\t2" +
            VERSION_DATE + VERSION_DATE + VERSION_DATE + VERSION_DATE +
            VERSION_DATE + VERSION_DATE + VERSION_DATE + VERSION_DATE +
            VERSION_DATE + "\t</DATA>\n" +

            "</METADATA-RESOURCE>\n",
            formatted.toString());
    }

    public void testEmptyCompactFormatResource()
    {
        ResourceFormatter formatter = getFormatter(MetadataFormatter.COMPACT);
        StringWriter formatted = new StringWriter();
        formatter.format(new PrintWriter(formatted), new ArrayList());
        assertEquals("", formatted.toString());
    }

    private List mResources;
}
