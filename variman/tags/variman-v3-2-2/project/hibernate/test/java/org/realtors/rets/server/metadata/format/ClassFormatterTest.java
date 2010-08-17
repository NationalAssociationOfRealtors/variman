/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.ArrayList;
import java.util.List;

import org.realtors.rets.common.metadata.MetadataType;
import org.realtors.rets.common.metadata.types.MClass;
import org.realtors.rets.common.metadata.types.MTable;
import org.realtors.rets.common.metadata.types.MUpdate;
import org.realtors.rets.server.metadata.ClassStandardNameEnum;

public class ClassFormatterTest extends FormatterTestCase
{
    protected List<MClass> getData()
    {
        List<MClass> classes = new ArrayList<MClass>();
        MClass clazz = new MClass();
        clazz.setClassName("RES");
        clazz.setStandardName(ClassStandardNameEnum.RESIDENTIAL.toString());
        clazz.setVisibleName("Single Family");
        clazz.setDescription("Single Family Residential");
        clazz.setXDBName("rets_Property_RES");
        MTable table = new MTable();
        table.setUniqueId(Long.valueOf(1));
        table.setSystemName("MLSNumber");
        clazz.addChild(MetadataType.TABLE, table);
        MUpdate update = new MUpdate();
        update.setUniqueId(Long.valueOf(1));
        update.setUpdateName("Add");
        clazz.addChild(MetadataType.UPDATE, update);
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
            MetadataType.TABLE.name() + "\n" +
            MetadataType.UPDATE.name() + "\n";
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
            MetadataType.TABLE.name() + EOL +
            MetadataType.UPDATE.name() + EOL +
            "</Class>" + EOL +
            "</METADATA-CLASS>" + EOL;
    }
}
