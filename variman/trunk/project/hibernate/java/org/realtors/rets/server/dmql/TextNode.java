package org.realtors.rets.server.dmql;

public class TextNode extends DmtAST
{

    private String text;

    public TextNode(antlr.Token tok)
    {
        text = tok.getText();
    }

    public String toString()
    {
        return "(" + getClass().getName().toString() + " " + text + ")";
    }

    public String text()
    {
        return text;
    }

    public void accept(DmtVisitor vis)
    {
        vis.visit(this);
    }

}

