/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.realtors.rets.server.metadata.SearchHelp;

public class SearchHelpFormatterTest extends FormatterTestCase
{
    protected void setUp()
    {
        SearchHelp searchHelp = new SearchHelp();
        searchHelp.setSearchHelpID("LN_SEARCH_HELP");
        searchHelp.setValue("Listing Number (all numbers)");
        mSearchHelps = new SearchHelp[] {searchHelp};
    }

    private SearchHelpFormatter getFormatter(int format)
    {
        SearchHelpFormatter formatter =
            SearchHelpFormatter.getInstance(format);
        formatter.setVersion("1.00.001", getDate());
        formatter.setResourceName("Property");
        return formatter;
    }

    public void testCompactFormatSeachHelp()
    {
        SearchHelpFormatter formatter =
            getFormatter(MetadataFormatter.COMPACT);
        StringWriter formatted = new StringWriter();
        formatter.format(new PrintWriter(formatted), mSearchHelps);
        assertEquals(
            "<METADATA-SEARCH_HELP Resource=\"Property\" Version=\"" + VERSION +
            "\" Date=\"" + DATE + "\">\n" +

            "<COLUMNS>\tSearchHelpID\tValue\t</COLUMNS>\n" +

            "<DATA>\tLN_SEARCH_HELP\tListing Number (all numbers)\t</DATA>\n" +

            "</METADATA-SEARCH_HELP>\n",

            formatted.toString());
    }

    public void testEmptyCompactFormatSearchHelp()
    {
        SearchHelpFormatter formatter =
            getFormatter(MetadataFormatter.COMPACT);
        StringWriter formatted = new StringWriter();
        formatter.format(new PrintWriter(formatted), new SearchHelp[0]);
        assertEquals("", formatted.toString());
    }

    private SearchHelp[] mSearchHelps;
}
