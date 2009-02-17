/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.ArrayList;
import java.util.List;

import org.realtors.rets.server.metadata.Lookup;
import org.realtors.rets.server.metadata.LookupType;

public class LookupFormatterTest extends FormatterTestCase
{
    protected List getData()
    {
        List lookups = new ArrayList();
        Lookup lookup = new Lookup();
        lookup.setMetadataEntryID("ElementarySchoolDistrict");
        lookup.setLookupName("E_SCHOOL");
        lookup.setVisibleName("Elementary School District");
        lookup.addLookupType(new LookupType(1));
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

            "<COLUMNS>\tMetadataEntryID\tLookupName\tVisibleName\tVersion\tDate\t</COLUMNS>\n" +

            "<DATA>\tElementarySchoolDistrict\tE_SCHOOL\tElementary School District" + VERSION_DATE +
            "\t</DATA>\n" +

            "</METADATA-LOOKUP>\n";
    }

    protected String getExpectedCompactRecursive()
    {
        return
            "<METADATA-LOOKUP Resource=\"Property\" Version=\"" + VERSION +
            "\" Date=\"" + DATE + "\">\n" +

            "<COLUMNS>\tMetadataEntryID\tLookupName\tVisibleName\tVersion\tDate\t</COLUMNS>\n" +

            "<DATA>\tElementarySchoolDistrict\tE_SCHOOL\tElementary School District" + VERSION_DATE +
            "\t</DATA>\n" +

            "</METADATA-LOOKUP>\n" +

            LookupType.TABLE + "\n";
    }

    protected String getExpectedStandard()
    {
        return
            "<METADATA-LOOKUP Resource=\"Property\" Version=\"" + VERSION +
            "\" Date=\"" + DATE + "\">" + EOL +
            "<LookupType>" + EOL +
            "<MetadataEntryID>ElementarySchoolDistrict</MetadataEntryID>" + EOL +
            "<LookupName>E_SCHOOL</LookupName>" + EOL +
            "<VisibleName>Elementary School District</VisibleName>" + EOL +
            "<LookupTypeVersion>" + VERSION + "</LookupTypeVersion>" + EOL +
            "<LookupTypeDate>" + DATE + "</LookupTypeDate>" + EOL +
            "</LookupType>" + EOL +
            "</METADATA-LOOKUP>" + EOL;
    }

    protected String getExpectedStandardRecursive()
    {
        return
            "<METADATA-LOOKUP Resource=\"Property\" Version=\"" + VERSION +
            "\" Date=\"" + DATE + "\">" + EOL +
            "<LookupType>" + EOL +
            "<MetadataEntryID>ElementarySchoolDistrict</MetadataEntryID>" + EOL +
            "<LookupName>E_SCHOOL</LookupName>" + EOL +
            "<VisibleName>Elementary School District</VisibleName>" + EOL +
            "<LookupTypeVersion>" + VERSION + "</LookupTypeVersion>" + EOL +
            "<LookupTypeDate>" + DATE + "</LookupTypeDate>" + EOL +
            LookupType.TABLE + EOL +
            "</LookupType>" + EOL +
            "</METADATA-LOOKUP>" + EOL;
    }
}
