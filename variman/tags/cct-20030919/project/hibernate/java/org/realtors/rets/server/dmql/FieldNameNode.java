package org.realtors.rets.server.dmql;

public class FieldNameNode extends DmtAST
{

    private String text;

    public FieldNameNode(antlr.Token tok)
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

