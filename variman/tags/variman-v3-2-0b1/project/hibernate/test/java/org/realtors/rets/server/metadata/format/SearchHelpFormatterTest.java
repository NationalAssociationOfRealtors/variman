/*
 */
package org.realtors.rets.server.metadata.format;

import java.util.ArrayList;
import java.util.List;

import org.realtors.rets.common.metadata.types.MSearchHelp;

public class SearchHelpFormatterTest extends FormatterTestCase
{
    protected List<MSearchHelp> getData()
    {
        List<MSearchHelp> searchHelps = new ArrayList<MSearchHelp>();
        MSearchHelp searchHelp = new MSearchHelp();
        searchHelp.setMetadataEntryID("LNSearchHelp");
        searchHelp.setSearchHelpID("LN_SEARCH_HELP");
        searchHelp.setValue("Listing Number (all numbers)");
        searchHelps.add(searchHelp);
        return searchHelps;
    }

    protected String[] getLevels()
    {
        return new String[] {"Property"};
    }

    protected MetadataFormatter getCompactFormatter()
    {
        return new CompactSearchHelpFormatter();
    }

    protected MetadataFormatter getStandardFormatter()
    {
        return new StandardSearchHelpFormatter();
    }

    protected String getExpectedCompact()
    {
        return
            "<METADATA-SEARCH_HELP Resource=\"Property\" Version=\"" + VERSION +
            "\" Date=\"" + DATE + "\">\n" +

            "<COLUMNS>\tMetadataEntryID\tSearchHelpID\tValue\t</COLUMNS>\n" +

            "<DATA>\tLNSearchHelp\tLN_SEARCH_HELP\tListing Number (all numbers)\t</DATA>\n" +

            "</METADATA-SEARCH_HELP>\n";
    }

    protected String getExpectedCompactRecursive()
    {
        return getExpectedCompact();
    }

    protected String getExpectedStandard()
    {
        return
            "<METADATA-SEARCH_HELP Resource=\"Property\" Version=\"" + VERSION +
            "\" Date=\"" + DATE + "\">" + EOL +
            "<SearchHelp>" + EOL +
            "<MetadataEntryID>LNSearchHelp</MetadataEntryID>" + EOL +
            "<SearchHelpID>LN_SEARCH_HELP</SearchHelpID>" + EOL +
            "<Value>Listing Number (all numbers)</Value>" + EOL +
            "</SearchHelp>" + EOL +
            "</METADATA-SEARCH_HELP>" + EOL;
    }

    protected String getExpectedStandardRecursive()
    {
        return getExpectedStandard();
    }
}