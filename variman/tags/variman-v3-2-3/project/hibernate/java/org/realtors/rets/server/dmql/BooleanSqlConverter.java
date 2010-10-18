/*
 * Variman RETS Server
 *
 * Author: Mark Klein
 * Copyright (c) 2009, The National Association of REALTORS
 * Distributed under a BSD-style license.  See LICENSE.TXT for details.
 */

/*
 */
package org.realtors.rets.server.dmql;

import java.io.PrintWriter;

import org.realtors.rets.server.Util;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

public class BooleanSqlConverter implements SqlConverter
{
    public BooleanSqlConverter(String string)
    {
        mString = string;
    }

    public void toSql(PrintWriter out)
    {
        if (mString.equals("0"))
            out.print("'0'");
        else
        if (mString.equals("1"))
            out.print("'1'");
        else
            out.print(mString); // Shouldn't happen and will cause failure.
    }

    public String toString()
    {
        return new ToStringBuilder(this, Util.SHORT_STYLE)
            .append(mString)
            .toString();
    }

    public boolean equals(Object obj)
    {
        if (!(obj instanceof BooleanSqlConverter))
        {
            return false;
        }
        BooleanSqlConverter rhs = (BooleanSqlConverter) obj;
        return new EqualsBuilder()
            .append(mString, rhs.mString)
            .isEquals();
    }

    private String mString;
}
