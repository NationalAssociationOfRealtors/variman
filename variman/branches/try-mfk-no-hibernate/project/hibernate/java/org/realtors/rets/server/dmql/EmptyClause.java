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

public class EmptyClause implements SqlConverter
{
    public EmptyClause(String field)
    {
        mField = field;
    }

    public void toSql(PrintWriter out)
    {
        out.print(mField);
        out.print(" is null ");
    }

    public String toString()
    {
        return new ToStringBuilder(this, Util.SHORT_STYLE)
            .append(mField)
            .toString();
    }

    public boolean equals(Object obj)
    {
        if (!(obj instanceof EmptyClause))
        {
            return false;
        }
        EmptyClause rhs = (EmptyClause) obj;
        return new EqualsBuilder()
            .append(mField, rhs.mField)
            .isEquals();
    }

    private String mField;
}
