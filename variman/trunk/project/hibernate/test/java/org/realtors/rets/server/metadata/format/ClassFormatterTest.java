/*
 */
package org.realtors.rets.server.metadata.format;

import org.realtors.rets.server.metadata.MClass;
import org.realtors.rets.server.metadata.ClassStandardNameEnum;

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
        String formatted = formatter.format(mClasses, "Property", "1.00.001",
                                            getDate());
//        assertEquals(
//            "<METADATA-CLASS Version=\"" + VERSION + "\"",
//            formatted
//        );
    }

    private MClass[] mClasses;
}
