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

import org.realtors.rets.server.Util;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

public class BetweenClause implements SqlConverter
{
    public BetweenClause(String field, SqlConverter left,
                        SqlConverter right)
    {
        mField = field;
        mLeft = left;
        mRight = right;
    }

    public void toSql(PrintWriter out)
    {
        out.print(mField);
        out.print(" BETWEEN ");
        mLeft.toSql(out);
        out.print(" AND ");
        mRight.toSql(out);
    }

    public String toString()
    {
        return new ToStringBuilder(this, Util.SHORT_STYLE)
            .append("field", mField)
            .append("left", mLeft)
            .append("right", mRight)
            .toString();
    }

    public boolean equals(Object obj)
    {
        if (!(obj instanceof BetweenClause))
        {
            return false;
        }
        BetweenClause rhs = (BetweenClause) obj;
        return new EqualsBuilder()
            .append(mField, rhs.mField)
            .append(mLeft, rhs.mLeft)
            .append(mRight, rhs.mRight)
            .isEquals();
    }

    private String mField;
    private SqlConverter mLeft;
    private SqlConverter mRight;
}
