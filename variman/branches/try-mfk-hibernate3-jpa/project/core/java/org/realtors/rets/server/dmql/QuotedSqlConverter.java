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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class QuotedSqlConverter implements SqlConverter
{
    public QuotedSqlConverter(String value)
    {
        mValue = value;
    }

    public void toSql(PrintWriter out)
    {
        out.print("'");
        out.print(mValue);
        out.print("'");
    }

    public String toString()
    {
        return new ToStringBuilder(this, Util.SHORT_STYLE)
            .append(mValue)
            .toString();
    }

    public boolean equals(Object obj)
    {
        if (!(obj instanceof QuotedSqlConverter))
        {
            return false;
        }
        QuotedSqlConverter rhs = (QuotedSqlConverter) obj;
        return new EqualsBuilder()
            .append(mValue, rhs.mValue)
            .isEquals();
    }

    private String mValue;
}
