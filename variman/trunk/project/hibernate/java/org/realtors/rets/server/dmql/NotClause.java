/*
 */
package org.realtors.rets.server.dmql;

import java.io.PrintWriter;

public class NotClause implements SqlConverter
{
    public NotClause(SqlConverter negation)
    {
        mNegation = negation;
    }

    public SqlConverter getNegation()
    {
        return mNegation;
    }

    public void setNegation(SqlConverter negation)
    {
        mNegation = negation;
    }

    public void toSql(PrintWriter out)
    {
        out.print("NOT ");
        mNegation.toSql(out);
    }

    private SqlConverter mNegation;
}
