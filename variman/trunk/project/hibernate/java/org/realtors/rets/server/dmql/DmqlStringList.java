/*
 * Variman RETS Server
 *
 * Author: Dave Dribin
 * Copyright (c) 2004, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.dmql;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.io.PrintWriter;

import org.realtors.rets.server.Util;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class DmqlStringList implements SqlConverter
{
    public DmqlStringList(String field)
    {
        mField = field;
        mSqlColumn = mField;
        mStrings = new ArrayList();
    }

    public DmqlStringList(String field, DmqlString string)
    {
        this(field);
        add(string);
    }

    public String getField()
    {
        return mField;
    }

    public void setSqlColumn(String sqlColumn)
    {
        mSqlColumn = sqlColumn;
    }

    public String getSqlColumn()
    {
        return mSqlColumn;
    }

    public void add(DmqlString string)
    {
        mStrings.add(string);
    }

    public Iterator getStrings()
    {
        return mStrings.iterator();
    }

    public void toSql(PrintWriter out)
    {
        String separator = "";
        for (int i = 0; i < mStrings.size(); i++)
        {
            DmqlString dmqlString = (DmqlString) mStrings.get(i);
            out.print(separator);
            out.print(mSqlColumn);
            dmqlString.toSql(out);
            separator = " OR ";
        }
    }

    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }

        if (!(o instanceof DmqlStringList))
        {
            return false;
        }

        final DmqlStringList rhs = (DmqlStringList) o;
        return new EqualsBuilder()
            .append(mField, rhs.mField)
            .append(mSqlColumn, rhs.mSqlColumn)
            .append(mStrings, rhs.mStrings)
            .isEquals();
    }

    public String toString()
    {
        return new ToStringBuilder(this, Util.SHORT_STYLE)
            .append(mField)
            .append(mSqlColumn)
            .append(mStrings)
            .toString();
    }

    private String mField;
    private String mSqlColumn;
    private List mStrings;
}
