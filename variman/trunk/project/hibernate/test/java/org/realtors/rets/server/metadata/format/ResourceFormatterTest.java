/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.HashSet;

import org.realtors.rets.server.metadata.Resource;
import org.realtors.rets.server.metadata.ResourceStandardNameEnum;
import org.realtors.rets.server.metadata.MClass;

public class ResourceFormatterTest extends FormatterTestCase
{
    protected void setUp()
    {
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
        mResources = new Resource[] {resource};
    }

    public void testCompactFormatResource()
    {
        ResourceFormatter formatter =
            ResourceFormatter.getInstance(MetadataFormatter.COMPACT);
        String formatted = formatter.format(mResources, "1.00.001", getDate());
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
            formatted);
    }

    private Resource[] mResources;
}
