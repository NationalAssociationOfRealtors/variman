package org.realtors.rets.server.dmql;

public class StringEqNode extends TextNode
{

    public StringEqNode(antlr.Token tok)
    {
        super(tok);
    }

    public void accept(DmtVisitor vis)
    {
        vis.visit(this);
    }

}

