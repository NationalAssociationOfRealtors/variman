/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.util.List;

import org.realtors.rets.server.metadata.SearchHelp;

public class CompactSearchHelpFormatter extends SearchHelpFormatter
{
    public void format(PrintWriter out, List searchHelps)
    {
        if (searchHelps.size() == 0)
        {
            return; 
        }
        TagBuilder tag = new TagBuilder(out);
        tag.begin("METADATA-SEARCH_HELP");
        tag.appendAttribute("Resource", mResourceName);
        tag.appendAttribute("Version", mVersion);
        tag.appendAttribute("Date", mDate);
        tag.endAttributes();
        tag.appendColumns(sColumns);
        for (int i = 0; i < searchHelps.size(); i++)
        {
            SearchHelp searchHelp = (SearchHelp) searchHelps.get(i);
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
