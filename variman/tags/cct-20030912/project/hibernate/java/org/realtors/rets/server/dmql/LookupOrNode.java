package org.realtors.rets.server.dmql;

public class LookupOrNode extends LookupNode
{

    public LookupOrNode(antlr.collections.AST list)
    {
        super(list);
    }

    public void accept(DmtVisitor vis)
    {
        vis.visit(this);
        list.accept(vis);
    }

}

