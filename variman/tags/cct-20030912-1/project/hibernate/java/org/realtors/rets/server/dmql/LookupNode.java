package org.realtors.rets.server.dmql;

public class LookupNode extends DmtAST
{

    protected DmtAST list;

    public LookupNode(antlr.collections.AST list)
    {
        this.list = (DmtAST) list;
    }

    public String toString()
    {
        return "(" + getClass().getName() + "\n" + list + ")";
    }

}

