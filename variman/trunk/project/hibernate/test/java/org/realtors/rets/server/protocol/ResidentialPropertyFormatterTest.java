/*
 */
package org.realtors.rets.server.protocol;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

import org.realtors.rets.server.LinesEqualTestCase;
import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.dmql.SimpleDmqlMetadata;

public class ResidentialPropertyFormatterTest extends LinesEqualTestCase
{
    public void testFormatting() throws RetsServerException
    {
        ResidentialPropertyFormatter formatter =
            new ResidentialPropertyFormatter();
        MockResultSet results = new MockResultSet();
        results.setColumns(COLUMNS);
        results.addRow(new String[] {"Main St.", "12345"});
        SimpleDmqlMetadata metadata = new SimpleDmqlMetadata();
        metadata.addString("StreetName", "r_STNAME");
        metadata.addString("PostalCode", "r_ZIP_CODE");
        StringWriter formatted = new StringWriter();
        SearchFormatterContext context =
            new SearchFormatterContext(new PrintWriter(formatted), results,
                                       Arrays.asList(COLUMNS), metadata);
        formatter.formatResults(context);
        assertLinesEqual(
            "<REData>\n" +
            "<REProperties>\n" +
            "<ResidentialProperty>\n" +
            "<Listing>\n" +
            "<StreetAddress>\n" +
            "<StreetNumber/>\n" +
            "<StreetName>Main St.</StreetName>\n" +
            "<PostalCode>12345</PostalCode>\n" +
            "</StreetAddress>\n" +
            "</Listing>\n" +
            "</ResidentialProperty>\n" +
            "</REProperties>\n" +
            "</REData>\n",
            formatted.toString()
        );
    }

    public static final String[] COLUMNS =
        new String[] {"r_STNAME", "r_ZIP_CODE"};
}
