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
        out.print("NOT (");
        mNegation.toSql(out);
        out.print(")");
    }

    private SqlConverter mNegation;
}
