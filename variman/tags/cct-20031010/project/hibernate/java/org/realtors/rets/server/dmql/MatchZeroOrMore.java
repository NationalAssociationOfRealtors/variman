/*
 */
package org.realtors.rets.server.dmql;

public class MatchZeroOrMore implements DmqlStringComponent
{
    public boolean conatinsWildcards()
    {
        return true;
    }

    public boolean equals(Object o)
    {
        return (o instanceof MatchZeroOrMore);
    }
}
