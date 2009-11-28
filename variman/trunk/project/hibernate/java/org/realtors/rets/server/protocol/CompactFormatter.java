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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.enums.Enum;

import org.realtors.rets.client.RetsVersion;
import org.realtors.rets.common.metadata.attrib.AttrDate;
import org.realtors.rets.common.util.DataRowBuilder;
import org.realtors.rets.common.util.RetsDateTime;
import org.realtors.rets.common.util.TagBuilder;

import org.realtors.rets.server.RetsServerException;
import org.realtors.rets.server.dmql.DmqlParserMetadata;

/**
 * Formats results in COMPACT format.
 */
public class CompactFormatter implements SearchResultsFormatter
{
    public CompactFormatter(LookupDecoding lookupDecoding, RetsVersion retsVersion)
    {
        mLookupDecoding = lookupDecoding;
        mRetsVersion = retsVersion;
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
            if (context.exceededLimit())
            {
                out.print("<MAXROWS/>\n");
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
        DataRowBuilder row = new DataRowBuilder(context.getWriter(), DELIMITER, DELIMITER_REPL);
        
        row.begin();
        for (int i = 0; i < numColumns; i++)
        {
            String value = context.getResultString(i);
            if ((mLookupDecoding == DECODE_LOOKUPS) &&
                context.isColumnALookup(i))
            {
                value = context.decodeLookupValue(i, value);
            }
            if (context.isColumnDateTime(i) && !context.isColumnRetsDateTime(i))
            {
            	// This is ugly, but got no choice. Try to convert the date
            	// and then properly render it. Let's try SQL format first in 
            	// case this date is returned by a SQL query.
            	if (value != null)
            	{
	            	try
	            	{
	            	    String retsDateTime = RetsDateTime.render(RetsDateTime.parseSql(value),
	            								context.getRetsVersion());
	            	    value = retsDateTime;
	            	}
	            	catch (ParseException e)
	            	{
	            		try
	            		{
		        			// Need to parse this as RETS_1_5.
		        			RetsDateTime retsDateTime = new RetsDateTime(value, RetsVersion.RETS_1_5);
		
		        			value = retsDateTime.toString();
	            		}
	            		catch (ParseException f)
	            		{
	            			// Ignore the error. The timestamp as returned from the SQL engine will be used.
	            		}
	            	}
            	}
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
    private static final String DELIMITER_REPL = "    ";
    private LookupDecoding mLookupDecoding;
    private RetsVersion mRetsVersion;
    private static final AttrDate mAttrDate = new AttrDate();
}
