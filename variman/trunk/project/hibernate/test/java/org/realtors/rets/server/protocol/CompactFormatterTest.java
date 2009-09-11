/*
 */
package org.realtors.rets.server.protocol;

import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import org.realtors.rets.client.RetsVersion;
import org.realtors.rets.common.util.RetsDateTime;

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
            "<DATA>\tMichigan Ave.\t60605\t1\tDW,FR\t</DATA>\n" +
            "<DATA>\tState St.\t60601\t\tDw\t</DATA>\n",
            format(CompactFormatter.NO_DECODING));
    }
    
    public void testDateFormat() throws RetsServerException
    {
        CompactFormatter formatter =
            new CompactFormatter(CompactFormatter.NO_DECODING, RetsVersion.RETS_1_5);
        MockResultSet results = new MockResultSet();
        String[] columns = new String[]{"r_DATE"};
        results.setColumns(columns);
        /*
         * This is pre RETS 1.7.2 format. Times are all GMT.
         * It appears that there is confusion over which format to use when
         * rendering compact format, especially since the 1.5 spec itself
         * is inconsistent:
         * "A fourteen-digit string with separators as above, and a space 
         * between the day and the hour, as YYYY-MM-DDThh:mm:ss[.sss], with 
         * a three-digit optional fractions of a second separated from the 
         * seconds with a decimal <".">"
         * We'll use YYYY-MM-DD hh:mm:ss.
         */
        RetsDateTime cal = new RetsDateTime();
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        cal.set(2008, 4, 9, 12, 17, 0);
        cal.setRetsVersion(RetsVersion.RETS_1_5);
        results.addRow(new Object[] {cal});
        SimpleDmqlMetadata metadata = new SimpleDmqlMetadata();
        metadata.addTemporal("DATE");
        StringWriter formatted = new StringWriter();
        SearchFormatterContext context =
            new SearchFormatterContext(
                new PrintWriter(formatted), results, Arrays.asList(columns),
                metadata, RetsVersion.RETS_1_5);
        context.setLimit(10000);
        formatter.formatResults(context);
        assertLinesEqual(
        		"<DELIMITER value=\"09\"/>\n" + 
        		"<COLUMNS>\tDATE\t</COLUMNS>\n" + 
        		"<DATA>\t2008-05-09T12:17:00\t</DATA>\n", formatted.toString());
        
        // This is RETS 1.7.2 (the default) format.
        results = new MockResultSet();
        cal = new RetsDateTime();
        cal.setTimeZone(TimeZone.getTimeZone("GMT-7"));
        cal.set(2008, 4, 9, 12, 17, 0);
        results.addRow(new Object[] {cal});
        formatted = new StringWriter();
        context =
            new SearchFormatterContext(
                new PrintWriter(formatted), results, Arrays.asList(columns),
                metadata, RetsVersion.RETS_1_7_2);
        context.setLimit(10000);
        formatter.formatResults(context);
        assertLinesEqual(
        		"<DELIMITER value=\"09\"/>\n" + 
        		"<COLUMNS>\tDATE\t</COLUMNS>\n" + 
        		"<DATA>\t2008-05-09T19:17:00Z\t</DATA>\n", formatted.toString());
    	
    }

    public void testCompactDecodedFormat() throws RetsServerException
    {
        assertLinesEqual(
            "<DELIMITER value=\"09\"/>\n" +
            "<COLUMNS>\tSTNAME\tZIP_CODE\tSTATUS\tIF\t</COLUMNS>\n" +
            "<DATA>\tMain St.\t12345\tActive\t\t</DATA>\n" +
            "<DATA>\tMichigan Ave.\t60605\tINACT\tDishwasher,Freezer\t" +
            "</DATA>\n" +
            "<DATA>\tState St.\t60601\t\tDw\t</DATA>\n",
            format(CompactFormatter.DECODE_LOOKUPS));
    }

    public void testMaxRows() throws RetsServerException
    {
        assertLinesEqual(
            "<DELIMITER value=\"09\"/>\n" +
            "<COLUMNS>\tSTNAME\tZIP_CODE\tSTATUS\tIF\t</COLUMNS>\n" +
            "<DATA>\tMain St.\t12345\t0\t\t</DATA>\n" +
            "<DATA>\tMichigan Ave.\t60605\t1\tDW,FR\t</DATA>\n" +
            "<MAXROWS/>\n",
            format(CompactFormatter.NO_DECODING, 2));
    }

    private String format(CompactFormatter.LookupDecoding lookupDecoding)
        throws RetsServerException
    {
        return format(lookupDecoding, Integer.MAX_VALUE);
    }

    private String format(CompactFormatter.LookupDecoding lookupDecoding,
                          int limit)
        throws RetsServerException
    {
        CompactFormatter formatter =
            new CompactFormatter(lookupDecoding, RetsVersion.RETS_1_5);
        MockResultSet results = new MockResultSet();
        results.setColumns(COLUMNS);
        results.addRow(new String[] {"Main St.", "12345", "0", null});
        results.addRow(new String[] {"Michigan Ave.", "60605", "1", "DW,FR"});
        results.addRow(new String[] {"State St.", "60601", null, "Dw"});
        SimpleDmqlMetadata metadata = new SimpleDmqlMetadata();
        metadata.addString("STNAME");
        metadata.addString("ZIP_CODE");
        metadata.addLookup("STATUS", new String[] {"0", "1"},
                           new String[] {"ACT", "INACT"},
                           new String[] {"Active", null});
        metadata.addLookupMulti("IF", new String[] {"FR", "DW"},
                                new String[] {"Freezer", "Dishwasher"});
        StringWriter formatted = new StringWriter();
        SearchFormatterContext context =
            new SearchFormatterContext(
                new PrintWriter(formatted), results, Arrays.asList(COLUMNS),
                metadata, RetsVersion.RETS_1_5);
        context.setLimit(limit);
        formatter.formatResults(context);
        return formatted.toString();
    }

    public static final String[] COLUMNS =
        new String[] {"r_STNAME", "r_ZIP_CODE", "r_STATUS", "r_IF"};
}
