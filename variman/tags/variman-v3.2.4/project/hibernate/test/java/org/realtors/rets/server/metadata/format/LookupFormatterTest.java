/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.ArrayList;
import java.util.List;

import org.realtors.rets.common.metadata.MetadataType;
import org.realtors.rets.common.metadata.types.MLookup;
import org.realtors.rets.common.metadata.types.MLookupType;

public class LookupFormatterTest extends FormatterTestCase
{
    protected List<MLookup> getData()
    {
        List<MLookup> lookups = new ArrayList<MLookup>();
        MLookup lookup = new MLookup();
        lookup.setMetadataEntryID("ElementarySchoolDistrict");
        lookup.setLookupName("E_SCHOOL");
        lookup.setVisibleName("Elementary School District");
        MLookupType lookupType = new MLookupType();
        lookupType.setUniqueId(Long.valueOf(1));
        lookupType.setValue("PoudreVally");
        lookup.addChild(MetadataType.LOOKUP_TYPE, lookupType);
        lookups.add(lookup);
        return lookups;
    }

    protected String[] getLevels()
    {
        return new String[] {"Property"};
    }

    protected MetadataFormatter getCompactFormatter()
    {
        return new CompactLookupFormatter();
    }

    protected MetadataFormatter getStandardFormatter()
    {
        return new StandardLookupFormatter();
    }

    protected String getExpectedCompact()
    {
        return
            "<METADATA-LOOKUP Resource=\"Property\" Version=\"" + VERSION +
            "\" Date=\"" + DATE + "\">\n" +

            "<COLUMNS>\tMetadataEntryID\tLookupName\tVisibleName\tLookupTypeVersion\tLookupTypeDate\t</COLUMNS>\n" +

            "<DATA>\tElementarySchoolDistrict\tE_SCHOOL\tElementary School District" + VERSION_DATE +
            "\t</DATA>\n" +

            "</METADATA-LOOKUP>\n";
    }

    protected String getExpectedCompactRecursive()
    {
        return
            "<METADATA-LOOKUP Resource=\"Property\" Version=\"" + VERSION +
            "\" Date=\"" + DATE + "\">\n" +

            "<COLUMNS>\tMetadataEntryID\tLookupName\tVisibleName\tLookupTypeVersion\tLookupTypeDate\t</COLUMNS>\n" +

            "<DATA>\tElementarySchoolDistrict\tE_SCHOOL\tElementary School District" + VERSION_DATE +
            "\t</DATA>\n" +

            "</METADATA-LOOKUP>\n" +

            MetadataType.LOOKUP_TYPE.name() + "\n";
    }

    protected String getExpectedStandard()
    {
        return
            "<METADATA-LOOKUP Resource=\"Property\" Version=\"" + VERSION +
            "\" Date=\"" + DATE + "\">" + EOL +
            "<Lookup>" + EOL +
            "<MetadataEntryID>ElementarySchoolDistrict</MetadataEntryID>" + EOL +
            "<LookupName>E_SCHOOL</LookupName>" + EOL +
            "<VisibleName>Elementary School District</VisibleName>" + EOL +
            "<LookupTypeVersion>" + VERSION + "</LookupTypeVersion>" + EOL +
            "<LookupTypeDate>" + DATE + "</LookupTypeDate>" + EOL +
            "</Lookup>" + EOL +
            "</METADATA-LOOKUP>" + EOL;
    }

    protected String getExpectedStandardRecursive()
    {
        return
            "<METADATA-LOOKUP Resource=\"Property\" Version=\"" + VERSION +
            "\" Date=\"" + DATE + "\">" + EOL +
            "<Lookup>" + EOL +
            "<MetadataEntryID>ElementarySchoolDistrict</MetadataEntryID>" + EOL +
            "<LookupName>E_SCHOOL</LookupName>" + EOL +
            "<VisibleName>Elementary School District</VisibleName>" + EOL +
            "<LookupTypeVersion>" + VERSION + "</LookupTypeVersion>" + EOL +
            "<LookupTypeDate>" + DATE + "</LookupTypeDate>" + EOL +
            MetadataType.LOOKUP_TYPE.name() + EOL +
            "</Lookup>" + EOL +
            "</METADATA-LOOKUP>" + EOL;
    }
}
