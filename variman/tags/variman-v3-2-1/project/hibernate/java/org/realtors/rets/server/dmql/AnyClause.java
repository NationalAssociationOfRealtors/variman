/*
 * Variman RETS Server
 *
 * Author: Mark Klein
 * Copyright (c) 2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

package org.realtors.rets.server.dmql;

import java.io.PrintWriter;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

import org.realtors.rets.server.Util;

public class AnyClause implements SqlConverter
{
    public AnyClause(String field)
    {
        mField = field;
    }

    public void toSql(PrintWriter out)
    {
        // FIXME: Are there referential integrity issues? 
        // In the BNF for Search in 1.7.2, .ANY. can represent
        // any possible value. From a SQL standpoint, that should
        // be the same as simply ignoring the field. But, because we
        // could end up with an empty WHERE clause, it would be best
        // to check the field both null and not null.
        out.print("(");
        out.print(mField);
        out.print(" is null or ");
        out.print(mField);
        out.print(" is not null) ");
    }

    public String toString()
    {
        return new ToStringBuilder(this, Util.SHORT_STYLE)
            .append(mField)
            .toString();
    }

    public boolean equals(Object obj)
    {
        if (!(obj instanceof AnyClause))
        {
            return false;
        }
        AnyClause rhs = (AnyClause) obj;
        return new EqualsBuilder()
            .append(mField, rhs.mField)
            .isEquals();
    }

    private String mField;
}
