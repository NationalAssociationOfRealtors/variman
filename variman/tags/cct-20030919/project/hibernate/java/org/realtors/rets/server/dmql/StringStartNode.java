package org.realtors.rets.server.dmql;

public class StringStartNode extends TextNode
{

    public StringStartNode(antlr.Token tok)
    {
        super(tok);
    }

    public void accept(DmtVisitor vis)
    {
        vis.visit(this);
    }

}

