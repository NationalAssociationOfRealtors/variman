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

public class MatchZeroOrMore implements DmqlStringComponent
{
    public boolean conatinsWildcards()
    {
        return true;
    }

    public String toSql()
    {
        return "%";
    }

    public String toString()
    {
        return "*";
    }

    public boolean equals(Object o)
    {
        return (o instanceof MatchZeroOrMore);
    }
}
