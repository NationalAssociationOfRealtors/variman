/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.ArrayList;
import java.util.List;

import org.realtors.rets.common.metadata.types.MLookupType;

public class LookupTypeFormatterTest extends FormatterTestCase
{
    protected List<MLookupType> getData()
    {
        List<MLookupType> lookupTypes = new ArrayList<MLookupType>();
        MLookupType lookupType = new MLookupType();
        lookupType.setMetadataEntryID("Aurora386");
        lookupType.setLongValue("Aurora 306");
        lookupType.setShortValue("306");
        lookupType.setValue("306");
        lookupTypes.add(lookupType);
        return lookupTypes;
    }

    protected String[] getLevels()
    {
        return new String[] {"Property", "E_SCHOOL"};
    }

    protected MetadataFormatter getCompactFormatter()
    {
        return new CompactLookupTypeFormatter();
    }

    protected MetadataFormatter getStandardFormatter()
    {
        return new StandardLookupTypeFormatter();
    }

    protected String getExpectedCompact()
    {
        return
            "<METADATA-LOOKUP_TYPE Resource=\"Property\" Lookup=\"E_SCHOOL\" " +
            "Version=\"" + VERSION + "\" Date=\"" + DATE + "\">\n" +

            "<COLUMNS>\tMetadataEntryID\tLongValue\tShortValue\tValue\t</COLUMNS>\n" +

            "<DATA>\tAurora386\tAurora 306\t306\t306\t</DATA>\n" +

            "</METADATA-LOOKUP_TYPE>\n";
    }

    protected String getExpectedCompactRecursive()
    {
        return getExpectedCompact();
    }

    protected String getExpectedStandard()
    {
        return
            "<METADATA-LOOKUP_TYPE Resource=\"Property\" Lookup=\"E_SCHOOL\" " +
            "Version=\"" + VERSION + "\" Date=\"" + DATE + "\">" + EOL +
            "<LookupType>" + EOL +
            "<MetadataEntryID>Aurora386</MetadataEntryID>" + EOL +
            "<LongValue>Aurora 306</LongValue>" + EOL +
            "<ShortValue>306</ShortValue>" + EOL +
            "<Value>306</Value>" + EOL +
            "</LookupType>" + EOL +
            "</METADATA-LOOKUP_TYPE>" + EOL;
    }

    protected String getExpectedStandardRecursive()
    {
        return getExpectedStandard();
    }
}
