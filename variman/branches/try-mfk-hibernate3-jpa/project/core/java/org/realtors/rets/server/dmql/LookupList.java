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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

public class LookupList implements SqlConverter
{
    public LookupList(LookupListType type, String field)
    {
        mType = type;
        mField = field;
        mSqlColumn = mField;
        mLookupMulti = false;
        mLookups = new ArrayList();
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

    public LookupListType getType()
    {
        return mType;
    }

    public void addLookup(String lookup)
    {
        mLookups.add(lookup);
    }

    public Iterator getLookups()
    {
        return mLookups.iterator();
    }

    public void setLookupMultiTable(String lookupMultiTable)
    {
        if (lookupMultiTable != null)
        {
            mLookupMulti = true;
            mLookupMultiTable = lookupMultiTable;
        }
        else
        {
            mLookupMulti = false;
            mLookupMultiTable = null;
        }
    }

    public void setLookupMulti(boolean lookupMulti)
    {
        mLookupMulti = lookupMulti;
    }

    public boolean isLookupMulti()
    {
        return mLookupMulti;
    }

    public void toSql(PrintWriter out)
    {
        if (mLookupMulti && (mLookupMultiTable != null))
        {
            toSqlLookupMultiTable(out);
        }
        else
        {
            toSqlLookup(out);
        }
    }

    private void toSqlLookup(PrintWriter out)
    {
        out.print(mType.getLookupPrefix());
        String sqlOperator = "";
        for (int i = 0; i < mLookups.size(); i++)
        {
            String lookup = (String) mLookups.get(i);
            out.print(sqlOperator);
            if (mLookupMulti)
            {
                printLookupMultiCondition(out, lookup);
            }
            else
            {
                printLookupCondition(out, lookup);
            }
            sqlOperator = mType.getSqlOperator();
        }
        out.print(mType.getLookupSuffix());
    }

    private void printLookupMultiCondition(PrintWriter out, String lookup)
    {
        out.print("(");
        out.print(mSqlColumn);
        out.print(" = '");
        out.print(lookup);
        out.print("' OR ");
        out.print(mSqlColumn);
        out.print(" LIKE '");
        out.print(lookup);
        out.print(",%' OR ");
        out.print(mSqlColumn);
        out.print(" LIKE '%,");
        out.print(lookup);
        out.print("' OR ");
        out.print(mSqlColumn);
        out.print(" LIKE '%,");
        out.print(lookup);
        out.print(",%')");
    }

    private void printLookupCondition(PrintWriter out, String lookup)
    {
        out.print(mSqlColumn);
        out.print(" = '");
        out.print(lookup);
        out.print("'");
    }

    private void toSqlLookupMultiTable(PrintWriter out)
    {
        out.print("id ");
        out.print(mType.getLookupMultiPrefix());
        out.print("IN (SELECT data_id FROM ");
        out.print(mLookupMultiTable);
        out.print(" WHERE name = '");
        out.print(mSqlColumn);
        out.print("' AND (");
        String sqlOperator = "";
        for (int i = 0; i < mLookups.size(); i++)
        {
            String lookup = (String) mLookups.get(i);
            out.print(sqlOperator);
            out.print("value = '");
            out.print(lookup);
            out.print("'");
            sqlOperator = mType.getSqlOperator();
        }
        out.print("))");
    }

    public String toString()
    {
        return new ToStringBuilder(this, Util.SHORT_STYLE)
            .append("type", mType)
            .append("looups", mLookups)
            .append("isLookupMulti", mLookupMulti)
            .append("field", mField)
            .toString();
    }

    public boolean equals(Object obj)
    {
        if (!(obj instanceof LookupList))
        {
            return false;
        }
        LookupList rhs = (LookupList) obj;
        return new EqualsBuilder()
            .append(mType, rhs.mType)
            .append(mField, rhs.mField)
            .append(mSqlColumn, rhs.mSqlColumn)
            .append(mLookups, rhs.mLookups)
            .append(mLookupMulti, rhs.mLookupMulti)
            .append(mLookupMultiTable, rhs.mLookupMultiTable)
            .isEquals();
    }

    private LookupListType mType;
    private String mField;
    private String mSqlColumn;
    private List mLookups;
    private boolean mLookupMulti;
    private String mLookupMultiTable;
}
