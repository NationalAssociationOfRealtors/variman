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
import java.util.ArrayList;
import java.util.List;

import org.realtors.rets.server.Util;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

public class OrClause implements SqlConverter
{
    public OrClause()
    {
        mElements = new ArrayList();
    }

    public OrClause(SqlConverter left, SqlConverter right)
    {
        this();
        add(left);
        add(right);
    }

    public void toSql(PrintWriter out)
    {
        String separator = "";
        for (int i = 0; i < mElements.size(); i++)
        {
            SqlConverter converter = (SqlConverter) mElements.get(i);
            out.print(separator);
            out.print("(");
            converter.toSql(out);
            out.print(")");
            separator = " OR ";
        }
    }

    public void add(SqlConverter sqlConverter)
    {
        mElements.add(sqlConverter);
    }

    public String toString()
    {
        return new ToStringBuilder(this, Util.SHORT_STYLE)
            .append(mElements)
            .toString();
    }

    public boolean equals(Object obj)
    {
        if (!(obj instanceof OrClause))
        {
            return false;
        }
        OrClause rhs = (OrClause) obj;
        return new EqualsBuilder()
            .append(mElements, rhs.mElements)
            .isEquals();
    }

    private List mElements;
}
