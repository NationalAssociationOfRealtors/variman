/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.metadata.format;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import org.realtors.rets.server.metadata.Table;
import org.realtors.rets.server.protocol.TableGroupFilter;

public class FormatterContext
{
    protected FormatterContext()
    {
        // Empty for subclasses
    }

    public FormatterContext(String version, Date date, boolean recursive,
                            PrintWriter writer,
                            FormatterLookup lookup)
    {
        mVersion = version;
        mDate = date;
        mRecursive = recursive;
        mWriter = writer;
        mLookup = lookup;
    }

    public String getVersion()
    {
        return mVersion;
    }

    protected void setVersion(String version)
    {
        mVersion = version;
    }

    public Date getDate()
    {
        return mDate;
    }

    protected void setDate(Date date)
    {
        mDate = date;
    }

    public boolean isRecursive()
    {
        return mRecursive;
    }

    protected void setRecursive(boolean recursive)
    {
        mRecursive = recursive;
    }

    public PrintWriter getWriter()
    {
        return mWriter;
    }

    protected void setWriter(PrintWriter writer)
    {
        mWriter = writer;
    }

    public void format(Collection data, String[] levels)
    {
        MetadataFormatter formatter = mLookup.lookupFormatter(data);
        formatter.format(this, data, levels);
    }

    protected void setLookup(FormatterLookup lookup)
    {
        mLookup = lookup;
    }

    /**
     * Checks to see if a table is accessible for a resource and class.
     *
     * @param table table to check
     * @param resource a RETS resource name
     * @param retsClass a RETS class name
     * @return <code>true</code> if table is valid
     */
    public boolean isAccessibleTable(Table table, String resource,
                                     String retsClass)
    {
        Set tables = mGroupFilter.findTables(mGroups, resource, retsClass);
        return tables.contains(table);
    }

    protected void setTableFilter(TableGroupFilter groupFilter, Set groups)
    {
        mGroupFilter = groupFilter;
        mGroups = groups;
    }

    public static final boolean RECURSIVE = true;
    public static final boolean NOT_RECURSIVE = false;

    private String mVersion;
    private Date mDate;
    private boolean mRecursive;
    private PrintWriter mWriter;
    private FormatterLookup mLookup;
    private TableGroupFilter mGroupFilter;
    private Set mGroups;
}
