/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.ArrayList;
import java.util.List;

import org.realtors.rets.server.metadata.ClassStandardNameEnum;
import org.realtors.rets.server.metadata.MClass;
import org.realtors.rets.server.metadata.Table;
import org.realtors.rets.server.metadata.Update;

public class ClassFormatterTest extends FormatterTestCase
{
    protected List getData()
    {
        List classes = new ArrayList();
        MClass clazz = new MClass();
        clazz.setClassName("RES");
        clazz.setStandardName(ClassStandardNameEnum.RESIDENTIAL);
        clazz.setVisibleName("Single Family");
        clazz.setDescription("Single Family Residential");
        clazz.setDbTable("rets_Property_RES");
        clazz.addTable(new Table(1));
        clazz.addUpdate(new Update(1));
        classes.add(clazz);
        return classes;
    }

    protected String[] getLevels()
    {
        return new String[] {"Property"};
    }

    protected MetadataFormatter getCompactFormatter()
    {
        return new CompactClassFormatter();
    }

    protected String getExpectedCompact()
    {
        return
            "<METADATA-CLASS Resource=\"Property\" " +
            "Version=\"" + VERSION + "\" " +
            "Date=\"" + DATE + "\">\n" +

            "<COLUMNS>\tClassName\tStandardName\tVisibleName\t" +
            "Description\tTableVersion\tTableDate\tUpdateVersion\t" +
            "UpdateDate\tClassTimeStamp\tDeletedFlagField\t" +
            "DeletedFlagValue\tHasKeyIndex\t</COLUMNS>\n" +

            "<DATA>\tRES\tResidentialProperty\tSingle Family\t" +
            "Single Family Residential" + VERSION_DATE + VERSION_DATE +
            "\t\t\t\t0\t</DATA>\n" +

            "</METADATA-CLASS>\n";
    }

    protected String getExpectedCompactRecursive()
    {
        return
            "<METADATA-CLASS Resource=\"Property\" " +
            "Version=\"" + VERSION + "\" " +
            "Date=\"" + DATE + "\">\n" +

            "<COLUMNS>\tClassName\tStandardName\tVisibleName\t" +
            "Description\tTableVersion\tTableDate\tUpdateVersion\t" +
            "UpdateDate\tClassTimeStamp\tDeletedFlagField\t" +
            "DeletedFlagValue\tHasKeyIndex\t</COLUMNS>\n" +


            "<DATA>\tRES\tResidentialProperty\tSingle Family\t" +
            "Single Family Residential" + VERSION_DATE + VERSION_DATE +
            "\t\t\t\t0\t</DATA>\n" +

            "</METADATA-CLASS>\n" +
            Table.TABLE + "\n" +
            Update.TABLE + "\n";
    }

    protected MetadataFormatter getStandardFormatter()
    {
        return new StandardClassFormatter();
    }

    protected String getExpectedStandard()
    {
        return
            "<METADATA-CLASS Resource=\"Property\" Version=\"" +
            VERSION + "\" Date=\"" + DATE + "\">" + EOL +
            "<Class>" + EOL +
            "<ClassName>RES</ClassName>" + EOL +
            "<StandardName>ResidentialProperty</StandardName>" + EOL +
            "<VisibleName>Single Family</VisibleName>" + EOL +
            "<Description>Single Family Residential</Description>" + EOL +
            "<TableVersion>" + VERSION + "</TableVersion>" + EOL +
            "<TableDate>" + DATE + "</TableDate>" + EOL +
            "<UpdateVersion>" + VERSION + "</UpdateVersion>" + EOL +
            "<UpdateDate>" + DATE + "</UpdateDate>" + EOL +
            "<ClassTimeStamp></ClassTimeStamp>" + EOL +
            "<DeletedFlagField></DeletedFlagField>" + EOL +
            "<DeletedFlagValue></DeletedFlagValue>" + EOL +
            "<HasKeyIndex>0</HasKeyIndex>" + EOL +
            "</Class>" + EOL +
            "</METADATA-CLASS>" + EOL;
    }

    protected String getExpectedStandardRecursive()
    {
        return
            "<METADATA-CLASS Resource=\"Property\" Version=\"" +
            VERSION + "\" Date=\"" + DATE + "\">" + EOL +
            "<Class>" + EOL +
            "<ClassName>RES</ClassName>" + EOL +
            "<StandardName>ResidentialProperty</StandardName>" + EOL +
            "<VisibleName>Single Family</VisibleName>" + EOL +
            "<Description>Single Family Residential</Description>" + EOL +
            "<TableVersion>" + VERSION + "</TableVersion>" + EOL +
            "<TableDate>" + DATE + "</TableDate>" + EOL +
            "<UpdateVersion>" + VERSION + "</UpdateVersion>" + EOL +
            "<UpdateDate>" + DATE + "</UpdateDate>" + EOL +
            "<ClassTimeStamp></ClassTimeStamp>" + EOL +
            "<DeletedFlagField></DeletedFlagField>" + EOL +
            "<DeletedFlagValue></DeletedFlagValue>" + EOL +
            "<HasKeyIndex>0</HasKeyIndex>" + EOL +
            Table.TABLE + EOL +
            Update.TABLE + EOL +
            "</Class>" + EOL +
            "</METADATA-CLASS>" + EOL;
    }
}
