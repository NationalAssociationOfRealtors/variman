/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.realtors.rets.server.metadata.SearchHelp;

public class SearchHelpFormatterTest extends FormatterTestCase
{
    protected void setUp()
    {
        mSearchHelps = new ArrayList();
        SearchHelp searchHelp = new SearchHelp();
        searchHelp.setSearchHelpID("LN_SEARCH_HELP");
        searchHelp.setValue("Listing Number (all numbers)");
        mSearchHelps.add(searchHelp);
    }

    private SearchHelpFormatter getCompactFormatter()
    {
        SearchHelpFormatter formatter = new CompactSearchHelpFormatter();
        formatter.setVersion("1.00.001", getDate());
        formatter.setLevels(new String[] {"Property"});
        return formatter;
    }

    public void testCompactFormatSeachHelp()
    {
        SearchHelpFormatter formatter = getCompactFormatter();
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
        SearchHelpFormatter formatter = getCompactFormatter();
        StringWriter formatted = new StringWriter();
        formatter.format(new PrintWriter(formatted), new ArrayList());
        assertEquals("", formatted.toString());
    }

    private List mSearchHelps;
}
