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
import java.util.Collection;

import org.realtors.rets.server.dmql.DmqlParserMetadata;

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
        mColumns = columns;
        mMetadata = metadata;
        mRowCount = 0;
        mLimit = Integer.MAX_VALUE;
    }

    public Collection getColumns()
    {
        return mColumns;
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
        return mResultSet.getString(column);
    }

    /** The writer to print to. */
    private PrintWriter mWriter;
    /** The results of the database query. */
    private ResultSet mResultSet;
    /** The column names used in the SELECT statement. */
    private Collection /* String */ mColumns;
    /** The RETS metadata. */
    private DmqlParserMetadata mMetadata;
    /** The number of rows processed. */
    private int mRowCount;
    /** The maximum number of rows to iterate through. */
    private int mLimit;
}
