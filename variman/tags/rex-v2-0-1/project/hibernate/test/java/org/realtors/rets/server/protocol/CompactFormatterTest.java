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
    public void testFormat() throws RetsServerException
    {
        CompactFormatter formatter = new CompactFormatter();
        MockResultSet results = new MockResultSet();
        results.setColumns(COLUMNS);
        results.addRow(new String[] {"Main St.", "12345"});
        results.addRow(new String[] {"Michigan Ave.", "60605"});
        SimpleDmqlMetadata metadata = new SimpleDmqlMetadata();
        metadata.addString("STNAME");
        metadata.addString("ZIP_CODE");
        StringWriter formatted = new StringWriter();
        SearchFormatterContext context =
            new SearchFormatterContext(new PrintWriter(formatted), results,
                                       Arrays.asList(COLUMNS), metadata);
        formatter.formatResults(context);
        assertLinesEqual(
            "<DELIMITER value=\"09\"/>\n" +
            "<COLUMNS>\tSTNAME\tZIP_CODE\t</COLUMNS>\n" +
            "<DATA>\tMain St.\t12345\t</DATA>\n" +
            "<DATA>\tMichigan Ave.\t60605\t</DATA>\n",
            formatted.toString());
    }

    public static final String[] COLUMNS =
        new String[] {"r_STNAME", "r_ZIP_CODE"};
}
