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
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.enums.Enum;

import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.dmql.DmqlParserMetadata;
import org.realtors.rets.server.metadata.DataTypeEnum;
import org.realtors.rets.server.metadata.Table;
import org.realtors.rets.server.metadata.format.DataRowBuilder;
import org.realtors.rets.server.metadata.format.TagBuilder;

/**
 * Formats results in COMPACT format.
 */
public class CompactFormatter implements SearchResultsFormatter
{
    private static final TimeZone GMT = TimeZone.getTimeZone("GMT");
    
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
            out.print("<DELIMITER value=\"09\"/>\r\n");
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
        columnsTag.print(StringUtils.join(fields.iterator(), DELIMITER));
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
        DmqlParserMetadata metadata = context.getMetadata();
        for (int i = 0; i < numColumns; i++)
        {
            if ((mLookupDecoding == DECODE_LOOKUPS) && context.isColumnALookup(i)) {
                String strValue = context.getResultString(i);
                strValue = context.decodeLookupValue(i, strValue);
                row.append(strValue);
            } else {
                Calendar cal = Calendar.getInstance(GMT);
                ResultSet rs = context.getResultSet();
                String columnName = context.getColumn(i);
                String fieldName = metadata.columnToField(columnName);
                Table table = metadata.getTable(fieldName);
                DataTypeEnum dataType = table.getDataType();
                if (dataType == DataTypeEnum.DATE) {
                    Date date = rs.getDate(i + 1, cal);
                    row.appendDate(date);
                } else if (dataType == DataTypeEnum.DATETIME) {
                    Timestamp dateTime = rs.getTimestamp(i + 1, cal);
                    row.appendDateTime(dateTime);
                } else if (dataType == DataTypeEnum.TIME) {
                    Time time = rs.getTime(i + 1, cal);
                    row.appendTime(time);
                } else if (dataType == DataTypeEnum.BOOLEAN) {
                    boolean bit = rs.getBoolean(i + 1);
                    row.append(bit);
                } else {
                    String strValue = context.getResultString(i);
                    row.append(strValue);
                }
            }
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
