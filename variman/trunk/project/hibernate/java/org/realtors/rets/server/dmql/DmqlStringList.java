/*
 */
package org.realtors.rets.server.dmql;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.io.StringWriter;
import java.io.PrintWriter;

public class DmqlStringList implements SqlConverter
{

    public DmqlStringList(String field, DmqlString string)
    {
        mField = field;
        mSqlColumn = mField;
        mStrings = new ArrayList();
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
        boolean first = true;
        for (int i = 0; i < mStrings.size(); i++)
        {
            DmqlString dmqlString = (DmqlString) mStrings.get(i);
            if (!first)
            {
                out.print(" OR ");
            }
            out.print(mSqlColumn);
            if (dmqlString.containsWildcards())
            {
                out.print(" LIKE ");
            }
            else
            {
                out.print(" = ");
            }
            dmqlString.toSql(out);
            first = false;
        }
    }

    private String mField;
    private String mSqlColumn;
    private List mStrings;
}
