package org.realtors.rets.server.dmql;

public class NowNode extends DmtAST
{

    public NowNode(antlr.Token tok)
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

