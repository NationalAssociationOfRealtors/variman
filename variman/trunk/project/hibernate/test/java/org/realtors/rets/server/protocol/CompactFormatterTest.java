/*
 */
package org.realtors.rets.server.protocol;

import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.Arrays;

import org.realtors.rets.server.LinesEqualTestCase;
import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.dmql.SimpleDmqlMetadata;

public class CompactFormatterTest extends LinesEqualTestCase
{
    public void testCompactFormat() throws RetsServerException
    {
        assertLinesEqual(
            "<DELIMITER value=\"09\"/>\n" +
            "<COLUMNS>\tSTNAME\tZIP_CODE\tSTATUS\tIF\t</COLUMNS>\n" +
            "<DATA>\tMain St.\t12345\t0\t\t</DATA>\n" +
            "<DATA>\tMichigan Ave.\t60605\t1\tDW,FR\t</DATA>\n",
            format(CompactFormatter.NO_DECODING));
    }

    public void testCompactDecodedFormat() throws RetsServerException
    {
        assertLinesEqual(
            "<DELIMITER value=\"09\"/>\n" +
            "<COLUMNS>\tSTNAME\tZIP_CODE\tSTATUS\tIF\t</COLUMNS>\n" +
            "<DATA>\tMain St.\t12345\tActive\t\t</DATA>\n" +
            "<DATA>\tMichigan Ave.\t60605\tInactive\tDishwasher,Freezer\t" +
            "</DATA>\n",
            format(CompactFormatter.DECODE_TO_SHORT_VALUE));
    }

    private String format(CompactFormatter.LookupDecoding lookupDecoding)
        throws RetsServerException
    {
        CompactFormatter formatter =
            new CompactFormatter(lookupDecoding);
        MockResultSet results = new MockResultSet();
        results.setColumns(COLUMNS);
        results.addRow(new String[] {"Main St.", "12345", "0", ""});
        results.addRow(new String[] {"Michigan Ave.", "60605", "1", "DW,FR"});
        SimpleDmqlMetadata metadata = new SimpleDmqlMetadata();
        metadata.addString("STNAME");
        metadata.addString("ZIP_CODE");
        metadata.addLookup("STATUS", new String[]{"0", "1"},
                           new String[]{"Active", "Inactive"});
        metadata.addLookupMulti("IF", new String[]{"FR", "DW"},
                                new String[]{"Freezer", "Dishwasher"});
        StringWriter formatted = new StringWriter();
        SearchFormatterContext context =
            new SearchFormatterContext(
                new PrintWriter(formatted), results, Arrays.asList(COLUMNS),
                metadata);
        formatter.formatResults(context);
        return formatted.toString();
    }

    public static final String[] COLUMNS =
        new String[] {"r_STNAME", "r_ZIP_CODE", "r_STATUS", "r_IF"};
}
