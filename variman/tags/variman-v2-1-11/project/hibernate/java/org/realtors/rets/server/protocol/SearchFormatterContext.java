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
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import org.apache.commons.lang.StringUtils;

import org.realtors.rets.server.dmql.DmqlParserMetadata;
import org.realtors.rets.server.dmql.DmqlFieldType;

/**
 * Contains all objects needed to format search results. It contains:
 *
 * <ul>
 * <li>A PrintWriter where output should be printed.</li>
 * <li>A ResultSet contain results from a database.</li>
 * <li>The SELECT;ed columns.</li>
 * <li>Metadata to do translation.</li>
 * </ul>
 *
 * It also contains methods that help deal with the limit on the number of rows
 * to format.
 *
 * Many methods take a column index which is zero-based.  Remember that
 * a <code>ResultSet</code> uses one-based column indexes.  If you use
 * the methods on this class, you do not have to deal with this.
 */
public class SearchFormatterContext
{
    /**
     * Construct a new search context.
     *
     * @param writer
     * @param resultSet
     * @param columns
     * @param metadata
     */
    public SearchFormatterContext(PrintWriter writer, ResultSet resultSet,
                                  Collection columns,
                                  DmqlParserMetadata metadata)
    {
        mWriter = writer;
        mResultSet = resultSet;
        mColumns = new ArrayList(columns);
        mColumnsSet = new HashSet(columns);
        mMetadata = metadata;
        mRowCount = 0;
        mLimit = Integer.MAX_VALUE;
    }

    public List getColumns()
    {
        return mColumns;
    }

    public int getNumberOfColumns()
    {
        return mColumns.size();
    }

    /**
     * Return the column name at the column index.
     *
     * @param columnIndex  column index
     * @return column name
     */
    public String getColumn(int columnIndex)
    {
        return (String) mColumns.get(columnIndex);
    }

    public DmqlParserMetadata getMetadata()
    {
        return mMetadata;
    }

    public PrintWriter getWriter()
    {
        return mWriter;
    }

    public ResultSet getResultSet()
    {
        return mResultSet;
    }

    public int getLimit()
    {
        return mLimit;
    }

    /**
     * Sets the limit on the number of rows to iterate through.
     *
     * @param limit
     */
    public void setLimit(int limit)
    {
        mLimit = limit;
    }

    /**
     * Checks if there are more results in the ResultSet, bounded by either the
     * end of the ResultSet or the supplied limit, and advances to the next row.
     *
     * @return <code>true</code> if there are more results in the result set.
     * @throws SQLException if an error occurs
     */
    public boolean hasNext() throws SQLException
    {
        if ((mRowCount < mLimit) && mResultSet.next())
        {
            mRowCount++;
            return true;
        }
        return false;
    }

    /**
     * Returns the number of rows processed so far. This is only accurate if
     * <b>only</b> hasNext() is used. If the result set is advanced elsewhere,
     * this count will not be accurate.
     *
     * @return the number of rows processed so far.
     */
    public int getRowCount()
    {
        return mRowCount;
    }

    public boolean exceededLimit()
    {
        return (mRowCount >= mLimit);
    }

    /**
     * Returns a String from the ResultSet using a RETS field name. The
     * actual column name is retrieved from the metadata.
     *
     * @param fieldName the RETS field name
     * @return a String from the ResultSet
     * @throws SQLException if an error occurs
     */
    public String getResultString(String fieldName) throws SQLException
    {
        String column = mMetadata.fieldToColumn(fieldName);
        if (mColumnsSet.contains(column))
        {
            return mResultSet.getString(column);
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns a String from the ResultSet using a <b>zero-based</b>
     * column index.  If you deal with the result set directly, remember
     * that columns are 1-based.
     *
     * @param columnIndex zero-based column index
     * @return a String from the ResultSet.
     * @throws SQLException
     */
    public String getResultString(int columnIndex)
        throws SQLException
    {
        return mResultSet.getString(columnIndex + 1);
    }

    /**
     * Decode the value for a lookup at the specified clolum.  The decoded
     * value is the long value if not null, then the short value if not
     * null, and finally just the original value if both are null.
     *
     * @param columnIndex 1-based column index
     * @param value lookup value
     * @return short value of the lookup
     */
    public String decodeLookupValue(int columnIndex, String value)
    {
        return decodeLookupValue(getColumn(columnIndex), value);
    }

    /**
     * Decode the value for a lookup at the specified clolum.  The decoded
     * value is the long value if not null, then the short value if not
     * null, and finally just the original value if both are null.
     *
     * @param column column name
     * @param value lookup value
     * @return short value of the lookup
     */
    public String decodeLookupValue(String column, String value)
    {
        String lookupName = mMetadata.columnToField(column);
        if (mMetadata.getFieldType(lookupName) == DmqlFieldType.LOOKUP)
        {
            return decodeSingleLookup(lookupName, value);
        }
        else
        {
            // StringUtils.split returns null on a null string, not an
            // empty array
            if (value == null)
            {
                return null;
            }
            String[] values = StringUtils.split(value, ",");
            StringBuffer decodedValues = new StringBuffer();
            String separator = "";
            for (int i = 0; i < values.length; i++)
            {
                decodedValues.append(separator);
                String decodedValue = decodeSingleLookup(lookupName, values[i]);
                decodedValues.append(decodedValue);
                separator = ",";
            }
            return decodedValues.toString();
        }
    }

    private String decodeSingleLookup(String lookupName, String value)
    {
        String decodedValue = mMetadata.getLookupLongValue(lookupName, value);
        if (decodedValue == null)
        {
            decodedValue = mMetadata.getLookupShortValue(lookupName, value);
            if (decodedValue == null)
            {
                decodedValue = value;
            }
        }
        return decodedValue;
    }

    /**
     * Checks if the column at the specified column index is a lookup.
     *
     * @param columnIndex 1-based column index
     * @return <code>true</code> if the column is a lookup
     */
    public boolean isColumnALookup(int columnIndex)
    {
        String column = (String) mColumns.get(columnIndex);
        return isColumnALookup(column);
    }

    /**
     * Checks if the column at the specified 1-based column index is a lookup.
     *
     * @param column column name
     * @return <code>true</code> if the column is a lookup
     */
    public boolean isColumnALookup(String column)
    {
        String field = mMetadata.columnToField(column);
        DmqlFieldType fieldType = mMetadata.getFieldType(field);
        return ((fieldType == DmqlFieldType.LOOKUP) ||
                (fieldType == DmqlFieldType.LOOKUP_MULTI));
    }

    /** The writer to print to. */
    private PrintWriter mWriter;
    /** The results of the database query. */
    private ResultSet mResultSet;
    /** The column names used in the SELECT statement. */
    private List /* String */ mColumns;
    private Set /* String */ mColumnsSet;
    /** The RETS metadata. */
    private DmqlParserMetadata mMetadata;
    /** The number of rows processed. */
    private int mRowCount;
    /** The maximum number of rows to iterate through. */
    private int mLimit;
}
