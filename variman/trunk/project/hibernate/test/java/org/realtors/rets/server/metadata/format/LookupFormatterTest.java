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

    protected String getExpectedCompact()
    {
        return
            "<METADATA-LOOKUP Resource=\"Property\" Version=\"" + VERSION +
            "\" Date=\"" + DATE + "\">\n" +

            "<COLUMNS>\tLookupName\tVisibleName\tVersion\tDate\t</COLUMNS>\n" +

            "<DATA>\tE_SCHOOL\tElementary School District" + VERSION_DATE +
            "\t</DATA>\n" +

            "</METADATA-LOOKUP>\n";
    }

    protected String getExpectedCompactRecursive()
    {
        return
            "<METADATA-LOOKUP Resource=\"Property\" Version=\"" + VERSION +
            "\" Date=\"" + DATE + "\">\n" +

            "<COLUMNS>\tLookupName\tVisibleName\tVersion\tDate\t</COLUMNS>\n" +

            "<DATA>\tE_SCHOOL\tElementary School District" + VERSION_DATE +
            "\t</DATA>\n" +

            "</METADATA-LOOKUP>\n" +

            LookupType.TABLE + "\n";
    }
}
