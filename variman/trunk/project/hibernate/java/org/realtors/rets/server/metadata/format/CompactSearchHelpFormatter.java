/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;

import org.realtors.rets.server.metadata.SearchHelp;

public class CompactSearchHelpFormatter extends SearchHelpFormatter
{
    public void format(PrintWriter out, SearchHelp[] searchHelps)
    {
        TagBuilder tag = new TagBuilder(out);
        tag.begin("METADATA-SEARCH_HELP");
        tag.appendAttribute("Resource", mResourceName);
        tag.appendAttribute("Version", mVersion);
        tag.appendAttribute("Date", mDate);
        tag.endAttributes();
        tag.appendColumns(sColumns);
        for (int i = 0; i < searchHelps.length; i++)
        {
            SearchHelp searchHelp = searchHelps[i];
            appendDataRow(out, searchHelp);
        }
        tag.end();
    }

    private void appendDataRow(PrintWriter out, SearchHelp searchHelp)
    {
        DataRowBuilder row = new DataRowBuilder(out);
        row.begin();
        row.append(searchHelp.getSearchHelpID());
        row.append(searchHelp.getValue());
        row.end();
    }

    private static final String[] sColumns = new String[] {
        "SearchHelpID", "Value",
    };
}
