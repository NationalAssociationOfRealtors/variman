/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.ArrayList;
import java.util.List;

import org.realtors.rets.server.metadata.LookupType;

public class LookupTypeFormatterTest extends FormatterTestCase
{
    protected List getData()
    {
        List lookupTypes = new ArrayList();
        LookupType lookupType = new LookupType();
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

    protected String getExpectedCompact()
    {
        return
            "<METADATA-LOOKUP_TYPE Resource=\"Property\" Lookup=\"E_SCHOOL\" " +
            "Version=\"" + VERSION + "\" Date=\"" + DATE + "\">\n" +

            "<COLUMNS>\tLongValue\tShortValue\tValue\t</COLUMNS>\n" +

            "<DATA>\tAurora 306\t306\t306\t</DATA>\n" +

            "</METADATA-LOOKUP_TYPE>\n";
    }

    protected String getExpectedCompactRecursive()
    {
        return
            "<METADATA-LOOKUP_TYPE Resource=\"Property\" Lookup=\"E_SCHOOL\" " +
            "Version=\"" + VERSION + "\" Date=\"" + DATE + "\">\n" +

            "<COLUMNS>\tLongValue\tShortValue\tValue\t</COLUMNS>\n" +

            "<DATA>\tAurora 306\t306\t306\t</DATA>\n" +

            "</METADATA-LOOKUP_TYPE>\n";
    }
}
