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

public class StringSqlConverter implements SqlConverter
{
    public StringSqlConverter(String string)
    {
        mString = string;
    }

    public void toSql(PrintWriter out)
    {
        out.print(mString);
    }

    public String toString()
    {
        return new ToStringBuilder(this, Util.SHORT_STYLE)
            .append(mString)
            .toString();
    }

    public boolean equals(Object obj)
    {
        if (!(obj instanceof StringSqlConverter))
        {
            return false;
        }
        StringSqlConverter rhs = (StringSqlConverter) obj;
        return new EqualsBuilder()
            .append(mString, rhs.mString)
            .isEquals();
    }

    private String mString;
}
