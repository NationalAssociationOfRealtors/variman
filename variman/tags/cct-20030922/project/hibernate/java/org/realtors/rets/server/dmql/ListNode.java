package org.realtors.rets.server.dmql;

public class ListNode extends DmtAST
{

    protected DmtAST ast1, ast2;

    public ListNode(antlr.collections.AST ast1, antlr.collections.AST ast2)
    {
        this.ast1 = (DmtAST) ast1;
        this.ast2 = (DmtAST) ast2;
    }

    public void accept(DmtVisitor vis)
    {
        if (ast1 != null)
            ast1.accept(vis);
        if (ast2 != null)
            ast2.accept(vis);
    }

}

