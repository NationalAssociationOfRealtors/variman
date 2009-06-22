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

public class GreaterThanClause implements SqlConverter
{
    public GreaterThanClause(String field, SqlConverter sqlConverter)
    {
        mField = field;
        mSqlConverter = sqlConverter;
    }

    public void toSql(PrintWriter out)
    {
        out.print(mField);
        out.print(" >= ");
        mSqlConverter.toSql(out);
    }

    public String toString()
    {
        return new ToStringBuilder(this, Util.SHORT_STYLE)
            .append("field", mField)
            .append("sqlConverter", mSqlConverter)
            .toString();
    }

    public boolean equals(Object obj)
    {
        if (!(obj instanceof GreaterThanClause))
        {
            return false;
        }
        GreaterThanClause rhs = (GreaterThanClause) obj;
        return new EqualsBuilder()
            .append(mField, rhs.mField)
            .append(mSqlConverter, rhs.mSqlConverter)
            .isEquals();
    }

    private String mField;
    private SqlConverter mSqlConverter;
}
