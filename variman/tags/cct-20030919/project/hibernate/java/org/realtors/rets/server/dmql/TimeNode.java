package org.realtors.rets.server.dmql;

public class TimeNode extends DmtAST
{

    private String hour, minute, second, frac;

    public TimeNode(antlr.Token tok)
    {
        this(tok.getText());
    }

    public TimeNode(String str)
    {
        hour = str.substring(0, 2);
        minute = str.substring(3, 5);
        second = str.substring(6, 8);
        frac = (str.length() > 8) ? str.substring(9, 11) : "00";
    }

    public String toString()
    {
        return "(" + getClass().getName().toString() + ")";
    }

    public String hour()
    {
        return hour;
    }

    public String minute()
    {
        return minute;
    }

    public String second()
    {
        return second;
    }

    public String frac()
    {
        return frac;
    }

    public void accept(DmtVisitor vis)
    {
        vis.visit(this);
    }

}

