/*
 */
package org.realtors.rets.server.metadata.format;

import org.realtors.rets.server.metadata.ClassStandardNameEnum;
import org.realtors.rets.server.metadata.MClass;

public class ClassFormatterTest extends FormatterTestCase
{
    protected void setUp()
    {
        MClass clazz = new MClass();
        clazz.setClassName("RES");
        clazz.setStandardName(ClassStandardNameEnum.RESIDENTIAL);
        clazz.setVisibleName("Single Family");
        clazz.setDescription("Single Family Residential");
        mClasses = new MClass[] {clazz};
    }

    public void testCompactFormatClass()
    {
        ClassFormatter formatter =
            ClassFormatter.getInstance(MetadataFormatter.COMPACT);
        formatter.setVersion("1.00.001", getDate());
        formatter.setResource("Property");
        String formatted = formatter.format(mClasses);
        assertEquals(
            "<METADATA-CLASS Resource=\"Property\" " +
            "Version=\"" + VERSION + "\" " +
            "Date=\"" + DATE + "\">\n" +

            "<COLUMNS>\tClassName\tStandardName\tVisibleName\tDBName\t" +
            "Description\tTableVersion\tTableDate\tUpdateVersion\t" +
            "UpdateDate\t</COLUMNS>\n" +

            "<DATA>\tRES\tResidentialProperty\tSingle Family\t\t" +
            "Single Family Residential" + VERSION_DATE + VERSION_DATE +
            "\t</DATA>\n" +

            "</METADATA-CLASS>\n",
            formatted
        );
    }

    private MClass[] mClasses;
}
