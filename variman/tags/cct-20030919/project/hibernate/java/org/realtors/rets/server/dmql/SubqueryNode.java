package org.realtors.rets.server.dmql;

public class SubqueryNode extends DmtAST
{

    public SubqueryNode(antlr.Token tok)
    {
    }

    public void accept(DmtVisitor vis)
    {
        vis.preVisit(this);
        basicAccept(vis);
        vis.postVisit(this);
    }

}

