/*
 */
package org.realtors.rets.server.protocol;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

import org.realtors.rets.client.RetsVersion;
import org.realtors.rets.server.LinesEqualTestCase;
import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.metadata.Table;
import org.realtors.rets.server.metadata.UnitEnum;
import org.realtors.rets.server.dmql.SimpleDmqlMetadata;

public class ResidentialPropertyFormatterTest extends LinesEqualTestCase
{
    public void testFormatting() throws RetsServerException
    {
        ResidentialPropertyFormatter formatter =
            new ResidentialPropertyFormatter();
        MockResultSet results = new MockResultSet();
        results.setColumns(COLUMNS);
        results.addRow(new String[] {"Main St.", "12345", "1000"});
        SimpleDmqlMetadata metadata = new SimpleDmqlMetadata();
        metadata.addString("StreetName", "r_STNAME");
        metadata.addString("PostalCode", "r_ZIP_CODE");
        metadata.addString("LivingArea", "r_SQFT");
        Table table = new Table(1);
        table.setUnits(UnitEnum.SQFT);
        metadata.addTable("LivingArea", table);

        StringWriter formatted = new StringWriter();
        SearchFormatterContext context =
            new SearchFormatterContext(new PrintWriter(formatted), results,
                                       Arrays.asList(COLUMNS), metadata, RetsVersion.RETS_1_5);
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
            "<LivingArea>\n" +
            "<Area Units=\"SqFeet\">1000</Area>\n" +
            "</LivingArea>\n" +
            "</ResidentialProperty>\n" +
            "</REProperties>\n" +
            "</REData>\n",
            formatted.toString()
        );
    }

    public void testNoUnits() throws RetsServerException
    {
        String[] columns = new String[] {"r_STNAME", "r_ZIP_CODE"};
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
                                       Arrays.asList(COLUMNS), metadata,
                                       RetsVersion.RETS_1_5);
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
            "<LivingArea>\n" +
            "<Area></Area>\n" +
            "</LivingArea>\n" +
            "</ResidentialProperty>\n" +
            "</REProperties>\n" +
            "</REData>\n",
            formatted.toString()
        );
    }

    public void testNoStreetNameColumn() throws RetsServerException
    {
        String[] columns = new String[] {"r_ZIP_CODE", "r_SQFT"};
        ResidentialPropertyFormatter formatter =
            new ResidentialPropertyFormatter();
        MockResultSet results = new MockResultSet();
        results.setColumns(columns);
        results.addRow(new String[] {"12345", "1000"});
        SimpleDmqlMetadata metadata = new SimpleDmqlMetadata();
        metadata.addString("StreetName", "r_STNAME");
        metadata.addString("PostalCode", "r_ZIP_CODE");
        metadata.addString("LivingArea", "r_SQFT");
        Table table = new Table(1);
        table.setUnits(UnitEnum.SQFT);
        metadata.addTable("LivingArea", table);

        StringWriter formatted = new StringWriter();
        SearchFormatterContext context =
            new SearchFormatterContext(new PrintWriter(formatted), results,
                                       Arrays.asList(columns), metadata,
                                       RetsVersion.RETS_1_5);
        formatter.formatResults(context);
        assertLinesEqual(
            "<REData>\n" +
            "<REProperties>\n" +
            "<ResidentialProperty>\n" +
            "<Listing>\n" +
            "<StreetAddress>\n" +
            "<StreetNumber/>\n" +
            "<StreetName></StreetName>\n" +
            "<PostalCode>12345</PostalCode>\n" +
            "</StreetAddress>\n" +
            "</Listing>\n" +
            "<LivingArea>\n" +
            "<Area Units=\"SqFeet\">1000</Area>\n" +
            "</LivingArea>\n" +
            "</ResidentialProperty>\n" +
            "</REProperties>\n" +
            "</REData>\n",
            formatted.toString()
        );
    }

    public static final String[] COLUMNS =
        new String[] {"r_STNAME", "r_ZIP_CODE", "r_SQFT"};
}
