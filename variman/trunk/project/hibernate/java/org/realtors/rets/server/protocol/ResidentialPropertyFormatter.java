/*
 */
package org.realtors.rets.server.protocol;

import java.io.PrintWriter;
import java.sql.SQLException;

import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.metadata.format.TagBuilder;

public class ResidentialPropertyFormatter implements SearchResultsFormatter
{

    public void formatResults(SearchFormatterContext context)
        throws RetsServerException
    {
        try
        {
            PrintWriter out = context.getWriter();
            TagBuilder reData = new TagBuilder(out, "REData")
                .beginContentOnNewLine();
            TagBuilder reProperties = new TagBuilder(out, "REProperties")
                .beginContentOnNewLine();
            while (context.hasNext())
            {
                formatRow(context);
            }
            reProperties.close();
            reData.close();
        }
        catch (SQLException e)
        {
            throw new RetsServerException(e);
        }
    }

    private void formatRow(SearchFormatterContext context)
        throws SQLException
    {
        PrintWriter out = context.getWriter();
        TagBuilder residentialProperty =
            new TagBuilder(out, "ResidentialProperty")
            .beginContentOnNewLine();
        TagBuilder listing = new TagBuilder(out, "Listing")
            .beginContentOnNewLine();
        TagBuilder.emptyTag(out, "StreetNumber");
        TagBuilder.simpleTag(out, "StreetName",
                             context.getResultString("StreetName"));
        TagBuilder.simpleTag(out, "PostalCode",
                             context.getResultString("PostalCode"));
        listing.close();
        residentialProperty.close();
    }
}
