package org.realtors.rets.server.dmql;

public class LookupNotNode extends LookupNode
{

    public LookupNotNode(antlr.collections.AST list)
    {
        super(list);
    }

    public void accept(DmtVisitor vis)
    {
        vis.visit(this);
        list.accept(vis);
    }

}

