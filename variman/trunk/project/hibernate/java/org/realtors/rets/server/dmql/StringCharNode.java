package org.realtors.rets.server.dmql;

public class StringCharNode extends DmtAST
{

    private int qs;
    private String str1, str2, qtext;

    public StringCharNode(antlr.Token tok1, antlr.Token qtok, antlr.Token tok2)
    {
        qtext = qtok.getText();
        qs = qtext.length();
        str1 = (tok1 == null) ? "" : tok1.getText();
        str2 = (tok2 == null) ? "" : tok2.getText();
    }

    public String toString()
    {
        return "(" + getClass().getName() + " " + str1 + qtext + str2 + ")";
    }

    public int qs()
    {
        return qs;
    }

    public String text1()
    {
        return str1;
    }

    public String text2()
    {
        return str2;
    }

    public void accept(DmtVisitor vis)
    {
        vis.visit(this);
    }

}

