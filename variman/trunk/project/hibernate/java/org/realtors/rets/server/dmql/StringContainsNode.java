package org.realtors.rets.server.dmql;

public class StringContainsNode extends TextNode
{

    public StringContainsNode(antlr.Token tok)
    {
        super(tok);
    }

    public void accept(DmtVisitor vis)
    {
        vis.visit(this);
    }

}

