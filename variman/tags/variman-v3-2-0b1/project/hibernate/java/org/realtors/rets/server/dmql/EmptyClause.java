/*
 * Variman RETS Server
 *
 * Author: Mark Klein, RealGo
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
        this(field, null);
    }

    public EmptyClause(String field, DmqlFieldType dmqlFieldType)
    {
        mField = field;
        mDmqlFieldType = dmqlFieldType;
    }

    public void toSql(PrintWriter out)
    {
        if (mDmqlFieldType == DmqlFieldType.CHARACTER) {
            out.print("(");
            out.print(mField);
            out.print(" is null or ");
            out.print(mField);
            out.print(" = '') ");
        } else {
            out.print(mField);
            out.print(" is null ");
        }
    }

    public String toString()
    {
        return new ToStringBuilder(this, Util.SHORT_STYLE)
            .append(mField)
            .append(mDmqlFieldType)
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
            .append(mDmqlFieldType, rhs.mDmqlFieldType)
            .isEquals();
    }

    private String mField;
    private DmqlFieldType mDmqlFieldType;
}
