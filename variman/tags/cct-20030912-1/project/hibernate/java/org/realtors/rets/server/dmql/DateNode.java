package org.realtors.rets.server.dmql;

public class DateNode extends DmtAST
{

    private String year, month, day;

    public DateNode(antlr.Token tok)
    {
        this(tok.getText());
    }

    public DateNode(String str)
    {
        year = str.substring(0, 4);
        month = str.substring(5, 7);
        day = str.substring(8, 10);
    }

    public String toString()
    {
        return "(" + getClass().getName().toString() + ")";
    }

    public String year()
    {
        return year;
    }

    public String month()
    {
        return month;
    }

    public String day()
    {
        return day;
    }

    public void accept(DmtVisitor vis)
    {
        vis.visit(this);
    }

}

