package org.realtors.rets.server.dmql;

public class DateTimeNode extends DmtAST
{

    private DateNode date;
    private TimeNode time;

    public DateTimeNode(antlr.Token tok)
    {
        date = new DateNode(tok.getText().substring(0, 10));
        time = new TimeNode(tok.getText().substring(11));
    }

    public String toString()
    {
        return "(" + getClass().getName().toString() + ")";
    }

    public String year()
    {
        return date.year();
    }

    public String month()
    {
        return date.year();
    }

    public String day()
    {
        return date.day();
    }

    public String hour()
    {
        return time.hour();
    }

    public String minute()
    {
        return time.minute();
    }

    public String second()
    {
        return time.second();
    }

    public String frac()
    {
        return time.frac();
    }

    public void accept(DmtVisitor vis)
    {
        vis.visit(this);
    }

}

