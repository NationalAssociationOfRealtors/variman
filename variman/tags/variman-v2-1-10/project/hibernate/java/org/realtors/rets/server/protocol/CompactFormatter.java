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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.enums.Enum;

import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.dmql.DmqlParserMetadata;
import org.realtors.rets.server.metadata.format.DataRowBuilder;
import org.realtors.rets.server.metadata.format.TagBuilder;

/**
 * Formats results in COMPACT format.
 */
public class CompactFormatter implements SearchResultsFormatter
{
    public CompactFormatter(LookupDecoding lookupDecoding)
    {
        mLookupDecoding = lookupDecoding;
    }

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
            out.print("<DELIMITER value=\"09\"/>\n");
            formatColumns(context);
            int numColumns = context.getNumberOfColumns();
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

    private void formatColumns(SearchFormatterContext context)
    {
        TagBuilder columnsTag = new TagBuilder(context.getWriter(), "COLUMNS")
            .beginContent();
        columnsTag.print(DELIMITER);
        Collection fields = columnsToFields(context.getColumns(),
                                            context.getMetadata());
        columnsTag.print(StringUtils.join(fields.iterator(), "\t"));
        columnsTag.print(DELIMITER);
        columnsTag.close();
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
        DataRowBuilder row = new DataRowBuilder(context.getWriter(), DELIMITER);
        row.begin();
        for (int i = 0; i < numColumns; i++)
        {
            String value = context.getResultString(i);
            if ((mLookupDecoding == DECODE_LOOKUPS) &&
                context.isColumnALookup(i))
            {
                value = context.decodeLookupValue(i, value);
            }
            row.append(value);
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

    public static final class LookupDecoding extends Enum
    {
        private LookupDecoding(String s)
        {
            super(s);
        }
    }

    public static final LookupDecoding NO_DECODING =
        new LookupDecoding("no decoding");
    public static final LookupDecoding DECODE_LOOKUPS =
        new LookupDecoding("decode lookups");

    private static final String DELIMITER = "\t";
    private LookupDecoding mLookupDecoding;
}
