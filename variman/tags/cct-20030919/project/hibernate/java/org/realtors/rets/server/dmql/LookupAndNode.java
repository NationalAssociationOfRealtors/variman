package org.realtors.rets.server.dmql;

public class LookupAndNode extends LookupNode
{

    public LookupAndNode(antlr.collections.AST list)
    {
        super(list);
    }

    public void accept(DmtVisitor vis)
    {
        vis.visit(this);
        list.accept(vis);
    }

}

