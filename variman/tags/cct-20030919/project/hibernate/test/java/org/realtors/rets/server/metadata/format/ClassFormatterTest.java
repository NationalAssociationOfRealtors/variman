/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.realtors.rets.server.metadata.ClassStandardNameEnum;
import org.realtors.rets.server.metadata.MClass;

public class ClassFormatterTest extends FormatterTestCase
{
    protected void setUp()
    {
        mClasses = new ArrayList();
        MClass clazz = new MClass();
        clazz.setClassName("RES");
        clazz.setStandardName(ClassStandardNameEnum.RESIDENTIAL);
        clazz.setVisibleName("Single Family");
        clazz.setDescription("Single Family Residential");
        mClasses.add(clazz);
    }

    private ClassFormatter getCompactFormatter()
    {
        ClassFormatter formatter = new CompactClassFormatter();
        formatter.setVersion("1.00.001", getDate());
        formatter.setLevels(new String[]{"Property"});
        return formatter;
    }

    public void testCompactFormatClass()
    {
        ClassFormatter formatter = getCompactFormatter();
        StringWriter formatted = new StringWriter();
        formatter.format(new PrintWriter(formatted), mClasses);
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
            formatted.toString()
        );
    }

    public void testEmptyCompactyFormatClass()
    {
        ClassFormatter formatter = getCompactFormatter();
        StringWriter formatted = new StringWriter();
        formatter.format(new PrintWriter(formatted), new ArrayList());
        assertEquals("", formatted.toString());
    }

    private List mClasses;
}
