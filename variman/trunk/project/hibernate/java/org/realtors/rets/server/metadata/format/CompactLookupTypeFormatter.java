/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;

import org.realtors.rets.server.metadata.LookupType;

public class CompactLookupTypeFormatter extends LookupTypeFormatter
{
    public void format(PrintWriter out, LookupType[] lookupTypes)
    {
        TagBuilder tag = new TagBuilder(out);
        tag.begin("METADATA-LOOKUP_TYPE");
        tag.appendAttribute("Resource", mResourceName);
        tag.appendAttribute("Lookup", mLookupName);
        tag.appendAttribute("Version", mVersion);
        tag.appendAttribute("Date", mDate);
        tag.endAttributes();
        tag.appendColumns(sColumns);
        for (int i = 0; i < lookupTypes.length; i++)
        {
            LookupType lookupType = lookupTypes[i];
            appendDataRow(out, lookupType);
        }
        tag.end();
    }

    private void appendDataRow(PrintWriter out, LookupType lookupType)
    {
        DataRowBuilder row = new DataRowBuilder(out);
        row.begin();
        row.append(lookupType.getLongValue());
        row.append(lookupType.getShortValue());
        row.append(lookupType.getValue());
        row.end();
    }

    private static final String[] sColumns = new String[] {
        "LongValue", "ShortValue", "Value",
    };
}
