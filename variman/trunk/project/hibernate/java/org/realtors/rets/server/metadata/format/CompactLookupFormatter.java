/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;

import org.realtors.rets.server.metadata.Lookup;

public class CompactLookupFormatter extends LookupFormatter
{
    public void format(PrintWriter out, Lookup[] lookups)
    {
        if (lookups.length == 0)
        {
            return;
        }
        TagBuilder tag = new TagBuilder(out);
        tag.begin("METADATA-LOOKUP");
        tag.appendAttribute("Resource", mResourceName);
        tag.appendAttribute("Version", mVersion);
        tag.appendAttribute("Date", mDate);
        tag.endAttributes();
        tag.appendColumns(sColumns);
        for (int i = 0; i < lookups.length; i++)
        {
            Lookup lookup = lookups[i];
            appendDataRow(out, lookup);
        }
        tag.end();
    }

    private void appendDataRow(PrintWriter out, Lookup lookup)
    {
        DataRowBuilder row = new DataRowBuilder(out);
        row.begin();
        row.append(lookup.getLookupName());
        row.append(lookup.getVisibleName());
        row.append(mVersion);
        row.append(mDate);
        row.end();
    }

    private static final String[] sColumns = new String[] {
        "LookupName", "VisibleName", "Version", "Date",
    };
}
