package org.realtors.rets.server.dmql;

public class RangeListNode extends ListNode
{

    public RangeListNode(antlr.collections.AST ast1, antlr.collections.AST ast2)
    {
        super(ast1, ast2);
    }

    public void accept(DmtVisitor vis)
    {
        if (ast1 != null)
            ast1.accept(vis);
        if ((ast1 != null) && (ast2 != null))
            vis.visit(this);
        if (ast2 != null)
            ast2.accept(vis);
    }

}

