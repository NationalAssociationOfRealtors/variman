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
import java.util.Iterator;
import java.util.List;

import org.realtors.rets.server.Util;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class DmqlString implements SqlConverter
{
    public static final DmqlStringComponent MATCH_ZERO_OR_MORE =
        new MatchZeroOrMore();

    public static final DmqlStringComponent MATCH_ZERO_OR_ONE =
        new MatchZeroOrOne();

    public DmqlString(String string)
    {
        this();
        add(string);
    }

    public DmqlString()
    {
        mComponents = new ArrayList();
        mStringMatchOperator = " = ";
    }

    public void add(DmqlStringComponent component)
    {
        mComponents.add(component);
        if (component.conatinsWildcards())
        {
            mStringMatchOperator = " LIKE ";
        }
    }

    public void add(String string)
    {
        mComponents.add(new ConstantStringComponent(string));
    }

    public List getComponents()
    {
        return mComponents;
    }

    public void toSql(PrintWriter out)
    {
        out.print(mStringMatchOperator);
        out.print("'");
        Iterator components = mComponents.iterator();
        while (components.hasNext())
        {
            DmqlStringComponent component =
                (DmqlStringComponent) components.next();
            out.print(component.toSql());
        }
        out.print("'");
    }

    public String toString()
    {
        return new ToStringBuilder(this, Util.SHORT_STYLE)
            .append(mComponents)
            .toString();
    }

    public boolean equals(Object object)
    {
        if (!(object instanceof DmqlString))
        {
            return false;
        }
        DmqlString rhs = (DmqlString) object;
        return new EqualsBuilder()
            .append(mComponents, rhs.mComponents)
            .isEquals();
    }

    private List mComponents;
    private String mStringMatchOperator;
}
