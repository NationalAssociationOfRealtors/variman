/*
 */
package org.realtors.rets.server.dmql;

import java.io.PrintWriter;

public class OrClause implements SqlConverter
{
    public SqlConverter getLeft()
    {
        return mLeft;
    }

    public void setLeft(SqlConverter left)
    {
        mLeft = left;
    }

    public SqlConverter getRight()
    {
        return mRight;
    }

    public void setRight(SqlConverter right)
    {
        mRight = right;
    }

    public void toSql(PrintWriter out)
    {
        mLeft.toSql(out);
        out.print(" OR ");
        mRight.toSql(out);
    }

    private SqlConverter mLeft;
    private SqlConverter mRight;
}
