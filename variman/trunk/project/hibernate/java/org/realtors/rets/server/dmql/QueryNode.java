package org.realtors.rets.server.dmql;

public class QueryNode extends DmtAST
{

    public QueryNode(antlr.Token tok)
    {
    }

    public void accept(DmtVisitor vis)
    {
        left().accept(vis);
        vis.visit(this);
        right().accept(vis);
    }

}

