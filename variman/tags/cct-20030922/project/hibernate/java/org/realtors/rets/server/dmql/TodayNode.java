package org.realtors.rets.server.dmql;

public class TodayNode extends DmtAST
{

    public TodayNode(antlr.Token tok)
    {
    }

    public String toString()
    {
        return "(" + getClass().getName().toString() + ")";
    }

    public void accept(DmtVisitor vis)
    {
        vis.visit(this);
    }

}

