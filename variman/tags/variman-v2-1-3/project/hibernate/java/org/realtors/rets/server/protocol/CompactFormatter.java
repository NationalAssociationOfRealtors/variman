/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.protocol;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.dmql.DmqlParserMetadata;
import org.realtors.rets.server.metadata.format.DataRowBuilder;
import org.realtors.rets.server.metadata.format.TagBuilder;

import org.apache.commons.lang.StringUtils;

/**
 * Formats results in COMPACT format.
 */
public class CompactFormatter implements SearchResultsFormatter
{
    /**
     * Formats results in COMPACT format
     *
     * @param context Search context
     * @throws RetsServerException if an error occurs
     */
    public void formatResults(SearchFormatterContext context)
        throws RetsServerException
    {
        try
        {
            PrintWriter out = context.getWriter();
            out.println("<DELIMITER value=\"09\"/>");
            TagBuilder columnsTag = new TagBuilder(out, "COLUMNS")
                .beginContent();
            columnsTag.print("\t");
            Collection fields = columnsToFields(context.getColumns(),
                                                context.getMetadata());
            columnsTag.print(StringUtils.join(fields.iterator(), "\t"));
            columnsTag.print("\t");
            columnsTag.close();
            int numColumns = context.getColumns().size();
            while (context.hasNext())
            {
                formatRow(context, numColumns);
            }
        }
        catch (SQLException e)
        {
            throw new RetsServerException(e);
        }
    }

    /**
     * Formats a single data row.
     *
     * @param context search context
     * @param numColumns number of columns
     * @throws SQLException if an error occurs
     */
    private void formatRow(SearchFormatterContext context, int numColumns)
        throws SQLException
    {
        DataRowBuilder row = new DataRowBuilder(context.getWriter());
        ResultSet results = context.getResultSet();
        row.begin();
        for (int i = 1; i <= numColumns; i++)
        {
            row.append(results.getString(i));
        }
        row.end();
    }

    /**
     * Converts the column names to field names, using the metadata.
     *
     * @param columns a collection of column names
     * @param metadata The metadata
     * @return a collection of field names
     */
    private Collection columnsToFields(Collection columns,
                                       DmqlParserMetadata metadata)
    {
        List fields = new ArrayList();
        for (Iterator i = columns.iterator(); i.hasNext();)
        {
            String column = (String) i.next();
            fields.add(metadata.columnToField(column));
        }
        return fields;
    }
}
