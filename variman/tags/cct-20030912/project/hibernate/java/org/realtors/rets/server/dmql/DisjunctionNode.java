package org.realtors.rets.server.dmql;

public class DisjunctionNode extends DmtAST
{

    public DisjunctionNode(antlr.Token tok)
    {
    }

    public void accept(DmtVisitor vis)
    {
        vis.preVisit(this);
        left().accept(vis);
        vis.visit(this);
        right().accept(vis);
        vis.postVisit(this);
    }

}

