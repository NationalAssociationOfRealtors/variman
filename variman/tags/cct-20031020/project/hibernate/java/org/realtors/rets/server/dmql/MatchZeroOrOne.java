/*
 */
package org.realtors.rets.server.dmql;

public class MatchZeroOrOne implements DmqlStringComponent
{
    public boolean conatinsWildcards()
    {
        return true;
    }

    public String toSql()
    {
        return "_";
    }

    public boolean equals(Object o)
    {
        return (o instanceof MatchZeroOrOne);
    }
}
