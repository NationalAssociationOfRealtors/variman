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
        if (mLookupMulti)
        {
            toSqlLookupMulti(out);
        }
        else
        {
            toSqlLookup(out);
        }
    }

    private void toSqlLookup(PrintWriter out)
    {
        boolean first = true;
        if (mType == LookupListType.NOT)
        {
            out.print("NOT (");
        }
        for (int i = 0; i < mLookups.size(); i++)
        {
            String lookup = (String) mLookups.get(i);
            if (!first)
            {
                out.print(sqlOperator());
            }
            out.print(mSqlColumn);
            out.print(" = '");
            out.print(lookup);
            out.print("'");
            first = false;
        }
        if (mType == LookupListType.NOT)
        {
            out.print(")");
        }
    }

    private void toSqlLookupMulti(PrintWriter out)
    {
        out.print("id ");
        if (mType == LookupListType.NOT)
        {
            out.print("NOT ");
        }
        out.print("IN (SELECT data_id FROM ");
        out.print(mLookupMultiTable);
        out.print(" WHERE name = '");
        out.print(mSqlColumn);
        out.print("' AND (");
        boolean first = true;
        for (int i = 0; i < mLookups.size(); i++)
        {
            String lookup = (String) mLookups.get(i);
            if (!first)
            {
                out.print(sqlOperator());
            }
            out.print("value = '");
            out.print(lookup);
            out.print("'");
            first = false;
        }
        out.print("))");
    }

    private String sqlOperator()
    {
        if (mType == LookupListType.AND)
        {
            return " AND ";
        }
        else if (mType == LookupListType.OR)
        {
            return " OR ";
        }
        else if (mType == LookupListType.NOT)
        {
            return " OR ";
        }
        else
        {
            return " ??? ";
        }
    }

    public String toString()
    {
        return new ToStringBuilder(this, Util.SHORT_STYLE)
            .append("type", mType)
            .append("looups", mLookups)
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
