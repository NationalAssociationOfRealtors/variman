/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.util.List;

import org.realtors.rets.server.metadata.Lookup;

public class CompactLookupFormatter extends LookupFormatter
{
    public void format(PrintWriter out, List lookups)
    {
        if (lookups.size() == 0)
        {
            return;
        }
        TagBuilder tag = new TagBuilder(out);
        tag.begin("METADATA-LOOKUP");
        tag.appendAttribute("Resource", mResourceName);
        tag.appendAttribute("Version", mVersion);
        tag.appendAttribute("Date", mDate);
        tag.endAttributes();
        tag.appendColumns(COLUMNS);
        for (int i = 0; i < lookups.size(); i++)
        {
            Lookup lookup = (Lookup) lookups.get(i);
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

    private static final String[] COLUMNS = new String[] {
        "LookupName", "VisibleName", "Version", "Date",
    };
}
